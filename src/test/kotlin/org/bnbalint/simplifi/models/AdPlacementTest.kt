package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class AdPlacementTest {
    @Test
    fun serializes_to_json() {
        val placement = AdPlacement(
            id = 3,
            name = "In-Browser Only",
            resource = "https://app.simpli.fi/api/ad_placements/3"
        )
        val json = JsonHelpers.getJson(placement)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/ad_placement.json")))
    }

    @Test
    fun deserializes_from_json() {
        val placement = JsonHelpers.readObject("/models/ad_placement.json", AdPlacement::class.java)
        assertThat(placement.id, `is`(3))
        assertThat(placement.name, `is`("In-Browser Only"))
        assertThat(placement.resource, `is`("https://app.simpli.fi/api/ad_placements/3"))
    }
}

