package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull


/**
 * Response types will only ever be deserialized (received from API)
 */
class CampaignStatsPagedResponseTest {

    @Test
    fun deserializes_from_json() {
        val response = JsonHelpers.readObject("/models/campaign_stats_paged_response.json", CampaignStatsPagedResponse::class.java)
        assertThat(response.campaignStats.size, `is`(1))
        assertThat(response.campaignStats[0].impressions, `is`(10000L))
        assertThat(response.campaignStats[0].clicks, `is`(250L))
        assertThat(response.campaignStats[0].ctr, `is`(0.025))
        assertThat(response.campaignStats[0].cpm, `is`(1.23))
        assertThat(response.campaignStats[0].totalSpend, `is`(1234.56))
        assertThat(response.campaignStats[0].resource, `is`("https://app.simpli.fi/api/campaigns/300/stats"))
        assertThat(response.paging.page, `is`(1))
        assertThat(response.paging.size, `is`(50))
        assertThat(response.paging.total, `is`(1))
        assertNull(response.paging.next)
        assertThat(response.hasNextPage(), `is`(false))
    }

    @Test
    fun deserializes_from_json_next_page() {
        val response = JsonHelpers.readObject("/models/campaign_stats_paged_response_next_page.json", CampaignStatsPagedResponse::class.java)
        assertThat(response.campaignStats.size, `is`(1))
        assertThat(response.campaignStats[0].impressions, `is`(10000L))
        assertThat(response.campaignStats[0].clicks, `is`(250L))
        assertThat(response.campaignStats[0].ctr, `is`(0.025))
        assertThat(response.campaignStats[0].cpm, `is`(1.23))
        assertThat(response.campaignStats[0].totalSpend, `is`(1234.56))
        assertThat(response.campaignStats[0].resource, `is`("https://app.simpli.fi/api/campaigns/300/stats"))
        assertThat(response.paging.page, `is`(1))
        assertThat(response.paging.size, `is`(50))
        assertThat(response.paging.total, `is`(1))
        assertThat(response.paging.next, `is`("/baseUrl/campaigns/300/stats?page=2"))
        assertThat(response.hasNextPage(), `is`(true))
    }

    @Test
    fun deserializes_from_json_empty_list() {
        val response = JsonHelpers.readObject("/models/campaign_stats_paged_response_empty_list.json", CampaignStatsPagedResponse::class.java)
        assertThat(response.campaignStats.size, `is`(0))
        assertThat(response.paging.page, `is`(1))
        assertThat(response.paging.size, `is`(50))
        assertThat(response.paging.total, `is`(1))
        assertNull(response.paging.next)
        assertThat(response.hasNextPage(), `is`(false))
    }
}

