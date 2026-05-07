package org.bnbalint.simplifi.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * Body for updating an ad
 * Only need to provide the fields you want to update
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
@JsonInclude(JsonInclude.Include.NON_NULL)
data class AdUpdateBody(
    val name: String? = null,
    val adFileTypeId: String? = null,
    val adSizeId: String? = null,
    val adPlacementId: Int? = null,
    val targetUrl: String? = null,
    val html: String? = null
    // technically supports extraHtml, but we aren't using it
)
