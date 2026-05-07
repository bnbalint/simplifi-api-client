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

class ListCampaignsTest {

    @Test
    fun listCampaigns_success_no_retry() {
        val server = MockWebServer()
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(JsonHelpers.normalizedJson("/models/campaigns_paged_response.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val response = client.listCampaigns(100L, attributesOnly = false)
            assertThat(server.requestCount, `is`(1))
            assertNotNull(response)
            assertThat(response.campaigns.size, `is`(1))
            val campaign = response.campaigns.first()
            assertThat(campaign.id, `is`(2587560L))
            assertThat(campaign.name, `is`("Cookie Audience - ID 10015"))
            assertThat(campaign.customId, `is`("10015"))
            assertThat(campaign.startDate, `is`("2021-10-01"))
            assertThat(campaign.endDate, `is`("2021-12-31"))
            assertThat(campaign.impressionCap, `is`(357143))
            assertThat(campaign.dailyImpressionCap, `is`(9868))
            assertNull(campaign.monthlyImpressionCap)
            assertThat(campaign.status, `is`("Ended"))
            assertThat(campaign.resource, `is`("https://app.simpli.fi/api/organizations/338742/campaigns/2587560"))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun listCampaigns_success_no_retry_page2() {
        val server = MockWebServer()
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(JsonHelpers.normalizedJson("/models/campaigns_paged_response.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val response = client.listCampaigns(100L, children = true)
            assertThat(server.requestCount, `is`(1))
            assertNotNull(response)
            assertThat(response.campaigns.size, `is`(1))
            val campaign = response.campaigns.first()
            assertThat(campaign.id, `is`(2587560L))
            assertThat(campaign.name, `is`("Cookie Audience - ID 10015"))
            assertThat(campaign.customId, `is`("10015"))
            assertThat(campaign.startDate, `is`("2021-10-01"))
            assertThat(campaign.endDate, `is`("2021-12-31"))
            assertThat(campaign.impressionCap, `is`(357143))
            assertThat(campaign.dailyImpressionCap, `is`(9868))
            assertNull(campaign.monthlyImpressionCap)
            assertThat(campaign.status, `is`("Ended"))
            assertThat(campaign.resource, `is`("https://app.simpli.fi/api/organizations/338742/campaigns/2587560"))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun listCampaigns_retries_on_429_then_success() {
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
                .setBody(JsonHelpers.normalizedJson("/models/campaigns_paged_response_no_paging.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val response = client.listCampaigns(100L, page = 2)
            assertThat(server.requestCount, `is`(2))
            assertNotNull(response)
            assertThat(response.campaigns.size, `is`(1))
            val campaign = response.campaigns.first()
            assertThat(campaign.id, `is`(300L))
            assertThat(campaign.name, `is`("Sample Campaign"))
            assertThat(campaign.customId, `is`("CMP-300"))
            assertThat(campaign.startDate, `is`("2025-01-01"))
            assertThat(campaign.endDate, `is`("2025-02-01"))
            assertThat(campaign.impressionCap, `is`(500000L))
            assertThat(campaign.dailyImpressionCap, `is`(1000L))
            assertThat(campaign.monthlyImpressionCap, `is`(20000L))
            assertThat(campaign.status, `is`("Active"))
            assertThat(campaign.resource, `is`("https://app.simpli.fi/api/campaigns/300"))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun listCampaigns_fails_after_max_retries_rate_limit() {
        val server = MockWebServer()
        repeat(4) { server.enqueue(MockResponse().setResponseCode(429).setHeader("Content-Type", "application/json").setBody("Too Many Requests")) }
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val ex = assertThrows(SimplifiRateLimitRetriesExhausted::class.java) { client.listCampaigns(100L) }
            assertTrue(ex.message?.contains("Failed after 3 retries due to rate limiting") == true)
            assertThat(server.requestCount, `is`(4))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun listCampaigns_retries_on_500_then_success() {
        val server = MockWebServer()
        server.enqueue(
            MockResponse()
                .setResponseCode(500)
                .setHeader("Content-Type", "application/json")
                .setBody("Too Many Requests")
        )
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(JsonHelpers.normalizedJson("/models/campaigns_paged_response_no_paging.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val response = client.listCampaigns(100L)
            assertThat(server.requestCount, `is`(2))
            assertNotNull(response)
            assertThat(response.campaigns.size, `is`(1))
            val campaign = response.campaigns.first()
            assertThat(campaign.id, `is`(300L))
            assertThat(campaign.name, `is`("Sample Campaign"))
            assertThat(campaign.customId, `is`("CMP-300"))
            assertThat(campaign.startDate, `is`("2025-01-01"))
            assertThat(campaign.endDate, `is`("2025-02-01"))
            assertThat(campaign.impressionCap, `is`(500000L))
            assertThat(campaign.dailyImpressionCap, `is`(1000L))
            assertThat(campaign.monthlyImpressionCap, `is`(20000L))
            assertThat(campaign.status, `is`("Active"))
            assertThat(campaign.resource, `is`("https://app.simpli.fi/api/campaigns/300"))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun listCampaigns_fails_after_max_retries_server_error() {
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
            val ex = assertThrows(SimplifiServerExceptionRetriesExhausted::class.java) { client.listCampaigns(100L) }
            assertTrue(ex.message?.contains("Failed after 3 retries due to server error") == true)
            assertThat(server.requestCount, `is`(4))
        } finally {
            server.shutdown()
        }
    }
}
