package org.bnbalint.simplifi.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * Campaign First Party Segment
 * @param id The ID of the campaign first party segment
 * @param firstPartySegmentId The ID of the first party segment
 * @param segmentTypeId The segment type ID
 * @param warningMessage contains details of any issues with the first_party_segments in the campaign. These issues may include a first_party_segment that no longer has rules or a first_party_segment used for conversion that no longer has custom_values. The field is null if there are no issues
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CampaignFirstPartySegment(
    val id: Long,
    val campaignId: Long,
    val firstPartySegmentId: Long,
    val segmentTypeId: Int,
    val warningMessage: String? = null,

    // url to this organization resource
    val resource: String
)

