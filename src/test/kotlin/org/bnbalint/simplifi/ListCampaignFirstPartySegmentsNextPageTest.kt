package org.bnbalint.simplifi

import org.bnbalint.simplifi.exceptions.SimplifiRateLimitRetriesExhausted
import org.bnbalint.simplifi.exceptions.SimplifiServerExceptionRetriesExhausted
import org.bnbalint.simplifi.json.JsonHelpers
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import kotlin.test.assertEquals


class ListCampaignFirstPartySegmentsNextPageTest {

    @Test
    fun listCampaignFirstPartySegmentsNextPage_success_no_retry() {
        val server = MockWebServer()
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(JsonHelpers.normalizedJson("/models/campaign_first_party_segments_paged_response_next_page.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/baseUrl/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val response = client.listCampaignFirstPartySegmentsNextPage("/baseUrl/organizations/529815/first_party_segments?page=2&size=1")
            assertNotNull(response)
            assertThat(server.requestCount, `is`(1))
            // paging assertions
            assertThat(response.hasNextPage(), `is`(true))
            assertThat(response.paging.page, `is`(1))
            assertThat(response.paging.size, `is`(10))
            assertThat(response.paging.total, `is`(8))
            assertThat(response.paging.next, `is`("/baseUrl/organizations/529815/first_party_segments?page=2&size=1"))
            // campaign first party segments list assertions
            assertThat(response.campaignFirstPartySegments.size, `is`(2))
            val firstSegment = response.campaignFirstPartySegments[0]
            assertThat(firstSegment.id, `is`(187L))
            assertThat(firstSegment.campaignId, `is`(904L))
            assertThat(firstSegment.firstPartySegmentId, `is`(300987L))
            assertThat(firstSegment.segmentTypeId, `is`(2))
            assertThat(firstSegment.warningMessage, `is`(nullValue()))
            assertThat(firstSegment.resource, `is`("https://app.simpli.fi/api/campaign_first_party_segments/187"))
            val secondSegment = response.campaignFirstPartySegments[1]
            assertThat(secondSegment.id, `is`(188L))
            assertThat(secondSegment.campaignId, `is`(904L))
            assertThat(secondSegment.firstPartySegmentId, `is`(309165L))
            assertThat(secondSegment.segmentTypeId, `is`(2))
            assertThat(secondSegment.warningMessage, `is`(nullValue()))
            assertThat(secondSegment.resource, `is`("https://app.simpli.fi/api/campaign_first_party_segments/188"))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun listCampaignFirstPartySegmentsNextPage_retries_on_500_then_success() {
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
                .setBody(JsonHelpers.normalizedJson("/models/campaign_first_party_segments_paged_response_next_page.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/baseUrl/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val response = client.listCampaignFirstPartySegmentsNextPage("/baseUrl/organizations/529815/first_party_segments?page=2&size=1")
            assertNotNull(response)
            assertThat(server.requestCount, `is`(2))
            // paging assertions
            assertThat(response.hasNextPage(), `is`(true))
            assertThat(response.paging.page, `is`(1))
            assertThat(response.paging.size, `is`(10))
            assertThat(response.paging.total, `is`(8))
            assertThat(response.paging.next, `is`("/baseUrl/organizations/529815/first_party_segments?page=2&size=1"))
            // campaign first party segments list assertions
            assertThat(response.campaignFirstPartySegments.size, `is`(2))
            val firstSegment = response.campaignFirstPartySegments[0]
            assertThat(firstSegment.id, `is`(187L))
            assertThat(firstSegment.campaignId, `is`(904L))
            assertThat(firstSegment.firstPartySegmentId, `is`(300987L))
            assertThat(firstSegment.segmentTypeId, `is`(2))
            assertThat(firstSegment.warningMessage, `is`(nullValue()))
            assertThat(firstSegment.resource, `is`("https://app.simpli.fi/api/campaign_first_party_segments/187"))
            val secondSegment = response.campaignFirstPartySegments[1]
            assertThat(secondSegment.id, `is`(188L))
            assertThat(secondSegment.campaignId, `is`(904L))
            assertThat(secondSegment.firstPartySegmentId, `is`(309165L))
            assertThat(secondSegment.segmentTypeId, `is`(2))
            assertThat(secondSegment.warningMessage, `is`(nullValue()))
            assertThat(secondSegment.resource, `is`("https://app.simpli.fi/api/campaign_first_party_segments/188"))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun listCampaignFirstPartySegmentsNextPage_fails_after_max_retries_server_error() {
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
            val ex = Assertions.assertThrows(SimplifiServerExceptionRetriesExhausted::class.java) {
                client.listCampaignFirstPartySegmentsNextPage("/baseUrl/organizations/529815/first_party_segments?page=2&size=1")
            }
            assertEquals(ex.message?.contains("Failed after 3 retries due to server error"), true)
            assertThat(server.requestCount, `is`(4))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun listCampaignFirstPartySegmentsNextPage_retries_on_429_then_success() {
        val server = MockWebServer()
        // First attempt - rate limited
        server.enqueue(
            MockResponse()
                .setResponseCode(429)
                .setHeader("Content-Type", "application/json")
                .setBody("Too Many Requests")
        )
        // Retry succeeds
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(JsonHelpers.normalizedJson("/models/campaign_first_party_segments_paged_response_next_page.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/baseUrl/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val response = client.listCampaignFirstPartySegmentsNextPage("/baseUrl/organizations/529815/first_party_segments?page=2&size=1")
            assertNotNull(response)
            assertThat(server.requestCount, `is`(2))
            // paging assertions
            assertThat(response.hasNextPage(), `is`(true))
            assertThat(response.paging.page, `is`(1))
            assertThat(response.paging.size, `is`(10))
            assertThat(response.paging.total, `is`(8))
            assertThat(response.paging.next, `is`("/baseUrl/organizations/529815/first_party_segments?page=2&size=1"))
            // campaign first party segments list assertions
            assertThat(response.campaignFirstPartySegments.size, `is`(2))
            val firstSegment = response.campaignFirstPartySegments[0]
            assertThat(firstSegment.id, `is`(187L))
            assertThat(firstSegment.campaignId, `is`(904L))
            assertThat(firstSegment.firstPartySegmentId, `is`(300987L))
            assertThat(firstSegment.segmentTypeId, `is`(2))
            assertThat(firstSegment.warningMessage, `is`(nullValue()))
            assertThat(firstSegment.resource, `is`("https://app.simpli.fi/api/campaign_first_party_segments/187"))
            val secondSegment = response.campaignFirstPartySegments[1]
            assertThat(secondSegment.id, `is`(188L))
            assertThat(secondSegment.campaignId, `is`(904L))
            assertThat(secondSegment.firstPartySegmentId, `is`(309165L))
            assertThat(secondSegment.segmentTypeId, `is`(2))
            assertThat(secondSegment.warningMessage, `is`(nullValue()))
            assertThat(secondSegment.resource, `is`("https://app.simpli.fi/api/campaign_first_party_segments/188"))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun listCampaignFirstPartySegmentsNextPage_fails_after_max_retries_rate_limit() {
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
            val ex = Assertions.assertThrows(SimplifiRateLimitRetriesExhausted::class.java) {
                client.listCampaignFirstPartySegmentsNextPage("/baseUrl/organizations/529815/first_party_segments?page=2&size=1")
            }
            assertEquals(ex.message?.contains("Failed after 3 retries due to rate limiting"), true)
            assertThat(server.requestCount, `is`(4))
        } finally {
            server.shutdown()
        }
    }


    @Test
    fun listCampaignFirstPartySegmentsNextPage_getNextPage() {
        val server = MockWebServer()
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(JsonHelpers.normalizedJson("/models/campaign_first_party_segments_paged_response_next_page.json"))
        )

        // this will be returned by getNextPage
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(JsonHelpers.normalizedJson("/models/campaign_first_party_segments_paged_response_next_page.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/baseUrl/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val response = client.listCampaignFirstPartySegmentsNextPage("/baseUrl/organizations/529815/first_party_segments?page=2&size=1")
            assertNotNull(response)
            assertThat(server.requestCount, `is`(1))
            // paging assertions
            assertThat(response.hasNextPage(), `is`(true))
            assertThat(response.paging.page, `is`(1))
            assertThat(response.paging.size, `is`(10))
            assertThat(response.paging.total, `is`(8))
            assertThat(response.paging.next, `is`("/baseUrl/organizations/529815/first_party_segments?page=2&size=1"))
            // campaign first party segments list assertions
            assertThat(response.campaignFirstPartySegments.size, `is`(2))
            var firstSegment = response.campaignFirstPartySegments[0]
            assertThat(firstSegment.id, `is`(187L))
            assertThat(firstSegment.campaignId, `is`(904L))
            assertThat(firstSegment.firstPartySegmentId, `is`(300987L))
            assertThat(firstSegment.segmentTypeId, `is`(2))
            assertThat(firstSegment.warningMessage, `is`(nullValue()))
            assertThat(firstSegment.resource, `is`("https://app.simpli.fi/api/campaign_first_party_segments/187"))
            var secondSegment = response.campaignFirstPartySegments[1]
            assertThat(secondSegment.id, `is`(188L))
            assertThat(secondSegment.campaignId, `is`(904L))
            assertThat(secondSegment.firstPartySegmentId, `is`(309165L))
            assertThat(secondSegment.segmentTypeId, `is`(2))
            assertThat(secondSegment.warningMessage, `is`(nullValue()))
            assertThat(secondSegment.resource, `is`("https://app.simpli.fi/api/campaign_first_party_segments/188"))



            // this will internally call listCampaignFirstPartySegmentsNextPage again, returning the enqueued response above
            val nextPageResponse = response.getNextPage(client)


            assertNotNull(nextPageResponse)
            assertThat(server.requestCount, `is`(2))
            // paging assertions
            assertThat(nextPageResponse.hasNextPage(), `is`(true))
            assertThat(nextPageResponse.paging.page, `is`(1))
            assertThat(nextPageResponse.paging.size, `is`(10))
            assertThat(nextPageResponse.paging.total, `is`(8))
            assertThat(nextPageResponse.paging.next, `is`("/baseUrl/organizations/529815/first_party_segments?page=2&size=1"))
            // campaign first party segments list assertions
            assertThat(nextPageResponse.campaignFirstPartySegments.size, `is`(2))
            firstSegment = nextPageResponse.campaignFirstPartySegments[0]
            assertThat(firstSegment.id, `is`(187L))
            assertThat(firstSegment.campaignId, `is`(904L))
            assertThat(firstSegment.firstPartySegmentId, `is`(300987L))
            assertThat(firstSegment.segmentTypeId, `is`(2))
            assertThat(firstSegment.warningMessage, `is`(nullValue()))
            assertThat(firstSegment.resource, `is`("https://app.simpli.fi/api/campaign_first_party_segments/187"))
            secondSegment = nextPageResponse.campaignFirstPartySegments[1]
            assertThat(secondSegment.id, `is`(188L))
            assertThat(secondSegment.campaignId, `is`(904L))
            assertThat(secondSegment.firstPartySegmentId, `is`(309165L))
            assertThat(secondSegment.segmentTypeId, `is`(2))
            assertThat(secondSegment.warningMessage, `is`(nullValue()))
            assertThat(secondSegment.resource, `is`("https://app.simpli.fi/api/campaign_first_party_segments/188"))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun listCampaignFirstPartySegmentsNextPage_getNextPage_noNextPage() {
        val server = MockWebServer()

        // there is no next_page in this response
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(JsonHelpers.normalizedJson("/models/campaign_first_party_segments_paged_response.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/baseUrl/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val response = client.listCampaignFirstPartySegmentsNextPage("/baseUrl/organizations/529815/first_party_segments?page=2&size=1")
            assertNotNull(response)
            assertThat(server.requestCount, `is`(1))
            // paging assertions
            assertThat(response.hasNextPage(), `is`(false))
            assertThat(response.paging.page, `is`(1))
            assertThat(response.paging.size, `is`(10))
            assertThat(response.paging.total, `is`(8))
            assertNull(response.paging.next)
            // campaign first party segments list assertions
            assertThat(response.campaignFirstPartySegments.size, `is`(2))
            val firstSegment = response.campaignFirstPartySegments[0]
            assertThat(firstSegment.id, `is`(187L))
            assertThat(firstSegment.campaignId, `is`(904L))
            assertThat(firstSegment.firstPartySegmentId, `is`(300987L))
            assertThat(firstSegment.segmentTypeId, `is`(2))
            assertThat(firstSegment.warningMessage, `is`(nullValue()))
            assertThat(firstSegment.resource, `is`("https://app.simpli.fi/api/campaign_first_party_segments/187"))
            val secondSegment = response.campaignFirstPartySegments[1]
            assertThat(secondSegment.id, `is`(188L))
            assertThat(secondSegment.campaignId, `is`(904L))
            assertThat(secondSegment.firstPartySegmentId, `is`(309165L))
            assertThat(secondSegment.segmentTypeId, `is`(2))
            assertThat(secondSegment.warningMessage, `is`(nullValue()))
            assertThat(secondSegment.resource, `is`("https://app.simpli.fi/api/campaign_first_party_segments/188"))



            // this won't make a call because there is no next page
            val nextPageResponse = response.getNextPage(client)


            assertNotNull(nextPageResponse)
            assertThat(server.requestCount, `is`(1))
            // paging assertions
            assertThat(nextPageResponse.hasNextPage(), `is`(false))
            assertThat(nextPageResponse.paging.page, `is`(1))
            assertThat(nextPageResponse.paging.size, `is`(100))
            assertThat(nextPageResponse.paging.total, `is`(0))
            assertNull(nextPageResponse.paging.next)
            // campaign first party segments list assertions
            assertThat(nextPageResponse.campaignFirstPartySegments.size, `is`(0))
        } finally {
            server.shutdown()
        }
    }
}
