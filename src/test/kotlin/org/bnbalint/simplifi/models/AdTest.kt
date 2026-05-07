package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test



class AdTest {

    @Test
    fun serializes_to_json() {
        @Suppress("ktlint:standard:max-line-length")
        val ad = Ad(
            id = 1098L,
            name = "image after all deleted",
            status = "Active",
            pacing = 100.0,
            clickTagVerified = false,
            previewTag = "<iframe src=\"//adspreview.simpli.fi/ads/development/697/1098/ad.html?sifitest=true&sifi_uid=&sifi_exchange_uid=&cb=9573207624&sifi=5825,697,1098,0,10,0,0,0,0,0,0,v,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,dalbid1,0,0,0,0,0,0,0,NoPC&request_id=42&dp_tmp=&sifi_kw=tv\" width=\"160\" height=\"90\" frameborder=\"0\" marginwidth=\"0\" marginheight=\"0\" scrolling=\"no\"></iframe>",
            targetUrl = "http://simpli.fi",
            resource = "https://app.simpli.fi/api/organizations/8/campaigns/697/ads/1098"
        )

        val json = JsonHelpers.getJson(ad)
        println(json)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/ad.json")))
    }

    @Test
    fun serialize_to_json_list_of_file_types() {
        val ad = Ad(
            id = 10L,
            name = "AdWithTypes",
            status = "Active",
            pacing = 10.0,
            clickTagVerified = true,
            previewTag = "<preview>",
            targetUrl = "https://example.com",
            adFileTypes = listOf(
                AdFileType(4, "HTML", "https://app.simpli.fi/api/ad_file_types/4"),
                AdFileType(1, "Image", "https://app.simpli.fi/api/ad_file_types/1")
            ),
            resource = "https://app.simpli.fi/api/organizations/8/campaigns/697/ads/10"
        )
        val json = JsonHelpers.getJson(ad)
        println(json)
        assertThat(json, `is`(JsonHelpers.normalizedJson("/models/ad_with_list_of_file_types.json")))
    }

    @Test
    fun deserializes_from_json_standard() {
        val ad = JsonHelpers.readObject("/models/ad_full.json", Ad::class.java)
        assertThat(ad.id, `is`(1098L))
        assertThat(ad.name, `is`("image after all deleted"))
        assertThat(ad.status, `is`("Active"))
        assertThat(ad.pacing, `is`(100.0))
        assertThat(ad.clickTagVerified, `is`(false))

        @Suppress("ktlint:standard:max-line-length", "ktlint:standard:argument-list-wrapping")
        assertThat(ad.previewTag, `is`("<iframe src=\"//adspreview.simpli.fi/ads/development/697/1098/ad.html?sifitest=true&sifi_uid=&sifi_exchange_uid=&cb=9573207624&sifi=5825,697,1098,0,10,0,0,0,0,0,0,v,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,dalbid1,0,0,0,0,0,0,0,NoPC&request_id=42&dp_tmp=&sifi_kw=tv\" width=\"160\" height=\"90\" frameborder=\"0\" marginwidth=\"0\" marginheight=\"0\" scrolling=\"no\"></iframe>"))
        assertThat(ad.targetUrl, `is`("http://simpli.fi"))
        assertThat(ad.resource, `is`("https://app.simpli.fi/api/organizations/8/campaigns/697/ads/1098"))
    }

