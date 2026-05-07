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

class ListAdsTest {

    @Test
    fun listAds_success_no_retry() {
        val server = MockWebServer()
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(JsonHelpers.normalizedJson("/models/ads_response.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val ads = client.listAds(8L, 697L, allowDeleted = true)
            assertThat(server.requestCount, `is`(1))
            assertThat(ads.size, `is`(1))
            val ad = ads.first()
            assertThat(ad.id, `is`(1098L))
            assertThat(ad.name, `is`("image after all deleted"))
            assertThat(ad.status, `is`("Active"))
            assertThat(ad.pacing, `is`(100.0))
            assertThat(ad.clickTagVerified, `is`(false))
            assertThat(ad.previewTag.startsWith("<iframe"), `is`(true))
            assertThat(ad.targetUrl, `is`("http://simpli.fi"))
            assertThat(ad.adFileTypes.size, `is`(0))
            assertThat(ad.adSizes.size, `is`(0))
            assertThat(ad.adPlacements.size, `is`(0))
            assertThat(ad.resource, `is`("https://app.simpli.fi/api/organizations/8/campaigns/697/ads/1098"))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun listAds_success_no_retry_ad_sizes() {
        val server = MockWebServer()
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(JsonHelpers.normalizedJson("/models/ads_response_with_sizes.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val ads = client.listAds(8L, 697L, include = "ad_sizes")
            assertThat(server.requestCount, `is`(1))
            assertThat(ads.size, `is`(1))
            val ad = ads.first()
            assertThat(ad.id, `is`(1098L))
            assertThat(ad.name, `is`("image after all deleted"))
            assertThat(ad.status, `is`("Active"))
            assertThat(ad.pacing, `is`(100.0))
            assertThat(ad.clickTagVerified, `is`(false))
            assertThat(ad.previewTag.startsWith("<iframe"), `is`(true))
            assertThat(ad.targetUrl, `is`("http://simpli.fi"))
            assertThat(ad.adFileTypes.size, `is`(0))
            assertThat(ad.adPlacements.size, `is`(0))
            assertThat(ad.adSizes.size, `is`(1))
            assertThat(ad.adSizes[0].id, `is`(3))
            assertThat(ad.adSizes[0].name, `is`("160x600"))
            assertThat(ad.adSizes[0].width, `is`(160))
            assertThat(ad.adSizes[0].height, `is`(600))
            assertThat(ad.adSizes[0].allowedAudioCompanionSize, `is`(false))
            assertThat(ad.adSizes[0].resource, `is`("https://app.simpli.fi/api/ad_sizes/3"))
            assertThat(ad.resource, `is`("https://app.simpli.fi/api/organizations/8/campaigns/697/ads/1098"))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun listAds_retries_on_429_then_success() {
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
                .setBody(JsonHelpers.normalizedJson("/models/ads_response.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val ads = client.listAds(8L, 697L, attributesOnly = false)
            assertThat(server.requestCount, `is`(2))
            assertThat(ads.size, `is`(1))
            val ad = ads.first()
            assertThat(ad.id, `is`(1098L))
            assertThat(ad.name, `is`("image after all deleted"))
            assertThat(ad.status, `is`("Active"))
            assertThat(ad.pacing, `is`(100.0))
            assertThat(ad.clickTagVerified, `is`(false))
            assertThat(ad.previewTag.startsWith("<iframe"), `is`(true))
            assertThat(ad.targetUrl, `is`("http://simpli.fi"))
            assertThat(ad.adFileTypes.size, `is`(0))
            assertThat(ad.adSizes.size, `is`(0))
            assertThat(ad.adPlacements.size, `is`(0))
            assertThat(ad.resource, `is`("https://app.simpli.fi/api/organizations/8/campaigns/697/ads/1098"))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun listAds_fails_after_max_retries_rate_limit() {
        val server = MockWebServer()
        repeat(4) { server.enqueue(MockResponse().setResponseCode(429).setHeader("Content-Type", "application/json").setBody("Too Many Requests")) }
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val ex = assertThrows(SimplifiRateLimitRetriesExhausted::class.java) {
                client.listAds(8L, 697L, include = null)
            }
            assertTrue(ex.message?.contains("Failed after 3 retries due to rate limiting") == true)
            assertThat(server.requestCount, `is`(4))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun listAds_retries_on_500_then_success() {
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
                .setBody(JsonHelpers.normalizedJson("/models/ads_response.json"))
        )
        server.start()
        try {
            val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
            val ads = client.listAds(8L, 697L)
            assertThat(server.requestCount, `is`(2))
            assertThat(ads.size, `is`(1))
            val ad = ads.first()
            assertThat(ad.id, `is`(1098L))
            assertThat(ad.name, `is`("image after all deleted"))
            assertThat(ad.status, `is`("Active"))
            assertThat(ad.pacing, `is`(100.0))
            assertThat(ad.clickTagVerified, `is`(false))
            assertThat(ad.previewTag.startsWith("<iframe"), `is`(true))
            assertThat(ad.targetUrl, `is`("http://simpli.fi"))
            assertThat(ad.adFileTypes.size, `is`(0))
            assertThat(ad.adSizes.size, `is`(0))
            assertThat(ad.adPlacements.size, `is`(0))
            assertThat(ad.resource, `is`("https://app.simpli.fi/api/organizations/8/campaigns/697/ads/1098"))
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun listAds_fails_after_max_retries_server_error() {
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
            val ex = assertThrows(SimplifiServerExceptionRetriesExhausted::class.java) { client.listAds(8L, 697L) }
            assertTrue(ex.message?.contains("Failed after 3 retries due to server error") == true)
            assertThat(server.requestCount, `is`(4))
        } finally {
            server.shutdown()
        }
    }
}
