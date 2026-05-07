package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull


class CampaignFirstPartySegmentTest {

    @Test
    fun serializes_to_json() {
        @Suppress("ktlint:standard:max-line-length")
        val campaignFirstPartySegment: CampaignFirstPartySegment = CampaignFirstPartySegment(
            resource = "https://app.simpli.fi/api/campaign_first_party_segments/187",
            id = 187L,
            campaignId = 904L,
            firstPartySegmentId = 300987L,
            segmentTypeId = 2,
            warningMessage = null
        )

        val json = JsonHelpers.getJson(campaignFirstPartySegment)
        println(json)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/campaign_first_party_segment.json")))
    }

    @Test
    fun deserializes_from_json() {
        val segment = JsonHelpers.readObject(
            "/models/campaign_first_party_segment.json",
            CampaignFirstPartySegment::class.java
        )
        assertThat(segment.resource, `is`("https://app.simpli.fi/api/campaign_first_party_segments/187"))
        assertThat(segment.id, `is`(187L))
        assertThat(segment.campaignId, `is`(904L))
        assertThat(segment.firstPartySegmentId, `is`(300987L))
        assertThat(segment.segmentTypeId, `is`(2))
        assertNull(segment.warningMessage)
    }
}

