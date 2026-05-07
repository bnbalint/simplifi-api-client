package org.bnbalint.simplifi.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * An ad file type that can be used for an ad.
 * @param id The unique identifier for the ad file type
 * @param name The name of the ad file type
 * @param resource The URL to this ad file type resource
 *
 * NOTE: https://app.simpli.fi/api/ad_file_types will return all valid id/name combinations
 * NOTE: We will be using id = 4, name = HTML
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class AdFileType(
    val id: Int,
    val name: String,

    // url to this organization resource
    val resource: String
)
