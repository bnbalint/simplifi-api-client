package org.bnbalint.simplifi

import org.bnbalint.simplifi.exceptions.SimplifiRateLimitRetriesExhausted
import org.bnbalint.simplifi.exceptions.SimplifiServerExceptionRetriesExhausted
import org.bnbalint.simplifi.json.JsonHelpers
import org.bnbalint.simplifi.models.CampaignFirstPartySegmentChangeBody
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import kotlin.test.assertTrue

class ChangeCampaignFirstPartySegmentsTest {

    private fun body() = CampaignFirstPartySegmentChangeBody(segmentMatchType = "any")

    @Test
    fun changeCampaignFirstPartySegments_success_no_retry() {
        val server = MockWebServer()
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(JsonHelpers.normalizedJson("/models/campaign_first_party_segment_change_response.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val response = client.changeCampaignFirstPartySegments(300L, body())
            assertThat(server.requestCount, `is`(1))
            assertNotNull(response)
            assertThat(response.summary, `is`("Segments updated"))
            assertThat(response.added.count, `is`(1))
            assertThat(response.updated.count, `is`(2))
            assertThat(response.deleted.count, `is`(0))
            assertThat(response.segmentMatchType, `is`("any"))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun changeCampaignFirstPartySegments_retries_on_429_then_success() {
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
                .setBody(JsonHelpers.normalizedJson("/models/campaign_first_party_segment_change_response.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val response = client.changeCampaignFirstPartySegments(300L, body())
            assertThat(server.requestCount, `is`(2))
            assertNotNull(response)
            assertThat(response.summary, `is`("Segments updated"))
            assertThat(response.added.count, `is`(1))
            assertThat(response.updated.count, `is`(2))
            assertThat(response.deleted.count, `is`(0))
            assertThat(response.segmentMatchType, `is`("any"))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun changeCampaignFirstPartySegments_fails_after_max_retries_rate_limit() {
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
            val ex = assertThrows(SimplifiRateLimitRetriesExhausted::class.java) { client.changeCampaignFirstPartySegments(300L, body()) }
            assertTrue(ex.message?.contains("Failed after 3 retries due to rate limiting") == true)
            assertThat(server.requestCount, `is`(4))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun changeCampaignFirstPartySegments_retries_on_500_then_success() {
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
                .setBody(JsonHelpers.normalizedJson("/models/campaign_first_party_segment_change_response.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val response = client.changeCampaignFirstPartySegments(300L, body())
            assertThat(server.requestCount, `is`(2))
            assertNotNull(response)
            assertThat(response.summary, `is`("Segments updated"))
            assertThat(response.added.count, `is`(1))
            assertThat(response.updated.count, `is`(2))
            assertThat(response.deleted.count, `is`(0))
            assertThat(response.segmentMatchType, `is`("any"))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun changeCampaignFirstPartySegments_fails_after_max_retries_server_error() {
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
            val ex = assertThrows(SimplifiServerExceptionRetriesExhausted::class.java) { client.changeCampaignFirstPartySegments(300L, body()) }
            assertTrue(ex.message?.contains("Failed after 3 retries due to server error") == true)
            assertThat(server.requestCount, `is`(4))
        } finally {
            server.shutdown()
        }
    }
}
