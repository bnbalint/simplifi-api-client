package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull

/**
 * Body types will only ever be serialized (sent to API), but we test
 * deserialization here to ensure the JSON structure is correct.
 */
class AdUpdateBodyTest {
    @Test
    fun serializes_to_json() {
        val body = AdUpdateBody(
            name = "New Ad",
            adFileTypeId = "4",
            adSizeId = "3",
            adPlacementId = 3,
            targetUrl = "https://example.com",
            html = "<div>Ad Html</div>"
        )
        val json = JsonHelpers.getJson(body)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/ad_update_body.json")))
    }

    @Test
    fun serializes_to_json_name_only() {
        val body = AdUpdateBody(
            name = "New Ad"
        )
        val json = JsonHelpers.getJson(body)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/ad_update_body_name_only.json")))
    }

    @Test
    fun deserializes_from_json() {
        val adBody = JsonHelpers.readObject("/models/ad_update_body.json", AdUpdateBody::class.java)
        assertThat(adBody.name, `is`("New Ad"))
        assertThat(adBody.adFileTypeId, `is`("4"))
        assertThat(adBody.adSizeId, `is`("3"))
        assertThat(adBody.adPlacementId, `is`(3))
        assertThat(adBody.targetUrl, `is`("https://example.com"))
        assertThat(adBody.html, `is`("<div>Ad Html</div>"))
    }

    @Test
    fun deserializes_from_json_name_only() {
        val adBody = JsonHelpers.readObject("/models/ad_update_body_name_only.json", AdUpdateBody::class.java)
        assertThat(adBody.name, `is`("New Ad"))
        assertNull(adBody.adFileTypeId)
        assertNull(adBody.adSizeId)
        assertNull(adBody.adPlacementId)
        assertNull(adBody.targetUrl)
        assertNull(adBody.html)
    }
}
