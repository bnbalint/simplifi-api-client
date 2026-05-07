package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

/**
 * Response types will only ever be deserialized (received from API)
 */
class AdsResponseTest {

    @Test
    fun deserializes_from_json() {
        val response = JsonHelpers.readObject("/models/ads_response.json", AdsResponse::class.java)
        assertThat(response.ads.size, `is`(1))
        assertThat(response.ads[0].id, `is`(1098L))
        assertThat(response.ads[0].name, `is`("image after all deleted"))
        assertThat(response.ads[0].status, `is`("Active"))
        assertThat(response.ads[0].pacing, `is`(100.0))
        assertThat(response.ads[0].clickTagVerified, `is`(false))

        @Suppress("ktlint:standard:max-line-length", "ktlint:standard:argument-list-wrapping")
        assertThat(response.ads[0].previewTag, `is`("<iframe src=\"//adspreview.simpli.fi/ads/development/697/1098/ad.html?sifitest=true&sifi_uid=&sifi_exchange_uid=&cb=9573207624&sifi=5825,697,1098,0,10,0,0,0,0,0,0,v,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,dalbid1,0,0,0,0,0,0,0,NoPC&request_id=42&dp_tmp=&sifi_kw=tv\" width=\"160\" height=\"90\" frameborder=\"0\" marginwidth=\"0\" marginheight=\"0\" scrolling=\"no\"></iframe>"))
        assertThat(response.ads[0].targetUrl, `is`("http://simpli.fi"))
        assertThat(response.ads[0].resource, `is`("https://app.simpli.fi/api/organizations/8/campaigns/697/ads/1098"))
    }

    @Test
    fun deserializes_from_json_with_next_page() {
        val response = JsonHelpers.readObject("/models/ads_response.json", AdsResponse::class.java)
        assertThat(response.ads.size, `is`(1))
        assertThat(response.ads[0].id, `is`(1098L))
        assertThat(response.ads[0].name, `is`("image after all deleted"))
        assertThat(response.ads[0].status, `is`("Active"))
        assertThat(response.ads[0].pacing, `is`(100.0))
        assertThat(response.ads[0].clickTagVerified, `is`(false))

        @Suppress("ktlint:standard:max-line-length", "ktlint:standard:argument-list-wrapping")
        assertThat(response.ads[0].previewTag, `is`("<iframe src=\"//adspreview.simpli.fi/ads/development/697/1098/ad.html?sifitest=true&sifi_uid=&sifi_exchange_uid=&cb=9573207624&sifi=5825,697,1098,0,10,0,0,0,0,0,0,v,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,dalbid1,0,0,0,0,0,0,0,NoPC&request_id=42&dp_tmp=&sifi_kw=tv\" width=\"160\" height=\"90\" frameborder=\"0\" marginwidth=\"0\" marginheight=\"0\" scrolling=\"no\"></iframe>"))
        assertThat(response.ads[0].targetUrl, `is`("http://simpli.fi"))
        assertThat(response.ads[0].resource, `is`("https://app.simpli.fi/api/organizations/8/campaigns/697/ads/1098"))
    }

    @Test
    fun deserializes_from_json_without_paging() {
        val response = JsonHelpers.readObject("/models/ads_response.json", AdsResponse::class.java)
        assertThat(response.ads.size, `is`(1))
        val ad = response.ads[0]
        assertThat(ad.id, `is`(1098L))
        assertThat(ad.name, `is`("image after all deleted"))
        assertThat(ad.status, `is`("Active"))
        assertThat(ad.pacing, `is`(100.0))
        assertThat(ad.clickTagVerified, `is`(false))
        @Suppress("ktlint:standard:max-line-length", "ktlint:standard:argument-list-wrapping")
        assertThat(ad.previewTag, `is`("<iframe src=\"//adspreview.simpli.fi/ads/development/697/1098/ad.html?sifitest=true&sifi_uid=&sifi_exchange_uid=&cb=9573207624&sifi=5825,697,1098,0,10,0,0,0,0,0,0,v,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,dalbid1,0,0,0,0,0,0,0,NoPC&request_id=42&dp_tmp=&sifi_kw=tv\" width=\"160\" height=\"90\" frameborder=\"0\" marginwidth=\"0\" marginheight=\"0\" scrolling=\"no\"></iframe>"))
        assertThat(ad.targetUrl, `is`("http://simpli.fi"))
        assertThat(ad.resource, `is`("https://app.simpli.fi/api/organizations/8/campaigns/697/ads/1098"))
        assertThat(ad.adFileTypes.isEmpty(), `is`(true))
        assertThat(ad.adSizes.isEmpty(), `is`(true))
        assertThat(ad.adPlacements.isEmpty(), `is`(true))
    }
}
