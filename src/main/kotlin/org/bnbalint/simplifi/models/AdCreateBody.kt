package org.bnbalint.simplifi.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * Body for creating an ad
 * similar to AdUpdateBody but all fields required
 * -----------------------------------------
 * @param name Name of the ad
 * @param adFileTypeId ID of the ad file type
 * @param adSizeId ID of the ad size
 * @param adPlacementId ID of the ad placement (optional)
 * @param targetUrl Target URL of the ad
 * @param html HTML content of the ad
 *
 * NOTE: adFileTypeId 4 is for HTML
 * ad_size_id
 * 1 = 300x250
 * 2 = 728x90
 * 3 = 160x600
 * 19 = 300x600
 * 28 = 320x50
 * 29 = 300x50
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class AdCreateBody(
    val name: String,
    val adFileTypeId: String,
    val adSizeId: String,
    val adPlacementId: Int? = null,
    val targetUrl: String,
    val html: String
    // technically supports extraHtml, but we aren't using it
)
