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
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class GetCampaignStatsTest {

    @Test
    fun getCampaignStats_success_no_retry() {
        val server = MockWebServer()
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(JsonHelpers.normalizedJson("/models/campaign_stats_paged_response.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val response = client.getCampaignStats(100L)
            assertThat(server.requestCount, `is`(1))
            assertNotNull(response)
            assertThat(response.campaignStats.size, `is`(1))
            val stat = response.campaignStats.first()
            assertThat(stat.impressions, `is`(10000L))
            assertThat(stat.clicks, `is`(250L))
            assertThat(stat.ctr, `is`(0.025))
            assertThat(stat.cpm, `is`(1.23))
            assertThat(stat.totalSpend, `is`(1234.56))
            assertThat(stat.resource, `is`("https://app.simpli.fi/api/campaigns/300/stats"))
            assertThat(response.paging.page, `is`(1))
            assertThat(response.paging.size, `is`(50))
            assertThat(response.paging.total, `is`(1))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun getCampaignStats_success_no_retry_dates_byDay() {
        val server = MockWebServer()
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(JsonHelpers.normalizedJson("/models/campaign_stats_paged_response.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val response = client.getCampaignStats(100L, "2025-01-01", "2025-01-31", byDay = true, byAd = false)
            assertThat(server.requestCount, `is`(1))
            assertNotNull(response)
            assertThat(response.campaignStats.size, `is`(1))
            val stat = response.campaignStats.first()
            assertThat(stat.impressions, `is`(10000L))
            assertThat(stat.clicks, `is`(250L))
            assertThat(stat.ctr, `is`(0.025))
            assertThat(stat.cpm, `is`(1.23))
            assertThat(stat.totalSpend, `is`(1234.56))
            assertThat(stat.resource, `is`("https://app.simpli.fi/api/campaigns/300/stats"))
            assertThat(response.paging.page, `is`(1))
            assertThat(response.paging.size, `is`(50))
            assertThat(response.paging.total, `is`(1))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun getCampaignStats_success_no_retry_dates_byAd() {
        val server = MockWebServer()
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(JsonHelpers.normalizedJson("/models/campaign_stats_paged_response.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val response = client.getCampaignStats(100L, "2025-01-01", "2025-01-31", byDay = false, byAd = true)
            assertThat(server.requestCount, `is`(1))
            assertNotNull(response)
            assertThat(response.campaignStats.size, `is`(1))
            val stat = response.campaignStats.first()
            assertThat(stat.impressions, `is`(10000L))
            assertThat(stat.clicks, `is`(250L))
            assertThat(stat.ctr, `is`(0.025))
            assertThat(stat.cpm, `is`(1.23))
            assertThat(stat.totalSpend, `is`(1234.56))
            assertThat(stat.resource, `is`("https://app.simpli.fi/api/campaigns/300/stats"))
            assertThat(response.paging.page, `is`(1))
            assertThat(response.paging.size, `is`(50))
            assertThat(response.paging.total, `is`(1))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun getCampaignStats_retries_on_429_then_success() {
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
                .setBody(JsonHelpers.normalizedJson("/models/campaign_stats_paged_response.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val response = client.getCampaignStats(100L)
            assertThat(server.requestCount, `is`(2))
            assertNotNull(response)
            assertThat(response.campaignStats.size, `is`(1))
            val stat = response.campaignStats.first()
            assertThat(stat.impressions, `is`(10000L))
            assertThat(stat.clicks, `is`(250L))
            assertThat(stat.ctr, `is`(0.025))
            assertThat(stat.cpm, `is`(1.23))
            assertThat(stat.totalSpend, `is`(1234.56))
            assertThat(stat.resource, `is`("https://app.simpli.fi/api/campaigns/300/stats"))
            assertThat(response.paging.page, `is`(1))
            assertThat(response.paging.size, `is`(50))
            assertThat(response.paging.total, `is`(1))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun getCampaignStats_fails_after_max_retries_rate_limit() {
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
            val ex = assertThrows(SimplifiRateLimitRetriesExhausted::class.java) { client.getCampaignStats(100L) }
            assertTrue(ex.message?.contains("Failed after 3 retries due to rate limiting") == true)
            assertThat(server.requestCount, `is`(4))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun getCampaignStats_retries_on_500_then_success() {
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
                .setBody(JsonHelpers.normalizedJson("/models/campaign_stats_paged_response.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val response = client.getCampaignStats(100L, page = 2)
            assertThat(server.requestCount, `is`(2))
            assertNotNull(response)
            assertThat(response.campaignStats.size, `is`(1))
            val stat = response.campaignStats.first()
            assertThat(stat.impressions, `is`(10000L))
            assertThat(stat.clicks, `is`(250L))
            assertThat(stat.ctr, `is`(0.025))
            assertThat(stat.cpm, `is`(1.23))
            assertThat(stat.totalSpend, `is`(1234.56))
            assertThat(stat.resource, `is`("https://app.simpli.fi/api/campaigns/300/stats"))
            assertThat(response.paging.page, `is`(1))
            assertThat(response.paging.size, `is`(50))
            assertThat(response.paging.total, `is`(1))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun getCampaignStats_fails_after_max_retries_server_error() {
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
            val ex = assertThrows(SimplifiServerExceptionRetriesExhausted::class.java) { client.getCampaignStats(100L) }
            assertTrue(ex.message?.contains("Failed after 3 retries due to server error") == true)
            assertThat(server.requestCount, `is`(4))
        } finally {
            server.shutdown()
        }
    }
}
