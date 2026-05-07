package org.bnbalint.simplifi.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming


/**
 * Campaign Update Body
 * during the first update (when we configure the new campaign)
 * we will populate all fields (NOTE that only customId is nullable in SimpliFi)
 * Other updates (updating the impressionCap, etc) may only populate a subset of these fields
 * ------------------------------------------------------------
 * @param name Name of the campaign
 * @param customId Custom ID of the campaign
 * @param impressionCap Total impression cap for the campaign
 * @param startDate Start date of the campaign (YYYY-MM-DD)
 * @param endDate End date of the campaign (YYYY-MM-DD)
 * @param dailyImpressionCap Daily impression cap for the campaign
 * @param autoAdjustDailyImpressionCap Whether to auto adjust daily impression cap
 * @param mediaTypeId Media type ID for the campaign
 * @param autoOptimize Whether to auto optimize the campaign
 * @param bid Bid amount for the campaign
 * @param bidTypeId Bid type ID for the campaign
 * @param frequencyCapping Frequency capping settings for the campaign
 * @param campaignTypeId Campaign type ID for the campaign
 * @param campaignGoal Campaign goal settings for the campaign
 * @param geoTargetIds List of geo target IDs for the campaign
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class CampaignUpdateBody(
    val name: String? = null,

    // allowed to be null - but we will be using this
    val customId: String? = null,
    val impressionCap: Long? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val dailyImpressionCap: Long? = null,
    val autoAdjustDailyImpressionCap: Boolean? = null,
    val mediaTypeId: Int? = null,
    val autoOptimize: Boolean? = null,
    val bid: Double? = null,
    val bidTypeId: Int? = null,
    val frequencyCapping: FrequencyCapping? = null,
    val campaignTypeId: Int? = null,
    val campaignGoal: CampaignGoal? = null,
    val geoTargetIds: List<String>? = null
)
