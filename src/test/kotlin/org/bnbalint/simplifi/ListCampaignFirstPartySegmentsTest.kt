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
import org.junit.jupiter.api.assertNull
import kotlin.test.assertTrue

class ListCampaignFirstPartySegmentsTest {

    @Test
    fun listCampaignFirstPartySegments_success_no_retry() {
        val server = MockWebServer()
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(JsonHelpers.normalizedJson("/models/campaign_first_party_segments_paged_response.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val response = client.listCampaignFirstPartySegments(904L)
            assertThat(server.requestCount, `is`(1))
            assertNotNull(response)
            val segments = response.campaignFirstPartySegments
            assertThat(segments.size, `is`(2))
            assertThat(segments[0].resource, `is`("https://app.simpli.fi/api/campaign_first_party_segments/187"))
            assertThat(segments[0].id, `is`(187L))
            assertThat(segments[0].campaignId, `is`(904L))
            assertThat(segments[0].firstPartySegmentId, `is`(300987L))
            assertThat(segments[0].segmentTypeId, `is`(2))
            assertNull(segments[0].warningMessage)
            assertThat(segments[1].resource, `is`("https://app.simpli.fi/api/campaign_first_party_segments/188"))
            assertThat(segments[1].id, `is`(188L))
            assertThat(segments[1].campaignId, `is`(904L))
            assertThat(segments[1].firstPartySegmentId, `is`(309165L))
            assertThat(segments[1].segmentTypeId, `is`(2))
            assertNull(segments[1].warningMessage)
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun listCampaignFirstPartySegments_retries_on_429_then_success() {
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
                .setBody(JsonHelpers.normalizedJson("/models/campaign_first_party_segments_paged_response.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val response = client.listCampaignFirstPartySegments(904L)
            assertThat(server.requestCount, `is`(2))
            assertNotNull(response)
            val segments = response.campaignFirstPartySegments
            assertThat(segments.size, `is`(2))
            assertThat(segments[0].resource, `is`("https://app.simpli.fi/api/campaign_first_party_segments/187"))
            assertThat(segments[0].id, `is`(187L))
            assertThat(segments[0].campaignId, `is`(904L))
            assertThat(segments[0].firstPartySegmentId, `is`(300987L))
            assertThat(segments[0].segmentTypeId, `is`(2))
            assertNull(segments[0].warningMessage)
            assertThat(segments[1].resource, `is`("https://app.simpli.fi/api/campaign_first_party_segments/188"))
            assertThat(segments[1].id, `is`(188L))
            assertThat(segments[1].campaignId, `is`(904L))
            assertThat(segments[1].firstPartySegmentId, `is`(309165L))
            assertThat(segments[1].segmentTypeId, `is`(2))
            assertNull(segments[1].warningMessage)
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun listCampaignFirstPartySegments_fails_after_max_retries_rate_limit() {
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
            val ex = assertThrows(SimplifiRateLimitRetriesExhausted::class.java) {
                client.listCampaignFirstPartySegments(300L, attributesOnly = false)
            }
            assertTrue(ex.message?.contains("Failed after 3 retries due to rate limiting") == true)
            assertThat(server.requestCount, `is`(4))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun listCampaignFirstPartySegments_retries_on_500_then_success() {
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
                .setBody(JsonHelpers.normalizedJson("/models/campaign_first_party_segments_paged_response.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val response = client.listCampaignFirstPartySegments(904L, page = 3)
            assertThat(server.requestCount, `is`(2))
            assertNotNull(response)
            val segments = response.campaignFirstPartySegments
            assertThat(segments.size, `is`(2))
            assertThat(segments[0].resource, `is`("https://app.simpli.fi/api/campaign_first_party_segments/187"))
            assertThat(segments[0].id, `is`(187L))
            assertThat(segments[0].campaignId, `is`(904L))
            assertThat(segments[0].firstPartySegmentId, `is`(300987L))
            assertThat(segments[0].segmentTypeId, `is`(2))
            assertNull(segments[0].warningMessage)
            assertThat(segments[1].resource, `is`("https://app.simpli.fi/api/campaign_first_party_segments/188"))
            assertThat(segments[1].id, `is`(188L))
            assertThat(segments[1].campaignId, `is`(904L))
            assertThat(segments[1].firstPartySegmentId, `is`(309165L))
            assertThat(segments[1].segmentTypeId, `is`(2))
            assertNull(segments[1].warningMessage)
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun listCampaignFirstPartySegments_fails_after_max_retries_server_error() {
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
            val ex = assertThrows(SimplifiServerExceptionRetriesExhausted::class.java) { client.listCampaignFirstPartySegments(300L) }
            assertTrue(ex.message?.contains("Failed after 3 retries due to server error") == true)
            assertThat(server.requestCount, `is`(4))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun listCampaignFirstPartySegments_empty_list() {
        val server = MockWebServer()
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(JsonHelpers.normalizedJson("/models/campaign_first_party_segments_paged_response_empty_list.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val response = client.listCampaignFirstPartySegments(904L)
            assertThat(server.requestCount, `is`(1))
            assertNotNull(response)
            val segments = response.campaignFirstPartySegments
            assertThat(segments.size, `is`(0))
        } finally {
            server.shutdown()
        }
    }
}
