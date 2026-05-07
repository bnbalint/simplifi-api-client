package org.bnbalint.simplifi

import org.bnbalint.simplifi.exceptions.SimplifiRateLimitRetriesExhausted
import org.bnbalint.simplifi.exceptions.SimplifiServerExceptionRetriesExhausted
import org.bnbalint.simplifi.json.JsonHelpers
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import kotlin.test.assertTrue

class GetOrganizationByCustomIdTest {

    @Test
    fun getOrganizationByCustomId_success_no_retry() {
        val server = MockWebServer()
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(JsonHelpers.normalizedJson("/models/organizations_response_no_paging.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val organization = client.getOrganizationByCustomId("ORG-100")
            assertThat(server.requestCount, `is`(1))
            assertNotNull(organization)
            assertThat(organization.id, `is`(100L))
            assertThat(organization.name, `is`("Sample Org"))
            assertThat(organization.customId, `is`("ORG-100"))
            assertThat(organization.ancestry, `is`("1/100"))
            assertThat(organization.publicKey, `is`("pub-key-xyz"))
            assertThat(organization.website, `is`("https://org.example.com"))
            assertThat(organization.resource, `is`("https://app.simpli.fi/api/organizations/100"))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun getOrganizationByCustomId_retries_on_429_then_success() {
        val server = MockWebServer()
        server.enqueue(
            MockResponse()
                .setResponseCode(429)
                .setHeader("Content-Type", "application/json")
                .setBody("Too Many Requests")
        )
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(JsonHelpers.normalizedJson("/models/organizations_response_no_paging.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val organization = client.getOrganizationByCustomId("ORG-100")
            assertThat(server.requestCount, `is`(2))
            assertNotNull(organization)
            assertThat(organization.id, `is`(100L))
            assertThat(organization.name, `is`("Sample Org"))
            assertThat(organization.customId, `is`("ORG-100"))
            assertThat(organization.ancestry, `is`("1/100"))
            assertThat(organization.publicKey, `is`("pub-key-xyz"))
            assertThat(organization.website, `is`("https://org.example.com"))
            assertThat(organization.resource, `is`("https://app.simpli.fi/api/organizations/100"))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun getOrganizationByCustomId_fails_after_max_retries_rate_limit() {
        val server = MockWebServer()
        repeat(4) { _ ->
            server.enqueue(
                MockResponse()
                    .setResponseCode(429)
                    .setHeader("Content-Type", "application/json")
                    .setBody("Too Many Requests")
            )
        }
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val ex = assertThrows(SimplifiRateLimitRetriesExhausted::class.java) { client.getOrganizationByCustomId("ORG-100") }
            assertTrue(ex.message?.contains("Failed after 3 retries due to rate limiting") == true)
            assertThat(server.requestCount, `is`(4))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun getOrganizationByCustomId_retries_on_500_then_success() {
        val server = MockWebServer()
        server.enqueue(
            MockResponse()
                .setResponseCode(500)
                .setHeader("Content-Type", "application/json")
                .setBody("Internal Server Error")
        )
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(JsonHelpers.normalizedJson("/models/organizations_response_no_paging.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val organization = client.getOrganizationByCustomId("ORG-100")
            assertThat(server.requestCount, `is`(2))
            assertNotNull(organization)
            assertThat(organization.id, `is`(100L))
            assertThat(organization.name, `is`("Sample Org"))
            assertThat(organization.customId, `is`("ORG-100"))
            assertThat(organization.ancestry, `is`("1/100"))
            assertThat(organization.publicKey, `is`("pub-key-xyz"))
            assertThat(organization.website, `is`("https://org.example.com"))
            assertThat(organization.resource, `is`("https://app.simpli.fi/api/organizations/100"))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun getOrganizationByCustomId_fails_after_max_retries_server_error() {
        val server = MockWebServer()
        repeat(4) { idx ->
            val codes = listOf(500, 502, 504, 503)
            server.enqueue(
                MockResponse()
                    .setResponseCode(codes[idx])
                    .setHeader("Content-Type", "application/json")
                    .setBody("Server Error ${codes[idx]}")
            )
        }
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val ex = assertThrows(SimplifiServerExceptionRetriesExhausted::class.java) { client.getOrganizationByCustomId("ORG-100") }
            assertTrue(ex.message?.contains("Failed after 3 retries due to server error") == true)
            assertThat(server.requestCount, `is`(4))
        } finally {
            server.shutdown()
        }
    }
}
