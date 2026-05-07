package org.bnbalint.simplifi.integrationTests

import org.bnbalint.simplifi.SimplifiApiClient
import org.bnbalint.simplifi.models.*
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test


/**
 * These are not unit tests
 * They exist to allow manual testing against the Simpli.Fi API
 * To run these tests, you must:
 *   1. Replace the placeholder values for appKey, userKey, etc.
 *   2. Run the test using IntelliJ
 *
 *   The GET tests can be likely run as-is once the credentials are set (depending on the account)
 *   The POST / PUT tests will actually create / modify data in the Simpli.Fi account
 *   so use caution when running those
 *   You will likely need to update the ID values in the POST / PUT tests
 *   to align with data that exists in your Simpli.Fi account
 */
@Disabled
class ManualTests {

    private val baseUrl = "https://app.simpli.fi/api" // Change if using a different environment
    private val apiVersion = "2024-10-08" // Change if using a different environment
    private val appKey = ""
    private val userKey = ""

    // these are the test values in the Simpli.Fi account
    private val baseOrgId = 283320L

    private val childOrgId = 529460L
    private val childOrgCustomId = "87562694"

    private val grandchildOrgId = 529815L
    private val grandchildOrgCustomId = 12345L
    private val grandchildOrgTagId = 267012L
    private val grandchildOrgFirstPartySegmentId = 1312856L
    private val simplifiCampaignId = 4458003L
    private val simplifiCampaignFirstPartySegmentId = 6229701L
    private val simplifiCampaign160by600AdId = 42354891L



    private lateinit var apiClient: SimplifiApiClient

    @BeforeEach
    fun setup() {
        apiClient = SimplifiApiClient(baseUrl, appKey, userKey, 3, 1, 1, 3, 1, 1, apiVersion)
    }

    //
    //
    //  GET Tests
    //
    //


    @Disabled
    @Test
    fun getOrganizationById() {

        val organization = apiClient.getOrganizationById(baseOrgId)
        println("Fetched organization: $organization")
        assertNotNull(organization, "Organization should not be null if credentials and ID are valid")
    }

    @Disabled
    @Test
    fun getOrganizationByCustomId() {

        val organization = apiClient.getOrganizationByCustomId(childOrgCustomId)
        println("Fetched organization: $organization")
        assertNotNull(organization, "Organization should not be null if credentials and ID are valid")
    }

    @Disabled
    @Test
    fun getOrganizationTagsByOrgId() {

        val organizationTags = apiClient.getOrganizationTags(grandchildOrgId)
        println("Fetched organizationTags: $organizationTags")
        assertNotNull(organizationTags, "OrganizationTags should not be null if credentials and ID are valid")
    }

    @Disabled
    @Test
    fun listCampaigns() {

        val campaigns = apiClient.listCampaigns(grandchildOrgId)
        println("Fetched campaigns: $campaigns")
        assertNotNull(campaigns, "Campaigns should not be null if credentials are valid")
    }


    @Disabled
    @Test
    fun listCampaigns_pagingFlow() {

        val totalCampaigns = mutableListOf<Campaign>()
        val size = 150

        // get all campaigns underneath a base org, with page size of 100 to force paging
        var campaignResponse = apiClient.listCampaigns(baseOrgId, children = true, size = size)
        println("Fetched campaignsResponse: $campaignResponse")

        org.junit.jupiter.api.assertNotNull(campaignResponse)
        var campaigns = campaignResponse.campaigns
        totalCampaigns.addAll(campaigns)
        println("Added ${campaigns.size} campaigns to storage, total is now ${totalCampaigns.size}")

        // loop through the pages until there are no more
        while (campaignResponse?.hasNextPage() == true) {
            campaignResponse = campaignResponse.getNextPage(apiClient)
            println("Fetched campaignsResponse: $campaignResponse")

            org.junit.jupiter.api.assertNotNull(campaignResponse)
            campaigns = campaignResponse.campaigns
            totalCampaigns.addAll(campaigns)
            println("Added ${campaigns.size} campaigns to storage, total is now ${totalCampaigns.size}")
        }
    }

