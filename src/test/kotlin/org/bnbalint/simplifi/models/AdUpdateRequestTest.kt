package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull

/**
 * Request types will only ever be serialized (sent to API), but we test
 * deserialization here to ensure the JSON structure is correct.
 */
class AdUpdateRequestTest {
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
        val req = AdUpdateRequest(ad = body)
        val json = JsonHelpers.getJson(req)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/ad_update_request.json")))
    }

    @Test
    fun serializes_to_json_name_only() {
        val body = AdUpdateBody(
            name = "New Ad"
        )
        val req = AdUpdateRequest(ad = body)
        val json = JsonHelpers.getJson(req)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/ad_update_request_name_only.json")))
    }

    @Test
    fun deserializes_from_json() {
        val adRequest = JsonHelpers.readObject("/models/ad_update_request.json", AdUpdateRequest::class.java)
        assertThat(adRequest.ad.name, `is`("New Ad"))
        assertThat(adRequest.ad.adFileTypeId, `is`("4"))
        assertThat(adRequest.ad.adSizeId, `is`("3"))
        assertThat(adRequest.ad.adPlacementId, `is`(3))
        assertThat(adRequest.ad.targetUrl, `is`("https://example.com"))
        assertThat(adRequest.ad.html, `is`("<div>Ad Html</div>"))
    }

    @Test
    fun deserializes_from_json_name_only() {
        val adRequest = JsonHelpers.readObject("/models/ad_update_request_name_only.json", AdUpdateRequest::class.java)
        assertThat(adRequest.ad.name, `is`("New Ad"))
        assertNull(adRequest.ad.adFileTypeId)
        assertNull(adRequest.ad.adSizeId)
        assertNull(adRequest.ad.adPlacementId)
        assertNull(adRequest.ad.targetUrl)
        assertNull(adRequest.ad.html)
    }
}

