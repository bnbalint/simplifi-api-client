package org.bnbalint.simplifi.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.bnbalint.simplifi.SimplifiApiClient

/**
 * Response model for campaign first party segments.
 * @param campaignFirstPartySegments The list of campaign first party segment assignments
 * @param paging Paging information for the response
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CampaignFirstPartySegmentsPagedResponse(
    val campaignFirstPartySegments: List<CampaignFirstPartySegment> = emptyList(),
    val paging: Paging
) {

    /**
     * Check if there is a next page of campaignFirstPartySegments.
     * @return True if there is a next page, false otherwise.
     */
    fun hasNextPage(): Boolean {
        return paging.next != null
    }

    /**
     * Get the next page of campaignFirstPartySegments if available.
     * @param apiClient The SimpliFi API client to use for the request.
     * @return The next page of campaigns, or an empty response if no next page is available.
     */
    fun getNextPage(apiClient: SimplifiApiClient): CampaignFirstPartySegmentsPagedResponse? {
        val nextPage = paging.next
        if (nextPage != null) {
            return apiClient.listCampaignFirstPartySegmentsNextPage(nextPage)
        } else {
            // No next page, send empty response
            return CampaignFirstPartySegmentsPagedResponse(
                campaignFirstPartySegments = emptyList(),
                paging = Paging(page = 1, size = 100, total = 0, next = null)
            )
        }
    }
}

