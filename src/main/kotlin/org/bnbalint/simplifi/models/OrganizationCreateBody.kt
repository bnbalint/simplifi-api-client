package org.bnbalint.simplifi.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * Request body for creating a new organization.
 * @param name The name of the organization.
 * @param parentId The ID of the parent organization.
 * @param customId An optional custom ID for the organization.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class OrganizationCreateBody(
    val name: String,
    val parentId: String,

    // allowed to be null, but we will be using this
    val customId: String? = null
)
