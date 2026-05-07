package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class OrganizationTest {
    @Test
    fun serializes_to_json() {
        val org = Organization(
            id = 100L,
            name = "Sample Org",
            customId = "ORG-100",
            ancestry = "1/100",
            publicKey = "pub-key-xyz",
            website = "https://org.example.com",
            resource = "https://app.simpli.fi/api/organizations/100"
        )
        val json = JsonHelpers.getJson(org)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/organization.json")))
    }

    @Test
    fun deserializes_from_json() {
        val org = JsonHelpers.readObject("/models/organization.json", Organization::class.java)
        assertThat(org.id, `is`(100L))
        assertThat(org.name, `is`("Sample Org"))
        assertThat(org.customId, `is`("ORG-100"))
        assertThat(org.ancestry, `is`("1/100"))
        assertThat(org.publicKey, `is`("pub-key-xyz"))
        assertThat(org.website, `is`("https://org.example.com"))
        assertThat(org.resource, `is`("https://app.simpli.fi/api/organizations/100"))
    }
}
