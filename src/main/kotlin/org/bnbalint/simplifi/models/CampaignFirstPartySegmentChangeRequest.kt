package org.bnbalint.simplifi.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * Request model for changing campaign first-party segments
 * @param campaignFirstPartySegments The details of the first-party segments to be changed
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CampaignFirstPartySegmentChangeRequest(
    val campaignFirstPartySegments: CampaignFirstPartySegmentChangeBody
)
