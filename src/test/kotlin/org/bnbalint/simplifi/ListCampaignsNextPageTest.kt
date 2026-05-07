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


class ListCampaignsNextPageTest {

    @Test
    fun listCampaignsNextPage_success_no_retry() {
        val server = MockWebServer()
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(JsonHelpers.normalizedJson("/models/campaigns_paged_response_next_page.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/baseUrl/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val response = client.listCampaignsNextPage("/baseUrl/organizations/283320/campaigns?children=true&page=1&size=1")
            assertNotNull(response)
            assertThat(server.requestCount, `is`(1))
            assertThat(response.hasNextPage(), `is`(true))
            val paging = response.paging
            assertNotNull(paging)
            assertThat(paging.page, `is`(1))
            assertThat(paging.size, `is`(1))
            assertThat(paging.total, `is`(395))
            assertThat(paging.next, `is`("/baseUrl/organizations/283320/campaigns?children=true&page=2&size=1"))
            // campaigns list assertions
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
                .setBody(JsonHelpers.normalizedJson("/models/campaigns_paged_response_next_page.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/baseUrl/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val response = client.listCampaignsNextPage("/baseUrl/organizations/283320/campaigns?children=true&page=1&size=1")
            assertNotNull(response)
            assertThat(server.requestCount, `is`(2))
            assertThat(response.hasNextPage(), `is`(true))
            val paging = response.paging
            assertNotNull(paging)
            assertThat(paging.page, `is`(1))
            assertThat(paging.size, `is`(1))
            assertThat(paging.total, `is`(395))
            assertThat(paging.next, `is`("/baseUrl/organizations/283320/campaigns?children=true&page=2&size=1"))
            // campaigns list assertions
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
    fun listCampaigns_fails_after_max_retries_rate_limit() {
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
            val client = SimplifiApiClient(server.url("/baseUrl/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val ex = assertThrows(SimplifiRateLimitRetriesExhausted::class.java) {
                client.listCampaignsNextPage("/baseUrl/organizations/283320/campaigns?children=true&page=1&size=1")
            }
            assertEquals(ex.message?.contains("Failed after 3 retries due to rate limiting"), true)
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
                .setBody("Server Error")
        )
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(JsonHelpers.normalizedJson("/models/campaigns_paged_response_next_page.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/baseUrl/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val response = client.listCampaignsNextPage("/baseUrl/organizations/283320/campaigns?children=true&page=1&size=1")
            assertNotNull(response)
            assertThat(server.requestCount, `is`(2))
            assertThat(response.hasNextPage(), `is`(true))
            val paging = response.paging
            assertNotNull(paging)
            assertThat(paging.page, `is`(1))
            assertThat(paging.size, `is`(1))
            assertThat(paging.total, `is`(395))
            assertThat(paging.next, `is`("/baseUrl/organizations/283320/campaigns?children=true&page=2&size=1"))
            // campaigns list assertions
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
            val client = SimplifiApiClient(server.url("/baseUrl/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val ex = assertThrows(SimplifiServerExceptionRetriesExhausted::class.java) {
                client.listCampaignsNextPage("/baseUrl/organizations/283320/campaigns?children=true&page=1&size=1")
            }
            assertEquals(ex.message?.contains("Failed after 3 retries due to server error"), true)
            assertThat(server.requestCount, `is`(4))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun listCampaignsNextPage_getNextPage() {
        val server = MockWebServer()
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(JsonHelpers.normalizedJson("/models/campaigns_paged_response_next_page.json"))
        )

        // this will be the response for the getNextPage call
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(JsonHelpers.normalizedJson("/models/campaigns_paged_response_next_page.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/baseUrl/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val response = client.listCampaignsNextPage("/baseUrl/organizations/283320/campaigns?children=true&page=1&size=1")
            assertNotNull(response)
            assertThat(server.requestCount, `is`(1))

            // campaigns list assertions
            assertThat(response.campaigns.size, `is`(1))
            var campaign = response.campaigns.first()
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

            assertThat(response.hasNextPage(), `is`(true))
            var paging = response.paging
            assertNotNull(paging)
            assertThat(paging.page, `is`(1))
            assertThat(paging.size, `is`(1))
            assertThat(paging.total, `is`(395))
            assertThat(
                paging.next,
                `is`("/baseUrl/organizations/283320/campaigns?children=true&page=2&size=1")
            )



            // this will internally call listCampaignsNextPage which will hit the second enqueued response
            val nextPageResponse = response.getNextPage(client)



            assertNotNull(nextPageResponse)
            assertThat(server.requestCount, `is`(2))

            // campaigns list assertions
            assertThat(nextPageResponse.campaigns.size, `is`(1))
            campaign = nextPageResponse.campaigns.first()
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

            assertThat(response.hasNextPage(), `is`(true))
            paging = response.paging
            assertNotNull(paging)
            assertThat(paging.page, `is`(1))
            assertThat(paging.size, `is`(1))
            assertThat(paging.total, `is`(395))
            assertThat(
                paging.next,
                `is`("/baseUrl/organizations/283320/campaigns?children=true&page=2&size=1")
            )
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun listCampaignsNextPage_getNextPage_noNextPage() {
        val server = MockWebServer()

        // this response has no next page
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(JsonHelpers.normalizedJson("/models/campaigns_paged_response.json"))
        )

        server.start()
        try {
            val client = SimplifiApiClient(server.url("/baseUrl/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val response = client.listCampaignsNextPage("/baseUrl/organizations/283320/campaigns?children=true&page=1&size=1")
            assertNotNull(response)
            assertThat(server.requestCount, `is`(1))

            // campaigns list assertions
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

            assertThat(response.hasNextPage(), `is`(false))
            val paging = response.paging
            assertNotNull(paging)
            assertThat(paging.page, `is`(1))
            assertThat(paging.size, `is`(1))
            assertThat(paging.total, `is`(395))
            assertNull(paging.next)



            // this won't actually call listCampaignsNextPage since there is no next page
            val nextPageResponse = response.getNextPage(client)



            assertNotNull(nextPageResponse)
            assertThat(server.requestCount, `is`(1))

            // campaigns list assertions
            assertThat(nextPageResponse.campaigns.size, `is`(0))
            assertNull(nextPageResponse.paging)
        } finally {
            server.shutdown()
        }
    }
}
