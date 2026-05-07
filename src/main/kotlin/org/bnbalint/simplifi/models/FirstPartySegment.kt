package org.bnbalint.simplifi.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * FirstPartySegment model
 * @param id The unique identifier for the first party segment
 * @param name The name of the first party segment
 * @param organizationId The organization ID associated with the first party segment
 * @param companyWide Whether the segment is company-wide
 * @param customValuesEnabled Whether custom values are enabled for the segment
 * @param active Whether the segment is active
 * @param resource The URL to this first party segment resource
 *
 * USAGE: We will create a first party segment for the audience organization
 * The first party segment that is used to create the Site Retargeting Audience
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class FirstPartySegment(
    val id: Long,
    val name: String,
    val organizationId: Long,
    val companyWide: Boolean,
    val customValuesEnabled: Boolean,
    val active: Boolean,

    // url to this organization resource
    val resource: String
)
