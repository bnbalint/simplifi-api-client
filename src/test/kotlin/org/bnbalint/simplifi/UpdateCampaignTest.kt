package org.bnbalint.simplifi

import org.bnbalint.simplifi.exceptions.SimplifiRateLimitRetriesExhausted
import org.bnbalint.simplifi.exceptions.SimplifiServerExceptionRetriesExhausted
import org.bnbalint.simplifi.json.JsonHelpers
import org.bnbalint.simplifi.models.*
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import kotlin.test.assertTrue

class UpdateCampaignTest {

    private fun buildBody(): CampaignUpdateBody = CampaignUpdateBody(
        name = "Sample Campaign",
        customId = "CMP-300",
        impressionCap = 100000,
        startDate = "2025-01-01",
        endDate = "2025-02-01",
        dailyImpressionCap = 5000,
        autoAdjustDailyImpressionCap = true,
        mediaTypeId = 1,
        autoOptimize = true,
        bid = 3.25,
        bidTypeId = 2,
        frequencyCapping = FrequencyCapping(howManyTimes = 5, hours = 24),
        campaignTypeId = 1,
        campaignGoal = CampaignGoal(goalValue = "0.1", cpaViewThruPer = "1.0", cpaClickThruPer = "1.0", goalType = "CTR"),
        geoTargetIds = listOf("100", "200")
    )

    @Test
    fun updateCampaign_success_no_retry() {
        val server = MockWebServer()
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(JsonHelpers.normalizedJson("/models/campaigns_paged_response_no_paging.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val campaign = client.updateCampaign(100L, 300L, buildBody())
            assertThat(server.requestCount, `is`(1))
            assertNotNull(campaign)
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
    fun updateCampaign_retries_on_429_then_success() {
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
            val campaign = client.updateCampaign(100L, 300L, buildBody())
            assertThat(server.requestCount, `is`(2))
            assertNotNull(campaign)
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
    fun updateCampaign_fails_after_max_retries_rate_limit() {
        val server = MockWebServer()
        repeat(4) { server.enqueue(MockResponse().setResponseCode(429).setHeader("Content-Type", "application/json").setBody("Too Many Requests")) }
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val ex = assertThrows(SimplifiRateLimitRetriesExhausted::class.java) { client.updateCampaign(100L, 300L, buildBody()) }
            assertTrue(ex.message?.contains("Failed after 3 retries due to rate limiting") == true)
            assertThat(server.requestCount, `is`(4))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun updateCampaign_retries_on_500_then_success() {
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
                .setBody(JsonHelpers.normalizedJson("/models/campaigns_paged_response_no_paging.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val campaign = client.updateCampaign(100L, 300L, buildBody())
            assertThat(server.requestCount, `is`(2))
            assertNotNull(campaign)
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
    fun updateCampaign_fails_after_max_retries_server_error() {
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
            val ex = assertThrows(SimplifiServerExceptionRetriesExhausted::class.java) { client.updateCampaign(100L, 300L, buildBody()) }
            assertTrue(ex.message?.contains("Failed after 3 retries due to server error") == true)
            assertThat(server.requestCount, `is`(4))
        } finally {
            server.shutdown()
        }
    }
}
