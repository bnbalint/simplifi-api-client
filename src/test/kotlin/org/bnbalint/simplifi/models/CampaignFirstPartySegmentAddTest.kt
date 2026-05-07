package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class CampaignFirstPartySegmentAddTest {
    @Test
    fun serializes_to_json() {
        val add = CampaignFirstPartySegmentAdd(
            firstPartySegmentId = 10L,
            segmentTypeId = 2,
            segmentTargetTypeId = 3
        )
        val json = JsonHelpers.getJson(add)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/campaign_first_party_segment_add.json")))
    }

    @Test
    fun deserializes_from_json() {
        val add = JsonHelpers.readObject("/models/campaign_first_party_segment_add.json", CampaignFirstPartySegmentAdd::class.java)
        assertThat(add.segmentTypeId, `is`(2))
        assertThat(add.segmentTargetTypeId, `is`(3))
        assertThat(add.firstPartySegmentId, `is`(10L))
    }
}