    @Test
    fun deserializes_from_json_standard_with_sizes() {
        val ad = JsonHelpers.readObject("/models/ad_full_with_sizes.json", Ad::class.java)

        assertThat(ad.id, `is`(1098L))
        assertThat(ad.name, `is`("image after all deleted"))
        assertThat(ad.status, `is`("Active"))
        assertThat(ad.pacing, `is`(100.0))
        assertThat(ad.clickTagVerified, `is`(false))

        @Suppress("ktlint:standard:max-line-length", "ktlint:standard:argument-list-wrapping")
        assertThat(ad.previewTag, `is`("<iframe src=\"//adspreview.simpli.fi/ads/development/697/1098/ad.html?sifitest=true&sifi_uid=&sifi_exchange_uid=&cb=9573207624&sifi=5825,697,1098,0,10,0,0,0,0,0,0,v,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,dalbid1,0,0,0,0,0,0,0,NoPC&request_id=42&dp_tmp=&sifi_kw=tv\" width=\"160\" height=\"90\" frameborder=\"0\" marginwidth=\"0\" marginheight=\"0\" scrolling=\"no\"></iframe>"))
        assertThat(ad.targetUrl, `is`("http://simpli.fi"))
        assertThat(ad.resource, `is`("https://app.simpli.fi/api/organizations/8/campaigns/697/ads/1098"))
        assertThat(ad.adSizes[0].id, `is`(3))
        assertThat(ad.adSizes[0].width, `is`(160))
        assertThat(ad.adSizes[0].height, `is`(600))
        assertThat(ad.adSizes[0].name, `is`("160x600"))
        assertThat(ad.adSizes[0].allowedAudioCompanionSize, `is`(false))
        assertThat(ad.adSizes[0].resource, `is`("https://app.simpli.fi/api/ad_sizes/3"))
    }

    @Test
    fun deserializes_from_json_standard_with_placements() {
        val ad = JsonHelpers.readObject("/models/ad_full_with_placements.json", Ad::class.java)

        assertThat(ad.id, `is`(1098L))
        assertThat(ad.name, `is`("image after all deleted"))
        assertThat(ad.status, `is`("Active"))
        assertThat(ad.pacing, `is`(100.0))
        assertThat(ad.clickTagVerified, `is`(false))

        @Suppress("ktlint:standard:max-line-length", "ktlint:standard:argument-list-wrapping")
        assertThat(ad.previewTag, `is`("<iframe src=\"//adspreview.simpli.fi/ads/development/697/1098/ad.html?sifitest=true&sifi_uid=&sifi_exchange_uid=&cb=9573207624&sifi=5825,697,1098,0,10,0,0,0,0,0,0,v,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,dalbid1,0,0,0,0,0,0,0,NoPC&request_id=42&dp_tmp=&sifi_kw=tv\" width=\"160\" height=\"90\" frameborder=\"0\" marginwidth=\"0\" marginheight=\"0\" scrolling=\"no\"></iframe>"))
        assertThat(ad.targetUrl, `is`("http://simpli.fi"))
        assertThat(ad.resource, `is`("https://app.simpli.fi/api/organizations/8/campaigns/697/ads/1098"))
        assertThat(ad.adPlacements[0].id, `is`(3))
        assertThat(ad.adPlacements[0].name, `is`("placement"))
        assertThat(ad.adPlacements[0].resource, `is`("https://app.simpli.fi/api/ad_placements/3"))
    }

    @Test
    fun deserializes_from_json_standard_with_file_types() {
        val ad = JsonHelpers.readObject("/models/ad_full_with_file_types.json", Ad::class.java)

        assertThat(ad.id, `is`(1098L))
        assertThat(ad.name, `is`("image after all deleted"))
        assertThat(ad.status, `is`("Active"))
        assertThat(ad.pacing, `is`(100.0))
        assertThat(ad.clickTagVerified, `is`(false))

        @Suppress("ktlint:standard:max-line-length", "ktlint:standard:argument-list-wrapping")
        assertThat(ad.previewTag, `is`("<iframe src=\"//adspreview.simpli.fi/ads/development/697/1098/ad.html?sifitest=true&sifi_uid=&sifi_exchange_uid=&cb=9573207624&sifi=5825,697,1098,0,10,0,0,0,0,0,0,v,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,dalbid1,0,0,0,0,0,0,0,NoPC&request_id=42&dp_tmp=&sifi_kw=tv\" width=\"160\" height=\"90\" frameborder=\"0\" marginwidth=\"0\" marginheight=\"0\" scrolling=\"no\"></iframe>"))
        assertThat(ad.targetUrl, `is`("http://simpli.fi"))
        assertThat(ad.resource, `is`("https://app.simpli.fi/api/organizations/8/campaigns/697/ads/1098"))
        assertThat(ad.adFileTypes[0].id, `is`(4))
        assertThat(ad.adFileTypes[0].name, `is`("HTML"))
        assertThat(ad.adFileTypes[0].resource, `is`("https://app.simpli.fi/api/ad_file_types/4"))
    }
}
