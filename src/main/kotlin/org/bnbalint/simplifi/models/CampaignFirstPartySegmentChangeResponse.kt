package org.bnbalint.simplifi.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * Response model for changes made to campaign first-party segments.
 * @param summary A summary of the campaign first-party segments after the changes were made
 * @param added Count of segments added.
 * @param updated Count of segments updated.
 * @param deleted Count of segments deleted.
 * @param segmentMatchType The match type for the segments ("any", or "all")
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CampaignFirstPartySegmentChangeResponse(
    val summary: String,
    val added: ChangeCount,
    val updated: ChangeCount,
    val deleted: ChangeCount,
    val segmentMatchType: String
)