    @Disabled
    @Test
    fun listCampaigns_allChildren() {

        val campaigns = apiClient.listCampaigns(baseOrgId, children = true)
        println("Fetched campaigns: $campaigns")
        assertNotNull(campaigns, "Campaigns should not be null if credentials are valid")
    }

    @Disabled
    @Test
    fun getCampaignById() {
        val campaign = apiClient.getCampaignById(simplifiCampaignId)
        println("Fetched campaign: $campaign")
        assertNotNull(campaign, "Campaigns should not be null if credentials are valid")
    }

    @Disabled
    @Test
    fun listCampaignFirstPartySegments() {
        val campaignFirstPartySegments = apiClient.listCampaignFirstPartySegments(simplifiCampaignId, size = 1)
        println("Fetched campaignFirstPartySegments: $campaignFirstPartySegments")
        assertNotNull(campaignFirstPartySegments, "CampaignFirstPartySegments should not be null if credentials are valid")
    }

    @Disabled
    @Test
    fun listCampaignFirstPartySegments_pagingFlow() {

        val totalCampaignFirstPartySegments = mutableListOf<CampaignFirstPartySegment>()
        val size = 1

        var response = apiClient.listCampaignFirstPartySegments(simplifiCampaignId, size = size)
        println("Fetched campaignFirstPartySegments: $response")
        assertNotNull(response, "CampaignFirstPartySegments should not be null if credentials are valid")

        org.junit.jupiter.api.assertNotNull(response)
        var campaignFirstPartySegments = response.campaignFirstPartySegments
        totalCampaignFirstPartySegments.addAll(campaignFirstPartySegments)
        println("Added ${campaignFirstPartySegments.size} segments to storage, total is now ${totalCampaignFirstPartySegments.size}")

        // loop through the pages until there are no more
        while (response?.hasNextPage() == true) {
            response = response.getNextPage(apiClient)
            println("Fetched campaignFirstPartySegments: $response")

            org.junit.jupiter.api.assertNotNull(response)
            campaignFirstPartySegments = response.campaignFirstPartySegments
            totalCampaignFirstPartySegments.addAll(campaignFirstPartySegments)
            println("Added ${campaignFirstPartySegments.size} segments to storage, total is now ${totalCampaignFirstPartySegments.size}")
        }
    }

    @Disabled
    @Test
    fun listAds() {
        val ads = apiClient.listAds(
            audienceOrgId = grandchildOrgId,
            simplifiCampaignId = simplifiCampaignId,
            include = "ad_file_types,ad_sizes,ad_placements"
        )
        println("Fetched ads: $ads")
        assertNotNull(ads, "Ads should not be null if credentials are valid")
    }


    @Disabled
    @Test
    fun getCampaignStats() {

        val campaignStats = apiClient.getCampaignStats(
            orgId = grandchildOrgId,
            startDate = "2024-01-01"
        )
        println("Fetched campaignStats: $campaignStats")
        assertNotNull(campaignStats, "CampaignStats should not be null if credentials are valid")
    }

    @Disabled
    @Test
    fun getCampaignStats_realData() {

        // this is a real Production campaign - don't mess around
        val statOrgId = 400042L
        val startDate = "2024-01-01"
        val endDate = "2025-12-31"

        val campaignStats = apiClient.getCampaignStats(
            orgId = statOrgId,
            startDate = startDate,
            endDate = endDate,
            byDay = true
        )
        println("Fetched campaignStats: $campaignStats")
        assertNotNull(campaignStats, "CampaignStats should not be null if credentials are valid")
    }

    @Disabled
    @Test
    fun getCampaignStats_pagingFlow() {

        // this is real production data - don't mess around
        val statOrgId = 400042L
        val startDate = "2024-01-01"
        val endDate = "2025-12-31"

        val totalStats = mutableListOf<CampaignStat>()
        val size = 150

        // need to set byDay = true to force paging
        var campaignStatResponse = apiClient.getCampaignStats(
            orgId = statOrgId,
            startDate = startDate,
            endDate = endDate,
            size = size,
            byDay = true
        )
        println("Fetched campaignsStatResponse: $campaignStatResponse")

        org.junit.jupiter.api.assertNotNull(campaignStatResponse)
        var stats = campaignStatResponse.campaignStats
        totalStats.addAll(stats)
        println("Added ${stats.size} stats to storage, total is now ${totalStats.size}")


        // loop through the pages until there are no more
        while (campaignStatResponse?.hasNextPage() == true) {
            campaignStatResponse = campaignStatResponse.getNextPage(apiClient)
            println("Fetched campaignsResponse: $campaignStatResponse")

            org.junit.jupiter.api.assertNotNull(campaignStatResponse)
            stats = campaignStatResponse.campaignStats
            totalStats.addAll(stats)
            println("Added ${stats.size} stats to storage, total is now ${totalStats.size}")
        }
    }


