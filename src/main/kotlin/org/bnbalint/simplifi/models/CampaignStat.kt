package org.bnbalint.simplifi.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * Model representing campaign statistics
 * @param impressions Number of impressions
 * @param clicks Number of clicks
 * @param ctr Click-through rate
 * @param cpm Cost per thousand impressions
 * @param totalSpend Total spend on the campaign
 * @param resource URL to this organization resource
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CampaignStat(

    val impressions: Long = -1,
    val clicks: Long = -1,
    val ctr: Double = -1.0,
    val cpm: Double = -1.0,
    val totalSpend: Double = -1.0,

    // url to this organization resource
    val resource: String
)
