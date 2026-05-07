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


class GetOrganizationByIdTest {


    @Test
    fun getOrganizationById_success_no_retry() {
        //--------------------------------------------------
        // CONFIGURE MOCKS
        val server = MockWebServer()
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(JsonHelpers.normalizedJson("/models/organizations_response_no_paging.json"))
        )
        server.start()

        //--------------------------------------------------
        // EXECUTE
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val organization = client.getOrganizationById(100L)

            //--------------------------------------------------
            // VERIFY RESULTS
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
    fun getOrganizationById_retries_on_429_then_success() {
        //--------------------------------------------------
        // CONFIGURE MOCKS
        val server = MockWebServer()

        // First attempt fails with 429 (rate limit)
        server.enqueue(
            MockResponse()
                .setResponseCode(429)
                .setHeader("Content-Type", "application/json")
                .setBody("Too Many Requests")
        )

        // Second attempt succeeds
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(JsonHelpers.normalizedJson("/models/organizations_response_no_paging.json"))
        )
        server.start()

        //--------------------------------------------------
        // EXECUTE
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val organization = client.getOrganizationById(100L)

            //--------------------------------------------------
            // VERIFY RESULTS
            assertThat(server.requestCount, `is`(2)) // Expect 2 total requests (initial + 1 retry)

            //--------------------------------------------------
            // VERIFY RESULTS
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
    fun getOrganizationById_fails_after_max_retries_rate_limit() {
        //--------------------------------------------------
        // CONFIGURE MOCKS
        val server = MockWebServer()

        // Enqueue 4 consecutive 429 responses (initial + 3 retries configured)
        repeat(4) {
            server.enqueue(
                MockResponse()
                    .setResponseCode(429)
                    .setHeader("Content-Type", "application/json")
                    .setBody("Too Many Requests")
            )
        }
        server.start()

        //--------------------------------------------------
        // EXECUTE
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val ex = assertThrows(SimplifiRateLimitRetriesExhausted::class.java) { client.getOrganizationById(100L) }

            //--------------------------------------------------
            // VERIFY RESULTS
            assertTrue(ex.message?.contains("Failed after 3 retries due to rate limiting") == true)
            assertThat(server.requestCount, `is`(4)) // Should have attempted 4 times total (1 initial + 3 retries)
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun getOrganizationById_retries_on_500_then_success() {
        //--------------------------------------------------
        // CONFIGURE MOCKS
        val server = MockWebServer()

        // First attempt fails with 500 (server error)
        server.enqueue(
            MockResponse()
                .setResponseCode(500)
                .setHeader("Content-Type", "application/json")
                .setBody("Internal Server Error")
        )

        // Second attempt succeeds
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(JsonHelpers.normalizedJson("/models/organizations_response_no_paging.json"))
        )
        server.start()

        //--------------------------------------------------
        // EXECUTE
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val organization = client.getOrganizationById(100L)

            //--------------------------------------------------
            // VERIFY RESULTS
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
    fun getOrganizationById_fails_after_max_retries_server_error() {
        //--------------------------------------------------
        // CONFIGURE MOCKS
        val server = MockWebServer()

        // First attempt - error
        server.enqueue(
            MockResponse()
                .setResponseCode(500)
                .setHeader("Content-Type", "application/json")
                .setBody("Internal Server Error")
        )

        // Second attempt - error
        server.enqueue(
            MockResponse()
                .setResponseCode(502)
                .setHeader("Content-Type", "application/json")
                .setBody("Bad Gateway")
        )

        // Third attempt - error
        server.enqueue(
            MockResponse()
                .setResponseCode(504)
                .setHeader("Content-Type", "application/json")
                .setBody("Gateway Timeout")
        )

        // Fourth attempt - error
        server.enqueue(
            MockResponse()
                .setResponseCode(503)
                .setHeader("Content-Type", "application/json")
                .setBody("Service Unavailable")
        )
        server.start()

        //--------------------------------------------------
        // EXECUTE
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val ex = assertThrows(SimplifiServerExceptionRetriesExhausted::class.java) { client.getOrganizationById(100L) }

            //--------------------------------------------------
            // VERIFY RESULTS
            assertTrue(ex.message?.contains("Failed after 3 retries due to server error") == true)
            assertThat(server.requestCount, `is`(4)) // Should have attempted 4 times total (1 initial + 3 retries)
        } finally {
            server.shutdown()
        }
    }
}
