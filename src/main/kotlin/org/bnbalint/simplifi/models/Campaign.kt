package org.bnbalint.simplifi.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * Model representing a Campaign in Simpli.Fi
 * @param id The unique identifier for the campaign
 * @param name The name of the campaign
 * @param customId A custom identifier for the campaign
 * @param startDate The start date of the campaign (format: YYYY-MM-DD)
 * @param endDate The end date of the campaign (format: YYYY-MM-DD)
 * @param impressionCap The total number of impressions allocated for the campaign
 * @param dailyImpressionCap The daily impression cap for the campaign (we will not set this (auto-adjust does this), but might be useful to see it)
 * @param status The current status of the campaign
 * @param resource The URL to this campaign resource
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class Campaign(
    // non-null in Simpli.Fi, set on creation
    val id: Long,

    // non-null in Simpli.Fi, set on creation
    val name: String,

    // nullable in Simpli.Fi - defaults to null on creation
    val customId: String?,

    // non-null in Simpli.Fi, defaults to today on creation
    val startDate: String,

    // nullable in Simpli.Fi - defaults to null on creation
    val endDate: String? = null,

    // nullable in Simpli.Fi - defaults to null on creation
    val impressionCap: Long? = null,

    // nullable in Simpli.Fi - defaults to null on creation (we will never set this, but could be useful to see it)
    val dailyImpressionCap: Long? = null,

    // nullable in Simpli.Fi - defaults to null on creation (we will never set this, but could be useful to see it)
    val monthlyImpressionCap: Long? = null,

    // non-null in Simpli.Fi - it will default to Draft upon creation
    val status: String,

    // NOTE: there are alot of things are not pulling back in this model

    // url to this organization resource
    val resource: String
)
