package org.bnbalint.simplifi.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * Paging information for paginated API responses.
 * @param page The current page number.
 * @param size The number of items per page.
 * @param total The total number of items available.
 * @param next The full URL for the next page of results.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class Paging(
    val page: Int,
    val size: Int,
    val total: Int,
    val next: String? = null
)
