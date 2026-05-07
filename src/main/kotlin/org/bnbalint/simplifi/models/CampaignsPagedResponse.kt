package org.bnbalint.simplifi.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.bnbalint.simplifi.SimplifiApiClient

/**
 * Response model for a list of campaigns.
 * @param campaigns The list of campaigns.
 * @param paging Paging information for the response - may be null if not paginated (e.g. getting by ID)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CampaignsPagedResponse(
    val campaigns: List<Campaign> = emptyList(),
    val paging: Paging?
) {

    /**
     * Check if there is a next page of campaigns.
     * @return True if there is a next page, false otherwise.
     */
    fun hasNextPage(): Boolean {
        return paging?.next != null
    }

    /**
     * Get the next page of campaigns if available.
     * @param apiClient The SimpliFi API client to use for the request.
     * @return The next page of campaigns, or an empty response if no next page is available.
     */
    fun getNextPage(apiClient: SimplifiApiClient): CampaignsPagedResponse? {
        val nextPage = paging?.next
        if (nextPage != null) {
            return apiClient.listCampaignsNextPage(nextPage)
        } else {
            return CampaignsPagedResponse(campaigns = emptyList(), paging = null) // No next page, send empty response
        }
    }
}
