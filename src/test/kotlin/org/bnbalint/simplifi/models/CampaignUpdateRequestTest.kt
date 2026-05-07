package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull

/**
 * Request types will only ever be serialized (sent to API), but we test
 * deserialization here to ensure the JSON structure is correct.
 */
class CampaignUpdateRequestTest {
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
        val req = CampaignUpdateRequest(campaign = body)
        val json = JsonHelpers.getJson(req)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/campaign_update_request.json")))
    }

    @Test
    fun deserializes_from_json() {
        val req = JsonHelpers.readObject("/models/campaign_update_request.json", CampaignUpdateRequest::class.java)
        assertThat(req.campaign.bid, `is`(2.50))
        assertThat(req.campaign.name, `is`("Updated Campaign"))
        assertThat(req.campaign.customId, `is`("CMP-300"))
        assertThat(req.campaign.impressionCap, `is`(100000))
        assertThat(req.campaign.startDate, `is`("2025-01-01"))
        assertThat(req.campaign.endDate, `is`("2025-02-01"))
        assertThat(req.campaign.dailyImpressionCap, `is`(1000))
        assertThat(req.campaign.autoAdjustDailyImpressionCap, `is`(true))
        assertThat(req.campaign.mediaTypeId, `is`(1))
        assertThat(req.campaign.autoOptimize, `is`(true))
        assertThat(req.campaign.bidTypeId, `is`(2))
        assertThat(req.campaign.frequencyCapping?.howManyTimes, `is`(5))
        assertThat(req.campaign.frequencyCapping?.hours, `is`(24))
        assertThat(req.campaign.campaignTypeId, `is`(10))
        assertThat(req.campaign.campaignGoal?.goalValue, `is`("0.10"))
        assertThat(req.campaign.campaignGoal?.cpaViewThruPer, `is`("1.00"))
        assertThat(req.campaign.campaignGoal?.cpaClickThruPer, `is`("1.00"))
        assertThat(req.campaign.campaignGoal?.goalType, `is`("CPA"))
        assertThat(req.campaign.geoTargetIds?.size, `is`(2))
        val geoTargetIds = req.campaign.geoTargetIds
        assertNotNull(geoTargetIds)
        val geo1 = geoTargetIds[0]
        val geo2 = geoTargetIds[1]
        assertThat(geo1, `is`("100"))
        assertThat(geo2, `is`("200"))
    }
}