    //
    //
    //  POST / PUT Tests
    //
    //

    @Test
    @Disabled
    fun createOrganization() {
        // This will create a new audience organization under the PTB partner organization
        val audienceOrganization = OrganizationCreateBody(
            name = "Audience ID - 45678",
            customId = "45678",
            parentId = "$childOrgId",
        )
        val organization = apiClient.createOrganization(audienceOrganization)
        println("Fetched organization: $organization")
        assertNotNull(organization, "Organization should not be null if credentials are valid")
    }

    @Test
    @Disabled
    fun createCampaign() {
        // This will create a campaign on the PTB 45678 Audience organization

        val body = CampaignCreateBody(
            name = "Cookie Audience - ID 45678 - DNA (Do not activate)",
            parentId = "$grandchildOrgId",
            customId = grandchildOrgCustomId.toString()
        )
        val campaign = apiClient.createCampaign(grandchildOrgId, body)
        println("Fetched campaign: $campaign")
        assertNotNull(campaign, "Campaign should not be null if credentials are valid")
    }

    @Test
    @Disabled
    fun createFirstPartySegment() {

        // NOTE:  if you use the same name more than once, you will get a SimpliFiUnprocessableEntityException


        // this piece is always the same
        val firstPartySegmentRule = FirstPartySegmentRule(
            domain = "meanttonevermatch.com",
            matchPattern = "",
            segmentUrlMatchTypeId = 7
        )

        // This will create a new FirstPartySegment on the PTB 45678 Audience
        val firstPartySegmentBody = FirstPartySegmentCreateBody(
            name = "45678 new",
            firstPartySegmentRules = listOf(firstPartySegmentRule),
            firstPartySegmentTags = listOf(grandchildOrgTagId)
        )
        val firstPartySegment = apiClient.createFirstPartySegment(grandchildOrgId, firstPartySegmentBody)
        println("Fetched firstPartySegment: $firstPartySegment")
        assertNotNull(firstPartySegment, "FirstPartySegment should not be null if credentials are valid")
    }



    @Test
    @Disabled
    fun updateCampaign() {
        val frequencyCapping = FrequencyCapping(
            howManyTimes = 4,
            hours = 24
        )
        val campaignGoal = CampaignGoal(
            goalType = "ctr",
            goalValue = "0.1",
            cpaViewThruPer = "1.0",
            cpaClickThruPer = "1.0"
        )
        val campaignBody = CampaignUpdateBody(
            name = "Cookie Audience - ID 12345 - DNA (Do not activate)",
            customId = grandchildOrgCustomId.toString(),
            impressionCap = 100000,
            startDate = "2024-11-01",
            endDate = "2026-12-31",
            dailyImpressionCap = 5000,
            autoAdjustDailyImpressionCap = true,
            mediaTypeId = 1,
            autoOptimize = true,
            bid = 3.5,
            bidTypeId = 1,
            frequencyCapping = frequencyCapping,
            campaignTypeId = 3,
            campaignGoal = campaignGoal,
            geoTargetIds = listOf("8180")
        )

        val campaign = apiClient.updateCampaign(grandchildOrgId, simplifiCampaignId, campaignBody)
        println("Fetched campaign: $campaign")
        assertNotNull(campaign, "Campaign should not be null if credentials are valid")
    }

    @Test
    @Disabled
    fun updateCampaign_nameOnly() {
        val campaignBody = CampaignUpdateBody(
            name = "Cookie Audience - ID 12345 - DNA (Do not activate)"
        )

        val campaign = apiClient.updateCampaign(grandchildOrgId, simplifiCampaignId, campaignBody)
        println("Fetched campaign: $campaign")
        assertNotNull(campaign, "Campaign should not be null if credentials are valid")
    }

