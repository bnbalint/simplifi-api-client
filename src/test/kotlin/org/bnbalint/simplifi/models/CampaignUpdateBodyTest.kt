package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull

class CampaignUpdateBodyTest {
    @Test
    fun serializes_to_json() {
        val body = CampaignUpdateBody(
            name = "Updated Campaign",
            customId = "CMP-300",
            impressionCap = 100000,
            startDate = "2025-01-01",
            endDate = "2025-02-01",
            dailyImpressionCap = 1000,
            autoAdjustDailyImpressionCap = true,
            mediaTypeId = 1,
            autoOptimize = true,
            bid = 2.50,
            bidTypeId = 2,
            frequencyCapping = FrequencyCapping(howManyTimes = 5, hours = 24),
            campaignTypeId = 10,
            campaignGoal = CampaignGoal(
                goalValue = "0.10",
                cpaViewThruPer = "1.00",
                cpaClickThruPer = "1.00",
                goalType = "CPA"
            ),
            geoTargetIds = listOf("100", "200")
        )
        val json = JsonHelpers.getJson(body)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/campaign_update_body.json")))
    }

    @Test
    fun serializes_to_json_name_only() {
        val body = CampaignUpdateBody(
            name = "Updated Campaign"
        )
        val json = JsonHelpers.getJson(body)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/campaign_update_body_name_only.json")))
    }

    @Test
    fun deserializes_from_json_full_update() {
        val body = JsonHelpers.readObject("/models/campaign_update_body.json", CampaignUpdateBody::class.java)
        assertThat(body.impressionCap, `is`(100000))
        assertThat(body.name, `is`("Updated Campaign"))
        assertThat(body.customId, `is`("CMP-300"))
        assertThat(body.startDate, `is`("2025-01-01"))
        assertThat(body.endDate, `is`("2025-02-01"))
        assertThat(body.dailyImpressionCap, `is`(1000))
        assertThat(body.autoAdjustDailyImpressionCap, `is`(true))
        assertThat(body.mediaTypeId, `is`(1))
        assertThat(body.autoOptimize, `is`(true))
        assertThat(body.bid, `is`(2.50))
        assertThat(body.bidTypeId, `is`(2))
        assertThat(body.frequencyCapping?.hours, `is`(24))
        assertThat(body.frequencyCapping?.howManyTimes, `is`(5))
        assertThat(body.campaignTypeId, `is`(10))
        assertThat(body.campaignGoal?.goalType, `is`("CPA"))
        assertThat(body.campaignGoal?.goalValue, `is`("0.10"))
        assertThat(body.campaignGoal?.cpaViewThruPer, `is`("1.00"))
        assertThat(body.campaignGoal?.cpaClickThruPer, `is`("1.00"))
        assertThat(body.geoTargetIds?.size, `is`(2))
        val geoTargetIds = body.geoTargetIds
        assertNotNull(geoTargetIds)
        val geo1 = geoTargetIds[0]
        val geo2 = geoTargetIds[1]
        assertThat(geo1, `is`("100"))
        assertThat(geo2, `is`("200"))
    }

    @Test
    fun deserializes_from_json_name_only() {
        val body = JsonHelpers.readObject("/models/campaign_update_body_name_only.json", CampaignUpdateBody::class.java)
        assertThat(body.name, `is`("Updated Campaign"))
        assertNull(body.impressionCap)
        assertNull(body.frequencyCapping)
        assertNull(body.campaignGoal)
        assertNull(body.customId)
        assertNull(body.startDate)
        assertNull(body.endDate)
        assertNull(body.dailyImpressionCap)
        assertNull(body.autoAdjustDailyImpressionCap)
        assertNull(body.mediaTypeId)
        assertNull(body.autoOptimize)
        assertNull(body.bid)
        assertNull(body.bidTypeId)
        assertNull(body.campaignTypeId)
        assertNull(body.geoTargetIds)
    }
}
