# Overview
Kotlin WebFlux blocking client for Simpli.Fi REST endpoints. <br> 
NOTE: This client implements a specific **subset** of the available endpoints for a proposed integration. <br>
Each method executes immediately and returns domain models from Simpli.Fi. <br>
429 and 5xx errors are retried with configurable backoff.


# Documentation
Simpli.Fi API documentation can be found [here](https://app.simpli.fi/apidocs). <br>


# Authentication
Each request is authenticated using headers. <br>
The `appKey` is sent via the `X-App-Key` header and the `userKey` is sent via the `X-User-Key` header. <br>


# Optional API Versioning
An optional `X-Api-Version` header can be attached to the request (populated with `apiVersion`) if provided. <br>
We are not currently intending to use this so the support is basic. <br>
Technically you would probably want to send a specific version **per request** rather than per client instance. <br>
This is because specific endpoints may be deprecated at different times. <br>
The current implementation will attach the version to **all requests** made by the client instance. <br>


# Retry Logic
Two retry policies are configured one for 429 (rate limiting) and one for 5xx (server errors). <br>
The specific configuration values for each policy are provided via the constructor. <br>
If the retries are unable to successfully complete the request, a specific exception is thrown: <br>
- `SimplifiRateLimitRetriesExhausted` for 429 retries <br>
- `SimplifiServerExceptionRetriesExhausted` for 5xx retries <br>


# Endpoints
Single-item methods unwrap first element or return null. <br>
List methods return an empty list if absent. <br>
Paged endpoints returned the PagedResponse type. <br>

- getOrganizationById
  - GET `/organizations/{id}`
  - Returns a 404 if the organization is not found
- getOrganizationByCustomId
  - GET `/organizations?custom_id={customId}`
  - Returns null if an organization is not found
- createOrganization
  - POST `/organizations`
- createOrganizationTag
  - THIS IS NOT IMPLEMENTED
  - We get an organization tag for free when we create the organization
- getOrganizationTags
  - GET `/organizations/{orgId}/organization_tags[?first_party_segment_id={firstPartySegmentId}]`
- createFirstPartySegment
  - POST `/organizations/{orgId}/first_party_segments`
- changeCampaignFirstPartySegments
  - PUT `/campaigns/{campaignId}/campaign_first_party_segments/change`
- listCampaignFirstPartySegments
  - GET `/campaigns/{campaignId}/campaign_first_party_segments[&page={page}][&size={size}][&attributes_only=true]`
  - Returns a PagedResponse
- listCampaignFirstPartySegmentsNextPage
  - GET the nextPageUrl from a previous PagedResponse of listCampaignFirstPartySegments
- listCampaigns
  - GET `/organizations/{orgId}/campaigns[?children=false][&page={page}][&size={size}][&attributes_only=true]`
  - Returns a PagedResponse
- listCampaignsNextPage
  - GET the nextPageUrl from a previous PagedResponse of listCampaigns
- getCampaignById
  - GET `/campaigns/{campaignId}`
  - Returns a 404 if the organization is not found
- createCampaign
  - POST `/organizations/{orgId}/campaigns`
- updateCampaign
  - PUT `/organizations/{orgId}/campaigns/{campaignId}`
- activateCampaign
  - POST `/organizations/{orgId}/campaigns/{campaignId}/activate`
- pauseCampaign
  - POST `/organizations/{orgId}/campaigns/{campaignId}/pause`
- getCampaignStats
  - GET `/organizations/{orgId}/campaign_stats[?start_date={startDate}][&end_date={endDate}][&by_day=false][&by_ad=false][&size={size}][&page={page}]`
  - Returns a PagedResponse
- getCampaignStatsNextPage
  - GET the nextPageUrl from a previous PagedResponse of getCampaignStats
- listAds
  - GET `/organizations/{orgId}/campaigns/{campaignId}/ads[&allow_deleted=true][&attributes_only=true][?include={include}]`
  - NOT paged
  - include defaults to ad_file_types,ad_sizes because we don't use ad_placements
- createAd
  - POST `/organizations/{orgId}/campaigns/{campaignId}/ads`
- updateAd
  - PUT `/organizations/{orgId}/campaigns/{campaignId}/ads/{adId}`


## Exceptions

The client will throw specific exceptions for error conditions. <br>
- SimplifiUnprocessableEntityException
  - thrown when a 422 is encountered from Simpli.Fi. The underlying message from Simpli.Fi is included in the exception message.
- SimplifiRateLimitRetriesExhausted
- SimplifiServerExceptionRetriesExhausted



## Errors

The following status codes have been observed in testing:
- 404 Not Found
  - the id provided did not match any resources
- 422 Unprocessable Entity
  - this will cause the endpoint to return a SimplifiUnprocessableEntityException from the client
  - possible causes:
    - the body provided to the endpoint was incorrect
    - trying to create an entity with a name that already exists
- 403 Forbidden
  - occurred when the id provided did not make sense
  - an audienceOrganizationId was provided when a campaignId was expected
- 200 OK but response failed with cause: : org.springframework.core.io.buffer.DataBufferLimitException: Exceeded limit on max bytes to buffer : 262144
  - the page size is too high so the response size is too large
  - we can set the attributes_only to true to reduce the response size - we don't store any of the non-attributes (actions, etc) anyway

The following status codes are documented by the Simpli.Fi API documentation:
- 429
  - rate limit exceeded




## Paging

We are unlikely to require paging in our usage of the Simpli.Fi API. <br>
However, some endpoints **can** return paged information and so basic support is provided. <br>
These endpoints are:
- listCampaigns
- getCampaignStats
  - this endpoint would only provide multiple pages if by_day or by_ad were used (which we don't intend to do)
- listCampaignFirstPartySegments
  - likely to never use this - we only create one of these per campaign
<br><br>

The maximum number of items that can be returned per page is 500, however we default to 100 to avoid the DataBufferLimitException.
This value can be overridden via the `size` parameter on the relevant methods. <br>


