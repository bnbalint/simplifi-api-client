package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

/**
 * Response types will only ever be deserialized (received from API), but we test
 * serialization here to ensure the JSON structure is correct.
 */
class OrganizationTagsResponseTest {
    @Test
    fun serializes_to_json() {
        val response = OrganizationTagsResponse(
            organizationTags = listOf(
                OrganizationTag(
                    id = 200L,
                    organizationId = 100L,
                    description = "Primary Tag",
                    tag = "PRIMARY",
                    allowUserMatching = true
                )
            )
        )
        val json = JsonHelpers.getJson(response)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/organization_tags_response.json")))
    }

    @Test
    fun deserializes_from_json() {
        val response = JsonHelpers.readObject("/models/organization_tags_response.json", OrganizationTagsResponse::class.java)
        assertThat(response.organizationTags.size, `is`(1))
        val orgTag = response.organizationTags[0]
        assertThat(orgTag.id, `is`(200L))
        assertThat(orgTag.organizationId, `is`(100L))
        assertThat(orgTag.description, `is`("Primary Tag"))
        assertThat(orgTag.tag, `is`("PRIMARY"))
        assertThat(orgTag.allowUserMatching, `is`(true))
    }
}

