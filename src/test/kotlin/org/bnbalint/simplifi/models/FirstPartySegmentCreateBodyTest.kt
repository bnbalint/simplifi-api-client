package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

/**
 * Body types will only ever be serialized (sent to API), but we test
 * deserialization here to ensure the JSON structure is correct.
 */
class FirstPartySegmentCreateBodyTest {
    @Test
    fun serializes_to_json() {
        val body = FirstPartySegmentCreateBody(
            name = "New Segment",
            customValuesEnabled = true,
            companyWide = false,
            firstPartySegmentRules = listOf(FirstPartySegmentRule(domain = "example.com", matchPattern = "/path", segmentUrlMatchTypeId = 1)),
            firstPartySegmentTags = listOf(1, 2)
        )
        val json = JsonHelpers.getJson(body)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/first_party_segment_create_body.json")))
    }

    @Test
    fun empty_tags() {
        val body = FirstPartySegmentCreateBody(
            name = "New Segment",
            customValuesEnabled = true,
            companyWide = false,
            firstPartySegmentRules = listOf(FirstPartySegmentRule("example.com", "/path", 1)),
            firstPartySegmentTags = emptyList()
        )
        val json = JsonHelpers.getJson(body)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/first_party_segment_create_body_no_tags.json")))
    }

    @Test
    fun deserializes_from_json() {
        val body = JsonHelpers.readObject("/models/first_party_segment_create_body.json", FirstPartySegmentCreateBody::class.java)
        assertThat(body.firstPartySegmentRules.size, `is`(1))
        assertThat(body.firstPartySegmentTags.size, `is`(2))
        assertThat(body.name, `is`("New Segment"))
        assertThat(body.customValuesEnabled, `is`(true))
        assertThat(body.companyWide, `is`(false))
        assertThat(body.firstPartySegmentRules[0].domain, `is`("example.com"))
        assertThat(body.firstPartySegmentRules[0].matchPattern, `is`("/path"))
        assertThat(body.firstPartySegmentRules[0].segmentUrlMatchTypeId, `is`(1))
        assertThat(body.firstPartySegmentTags[0], `is`(1))
        assertThat(body.firstPartySegmentTags[1], `is`(2))
    }
}
