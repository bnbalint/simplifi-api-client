package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

/**
 * Request types will only ever be serialized (sent to API), but we test
 * deserialization here to ensure the JSON structure is correct.
 */
class CampaignFirstPartySegmentChangeRequestTest {
    @Test
    fun serializes_to_json() {
        val body = CampaignFirstPartySegmentChangeBody(
            add = listOf(CampaignFirstPartySegmentAdd(10L, 2, 3)),
            delete = emptyList(),
            update = listOf(CampaignFirstPartySegmentUpdate(11L, 4)),
            segmentMatchType = "any"
        )
        val request = CampaignFirstPartySegmentChangeRequest(campaignFirstPartySegments = body)
        val json = JsonHelpers.getJson(request)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/campaign_first_party_segment_change_request.json")))
    }

    // Edge case test for empty update/delete lists
    @Test
    fun serializes_empty_lists() {
        val body = CampaignFirstPartySegmentChangeBody(
            add = emptyList(),
            delete = emptyList(),
            update = emptyList(),
            segmentMatchType = "any"
        )
        val request = CampaignFirstPartySegmentChangeRequest(campaignFirstPartySegments = body)
        val json = JsonHelpers.getJson(request)

        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/campaign_first_party_segment_change_request_all_empty_lists.json")))
    }

    @Test
    fun serialize_with_delete_list() {
        val body = CampaignFirstPartySegmentChangeBody(
            add = emptyList(),
            delete = listOf(1L, 2L),
            update = emptyList(),
            segmentMatchType = "any"
        )
        val request = CampaignFirstPartySegmentChangeRequest(campaignFirstPartySegments = body)
        val json = JsonHelpers.getJson(request)

        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/campaign_first_party_segment_change_request_delete_list.json")))
    }

    @Test
    fun deserializes_from_json() {
        val req = JsonHelpers.readObject("/models/campaign_first_party_segment_change_request.json", CampaignFirstPartySegmentChangeRequest::class.java)
        assertThat(req.campaignFirstPartySegments.segmentMatchType, `is`("any"))
        assertThat(req.campaignFirstPartySegments.add.size, `is`(1))
        assertThat(req.campaignFirstPartySegments.add[0].firstPartySegmentId, `is`(10L))
        assertThat(req.campaignFirstPartySegments.add[0].segmentTypeId, `is`(2))
        assertThat(req.campaignFirstPartySegments.add[0].segmentTargetTypeId, `is`(3))
        assertThat(req.campaignFirstPartySegments.update.size, `is`(1))
        assertThat(req.campaignFirstPartySegments.update[0].id, `is`(11L))
        assertThat(req.campaignFirstPartySegments.update[0].segmentTargetTypeId, `is`(4))
    }

    @Test
    fun deserializes_from_json_empty_list() {
        val request = JsonHelpers.readObject(
            "/models/campaign_first_party_segment_change_request_all_empty_lists.json",
            CampaignFirstPartySegmentChangeRequest::class.java
        )
        assertThat(request.campaignFirstPartySegments.segmentMatchType, `is`("any"))
        assertThat(request.campaignFirstPartySegments.add.size, `is`(0))
        assertThat(request.campaignFirstPartySegments.update.size, `is`(0))
        assertThat(request.campaignFirstPartySegments.delete.size, `is`(0))
    }

    @Test
    fun deserializes_from_json_delete_list() {
        val request = JsonHelpers.readObject(
            "/models/campaign_first_party_segment_change_request_delete_list.json",
            CampaignFirstPartySegmentChangeRequest::class.java
        )
        assertThat(request.campaignFirstPartySegments.segmentMatchType, `is`("any"))
        assertThat(request.campaignFirstPartySegments.add.size, `is`(0))
        assertThat(request.campaignFirstPartySegments.update.size, `is`(0))
        assertThat(request.campaignFirstPartySegments.delete.size, `is`(2))
        assertThat(request.campaignFirstPartySegments.delete[0], `is`(1L))
        assertThat(request.campaignFirstPartySegments.delete[1], `is`(2L))
    }
}

