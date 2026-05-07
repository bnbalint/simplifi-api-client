package org.bnbalint.simplifi.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * Request model for creating a new first party segment
 * @param firstPartySegment The first party segment details to be created
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class FirstPartySegmentCreateRequest(
    val firstPartySegment: FirstPartySegmentCreateBody
)
