package org.bnbalint.simplifi.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * FirstPartySegmentCreateBody model
 * @param name The name of the first party segment
 * @param customValuesEnabled Whether custom values are enabled for the segment
 * @param companyWide Whether the segment is company-wide
 * @param firstPartySegmentRules The list of rules associated with the first party segment
 * @param firstPartySegmentTags The list of tag IDs associated with the first party segment
 *
 * USAGE: We will create a first party segment for the audience organization
 * The first party segment that is used to create the Site Retargeting Audience
 * For our purposes:
 *  customValuesEnabled = false
 *  companyWide = false
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class FirstPartySegmentCreateBody(
    val name: String,
    val customValuesEnabled: Boolean = false,
    val companyWide: Boolean = false,
    val firstPartySegmentRules: List<FirstPartySegmentRule>,
    val firstPartySegmentTags: List<Long>
)
