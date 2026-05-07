package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class ChangeCountTest {
    @Test
    fun serializes_to_json() {
        val changeCount = ChangeCount(count = 3)
        val json = JsonHelpers.getJson(changeCount)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/change_count.json")))
    }

    @Test
    fun deserializes_from_json() {
        val changeCount = JsonHelpers.readObject("/models/change_count.json", ChangeCount::class.java)
        assertThat(changeCount.count, `is`(3))
    }
}

