package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

/**
 * Request types will only ever be serialized (sent to API), but we test
 * deserialization here to ensure the JSON structure is correct.
 */
class OrganizationCreateRequestTest {
    @Test
    fun serializes_to_json() {
        val body = OrganizationCreateBody(
            name = "New Org",
            parentId = "1",
            customId = "ORG-NEW"
        )
        val req = OrganizationCreateRequest(organization = body)
        val json = JsonHelpers.getJson(req)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/organization_create_request.json")))
    }

    @Test
    fun deserializes_from_json() {
        val request = JsonHelpers.readObject("/models/organization_create_request.json", OrganizationCreateRequest::class.java)
        assertThat(request.organization.name, `is`("New Org"))
        assertThat(request.organization.parentId, `is`("1"))
        assertThat(request.organization.customId, `is`("ORG-NEW"))
    }
}

