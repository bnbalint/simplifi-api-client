package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class CampaignTest {
    @Test
    fun serializes_to_json() {
        val campaign = Campaign(
            id = 300L,
            name = "Sample Campaign",
            customId = "CMP-300",
            startDate = "2025-01-01",
            endDate = "2025-02-01",
            impressionCap = 500000,
            dailyImpressionCap = 1000,
            monthlyImpressionCap = 20000,
            status = "Active",
            resource = "https://app.simpli.fi/api/campaigns/300"
        )
        val json = JsonHelpers.getJson(campaign)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/campaign.json")))
    }

    @Test
    fun deserializes_from_json() {
        val campaign = JsonHelpers.readObject("/models/campaign.json", Campaign::class.java)
        assertThat(campaign.id, `is`(300L))
        assertThat(campaign.name, `is`("Sample Campaign"))
        assertThat(campaign.customId, `is`("CMP-300"))
        assertThat(campaign.startDate, `is`("2025-01-01"))
        assertThat(campaign.endDate, `is`("2025-02-01"))
        assertThat(campaign.impressionCap, `is`(500000L))
        assertThat(campaign.dailyImpressionCap, `is`(1000L))
        assertThat(campaign.monthlyImpressionCap, `is`(20000L))
        assertThat(campaign.status, `is`("Active"))
        assertThat(campaign.resource, `is`("https://app.simpli.fi/api/campaigns/300"))
    }
}

