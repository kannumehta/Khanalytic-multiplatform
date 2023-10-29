package com.khanalytic.integrations.swiggy

import com.khanalytic.integrations.PlatformResponseParser
import com.khanalytic.integrations.models.Complaint
import com.khanalytic.integrations.models.Menu
import com.khanalytic.integrations.models.SalesSummary
import com.khanalytic.integrations.responses.ComplaintIdsBatch
import com.khanalytic.integrations.responses.MenuOrdersBatch
import com.khanalytic.integrations.swiggy.responses.BrandDetail
import com.khanalytic.integrations.swiggy.responses.BrandDetailResponse
import com.khanalytic.integrations.swiggy.responses.BrandsResponse
import com.khanalytic.integrations.swiggy.responses.ComplaintResponse
import com.khanalytic.integrations.swiggy.responses.ComplaintsIdResponse
import com.khanalytic.integrations.swiggy.responses.MenuOrderResponse
import com.khanalytic.integrations.swiggy.responses.MenuResponse
import com.khanalytic.integrations.swiggy.responses.SalesSummaryResponse
import com.khanalytic.integrations.swiggy.responses.toComplaint
import com.khanalytic.integrations.swiggy.responses.toComplaintIdsBatch
import com.khanalytic.integrations.swiggy.responses.toMenu
import com.khanalytic.integrations.swiggy.responses.toMenuOrdersBatch
import com.khanalytic.integrations.swiggy.responses.toSalesSummary
import kotlinx.serialization.json.Json

class SwiggyResponseParser(private val serializer: Json) : PlatformResponseParser {
    override fun parseBrandIds(response: String): List<String> {
        val regex = Regex(".*globals.userInfoDataParam\\s*=\\s*(.*?);.*")
        return regex.find(response)?.groups?.get(1)?.value?.let { json: String ->
            serializer.decodeFromString<BrandsResponse>(json).outlets.map { it.resId.toString() }
        } ?: throw DataParseException("failed to extract brands list")
    }

    @Throws(Exception::class) override fun parseBrand(response: String): BrandDetail =
        serializer.decodeFromString<BrandDetailResponse>(response).data

    @Throws(Exception::class) override fun parseSalesSummary(response: String): SalesSummary =
        serializer.decodeFromString<SalesSummaryResponse>(response)
            .data.orderMetricsV4.summary.toSalesSummary()

    @Throws(Exception::class) override fun parseMenu(response: String): Menu =
        serializer.decodeFromString<MenuResponse>(response).toMenu()

    @Throws(Exception::class) override fun parseOrders(
        response: String,
        menu: Menu
    ): MenuOrdersBatch =
        serializer.decodeFromString<MenuOrderResponse>(response).toMenuOrdersBatch(menu)

    @Throws(Exception::class) override fun parseComplaintIdsBatch(response: String): ComplaintIdsBatch =
        serializer.decodeFromString<ComplaintsIdResponse>(response).toComplaintIdsBatch()

    @Throws(Exception::class) override fun parseComplaint(response: String): Complaint =
        serializer.decodeFromString<ComplaintResponse>(response).toComplaint()
}