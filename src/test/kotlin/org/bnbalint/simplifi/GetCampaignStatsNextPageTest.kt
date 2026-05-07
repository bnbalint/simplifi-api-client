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
import kotlin.test.assertEquals


class GetCampaignStatsNextPageTest {

    @Test
    fun getCampaignStatsNextPage_success_no_retry() {
        val server = MockWebServer()
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(JsonHelpers.normalizedJson("/models/campaign_stats_paged_response_next_page.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/baseUrl/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val response = client.getCampaignStatsNextPage("/baseUrl/campaigns/300/stats?page=2")
            assertNotNull(response)
            assertThat(server.requestCount, `is`(1))
            // paging assertions
            assertThat(response.hasNextPage(), `is`(true))
            assertThat(response.paging.page, `is`(1))
            assertThat(response.paging.size, `is`(50))
            assertThat(response.paging.total, `is`(1))
            assertThat(response.paging.next, `is`("/baseUrl/campaigns/300/stats?page=2"))
            // campaign stats list assertions
            assertThat(response.campaignStats.size, `is`(1))
            val campaignStat = response.campaignStats.first()
            assertThat(campaignStat.impressions, `is`(10000L))
            assertThat(campaignStat.clicks, `is`(250L))
            assertThat(campaignStat.ctr, `is`(0.025))
            assertThat(campaignStat.cpm, `is`(1.23))
            assertThat(campaignStat.totalSpend, `is`(1234.56))
            assertThat(campaignStat.resource, `is`("https://app.simpli.fi/api/campaigns/300/stats"))
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
                .setBody(JsonHelpers.normalizedJson("/models/campaign_stats_paged_response_next_page.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/baseUrl").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val response = client.getCampaignStatsNextPage("/baseUrl/campaigns/300/stats?page=2")
            assertNotNull(response)
            assertThat(server.requestCount, `is`(2))
            assertThat(response.hasNextPage(), `is`(true))
            assertThat(response.paging.page, `is`(1))
            assertThat(response.paging.size, `is`(50))
            assertThat(response.paging.total, `is`(1))
            assertThat(response.paging.next, `is`("/baseUrl/campaigns/300/stats?page=2"))
            // campaign stats list assertions
            assertThat(response.campaignStats.size, `is`(1))
            val campaignStat = response.campaignStats.first()
            assertThat(campaignStat.impressions, `is`(10000L))
            assertThat(campaignStat.clicks, `is`(250L))
            assertThat(campaignStat.ctr, `is`(0.025))
            assertThat(campaignStat.cpm, `is`(1.23))
            assertThat(campaignStat.totalSpend, `is`(1234.56))
            assertThat(campaignStat.resource, `is`("https://app.simpli.fi/api/campaigns/300/stats"))
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
            val client = SimplifiApiClient(server.url("/baseUrl").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val ex = assertThrows(SimplifiRateLimitRetriesExhausted::class.java) {
                client.getCampaignStatsNextPage("/baseUrl/campaigns/300/stats?page=2")
            }
            assertEquals(ex.message?.contains("Failed after 3 retries due to rate limiting"), true)
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
                .setBody("Server Error")
        )
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(JsonHelpers.normalizedJson("/models/campaign_stats_paged_response_next_page.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/baseUrl/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val response = client.getCampaignStatsNextPage("/baseUrl/campaigns/300/stats?page=2")
            assertNotNull(response)
            assertThat(server.requestCount, `is`(2))
            assertThat(response.hasNextPage(), `is`(true))
            assertThat(response.paging.page, `is`(1))
            assertThat(response.paging.size, `is`(50))
            assertThat(response.paging.total, `is`(1))
            assertThat(response.paging.next, `is`("/baseUrl/campaigns/300/stats?page=2"))
            // campaign stats list assertions
            assertThat(response.campaignStats.size, `is`(1))
            val campaignStat = response.campaignStats.first()
            assertThat(campaignStat.impressions, `is`(10000L))
            assertThat(campaignStat.clicks, `is`(250L))
            assertThat(campaignStat.ctr, `is`(0.025))
            assertThat(campaignStat.cpm, `is`(1.23))
            assertThat(campaignStat.totalSpend, `is`(1234.56))
            assertThat(campaignStat.resource, `is`("https://app.simpli.fi/api/campaigns/300/stats"))
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
            val client = SimplifiApiClient(server.url("/baseUrl/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val ex = assertThrows(SimplifiServerExceptionRetriesExhausted::class.java) {
                client.getCampaignStatsNextPage("/baseUrl/campaigns/300/stats?page=2")
            }
            assertEquals(ex.message?.contains("Failed after 3 retries due to server error"), true)
            assertThat(server.requestCount, `is`(4))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun getCampaignStatsNextPage_getNextPage() {
        val server = MockWebServer()
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(JsonHelpers.normalizedJson("/models/campaign_stats_paged_response_next_page.json"))
        )

        // this will be used by the getNextPage call
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(JsonHelpers.normalizedJson("/models/campaign_stats_paged_response_next_page.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/baseUrl/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val response = client.getCampaignStatsNextPage("/baseUrl/campaigns/300/stats?page=2")
            assertNotNull(response)
            assertThat(server.requestCount, `is`(1))
            // paging assertions
            assertThat(response.hasNextPage(), `is`(true))
            assertThat(response.paging.page, `is`(1))
            assertThat(response.paging.size, `is`(50))
            assertThat(response.paging.total, `is`(1))
            assertThat(response.paging.next, `is`("/baseUrl/campaigns/300/stats?page=2"))
            // campaign stats list assertions
            assertThat(response.campaignStats.size, `is`(1))
            var campaignStat = response.campaignStats.first()
            assertThat(campaignStat.impressions, `is`(10000L))
            assertThat(campaignStat.clicks, `is`(250L))
            assertThat(campaignStat.ctr, `is`(0.025))
            assertThat(campaignStat.cpm, `is`(1.23))
            assertThat(campaignStat.totalSpend, `is`(1234.56))
            assertThat(campaignStat.resource, `is`("https://app.simpli.fi/api/campaigns/300/stats"))


            // this will internally call getCampaignStatsNextPage again, returning the enqueued response above
            val nextPageResponse = response.getNextPage(client)


            assertNotNull(nextPageResponse)
            assertThat(server.requestCount, `is`(2))
            // paging assertions
            assertThat(nextPageResponse.hasNextPage(), `is`(true))
            assertThat(nextPageResponse.paging.page, `is`(1))
            assertThat(nextPageResponse.paging.size, `is`(50))
            assertThat(nextPageResponse.paging.total, `is`(1))
            assertThat(nextPageResponse.paging.next, `is`("/baseUrl/campaigns/300/stats?page=2"))
            // campaign stats list assertions
            assertThat(nextPageResponse.campaignStats.size, `is`(1))
            campaignStat = nextPageResponse.campaignStats.first()
            assertThat(campaignStat.impressions, `is`(10000L))
            assertThat(campaignStat.clicks, `is`(250L))
            assertThat(campaignStat.ctr, `is`(0.025))
            assertThat(campaignStat.cpm, `is`(1.23))
            assertThat(campaignStat.totalSpend, `is`(1234.56))
            assertThat(campaignStat.resource, `is`("https://app.simpli.fi/api/campaigns/300/stats"))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun getCampaignStatsNextPage_getNextPage_noNextPage() {
        val server = MockWebServer()

        // this response has no next page
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(JsonHelpers.normalizedJson("/models/campaign_stats_paged_response.json"))
        )

        server.start()
        try {
            val client = SimplifiApiClient(server.url("/baseUrl/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val response = client.getCampaignStatsNextPage("/baseUrl/campaigns/300/stats?page=2")
            assertNotNull(response)
            assertThat(server.requestCount, `is`(1))
            // paging assertions
            assertThat(response.hasNextPage(), `is`(false))
            assertThat(response.paging.page, `is`(1))
            assertThat(response.paging.size, `is`(50))
            assertThat(response.paging.total, `is`(1))
            assertNull(response.paging.next)
            // campaign stats list assertions
            assertThat(response.campaignStats.size, `is`(1))
            val campaignStat = response.campaignStats.first()
            assertThat(campaignStat.impressions, `is`(10000L))
            assertThat(campaignStat.clicks, `is`(250L))
            assertThat(campaignStat.ctr, `is`(0.025))
            assertThat(campaignStat.cpm, `is`(1.23))
            assertThat(campaignStat.totalSpend, `is`(1234.56))
            assertThat(campaignStat.resource, `is`("https://app.simpli.fi/api/campaigns/300/stats"))


            // this won't do another request since there is no next page
            val nextPageResponse = response.getNextPage(client)


            assertNotNull(nextPageResponse)
            assertThat(server.requestCount, `is`(1))
            // paging assertions
            assertThat(nextPageResponse.hasNextPage(), `is`(false))
            assertThat(nextPageResponse.paging.page, `is`(1))
            assertThat(nextPageResponse.paging.size, `is`(100))
            assertThat(nextPageResponse.paging.total, `is`(0))
            assertNull(nextPageResponse.paging.next)
            // campaign stats list assertions
            assertThat(nextPageResponse.campaignStats.size, `is`(0))
        } finally {
            server.shutdown()
        }
    }
}
