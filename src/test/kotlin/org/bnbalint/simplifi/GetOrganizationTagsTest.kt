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
import kotlin.test.assertTrue

class GetOrganizationTagsTest {

    @Test
    fun getOrganizationTags_success_no_retry() {
        val server = MockWebServer()
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(JsonHelpers.normalizedJson("/models/organization_tags_response.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val tags = client.getOrganizationTags(100L)
            assertThat(server.requestCount, `is`(1))
            assertThat(tags.size, `is`(1))
            val tag = tags.first()
            assertThat(tag.id, `is`(200L))
            assertThat(tag.organizationId, `is`(100L))
            assertThat(tag.description, `is`("Primary Tag"))
            assertThat(tag.tag, `is`("PRIMARY"))
            assertThat(tag.allowUserMatching, `is`(true))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun getOrganizationTags_retries_on_429_then_success() {
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
                .setBody(JsonHelpers.normalizedJson("/models/organization_tags_response.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val tags = client.getOrganizationTags(100L, firstPartySegmentId = 1L)
            assertThat(server.requestCount, `is`(2))
            assertThat(tags.size, `is`(1))
            val tag = tags.first()
            assertThat(tag.id, `is`(200L))
            assertThat(tag.organizationId, `is`(100L))
            assertThat(tag.description, `is`("Primary Tag"))
            assertThat(tag.tag, `is`("PRIMARY"))
            assertThat(tag.allowUserMatching, `is`(true))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun getOrganizationTags_fails_after_max_retries_rate_limit() {
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
            val ex = assertThrows(SimplifiRateLimitRetriesExhausted::class.java) { client.getOrganizationTags(100L) }
            assertTrue(ex.message?.contains("Failed after 3 retries due to rate limiting") == true)
            assertThat(server.requestCount, `is`(4))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun getOrganizationTags_retries_on_500_then_success() {
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
                .setBody(JsonHelpers.normalizedJson("/models/organization_tags_response.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val tags = client.getOrganizationTags(100L)
            assertThat(server.requestCount, `is`(2))
            assertThat(tags.size, `is`(1))
            val tag = tags.first()
            assertThat(tag.id, `is`(200L))
            assertThat(tag.organizationId, `is`(100L))
            assertThat(tag.description, `is`("Primary Tag"))
            assertThat(tag.tag, `is`("PRIMARY"))
            assertThat(tag.allowUserMatching, `is`(true))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun getOrganizationTags_fails_after_max_retries_server_error() {
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
            val ex = assertThrows(SimplifiServerExceptionRetriesExhausted::class.java) { client.getOrganizationTags(100L) }
            assertTrue(ex.message?.contains("Failed after 3 retries due to server error") == true)
            assertThat(server.requestCount, `is`(4))
        } finally {
            server.shutdown()
        }
    }
}
