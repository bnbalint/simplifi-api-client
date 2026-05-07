package org.bnbalint.simplifi.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.bnbalint.simplifi.SimplifiApiClient

/**
 * Response model for a list of campaign stats.
 * @param campaignStats The list of campaign stats.
 * @param paging Paging information for the response
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CampaignStatsPagedResponse(
    val campaignStats: List<CampaignStat> = emptyList(),
    val paging: Paging
) {

    /**
     * Check if there is a next page of campaign stats.
     * @return True if there is a next page, false otherwise.
     */
    fun hasNextPage(): Boolean {
        return paging.next != null
    }

    /**
     * Get the next page of campaign stats if available.
     * @param apiClient The SimpliFi API client to use for the request.
     * @return The next page of campaign stats, or an empty response if no next page is available.
     */
    fun getNextPage(apiClient: SimplifiApiClient): CampaignStatsPagedResponse? {
        val nextPage = paging.next
        if (nextPage != null) {
            return apiClient.getCampaignStatsNextPage(nextPage)
        } else {
            // No next page, send empty response
            return CampaignStatsPagedResponse(
                campaignStats = emptyList(),
                paging = Paging(page = 1, size = 100, total = 0, next = null)
            )
        }
    }
}
