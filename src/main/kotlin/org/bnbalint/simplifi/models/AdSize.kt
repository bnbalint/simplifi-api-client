package org.bnbalint.simplifi.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * An ad size that can be used for an ad.
 * @param id The unique identifier for the ad size
 * @param name The name of the ad size
 * @param width The width of the ad size in pixels
 * @param height The height of the ad size in pixels
 * @param allowedAudioCompanionSize Whether audio companion size is allowed
 * @param resource The URL to this ad size resource
 *
 * NOTE: https://app.simpli.fi/api/ad_sizes will return all valid id/name combinations
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class AdSize(
    val id: Int,
    val name: String,
    val width: Int,
    val height: Int,
    val allowedAudioCompanionSize: Boolean,

    // url to this organization resource
    val resource: String
)
