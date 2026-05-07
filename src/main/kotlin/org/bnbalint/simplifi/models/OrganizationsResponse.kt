package org.bnbalint.simplifi.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * Response model for a list of organizations.
 * @param organizations The list of organizations.
 * @param paging Paging information for the response - may be null if not paginated (e.g. getting by ID)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class OrganizationsResponse(
    val organizations: List<Organization> = emptyList(),
    val paging: Paging?
)
