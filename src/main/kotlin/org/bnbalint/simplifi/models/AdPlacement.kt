package org.bnbalint.simplifi.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * An ad placement where an ad can be served.
 * @param id The unique identifier for the ad placement
 * @param name The name of the ad placement
 * @param resource The URL to this ad placement resource
 *
 * NOTE: https://app.simpli.fi/api/ad_placements will return all valid id/name combinations
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class AdPlacement(
    val id: Int,
    val name: String,

    // url to this organization resource
    val resource: String
)
