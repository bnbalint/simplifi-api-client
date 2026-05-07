package org.bnbalint.simplifi

import org.bnbalint.simplifi.exceptions.SimplifiRateLimitRetriesExhausted
import org.bnbalint.simplifi.exceptions.SimplifiServerExceptionRetriesExhausted
import org.bnbalint.simplifi.exceptions.SimplifiUnprocessableEntityException
import org.bnbalint.simplifi.json.JsonHelpers
import org.bnbalint.simplifi.models.FirstPartySegmentCreateBody
import org.bnbalint.simplifi.models.FirstPartySegmentRule
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import kotlin.test.assertTrue

class CreateFirstPartySegmentTest {

    private fun buildBody(): FirstPartySegmentCreateBody = FirstPartySegmentCreateBody(
        name = "Segment A",
        customValuesEnabled = false,
        companyWide = true,
        firstPartySegmentRules = listOf(FirstPartySegmentRule(domain = "example.com", matchPattern = "/path", segmentUrlMatchTypeId = 1)),
        firstPartySegmentTags = listOf(200)
    )

    @Test
    fun createFirstPartySegment_success_no_retry() {
        val server = MockWebServer()
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(JsonHelpers.normalizedJson("/models/first_party_segments_response.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val segment = client.createFirstPartySegment(100L, buildBody())
            assertThat(server.requestCount, `is`(1))
            assertNotNull(segment)
            assertThat(segment.id, `is`(500L))
            assertThat(segment.name, `is`("Segment A"))
            assertThat(segment.organizationId, `is`(100L))
            assertThat(segment.companyWide, `is`(true))
            assertThat(segment.customValuesEnabled, `is`(false))
            assertThat(segment.active, `is`(true))
            assertThat(segment.resource, `is`("https://app.simpli.fi/api/first_party_segments/500"))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun createFirstPartySegment_retries_on_429_then_success() {
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
                .setBody(JsonHelpers.normalizedJson("/models/first_party_segments_response.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val segment = client.createFirstPartySegment(100L, buildBody())
            assertThat(server.requestCount, `is`(2))
            assertNotNull(segment)
            assertThat(segment.id, `is`(500L))
            assertThat(segment.name, `is`("Segment A"))
            assertThat(segment.organizationId, `is`(100L))
            assertThat(segment.companyWide, `is`(true))
            assertThat(segment.customValuesEnabled, `is`(false))
            assertThat(segment.active, `is`(true))
            assertThat(segment.resource, `is`("https://app.simpli.fi/api/first_party_segments/500"))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun createFirstPartySegment_fails_after_max_retries_rate_limit() {
        val server = MockWebServer()
        repeat(4) {
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
            val ex = assertThrows(SimplifiRateLimitRetriesExhausted::class.java) { client.createFirstPartySegment(100L, buildBody()) }
            assertTrue(ex.message?.contains("Failed after 3 retries due to rate limiting") == true)
            assertThat(server.requestCount, `is`(4))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun createFirstPartySegment_retries_on_500_then_success() {
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
                .setBody(JsonHelpers.normalizedJson("/models/first_party_segments_response.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val segment = client.createFirstPartySegment(100L, buildBody())
            assertThat(server.requestCount, `is`(2))
            assertNotNull(segment)
            assertThat(segment.id, `is`(500L))
            assertThat(segment.name, `is`("Segment A"))
            assertThat(segment.organizationId, `is`(100L))
            assertThat(segment.companyWide, `is`(true))
            assertThat(segment.customValuesEnabled, `is`(false))
            assertThat(segment.active, `is`(true))
            assertThat(segment.resource, `is`("https://app.simpli.fi/api/first_party_segments/500"))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun createFirstPartySegment_fails_after_max_retries_server_error() {
        val server = MockWebServer()
        listOf(500, 502, 504, 503).forEach { code ->
            server.enqueue(
                MockResponse()
                    .setResponseCode(code)
                    .setHeader("Content-Type", "application/json")
                    .setBody("Server Error $code")
            )
        }
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val ex = assertThrows(SimplifiServerExceptionRetriesExhausted::class.java) { client.createFirstPartySegment(100L, buildBody()) }
            assertTrue(ex.message?.contains("Failed after 3 retries due to server error") == true)
            assertThat(server.requestCount, `is`(4))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun createFirstPartySegment_422_unprocessable_entity_errors_parsed() {
        val server = MockWebServer()
        server.enqueue(
            MockResponse()
                .setResponseCode(422)
                .setHeader("Content-Type", "application/json")
                .setBody(JsonHelpers.normalizedJson("/models/errors_response.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val ex = assertThrows(SimplifiUnprocessableEntityException::class.java) { client.createFirstPartySegment(100L, buildBody()) }
            assertThat(server.requestCount, `is`(1))
            assertThat(ex.errorsResponse.errors.size, `is`(1))
            assertThat(ex.errorsResponse.errors[0], `is`("Name has already been taken"))
        } finally {
            server.shutdown()
        }
    }
}
