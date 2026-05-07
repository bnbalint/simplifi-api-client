package org.bnbalint.simplifi.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming


/**
 * Model representing the goal settings of a campaign
 * @param goalValue The target goal value, as a string representing a decimal (e.g., "0.1").
 * @param cpaViewThruPer The cost per action for view-through conversions, as a string representing a decimal (e.g., "1.0").
 * @param cpaClickThruPer The cost per action for click-through conversions, as a string representing a decimal (e.g., "1.0").
 * @param goalType The type of goal (e.g., "ctr", "cpa").
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CampaignGoal(

    // string of a decimal (e.g. 0.1)
    val goalValue: String,

    // string of a decimal (e.g. 1.0)
    val cpaViewThruPer: String,

    // string of a decimal (e.g. 1.0)
    val cpaClickThruPer: String,
    val goalType: String
)
