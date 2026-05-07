package org.bnbalint.simplifi

import org.bnbalint.simplifi.exceptions.SimplifiRateLimitRetriesExhausted
import org.bnbalint.simplifi.exceptions.SimplifiServerExceptionRetriesExhausted
import org.bnbalint.simplifi.exceptions.SimplifiUnprocessableEntityException
import org.bnbalint.simplifi.models.*
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono
import reactor.util.retry.Retry
import java.time.Duration

/**
 * SimpliFi API Client
 * @param baseUrl The base URL for the SimpliFi API
 * @param appKey The application key for authentication
 * @param userKey The user key for authentication
 * @param rateLimitMaxRetries Maximum number of retries for 429 Rate Limit errors
 * @param rateLimitMaxBackoff Maximum backoff time in milliseconds for 429 Rate Limit errors
 * @param rateLimitInitialWait Initial wait time in milliseconds between retries for 429 Rate Limit errors
 * @param serverErrorMaxRetries Maximum number of retries for 5xx Server errors
 * @param serverErrorMaxBackoff Maximum backoff time in milliseconds for 5xx Server errors
 * @param serverErrorInitialWait Initial wait time in milliseconds between retries for 5xx Server errors
 * @param apiVersion Optional API version to use
 *
 */
class SimplifiApiClient(
    val baseUrl: String,
    appKey: String,
    userKey: String,
    rateLimitMaxRetries: Long,
    rateLimitMaxBackoff: Long,
    rateLimitInitialWait: Long,
    serverErrorMaxRetries: Long,
    serverErrorMaxBackoff: Long,
    serverErrorInitialWait: Long,
    apiVersion: String? = null
) {

    private var client: WebClient

    /**
     * Request logging filter
     * log the request method and URL when in debug mode
     */
    private val requestLoggingFilter: ExchangeFilterFunction = ExchangeFilterFunction.ofRequestProcessor { request ->
        log.debug("Simpli.fi request: {} {}", request.method(), request.url())
        Mono.just(request)
    }

    /**
     * Response logging filter
     * log the response status code when in debug mode
     */
    private val responseLoggingFilter: ExchangeFilterFunction = ExchangeFilterFunction.ofResponseProcessor { response ->
        log.debug("Simpli.fi response: {}", response.statusCode())
        Mono.just(response)
    }

    /**
     * Error logging filter
     * Log each error when we encounter it
     * 429 and 5xx errors are caught and retried elsewhere, so we only log other errors here
     * do NOT log 429s because we don't need more information about those errors
     * we DO log the 5xx errors here because we only log the status code during the retry (not any further information)
     */
    private val errorLoggingFilter: ExchangeFilterFunction = ExchangeFilterFunction.ofResponseProcessor { response ->
        // Skip body logging for 429 (rate limit) and 422 (we need body later for error parsing)
        if (response.statusCode().isError && response.statusCode().value() != 429 && response.statusCode().value() != 422) {
            response.bodyToMono(String::class.java).defaultIfEmpty("")
                .flatMap { body ->
                    val msg = "Simpli.fi API error ${response.statusCode().value()}: ${body.take(500)}"
                    log.error(msg)
                    Mono.just(response)
                }
        } else {
            Mono.just(response)
        }
    }

    init {
        val builder = WebClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader("X-App-Key", appKey)
            .defaultHeader("X-User-Key", userKey)
            .filter(requestLoggingFilter)
            .filter(responseLoggingFilter)
            .filter(errorLoggingFilter)

        apiVersion?.let { builder.defaultHeader("X-Api-Version", it) }

        client = builder.build()
    }








    /**
     * Retry configuration for handling 429 Rate Limit errors
     * Wait rateLimitInitialWait milliseconds between retries, up to a max of rateLimitMaxBackoff milliseconds
     * Retry a total of rateLimitMaxRetries times
     */
    private val rateLimitRetry: Retry =
        Retry.backoff(rateLimitMaxRetries, Duration.ofMillis(rateLimitInitialWait))
            .maxBackoff(Duration.ofMillis(rateLimitMaxBackoff))
            .filter { ex -> ex is WebClientResponseException && ex.statusCode.value() == 429 }
            .doBeforeRetry { signal ->
                log.warn(
                    "Retrying due to 429 (attempt {} of {})",
                    signal.totalRetries() + 1,
                    rateLimitMaxRetries
                )
            }
            .onRetryExhaustedThrow { _, signal ->
                throw SimplifiRateLimitRetriesExhausted(
                    "Failed after ${signal.totalRetries()} retries due to rate limiting",
                    signal.failure() as Exception
                )
            }

    /**
     * Retry configuration for handling 5xx Server errors
     * Wait serverErrorInitialWait milliseconds between retries, up to a max of serverErrorMaxBackoff milliseconds
     * Retry a total of serverErrorMaxRetries times
     */
    private val serverErrorRetry: Retry =
        Retry.backoff(serverErrorMaxRetries, Duration.ofMillis(serverErrorInitialWait))
            .maxBackoff(Duration.ofMillis(serverErrorMaxBackoff))
            .filter { ex -> ex is WebClientResponseException && ex.statusCode.is5xxServerError }
            .doBeforeRetry { signal ->
                log.warn(
                    "Retrying due to 5xx (attempt {} of {})",
                    signal.totalRetries() + 1,
                    serverErrorMaxRetries
                )
            }
            .onRetryExhaustedThrow { _, signal ->
                throw SimplifiServerExceptionRetriesExhausted(
                    "Failed after ${signal.totalRetries()} retries due to server error",
                    signal.failure() as Exception
                )
            }

    /**
     * Standard retrieval with error handling
     * for 422, 429 and 5xx errors
     * @param bodyClass The class of the response body
     * @return Mono of the response body
     */
    private fun <T> WebClient.RequestHeadersSpec<*>.retrieveWithStandardHandling(bodyClass: Class<T>): Mono<T> {
        return this.retrieve()
            // 422 Unprocessable Entity from SimpliFi contain an ErrorsResponse json
            // grab this here so we don't lose the body
            .onStatus({ it.value() == 422 }) { resp ->
                resp.bodyToMono(ErrorsResponse::class.java)
                    .defaultIfEmpty(ErrorsResponse())
                    .flatMap { errors -> Mono.error(SimplifiUnprocessableEntityException(errors)) }
            }
            // Convert a 429 response code to an exception we can act on below for retry
            .onStatus({ it.value() == 429 }) { resp ->
                resp.bodyToMono(String::class.java)
                    .defaultIfEmpty("")
                    .flatMap { body ->
                        Mono.error(
                            WebClientResponseException.create(
                                429,
                                "Too Many Requests",
                                resp.headers().asHttpHeaders(),
                                body.toByteArray(),
                                null
                            )
                        )
                    }
            }
            // Convert a 5xx response code to an exception we can act on below for retry
            .onStatus({ it.is5xxServerError }) { resp ->
                resp.bodyToMono(String::class.java).defaultIfEmpty("").flatMap { body ->
                    Mono.error(
                        WebClientResponseException.create(
                            resp.statusCode().value(),
                            "SimpliFi Server Error",
                            resp.headers().asHttpHeaders(),
                            body.toByteArray(),
                            null
                        )
                    )
                }
            }
            .bodyToMono(bodyClass)
            .retryWhen(rateLimitRetry)
            .retryWhen(serverErrorRetry)
    }

    // ----------------------------------------------------------------------
    // Endpoint implementations
    // ----------------------------------------------------------------------

    /**
     * Get organization by ID
     * @param id The organization ID to retrieve
     * @return The Organization object
     *
     * Returns a 404 if the organization is not found
     */
    fun getOrganizationById(id: Long): Organization? {
        return client.get()
            .uri { builder -> builder.path("/organizations/$id").build() }
            .retrieveWithStandardHandling(OrganizationsResponse::class.java)
            .mapNotNull { it.organizations.firstOrNull() }
            .block()
    }

    /**
     * Get organization by custom ID
     * @param customId The custom ID to search for
     * @return The Organization object
     *
     * Returns null if the organization is not found
     */
    fun getOrganizationByCustomId(customId: String): Organization? {
        return client.get()
            .uri { builder ->
                val uriBuilder = builder.path("/organizations")
                uriBuilder.queryParam("custom_id", customId)
                uriBuilder.build()
            }
            .retrieveWithStandardHandling(OrganizationsResponse::class.java)
            .mapNotNull { it.organizations.firstOrNull() }
            .block()
    }

    /**
     * Create organization
     * @param body The organization creation details
     * @return The created Organization object
     */
    fun createOrganization(body: OrganizationCreateBody): Organization? {
        return client.post()
            .uri("/organizations")
            .bodyValue(OrganizationCreateRequest(body))
            .retrieveWithStandardHandling(OrganizationsResponse::class.java)
            .mapNotNull { it.organizations.firstOrNull() }
            .block()
    }

    /**
     * This is not implemented BECAUSE an organization tag is created automatically with the organization!!
     */
    fun createOrganizationTag() {
        throw NotImplementedError("Not implemented yet")
    }

    /**
     * Get organization tags
     * @param orgId The organization ID - this will be the Audience organization ID
     * @param firstPartySegmentId Optional first party segment ID to filter tags
     * @return List of OrganizationTag objects
     *
     * IMPORTANT - an organization tag is created automatically with the organization!!
     * USAGE - We need the audienceOrgTagId to create the firstPartySegment
     */
    fun getOrganizationTags(orgId: Long, firstPartySegmentId: Long? = null): List<OrganizationTag> {
        return client.get()
            .uri { b ->
                var u = b.path("/organizations/$orgId/organization_tags")
                firstPartySegmentId?.let { u = u.queryParam("first_party_segment_id", it) }
                u.build()
            }
            .retrieveWithStandardHandling(OrganizationTagsResponse::class.java)
            .map { it.organizationTags }
            .block() ?: emptyList()
    }

    /**
     * Create first party segment
     * @param orgId The organization ID - this will be the Audience organization ID where the segment should be created
     * @param segment The FirstPartySegmentCreateBody to be sent
     * @return The created FirstPartySegment object
     */
    fun createFirstPartySegment(orgId: Long, segment: FirstPartySegmentCreateBody): FirstPartySegment? {
        return client.post()
            .uri("/organizations/$orgId/first_party_segments")
            .bodyValue(FirstPartySegmentCreateRequest(segment))
            .retrieveWithStandardHandling(FirstPartySegmentsResponse::class.java)
            .mapNotNull { it.firstPartySegments.firstOrNull() }
            .block()
    }

    /**
     * List campaigns
     * @param orgId The organization ID - this will be the Audience organization ID
     * @param children Whether to include campaigns from child organizations, defaults to false
     * @param size page size for pagination - defaults to 100. The max is 500, but could not do more than ~150 without getting a data buffer exception
     * @param page Optional page number for pagination
     * @param attributesOnly Whether to omit the lists of actions and resources, defaults to true
     * @return CampaignsPagedResponse object containing list of Campaign objects and paging info
     *
     * NOTE: We do not include any of the non-Attributes in the models, so we never need to include them in the responses
     */
    fun listCampaigns(orgId: Long, children: Boolean = false, size: Int = 100, page: Int? = null, attributesOnly: Boolean = true): CampaignsPagedResponse? {
        return client.get()
            .uri { builder ->
                var uriBuilder = builder.path("/organizations/$orgId/campaigns")
                    .queryParam("size", size)
                if (children) uriBuilder = uriBuilder.queryParam("children", "true")
                if (attributesOnly) uriBuilder = uriBuilder.queryParam("attributes_only", "true")
                page?.let { uriBuilder = uriBuilder.queryParam("page", it) }
                uriBuilder.build()
            }
            .retrieveWithStandardHandling(CampaignsPagedResponse::class.java)
            .block()
    }

    /**
     * List campaigns next page
     * @param nextPage The next page URL from the previous response
     * @return CampaignsPagedResponse object containing list of Campaign objects and paging info
     *
     * USAGE - called from CampaignsPagedResponse.getNextPage, when nextPage is populated to get the next page of results
     * NOTE: nextPage already has the baseUrl included
     * NOTE: nextPage copies over all queryParams from the previous request
     */
    fun listCampaignsNextPage(nextPage: String): CampaignsPagedResponse? {

        // strip the base url
        val pathWithQueryParams = nextPage.replace(baseUrl, "")
        return client.get()
            .uri(pathWithQueryParams)
            .retrieveWithStandardHandling(CampaignsPagedResponse::class.java)
            .block()
    }

    /**
     * Get campaign
     * @param simplifiCampaignId The campaign ID of the Simpli.Fi campaign to retrieve
     * @return The Campaign object
     *
     *  Returns a 404 if the organization is not found
     */
    fun getCampaignById(simplifiCampaignId: Long): Campaign? {
        return client.get()
            .uri { builder -> builder.path("/campaigns/$simplifiCampaignId").build() }
            .retrieveWithStandardHandling(CampaignsPagedResponse::class.java)
            .mapNotNull { it.campaigns.firstOrNull() }
            .block()
    }

    /**
     * Create campaign
     * @param orgId The organization ID - this will be the Audience organization ID where the campaign should be created
     * @param campaignCreateBody The createBody of the Campaign to be created
     * @return The created Campaign object
     */
    fun createCampaign(orgId: Long, campaignCreateBody: CampaignCreateBody): Campaign? {
        return client.post()
            .uri("/organizations/$orgId/campaigns")
            .bodyValue(CampaignCreateRequest(campaignCreateBody))
            .retrieveWithStandardHandling(CampaignsPagedResponse::class.java)
            .mapNotNull { it.campaigns.firstOrNull() }
            .block()
    }

    /**
     * Update campaign
     * @param orgId The organization ID - this will be the Audience organization ID where the campaign is located
     * @param simplifiCampaignId The campaign ID of the Simpli.Fi campaign to update
     * @param campaignUpdateBody The updateBody containing the values to update (only the values filled in will be updated)
     * @return The updated Campaign object
     */
    fun updateCampaign(orgId: Long, simplifiCampaignId: Long, campaignUpdateBody: CampaignUpdateBody): Campaign? {
        return client.put()
            .uri("/organizations/$orgId/campaigns/$simplifiCampaignId")
            .bodyValue(CampaignUpdateRequest(campaignUpdateBody))
            .retrieveWithStandardHandling(CampaignsPagedResponse::class.java)
            .mapNotNull { it.campaigns.firstOrNull() }
            .block()
    }

    /**
     * Change campaign first party segments
     * @param simplifiCampaignId The campaign ID of the Simpli.Fi campaign to update
     * @param campaignFirstPartySegmentChangeBody The CampaignFirstPartySegmentChangeBody to send with the request
     * @return The response containing the results of the change
     *
     * USAGE - used to add the Site Retargeting Audience to the Campaign
     */
    fun changeCampaignFirstPartySegments(
        simplifiCampaignId: Long,
        campaignFirstPartySegmentChangeBody: CampaignFirstPartySegmentChangeBody
    ): CampaignFirstPartySegmentChangeResponse? {
        return client.put()
            .uri("/campaigns/$simplifiCampaignId/campaign_first_party_segments/change")
            .bodyValue(CampaignFirstPartySegmentChangeRequest(campaignFirstPartySegmentChangeBody))
            .retrieveWithStandardHandling(CampaignFirstPartySegmentChangeResponse::class.java)
            .block()
    }

    /**
     * Activate campaign
     * @param audienceOrgId The organization ID - this will be the Audience organization ID where the campaign is located
     * @param simplifiCampaignId The campaign ID of the Simpli.Fi campaign to activate
     * @return The activated Campaign object
     */
    fun activateCampaign(audienceOrgId: Long, simplifiCampaignId: Long): Campaign? {
        return client.post()
            .uri("/organizations/$audienceOrgId/campaigns/$simplifiCampaignId/activate")
            .retrieveWithStandardHandling(CampaignsPagedResponse::class.java)
            .mapNotNull { it.campaigns.firstOrNull() }
            .block()
    }

    /**
     * Pause campaign
     * @param audienceOrgId The organization ID - this will be the Audience organization ID where the campaign is located
     * @param simplifiCampaignId The campaign ID of the Simpli.Fi campaign to pause
     * @return The paused Campaign object
     */
    fun pauseCampaign(audienceOrgId: Long, simplifiCampaignId: Long): Campaign? {
        return client.post()
            .uri("/organizations/$audienceOrgId/campaigns/$simplifiCampaignId/pause")
            .retrieveWithStandardHandling(CampaignsPagedResponse::class.java)
            .mapNotNull { it.campaigns.firstOrNull() }
            .block()
    }

    /**
     * List ads for a campaign
     * @param audienceOrgId The organization ID - this will be the Audience organization ID where the campaign is located
     * @param simplifiCampaignId The campaign ID of the Simpli.Fi campaign
     * @param allowDeleted Whether to include deleted ads - not included by default
     * @param attributesOnly Whether to omit the lists of actions and resources, defaults to true
     * @param include include parameter to include additional related data - can be filled with any subset of ad_file_types,ad_sizes,ad_placements
     *   defaults to ad_file_types,ad_sizes because we don't use ad_placements
     *   set to null if you don't want this data returned
     * @return List of Ads
     *
     * NOTE: We do not include any of the non-Attributes in the models, so we never need to include them in the responses
     * NOTE: No paging for this endpoint (verified with documentation)
     */
    fun listAds(
        audienceOrgId: Long,
        simplifiCampaignId: Long,
        allowDeleted: Boolean = false,
        attributesOnly: Boolean = true,
        include: String? = "ad_file_types,ad_sizes",
    ): List<Ad> {
        return client.get()
            .uri { builder ->
                var uriBuilder = builder.path("/organizations/$audienceOrgId/campaigns/$simplifiCampaignId/ads")
                include?.let { uriBuilder = uriBuilder.queryParam("include", it) }
                if (allowDeleted) uriBuilder = uriBuilder.queryParam("allow_deleted", "true")
                if (attributesOnly) uriBuilder = uriBuilder.queryParam("attributes_only", "true")
                uriBuilder.build()
            }
            .retrieveWithStandardHandling(AdsResponse::class.java)
            .map { it.ads }
            .block() ?: emptyList()
    }


    /**
     * Create ad for a campaign
     * @param audienceOrgId The organization ID - this will be the Audience organization ID where the campaign is located
     * @param simplifiCampaignId The campaign ID of the Simpli.Fi campaign
     * @param adCreateBody The AdCreateBody to be sent
     * @return The created Ad object
     */
    fun createAd(audienceOrgId: Long, simplifiCampaignId: Long, adCreateBody: AdCreateBody): Ad? {
        return client.post()
            .uri("/organizations/$audienceOrgId/campaigns/$simplifiCampaignId/ads")
            .bodyValue(AdCreateRequest(adCreateBody))
            .retrieveWithStandardHandling(AdsResponse::class.java)
            .mapNotNull { it.ads.firstOrNull() }
            .block()
    }

    /**
     * Update ad for a campaign
     * @param audienceOrgId The organization ID - this will be the Audience organization ID where the campaign is located
     * @param simplifiCampaignId The campaign ID of the Simpli.Fi campaign where the ad is located
     * @param adId The ad ID to be updated
     * @param adUpdateBody The AdUpdateBody containing the values to update (only the values filled in will be updated)
     * @return The updated Ad object
     */
    fun updateAd(audienceOrgId: Long, simplifiCampaignId: Long, adId: Long, adUpdateBody: AdUpdateBody): Ad? {
        return client.put()
            .uri("/organizations/$audienceOrgId/campaigns/$simplifiCampaignId/ads/$adId")
            .bodyValue(AdUpdateRequest(adUpdateBody))
            .retrieveWithStandardHandling(AdsResponse::class.java)
            .mapNotNull { it.ads.firstOrNull() }
            .block()
    }

    /**
     * Get campaign stats
     * @param orgId The organization ID - this will be the Audience organization ID
     * @param startDate Optional start date filter (YYYY-MM-DD), defaults to today. Must be within two years
     * @param endDate Optional end date filter (YYYY-MM-DD), default to today
     * @param byDay Whether to break down stats by day (default is false)
     * @param byAd Whether to break down stats by ad (default is false)
     * @param size page size for pagination - defaults to 100. The max is 500
     * @param page Optional page number for pagination
     * @return List of CampaignStat objects
     *
     *  USAGE - pass the orgId and the startDate equivalent to the startDate of the campaign
     *  This endpoint supports paging, realistically we will never see it unless we use the byDay option (which we won't)
     */
    fun getCampaignStats(
        orgId: Long,
        startDate: String? = null,
        endDate: String? = null,
        byDay: Boolean = false,
        byAd: Boolean = false,
        size: Int = 100,
        page: Int? = null,
    ): CampaignStatsPagedResponse? {
        return client.get()
            .uri { builder ->
                var uriBuilder = builder.path("/organizations/$orgId/campaign_stats")
                    .queryParam("size", size)
                startDate?.let { uriBuilder = uriBuilder.queryParam("start_date", it) }
                endDate?.let { uriBuilder = uriBuilder.queryParam("end_date", it) }
                page?.let { uriBuilder = uriBuilder.queryParam("page", it) }
                if (byDay) uriBuilder = uriBuilder.queryParam("by_day", "true")
                if (byAd) uriBuilder = uriBuilder.queryParam("by_ad", "true")
                uriBuilder.build()
            }
            .retrieveWithStandardHandling(CampaignStatsPagedResponse::class.java)
            .block()
    }

    /**
     * Get campaign stats next page
     * @param nextPage The next page URL from the previous response
     * @return CampaignStatsPagedResponse object containing list of CampaignStat objects and paging info
     *
     * USAGE - called from CampaignStatsPagedResponse.getNextPage, when nextPage is populated to get the next page of results
     * NOTE: nextPage already has the baseUrl included
     * NOTE: nextPage copies over all queryParams from the previous request
     */
    fun getCampaignStatsNextPage(nextPage: String): CampaignStatsPagedResponse? {

        // strip the base url
        val pathWithQueryParams = nextPage.replace(baseUrl, "")
        return client.get()
            .uri(pathWithQueryParams)
            .retrieveWithStandardHandling(CampaignStatsPagedResponse::class.java)
            .block()
    }

    /**
     * List campaign first party segments
     * @param simplifiCampaignId The campaign ID
     * @param size page size for pagination - defaults to 100. The max is 500
     * @param page Optional page number for pagination
     * @param attributesOnly Whether to omit the lists of actions and resources, defaults to true
     * @return CampaignFirstPartySegmentsPagedResponse object containing list of CampaignFirstPartySegment objects and paging info
     */
    fun listCampaignFirstPartySegments(
        simplifiCampaignId: Long,
        size: Int = 100,
        page: Int? = null,
        attributesOnly: Boolean = true,
    ): CampaignFirstPartySegmentsPagedResponse? {
        return client.get()
            .uri { builder ->
                var uriBuilder = builder.path("/campaigns/$simplifiCampaignId/campaign_first_party_segments")
                    .queryParam("size", size)
                page?.let { uriBuilder = uriBuilder.queryParam("page", it) }
                if (attributesOnly) uriBuilder = uriBuilder.queryParam("attributes_only", "true")
                uriBuilder.build()
            }
            .retrieveWithStandardHandling(CampaignFirstPartySegmentsPagedResponse::class.java)
            .block()
    }

    /**
     * listCampaignFirstPartySegmentsNextPage
     * @param nextPage The next page URL from the previous response
     * @return CampaignFirstPartySegmentsPagedResponse object containing list of CampaignFirstPartySegment objects and paging info
     *
     * USAGE - called after listCampaignFirstPartySegments, when nextPage is populated to get the next page of results
     * NOTE: nextPage already has the baseUrl included
     * NOTE: nextPage copies over all queryParams from the previous request
     */
    fun listCampaignFirstPartySegmentsNextPage(nextPage: String): CampaignFirstPartySegmentsPagedResponse? {

        // strip the base url
        val pathWithQueryParams = nextPage.replace(baseUrl, "")
        return client.get()
            .uri(pathWithQueryParams)
            .retrieveWithStandardHandling(CampaignFirstPartySegmentsPagedResponse::class.java)
            .block()
    }

    companion object {
        private val log = LoggerFactory.getLogger(SimplifiApiClient::class.java)
    }
}
