package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

/**
 * Body types will only ever be serialized (sent to API), but we test
 * deserialization here to ensure the JSON structure is correct.
 */
class OrganizationCreateBodyTest {
    @Test
    fun serializes_to_json() {
        val body = OrganizationCreateBody(
            name = "New Org",
            parentId = "1",
            customId = "ORG-NEW"
        )
        val json = JsonHelpers.getJson(body)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/organization_create_body.json")))
    }

    @Test
    fun deserializes_from_json() {
        val body = JsonHelpers.readObject("/models/organization_create_body.json", OrganizationCreateBody::class.java)
        assertThat(body.name, `is`("New Org"))
        assertThat(body.parentId, `is`("1"))
        assertThat(body.customId, `is`("ORG-NEW"))
    }
}

