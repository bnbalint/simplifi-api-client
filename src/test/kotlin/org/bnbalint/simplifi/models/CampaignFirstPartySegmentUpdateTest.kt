package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class CampaignFirstPartySegmentUpdateTest {
    @Test
    fun serializes_to_json() {
        val update = CampaignFirstPartySegmentUpdate(
            id = 11L,
            segmentTargetTypeId = 4
        )
        val json = JsonHelpers.getJson(update)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/campaign_first_party_segment_update.json")))
    }

    @Test
    fun deserializes_from_json() {
        val update = JsonHelpers.readObject("/models/campaign_first_party_segment_update.json", CampaignFirstPartySegmentUpdate::class.java)
        assertThat(update.id, `is`(11L))
        assertThat(update.segmentTargetTypeId, `is`(4))
    }
}

