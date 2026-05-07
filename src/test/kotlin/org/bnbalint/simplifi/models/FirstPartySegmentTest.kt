package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class FirstPartySegmentTest {
    @Test
    fun serializes_to_json() {
        val segment = FirstPartySegment(
            id = 500L,
            name = "Segment A",
            organizationId = 100L,
            companyWide = true,
            customValuesEnabled = false,
            active = true,
            resource = "https://app.simpli.fi/api/first_party_segments/500"
        )
        val json = JsonHelpers.getJson(segment)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/first_party_segment.json")))
    }

    @Test
    fun deserializes_from_json() {
        val segment = JsonHelpers.readObject("/models/first_party_segment.json", FirstPartySegment::class.java)
        assertThat(segment.companyWide, `is`(true))
        assertThat(segment.active, `is`(true))
    }
}

