package org.bnbalint.simplifi.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * Model representing an update to a campaign's first-party segment
 * @param id The ID of the first-party segment
 * @param segmentTargetTypeId The target type ID for the segment
 *
 * NOTE: We don't intend to use this at this time, but it's defined for completeness.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CampaignFirstPartySegmentUpdate(
    val id: Long,
    val segmentTargetTypeId: Int
)
