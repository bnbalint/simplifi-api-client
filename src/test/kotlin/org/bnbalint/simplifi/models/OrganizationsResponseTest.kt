package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull

/**
 * Response types will only ever be deserialized (received from API)
 */
class OrganizationsResponseTest {

    @Test
    fun deserializes_from_json() {
        val response = JsonHelpers.readObject("/models/organizations_response.json", OrganizationsResponse::class.java)
        assertThat(response.organizations.size, `is`(1))
        val organization = response.organizations[0]
        assertThat(organization.id, `is`(100L))
        assertThat(organization.name, `is`("Sample Org"))
        assertThat(organization.customId, `is`("ORG-100"))
        assertThat(organization.ancestry, `is`("1/100"))
        assertThat(organization.publicKey, `is`("pub-key-xyz"))
        assertThat(organization.website, `is`("https://org.example.com"))
        assertThat(organization.resource, `is`("https://app.simpli.fi/api/organizations/100"))
        assertThat(response.paging?.page, `is`(1))
        assertThat(response.paging?.size, `is`(50))
        assertThat(response.paging?.total, `is`(1))
        assertNull(response.paging?.next)
    }

    @Test
    fun deserializes_from_json_next_page() {
        val response = JsonHelpers.readObject("/models/organizations_response_next_page.json", OrganizationsResponse::class.java)
        assertThat(response.organizations.size, `is`(1))
        val organization = response.organizations[0]
        assertThat(organization.id, `is`(100L))
        assertThat(organization.name, `is`("Sample Org"))
        assertThat(organization.customId, `is`("ORG-100"))
        assertThat(organization.ancestry, `is`("1/100"))
        assertThat(organization.publicKey, `is`("pub-key-xyz"))
        assertThat(organization.website, `is`("https://org.example.com"))
        assertThat(organization.resource, `is`("https://app.simpli.fi/api/organizations/100"))
        assertThat(response.paging?.page, `is`(1))
        assertThat(response.paging?.size, `is`(50))
        assertThat(response.paging?.total, `is`(1))
        assertThat(response.paging?.next, `is`("https://app.simpli.fi/api/organizations?page=2"))
    }

    @Test
    fun deserializes_from_json_empty_list() {
        val response = JsonHelpers.readObject("/models/organizations_response_empty_list.json", OrganizationsResponse::class.java)
        assertThat(response.organizations.size, `is`(0))
        assertThat(response.paging?.page, `is`(1))
        assertThat(response.paging?.size, `is`(50))
        assertThat(response.paging?.total, `is`(1))
        assertNull(response.paging?.next)
    }

    @Test
    fun deserializes_from_json_no_paging() {
        val response = JsonHelpers.readObject("/models/organizations_response_no_paging.json", OrganizationsResponse::class.java)
        assertThat(response.organizations.size, `is`(1))
        val organization = response.organizations[0]
        assertThat(organization.id, `is`(100L))
        assertThat(organization.name, `is`("Sample Org"))
        assertThat(organization.customId, `is`("ORG-100"))
        assertThat(organization.ancestry, `is`("1/100"))
        assertThat(organization.publicKey, `is`("pub-key-xyz"))
        assertThat(organization.website, `is`("https://org.example.com"))
        assertThat(organization.resource, `is`("https://app.simpli.fi/api/organizations/100"))
        assertNull(response.paging)
    }
}
