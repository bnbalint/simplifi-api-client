package org.bnbalint.simplifi.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * Response model for a list of errors
 * @param errors The list of errors
 *
 * USAGE: this can be returned from the SimpliFi API for a bad request
 * e.g. when trying to create an entity when the name is already taken
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ErrorsResponse(
    val errors: List<String> = emptyList()
)
