package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

/**
 * Body types will only ever be serialized (sent to API), but we test
 * deserialization here to ensure the JSON structure is correct.
 */
class AdCreateBodyTest {
    @Test
    fun serializes_to_json() {
        val body = AdCreateBody(
            name = "New Ad",
            adFileTypeId = "4",
            adSizeId = "3",
            adPlacementId = 3,
            targetUrl = "https://example.com",
            html = "<div>Ad Html</div>"
        )
        val json = JsonHelpers.getJson(body)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/ad_create_body.json")))
    }

    @Test
    fun deserializes_from_json() {
        val adBody = JsonHelpers.readObject("/models/ad_create_body.json", AdCreateBody::class.java)
        assertThat(adBody.name, `is`("New Ad"))
        assertThat(adBody.adFileTypeId, `is`("4"))
        assertThat(adBody.adSizeId, `is`("3"))
        assertThat(adBody.adPlacementId, `is`(3))
        assertThat(adBody.targetUrl, `is`("https://example.com"))
        assertThat(adBody.html, `is`("<div>Ad Html</div>"))
    }
}
