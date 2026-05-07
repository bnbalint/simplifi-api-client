package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class FrequencyCappingTest {
    @Test
    fun serializes_to_json() {
        val cap = FrequencyCapping(
            howManyTimes = 5,
            hours = 24
        )
        val json = JsonHelpers.getJson(cap)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/frequency_capping.json")))
    }

    @Test
    fun deserializes_from_json() {
        val cap = JsonHelpers.readObject("/models/frequency_capping.json", FrequencyCapping::class.java)
        assertThat(cap.howManyTimes, `is`(5))
        assertThat(cap.hours, `is`(24))
    }
}

