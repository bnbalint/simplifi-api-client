package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

/**
 * Body types will only ever be serialized (sent to API), but we test
 * deserialization here to ensure the JSON structure is correct.
 */
class CampaignFirstPartySegmentChangeBodyTest {
    @Test
    fun serializes_to_json() {
        val body = CampaignFirstPartySegmentChangeBody(
            add = listOf(CampaignFirstPartySegmentAdd(10L, 2, 3)),
            delete = emptyList(),
            update = listOf(CampaignFirstPartySegmentUpdate(11L, 4)),
            segmentMatchType = "any"
        )
        val json = JsonHelpers.getJson(body)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/campaign_first_party_segment_change_body.json")))
    }


    @Test
    fun deserializes_from_json() {
        val body = JsonHelpers.readObject("/models/campaign_first_party_segment_change_body.json", CampaignFirstPartySegmentChangeBody::class.java)
        assertThat(body.segmentMatchType, `is`("any"))
        assertThat(body.add.size, `is`(1))
        assertThat(body.add[0].firstPartySegmentId, `is`(10L))
        assertThat(body.add[0].segmentTypeId, `is`(2))
        assertThat(body.add[0].segmentTargetTypeId, `is`(3))
        assertThat(body.update.size, `is`(1))
        assertThat(body.update[0].id, `is`(11L))
        assertThat(body.update[0].segmentTargetTypeId, `is`(4))
    }
}

