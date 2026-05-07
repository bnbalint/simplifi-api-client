package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class CampaignCreateBodyTest {
    @Test
    fun serializes_to_json() {
        val body = CampaignCreateBody(
            name = "Cookie Audience ID - 12345",
            parentId = "345",
            customId = "CMP-300"
        )
        val json = JsonHelpers.getJson(body)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/campaign_create_body.json")))
    }

    @Test
    fun deserializes_from_json_full_update() {
        val body = JsonHelpers.readObject("/models/campaign_create_body.json", CampaignCreateBody::class.java)
        assertThat(body.name, `is`("Cookie Audience ID - 12345"))
        assertThat(body.customId, `is`("CMP-300"))
        assertThat(body.parentId, `is`("345"))
    }
}
