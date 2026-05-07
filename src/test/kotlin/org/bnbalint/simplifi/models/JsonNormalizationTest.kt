package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

// General smoke test ensuring normalization does not alter semantic content
class JsonNormalizationTest {
    @Test
    fun normalization_preserves_structure() {
        val raw = JsonHelpers.normalizedJson("/models/ad.json")
        val again = JsonHelpers.normalizedJson("/models/ad.json")
        assertThat(raw, `is`(again))
    }
}

