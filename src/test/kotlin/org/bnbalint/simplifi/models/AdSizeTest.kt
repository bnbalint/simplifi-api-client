package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class AdSizeTest {
    @Test
    fun serializes_to_json() {
        val size = AdSize(
            id = 3,
            name = "160x600",
            width = 160,
            height = 600,
            allowedAudioCompanionSize = false,
            resource = "https://app.simpli.fi/api/ad_sizes/3"
        )
        val json = JsonHelpers.getJson(size)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/ad_size.json")))
    }

    @Test
    fun deserializes_from_json() {
        val size = JsonHelpers.readObject("/models/ad_size.json", AdSize::class.java)
        assertThat(size.id, `is`(3))
        assertThat(size.name, `is`("160x600"))
        assertThat(size.width, `is`(160))
        assertThat(size.height, `is`(600))
        assertThat(size.allowedAudioCompanionSize, `is`(false))
        assertThat(size.resource, `is`("https://app.simpli.fi/api/ad_sizes/3"))
    }
}

