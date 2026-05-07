package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class CampaignGoalTest {
    @Test
    fun serializes_to_json() {
        val goal = CampaignGoal(
            goalValue = "0.10",
            cpaViewThruPer = "1.00",
            cpaClickThruPer = "1.00",
            goalType = "CPA"
        )
        val json = JsonHelpers.getJson(goal)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/campaign_goal.json")))
    }

    @Test
    fun deserializes_from_json() {
        val goal = JsonHelpers.readObject("/models/campaign_goal.json", CampaignGoal::class.java)
        assertThat(goal.goalType, `is`("CPA"))
        assertThat(goal.goalValue, `is`("0.10"))
        assertThat(goal.cpaClickThruPer, `is`("1.00"))
        assertThat(goal.cpaViewThruPer, `is`("1.00"))
    }
}

