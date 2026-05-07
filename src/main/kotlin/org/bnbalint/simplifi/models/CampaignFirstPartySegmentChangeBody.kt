package org.bnbalint.simplifi.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * Request body for adding, deleting, or updating first-party segments in a campaign.
 * @param add List of segments to add.
 * @param delete List of segment IDs to delete.
 * @param update List of segments to update.
 * @param segmentMatchType The match type for the segments ("any", or "all")
 *
 * NOTE: We will use segmentMatchType = "any" because we will only have one first-party segment per campaign.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CampaignFirstPartySegmentChangeBody(
    val add: List<CampaignFirstPartySegmentAdd> = emptyList(),

    // no plans to use delete right now
    val delete: List<Long> = emptyList(),

    // no plans to use update right now
    val update: List<CampaignFirstPartySegmentUpdate> = emptyList(),
    val segmentMatchType: String
)