    @Test
    @Disabled
    fun changeCampaignFirstPartySegments_deleteSiteRetargetingAudience() {

        // this would be used to delete the Site Retargeting Audience to the campaign
        // NOTE: you must delete it using the CAMPAIGN FIRST PARTY SEGMENT ID, not the Audience First Party Segment ID
        val campaignFirstPartySegmentBody = CampaignFirstPartySegmentChangeBody(
            delete = listOf(simplifiCampaignFirstPartySegmentId),
            segmentMatchType = "any"
        )

        val response = apiClient.changeCampaignFirstPartySegments(simplifiCampaignId, campaignFirstPartySegmentBody)
        println("Fetched response: $response")
        assertNotNull(response, "CampaignFirstPartySegments should not be null if credentials are valid")
    }

    @Test
    @Disabled
    fun changeCampaignFirstPartySegments_addSiteRetargetingAudience() {

        // this would be used to add the Site Retargeting Audience to the campaign
        val add = CampaignFirstPartySegmentAdd(
            firstPartySegmentId = grandchildOrgFirstPartySegmentId,
            segmentTypeId = 1,
            segmentTargetTypeId = 2
        )

        val campaignFirstPartySegmentBody = CampaignFirstPartySegmentChangeBody(
            add = listOf(add),
            segmentMatchType = "any"
        )

        val response = apiClient.changeCampaignFirstPartySegments(simplifiCampaignId, campaignFirstPartySegmentBody)
        println("Fetched response: $response")
        assertNotNull(response, "CampaignFirstPartySegments should not be null if credentials are valid")
    }

    //@Test // TODO: this has not been tested because we don't have a test account yet
    @Disabled
    fun activateCampaign() {
        val campaign = apiClient.activateCampaign(grandchildOrgId, simplifiCampaignId)
        println("Fetched campaign: $campaign")
        assertNotNull(campaign, "Campaign should not be null if credentials are valid")
    }

    @Test
    @Disabled
    fun pauseCampaign() {
        val campaign = apiClient.pauseCampaign(grandchildOrgId, simplifiCampaignId)
        println("Fetched campaign: $campaign")
        assertNotNull(campaign, "Campaign should not be null if credentials are valid")
    }

    @Test
    @Disabled
    fun createAd() {

        @Suppress("ktlint:standard:max-line-length")
        val adBody = AdCreateBody(
            name = "160x600 duplicate",
            adFileTypeId = "4",
            adSizeId = "3",
            targetUrl = "https://www.corgithings.com",
            html = "<div style=\"width: 100px; height: 100px; background-color: skyblue; display: flex; align-items: center; justify-content: center; text-align: center;\">Pretty Ad</div>"
        )

        val ad = apiClient.createAd(grandchildOrgId, simplifiCampaignId, adBody)
        println("Fetched ad: $ad")
        assertNotNull(ad, "Ad should not be null if credentials are valid")
    }

    @Test
    @Disabled
    fun updateAd() {
        @Suppress("ktlint:standard:max-line-length")
        val adBody = AdUpdateBody(
            name = "160x600",
            adFileTypeId = "4",
            adSizeId = "3",
            targetUrl = "https://www.corgithings.com",
            html = "<div style=\"width: 100px; height: 100px; background-color: skyblue; display: flex; align-items: center; justify-content: center; text-align: center;\">New Pretty Ad</div>"
        )

        val ad = apiClient.updateAd(grandchildOrgId, simplifiCampaignId, simplifiCampaign160by600AdId, adBody)
        println("Fetched ad: $ad")
        assertNotNull(ad, "Ad should not be null if credentials are valid")
    }

    @Test
    @Disabled
    fun updateAd_nameOnly() {
        @Suppress("ktlint:standard:max-line-length")
        val adBody = AdUpdateBody(
            name = "160x600 new name"
        )

        val ad = apiClient.updateAd(grandchildOrgId, simplifiCampaignId, simplifiCampaign160by600AdId, adBody)
        println("Fetched ad: $ad")
        assertNotNull(ad, "Ad should not be null if credentials are valid")
    }
}
