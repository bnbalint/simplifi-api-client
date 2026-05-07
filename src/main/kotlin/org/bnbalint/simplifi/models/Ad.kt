package org.bnbalint.simplifi.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * Ad model
 * @param id The unique identifier for the ad
 * @param name The name of the ad
 * @param status The status of the ad
 * @param pacing The pacing value of the ad
 * @param clickTagVerified Whether the click tag is verified
 * @param previewTag The preview tag of the ad
 * @param targetUrl The target URL of the ad
 * @param adFileTypes The list of ad file types associated with the ad
 * @param adSizes The list of ad sizes associated with the ad
 * @param adPlacements The list of ad placements associated with the ad
 * @param resource The URL to this ad resource
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class Ad(
    val id: Long,
    val name: String,
    val status: String,
    val pacing: Double,
    val clickTagVerified: Boolean,
    val previewTag: String,
    val targetUrl: String,
    val adFileTypes: List<AdFileType> = emptyList(),
    val adSizes: List<AdSize> = emptyList(),
    val adPlacements: List<AdPlacement> = emptyList(),

    // url to this organization resource
    val resource: String
)
