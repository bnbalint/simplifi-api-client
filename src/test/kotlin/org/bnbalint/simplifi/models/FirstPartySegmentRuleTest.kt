package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class FirstPartySegmentRuleTest {
    @Test
    fun serializes_to_json() {
        val rule = FirstPartySegmentRule(
            domain = "example.com",
            matchPattern = "/path",
            segmentUrlMatchTypeId = 1
        )
        val json = JsonHelpers.getJson(rule)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/first_party_segment_rule.json")))
    }

    @Test
    fun deserializes_from_json() {
        val rule = JsonHelpers.readObject("/models/first_party_segment_rule.json", FirstPartySegmentRule::class.java)
        assertThat(rule.domain, `is`("example.com"))
        assertThat(rule.matchPattern, `is`("/path"))
        assertThat(rule.segmentUrlMatchTypeId, `is`(1))
    }
}
