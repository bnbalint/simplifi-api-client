package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull


/**
 * Response types will only ever be deserialized (received from API)
 */
class CampaignsPagedResponseTest {

    @Test
    fun deserializes_from_json() {
        val response = JsonHelpers.readObject("/models/campaigns_paged_response.json", CampaignsPagedResponse::class.java)
        assertThat(response.campaigns.size, `is`(1))
        assertThat(response.campaigns[0].id, `is`(2587560L))
        assertThat(response.campaigns[0].name, `is`("Cookie Audience - ID 10015"))
        assertThat(response.campaigns[0].customId, `is`("10015"))
        assertThat(response.campaigns[0].startDate, `is`("2021-10-01"))
        assertThat(response.campaigns[0].endDate, `is`("2021-12-31"))
        assertThat(response.campaigns[0].impressionCap, `is`(357143))
        assertThat(response.campaigns[0].dailyImpressionCap, `is`(9868))
        assertNull(response.campaigns[0].monthlyImpressionCap)
        assertThat(response.campaigns[0].status, `is`("Ended"))
        assertThat(response.campaigns[0].resource, `is`("https://app.simpli.fi/api/organizations/338742/campaigns/2587560"))
        assertThat(response.paging?.page, `is`(1))
        assertThat(response.paging?.size, `is`(1))
        assertThat(response.paging?.total, `is`(395))
        assertNull(response.paging?.next)
        assertThat(response.hasNextPage(), `is`(false))
    }

    @Test
    fun deserializes_from_json_next_page() {
        val response = JsonHelpers.readObject("/models/campaigns_paged_response_next_page.json", CampaignsPagedResponse::class.java)
        assertThat(response.campaigns.size, `is`(1))
        assertThat(response.campaigns[0].id, `is`(2587560L))
        assertThat(response.campaigns[0].name, `is`("Cookie Audience - ID 10015"))
        assertThat(response.campaigns[0].customId, `is`("10015"))
        assertThat(response.campaigns[0].startDate, `is`("2021-10-01"))
        assertThat(response.campaigns[0].endDate, `is`("2021-12-31"))
        assertThat(response.campaigns[0].impressionCap, `is`(357143))
        assertThat(response.campaigns[0].dailyImpressionCap, `is`(9868))
        assertNull(response.campaigns[0].monthlyImpressionCap)
        assertThat(response.campaigns[0].status, `is`("Ended"))
        assertThat(response.campaigns[0].resource, `is`("https://app.simpli.fi/api/organizations/338742/campaigns/2587560"))
        assertThat(response.paging?.page, `is`(1))
        assertThat(response.paging?.size, `is`(1))
        assertThat(response.paging?.total, `is`(395))
        assertThat(response.paging?.next, `is`("/baseUrl/organizations/283320/campaigns?children=true&page=2&size=1"))
        assertThat(response.hasNextPage(), `is`(true))
    }

    @Test
    fun deserializes_from_json_empty_list() {
        val response = JsonHelpers.readObject("/models/campaigns_paged_response_empty_list.json", CampaignsPagedResponse::class.java)
        assertThat(response.campaigns.size, `is`(0))
        assertThat(response.paging?.page, `is`(1))
        assertThat(response.paging?.size, `is`(50))
        assertThat(response.paging?.total, `is`(1))
        assertNull(response.paging?.next)
    }

    @Test
    fun deserializes_from_json_no_paging() {
        val response = JsonHelpers.readObject("/models/campaigns_paged_response_no_paging.json", CampaignsPagedResponse::class.java)
        assertThat(response.campaigns.size, `is`(1))
        val campaign = response.campaigns[0]
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
        assertNull(response.paging)
        assertThat(response.hasNextPage(), `is`(false))
    }

    @Test
    fun deserializes_from_json_no_next_page() {
        val response = JsonHelpers.readObject("/models/campaigns_paged_response.json", CampaignsPagedResponse::class.java)
        assertThat(response.campaigns.size, `is`(1))
        assertThat(response.campaigns[0].id, `is`(2587560L))
        assertThat(response.campaigns[0].name, `is`("Cookie Audience - ID 10015"))
        assertThat(response.campaigns[0].customId, `is`("10015"))
        assertThat(response.campaigns[0].startDate, `is`("2021-10-01"))
        assertThat(response.campaigns[0].endDate, `is`("2021-12-31"))
        assertThat(response.campaigns[0].impressionCap, `is`(357143))
        assertThat(response.campaigns[0].dailyImpressionCap, `is`(9868))
        assertNull(response.campaigns[0].monthlyImpressionCap)
        assertThat(response.campaigns[0].status, `is`("Ended"))
        assertThat(response.campaigns[0].resource, `is`("https://app.simpli.fi/api/organizations/338742/campaigns/2587560"))
        assertThat(response.paging?.page, `is`(1))
        assertThat(response.paging?.size, `is`(1))
        assertThat(response.paging?.total, `is`(395))
        assertNull(response.paging?.next)
        assertThat(response.hasNextPage(), `is`(false))
    }
}
