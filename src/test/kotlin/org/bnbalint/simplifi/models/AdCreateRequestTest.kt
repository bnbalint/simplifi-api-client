package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

/**
 * Request types will only ever be serialized (sent to API), but we test
 * deserialization here to ensure the JSON structure is correct.
 */
class AdCreateRequestTest {
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
        val req = AdCreateRequest(ad = body)
        val json = JsonHelpers.getJson(req)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/ad_create_request.json")))
    }

    @Test
    fun deserializes_from_json() {
        val adRequest = JsonHelpers.readObject("/models/ad_create_request.json", AdCreateRequest::class.java)
        assertThat(adRequest.ad.name, `is`("New Ad"))
        assertThat(adRequest.ad.adFileTypeId, `is`("4"))
        assertThat(adRequest.ad.adSizeId, `is`("3"))
        assertThat(adRequest.ad.adPlacementId, `is`(3))
        assertThat(adRequest.ad.targetUrl, `is`("https://example.com"))
        assertThat(adRequest.ad.html, `is`("<div>Ad Html</div>"))
    }
}

