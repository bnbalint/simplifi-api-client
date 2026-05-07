package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

/**
 * Request types will only ever be serialized (sent to API), but we test
 * deserialization here to ensure the JSON structure is correct.
 */
class CampaignCreateRequestTest {
    @Test
    fun serializes_to_json() {
        val body = CampaignCreateBody(
            name = "Cookie Audience ID - 12345",
            parentId = "345",
            customId = "CMP-300"
        )
        val req = CampaignCreateRequest(organization = body)
        val json = JsonHelpers.getJson(req)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/campaign_create_request.json")))
    }

    @Test
    fun deserializes_from_json() {
        val req = JsonHelpers.readObject("/models/campaign_create_request.json", CampaignCreateRequest::class.java)
        assertThat(req.organization.name, `is`("Cookie Audience ID - 12345"))
        assertThat(req.organization.customId, `is`("CMP-300"))
        assertThat(req.organization.parentId, `is`("345"))
    }
}
