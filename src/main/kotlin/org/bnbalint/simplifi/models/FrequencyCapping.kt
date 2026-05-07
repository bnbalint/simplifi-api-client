package org.bnbalint.simplifi.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * Model representing frequency capping settings for ad campaigns.
 * @param howManyTimes The maximum number of times an ad can be shown to a user.
 * @param hours The time frame in hours during which the frequency cap applies.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class FrequencyCapping(
    val howManyTimes: Int,
    val hours: Int
)
