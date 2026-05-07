package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class AdFileTypeTest {
    @Test
    fun serializes_to_json() {
        val aft = AdFileType(
            id = 4,
            name = "HTML",
            resource = "https://app.simpli.fi/api/ad_file_types/4"
        )
        val json = JsonHelpers.getJson(aft)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/ad_file_type.json")))
    }

    @Test
    fun deserializes_from_json() {
        val adFileType = JsonHelpers.readObject("/models/ad_file_type.json", AdFileType::class.java)
        assertThat(adFileType.name, `is`("HTML"))
        assertThat(adFileType.resource, `is`("https://app.simpli.fi/api/ad_file_types/4"))
        assertThat(adFileType.id, `is`(4))
    }
}

