package org.bnbalint.simplifi.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * Data model representing an organization tag
 * @param id Unique identifier for the organization tag
 * @param organizationId Identifier for the associated organization
 * @param description Description of the tag
 * @param tag The tag string
 * @param allowUserMatching Flag indicating if user matching is allowed
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class OrganizationTag(
    val id: Long,
    val organizationId: Long,
    val description: String,
    val tag: String,
    val allowUserMatching: Boolean
)
