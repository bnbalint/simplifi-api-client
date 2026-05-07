package org.bnbalint.simplifi.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * Organization data model.
 * @param id Unique identifier for the organization
 * @param name Name of the organization
 * @param customId Custom identifier for the organization
 * @param ancestry Ancestry information for the organization
 * @param publicKey Public key associated with the organization
 * @param website Website URL of the organization (nullable)
 * @param resource URL to this organization resource
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class Organization(
    val id: Long,
    val name: String,
    val customId: String,
    val ancestry: String,
    val publicKey: String,

    // allowed to be null
    val website: String?,

    // url to this organization resource
    val resource: String
)
