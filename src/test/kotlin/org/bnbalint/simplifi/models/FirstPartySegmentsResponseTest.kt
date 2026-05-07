package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull

/**
 * Response types will only ever be deserialized (received from API)
 */
class FirstPartySegmentsResponseTest {

    @Test
    fun deserializes_from_json() {
        val response = JsonHelpers.readObject("/models/first_party_segments_response.json", FirstPartySegmentsResponse::class.java)
        assertThat(response.firstPartySegments.size, `is`(1))
        val segment = response.firstPartySegments[0]
        assertThat(segment.id, `is`(500L))
        assertThat(segment.name, `is`("Segment A"))
        assertThat(segment.organizationId, `is`(100L))
        assertThat(segment.companyWide, `is`(true))
        assertThat(segment.customValuesEnabled, `is`(false))
        assertThat(segment.active, `is`(true))
        assertThat(segment.resource, `is`("https://app.simpli.fi/api/first_party_segments/500"))
        assertThat(response.paging?.page, `is`(1))
        assertThat(response.paging?.size, `is`(50))
        assertThat(response.paging?.total, `is`(1))
        assertNull(response.paging?.next)
    }

    @Test
    fun deserializes_from_json_next_page() {
        val response = JsonHelpers.readObject("/models/first_party_segments_response_next_page.json", FirstPartySegmentsResponse::class.java)
        assertThat(response.firstPartySegments.size, `is`(1))
        val segment = response.firstPartySegments[0]
        assertThat(segment.id, `is`(500L))
        assertThat(segment.name, `is`("Segment A"))
        assertThat(segment.organizationId, `is`(100L))
        assertThat(segment.companyWide, `is`(true))
        assertThat(segment.customValuesEnabled, `is`(false))
        assertThat(segment.active, `is`(true))
        assertThat(segment.resource, `is`("https://app.simpli.fi/api/first_party_segments/500"))
        assertThat(response.paging?.page, `is`(1))
        assertThat(response.paging?.size, `is`(50))
        assertThat(response.paging?.total, `is`(1))
        assertThat(response.paging?.next, `is`("https://app.simpli.fi/api/first_party_segments?page=2"))
    }

    @Test
    fun deserializes_from_json_no_paging() {
        val response = JsonHelpers.readObject("/models/first_party_segments_response_no_paging.json", FirstPartySegmentsResponse::class.java)
        assertThat(response.firstPartySegments.size, `is`(1))
        val segment = response.firstPartySegments[0]
        assertThat(segment.id, `is`(500L))
        assertThat(segment.name, `is`("Segment A"))
        assertThat(segment.organizationId, `is`(100L))
        assertThat(segment.companyWide, `is`(true))
        assertThat(segment.customValuesEnabled, `is`(false))
        assertThat(segment.active, `is`(true))
        assertThat(segment.resource, `is`("https://app.simpli.fi/api/first_party_segments/500"))
        assertNull(response.paging)
    }

    @Test
    fun deserializes_from_json_empty_list() {
        val response = JsonHelpers.readObject("/models/first_party_segments_response_empty_list.json", FirstPartySegmentsResponse::class.java)
        assertThat(response.firstPartySegments.size, `is`(0))
        assertThat(response.paging?.page, `is`(1))
        assertThat(response.paging?.size, `is`(50))
        assertThat(response.paging?.total, `is`(1))
        assertNull(response.paging?.next)
    }

    @Test
    fun deserializes_from_json_full_response_from_create() {
        val response = JsonHelpers.readObject("/models/first_party_segments_response_full.json", FirstPartySegmentsResponse::class.java)
        assertThat(response.firstPartySegments.size, `is`(1))
        val segment = response.firstPartySegments[0]
        assertThat(segment.id, `is`(1407827L))
        assertThat(segment.name, `is`("45678"))
        assertThat(segment.organizationId, `is`(529460L))
        assertThat(segment.companyWide, `is`(false))
        assertThat(segment.customValuesEnabled, `is`(false))
        assertThat(segment.active, `is`(false))
        assertThat(segment.resource, `is`("https://app.simpli.fi/api/first_party_segments/1407827"))
        assertNull(response.paging)
    }
}
