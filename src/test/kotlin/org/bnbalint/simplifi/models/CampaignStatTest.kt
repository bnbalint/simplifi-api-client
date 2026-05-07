package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class CampaignStatTest {
    @Test
    fun serializes_to_json() {
        val stat = CampaignStat(
            impressions = 10000L,
            clicks = 250L,
            ctr = 0.025,
            cpm = 1.23,
            totalSpend = 1234.56,
            resource = "https://app.simpli.fi/api/campaigns/300/stats"
        )
        val json = JsonHelpers.getJson(stat)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/campaign_stat.json")))
    }



    @Test
    fun deserializes_from_json() {
        val stat = JsonHelpers.readObject("/models/campaign_stat.json", CampaignStat::class.java)
        assertThat(stat.impressions, `is`(10000L))
        assertThat(stat.clicks, `is`(250L))
        assertThat(stat.ctr, `is`(0.025))
        assertThat(stat.cpm, `is`(1.23))
        assertThat(stat.totalSpend, `is`(1234.56))
        assertThat(stat.resource, `is`("https://app.simpli.fi/api/campaigns/300/stats"))
    }
}

