package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

/**
 * Request types will only ever be serialized (sent to API), but we test
 * deserialization here to ensure the JSON structure is correct.
 */
class FirstPartySegmentCreateRequestTest {
    @Test
    fun serializes_to_json() {
        val body = FirstPartySegmentCreateBody(
            name = "New Segment",
            customValuesEnabled = true,
            companyWide = false,
            firstPartySegmentRules = listOf(
                FirstPartySegmentRule(domain = "example.com", matchPattern = "/path", segmentUrlMatchTypeId = 1)
            ),
            firstPartySegmentTags = listOf(1, 2)
        )
        val req = FirstPartySegmentCreateRequest(firstPartySegment = body)
        val json = JsonHelpers.getJson(req)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/first_party_segment_create_request.json")))
    }

    @Test
    fun serializes_to_json_real_example() {
        val body = FirstPartySegmentCreateBody(
            name = "45678",
            customValuesEnabled = false,
            companyWide = false,
            firstPartySegmentRules = listOf(
                FirstPartySegmentRule(domain = "meantonevermatch.com", matchPattern = "", segmentUrlMatchTypeId = 7)
            ),
            firstPartySegmentTags = listOf(278428)
        )
        val req = FirstPartySegmentCreateRequest(firstPartySegment = body)
        val json = JsonHelpers.getJson(req)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/first_party_segment_create_request_real_example.json")))
    }

    @Test
    fun deserializes_from_json() {
        val req = JsonHelpers.readObject("/models/first_party_segment_create_request.json", FirstPartySegmentCreateRequest::class.java)
        assertThat(req.firstPartySegment.name, `is`("New Segment"))
        assertThat(req.firstPartySegment.customValuesEnabled, `is`(true))
        assertThat(req.firstPartySegment.companyWide, `is`(false))
        assertThat(req.firstPartySegment.firstPartySegmentRules.size, `is`(1))
        assertThat(req.firstPartySegment.firstPartySegmentRules[0].domain, `is`("example.com"))
        assertThat(req.firstPartySegment.firstPartySegmentRules[0].matchPattern, `is`("/path"))
        assertThat(req.firstPartySegment.firstPartySegmentRules[0].segmentUrlMatchTypeId, `is`(1))
        assertThat(req.firstPartySegment.firstPartySegmentTags.size, `is`(2))
        assertThat(req.firstPartySegment.firstPartySegmentTags[0], `is`(1))
        assertThat(req.firstPartySegment.firstPartySegmentTags[1], `is`(2))
    }

    @Test
    fun deserializes_from_json_real_example() {
        val req = JsonHelpers.readObject("/models/first_party_segment_create_request_real_example.json", FirstPartySegmentCreateRequest::class.java)
        assertThat(req.firstPartySegment.name, `is`("45678"))
        assertThat(req.firstPartySegment.customValuesEnabled, `is`(false))
        assertThat(req.firstPartySegment.companyWide, `is`(false))
        assertThat(req.firstPartySegment.firstPartySegmentRules.size, `is`(1))
        assertThat(req.firstPartySegment.firstPartySegmentRules[0].domain, `is`("meantonevermatch.com"))
        assertThat(req.firstPartySegment.firstPartySegmentRules[0].matchPattern, `is`(""))
        assertThat(req.firstPartySegment.firstPartySegmentRules[0].segmentUrlMatchTypeId, `is`(7))
        assertThat(req.firstPartySegment.firstPartySegmentTags.size, `is`(1))
        assertThat(req.firstPartySegment.firstPartySegmentTags[0], `is`(278428))
    }
}
