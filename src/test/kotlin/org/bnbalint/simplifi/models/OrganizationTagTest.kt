package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class OrganizationTagTest {
    @Test
    fun serializes_to_json() {
        val tag = OrganizationTag(
            id = 200L,
            organizationId = 100L,
            description = "Primary Tag",
            tag = "PRIMARY",
            allowUserMatching = true
        )
        val json = JsonHelpers.getJson(tag)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/organization_tag.json")))
    }

    @Test
    fun deserializes_from_json() {
        val tag = JsonHelpers.readObject("/models/organization_tag.json", OrganizationTag::class.java)
        assertThat(tag.id, `is`(200L))
        assertThat(tag.organizationId, `is`(100L))
        assertThat(tag.description, `is`("Primary Tag"))
        assertThat(tag.tag, `is`("PRIMARY"))
        assertThat(tag.allowUserMatching, `is`(true))
    }
}

