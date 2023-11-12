package com.khanalytic.integrations.swiggy

import com.khanalytic.integrations.GeocoderApi
import com.khanalytic.integrations.Serialization
import com.khanalytic.models.Complaint
import com.khanalytic.models.Menu
import com.khanalytic.models.SalesSummary
import com.khanalytic.integrations.responses.ComplaintIdsBatch
import com.khanalytic.integrations.responses.MenuOrdersBatch
import com.khanalytic.integrations.swiggy.responses.BrandDetailResponse
import com.khanalytic.integrations.swiggy.responses.BrandsResponse
import com.khanalytic.integrations.swiggy.responses.ComplaintResponse
import com.khanalytic.integrations.swiggy.responses.ComplaintsIdResponse
import com.khanalytic.integrations.swiggy.responses.MenuOrderResponse
import com.khanalytic.integrations.swiggy.responses.MenuResponse
import com.khanalytic.integrations.swiggy.responses.SalesSummaryResponse
import com.khanalytic.integrations.swiggy.responses.toBrand
import com.khanalytic.integrations.swiggy.responses.toComplaint
import com.khanalytic.integrations.swiggy.responses.toComplaintIdsBatch
import com.khanalytic.integrations.swiggy.responses.toMenu
import com.khanalytic.integrations.swiggy.responses.toMenuOrdersBatch
import com.khanalytic.integrations.swiggy.responses.toSalesSummary
import com.khanalytic.models.Brand
import kotlinx.datetime.LocalDate
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SwiggyResponseParser : KoinComponent {
    private val serializer = Serialization.serializer
    private val geocoderApi: GeocoderApi by inject()

    fun parseBrandsResponse(response: String): BrandsResponse {
        val regex = Regex(".*globals.userInfoDataParam\\s*=\\s*(.*?);.*")
        return regex.find(response)?.groups?.get(1)?.value?.let { json: String ->
            serializer.decodeFromString<BrandsResponse>(json)
        } ?: throw DataParseException("failed to extract brands list")
    }

    @Throws(Exception::class) suspend fun parseBrand(
        platformId: Long,
        remoteBrandId: String,
        active: Boolean,
        response: String,
    ): Brand {
        val brandDetail = serializer.decodeFromString<BrandDetailResponse>(response).data
        val location = geocoderApi.
        gecode(brandDetail.address)
        return brandDetail.toBrand(platformId, remoteBrandId, location, active)
    }

    @Throws(Exception::class) fun parseSalesSummary(
        platformBrandId: Long,
        date: LocalDate,
        response: String
    ): SalesSummary =
        serializer.decodeFromString<SalesSummaryResponse>(response)
            .data.orderMetricsV4.summary.toSalesSummary(platformBrandId, date)

    @Throws(Exception::class) fun parseMenu(platformBrandId: Long, response: String): Menu =
        serializer.decodeFromString<MenuResponse>(response).toMenu(platformBrandId)

    @Throws(Exception::class) fun parseOrders(
        response: String,
        platformBrandId: Long,
        menu: Menu
    ): MenuOrdersBatch =
        serializer.decodeFromString<MenuOrderResponse>(response)
            .toMenuOrdersBatch(platformBrandId, menu)

    @Throws(Exception::class) fun parseComplaintIdsBatch(response: String): ComplaintIdsBatch =
        serializer.decodeFromString<ComplaintsIdResponse>(response).toComplaintIdsBatch()

    @Throws(Exception::class) fun parseComplaint(
        platformBrandId: Long,
        response: String
    ): Complaint =
        serializer.decodeFromString<ComplaintResponse>(response).toComplaint(platformBrandId)
}