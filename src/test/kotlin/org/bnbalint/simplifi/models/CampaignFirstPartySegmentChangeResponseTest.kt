package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

/**
 * Response types will only ever be deserialized (received from API), but we test
 * serialization here to ensure the JSON structure is correct.
 */
class CampaignFirstPartySegmentChangeResponseTest {
    @Test
    fun serializes_to_json() {
        val response = CampaignFirstPartySegmentChangeResponse(
            summary = "Segments updated",
            added = ChangeCount(1),
            updated = ChangeCount(2),
            deleted = ChangeCount(0),
            segmentMatchType = "any"
        )
        val json = JsonHelpers.getJson(response)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/campaign_first_party_segment_change_response.json")))
    }

    @Test
    fun deserializes_from_json() {
        val response = JsonHelpers.readObject("/models/campaign_first_party_segment_change_response.json", CampaignFirstPartySegmentChangeResponse::class.java)
        assertThat(response.added.count, `is`(1))
        assertThat(response.updated.count, `is`(2))
        assertThat(response.deleted.count, `is`(0))
        assertThat(response.segmentMatchType, `is`("any"))
    }
}


