package org.bnbalint.simplifi.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * Model representing the addition of a first-party segment to a campaign.
 * @param firstPartySegmentId The ID of the first-party segment to add.
 * @param segmentTypeId The type ID of the segment.
 * @param segmentTargetTypeId The target type ID of the segment.
 *
 * USAGE: Used to add the Site Retargeting Audience to the Campaign
 * When adding:
 * segmentTypeId = 1
 * segmentTargetTypeId = 2
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CampaignFirstPartySegmentAdd(
    val firstPartySegmentId: Long,
    val segmentTypeId: Int,
    val segmentTargetTypeId: Int
)
