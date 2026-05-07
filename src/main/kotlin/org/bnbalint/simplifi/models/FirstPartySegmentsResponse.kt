package org.bnbalint.simplifi.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * Response model for first party segments.
 * @param firstPartySegments The list of first party segments
 * @param paging Paging information for the response - may be null if not paginated (e.g. after a create)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class FirstPartySegmentsResponse(
    val firstPartySegments: List<FirstPartySegment> = emptyList(),
    val paging: Paging?
)
