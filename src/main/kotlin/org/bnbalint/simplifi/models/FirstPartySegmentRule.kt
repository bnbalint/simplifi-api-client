package org.bnbalint.simplifi.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * Model representing a first-party segment rule.
 * @param domain The domain to which the rule applies.
 * @param matchPattern The pattern used to match URLs.
 * @param segmentUrlMatchTypeId The type of URL match
 *
 * USAGE: part of creating the Site Retargeting Audience
 * NOTE: we use a specific combination to indicate it should find our S3 audience file
 * domain = "meantonevermatch.com"
 * matchPattern = ""
 * segmentUrlMatchTypeId = 7
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class FirstPartySegmentRule(
    val domain: String,
    val matchPattern: String,
    val segmentUrlMatchTypeId: Int
)
