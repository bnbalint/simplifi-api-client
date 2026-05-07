package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull

/**
 * Response type will only ever be deserialized (received from API)
 */
class CampaignFirstPartySegmentsPagedResponseTest {

    @Test
    fun deserializes_from_json() {
        val response = JsonHelpers.readObject(
            "/models/campaign_first_party_segments_paged_response.json",
            CampaignFirstPartySegmentsPagedResponse::class.java
        )
        assertThat(response.campaignFirstPartySegments.size, `is`(2))
        // first
        assertThat(response.campaignFirstPartySegments[0].resource, `is`("https://app.simpli.fi/api/campaign_first_party_segments/187"))
        assertThat(response.campaignFirstPartySegments[0].id, `is`(187L))
        assertThat(response.campaignFirstPartySegments[0].campaignId, `is`(904L))
        assertThat(response.campaignFirstPartySegments[0].firstPartySegmentId, `is`(300987L))
        assertThat(response.campaignFirstPartySegments[0].segmentTypeId, `is`(2))
        assertNull(response.campaignFirstPartySegments[0].warningMessage)
        // second
        assertThat(response.campaignFirstPartySegments[1].resource, `is`("https://app.simpli.fi/api/campaign_first_party_segments/188"))
        assertThat(response.campaignFirstPartySegments[1].id, `is`(188L))
        assertThat(response.campaignFirstPartySegments[1].campaignId, `is`(904L))
        assertThat(response.campaignFirstPartySegments[1].firstPartySegmentId, `is`(309165L))
        assertThat(response.campaignFirstPartySegments[1].segmentTypeId, `is`(2))
        assertNull(response.campaignFirstPartySegments[1].warningMessage)
        // paging
        assertThat(response.paging.page, `is`(1))
        assertThat(response.paging.size, `is`(10))
        assertThat(response.paging.total, `is`(8))
        assertNull(response.paging.next)
        assertThat(response.hasNextPage(), `is`(false))
    }

    @Test
    fun deserializes_from_json_empty_list() {
        val response = JsonHelpers.readObject(
            "/models/campaign_first_party_segments_paged_response_empty_list.json",
            CampaignFirstPartySegmentsPagedResponse::class.java
        )
        assertThat(response.campaignFirstPartySegments.size, `is`(0))
        assertThat(response.paging.page, `is`(1))
        assertThat(response.paging.size, `is`(10))
        assertThat(response.paging.total, `is`(8))
        assertNull(response.paging.next)
        assertThat(response.hasNextPage(), `is`(false))
    }

    @Test
    fun deserializes_from_json_next_page() {
        val response = JsonHelpers.readObject(
            "/models/campaign_first_party_segments_paged_response_next_page.json",
            CampaignFirstPartySegmentsPagedResponse::class.java
        )
        assertThat(response.campaignFirstPartySegments.size, `is`(2))
        // first
        assertThat(response.campaignFirstPartySegments[0].resource, `is`("https://app.simpli.fi/api/campaign_first_party_segments/187"))
        assertThat(response.campaignFirstPartySegments[0].id, `is`(187L))
        assertThat(response.campaignFirstPartySegments[0].campaignId, `is`(904L))
        assertThat(response.campaignFirstPartySegments[0].firstPartySegmentId, `is`(300987L))
        assertThat(response.campaignFirstPartySegments[0].segmentTypeId, `is`(2))
        assertNull(response.campaignFirstPartySegments[0].warningMessage)
        // second
        assertThat(response.campaignFirstPartySegments[1].resource, `is`("https://app.simpli.fi/api/campaign_first_party_segments/188"))
        assertThat(response.campaignFirstPartySegments[1].id, `is`(188L))
        assertThat(response.campaignFirstPartySegments[1].campaignId, `is`(904L))
        assertThat(response.campaignFirstPartySegments[1].firstPartySegmentId, `is`(309165L))
        assertThat(response.campaignFirstPartySegments[1].segmentTypeId, `is`(2))
        assertNull(response.campaignFirstPartySegments[1].warningMessage)
        // paging
        assertThat(response.paging.page, `is`(1))
        assertThat(response.paging.size, `is`(10))
        assertThat(response.paging.total, `is`(8))
        assertThat(response.paging.next, `is`("/baseUrl/organizations/529815/first_party_segments?page=2&size=1"))
        assertThat(response.hasNextPage(), `is`(true))
    }
}

