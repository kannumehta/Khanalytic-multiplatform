package com.khanalytic.integrations.swiggy

import com.khanalytic.integrations.http.HttpRequestBuilderExtensions.origin
import com.khanalytic.integrations.http.HttpRequestBuilderExtensions.referer
import com.khanalytic.integrations.Page
import com.khanalytic.integrations.PlatformApi
import com.khanalytic.integrations.Serialization
import com.khanalytic.models.Complaint
import com.khanalytic.models.Menu
import com.khanalytic.models.MenuOrder
import com.khanalytic.models.SalesSummary
import com.khanalytic.integrations.responses.ComplaintIdsBatch
import com.khanalytic.integrations.responses.MenuOrdersBatch
import com.khanalytic.integrations.swiggy.SwiggyConstants.acceptHtml
import com.khanalytic.integrations.swiggy.SwiggyConstants.rmsAccessToken
import com.khanalytic.integrations.swiggy.SwiggyConstants.commonHeaders
import com.khanalytic.integrations.swiggy.SwiggyConstants.requestedBy
import com.khanalytic.integrations.swiggy.SwiggyConstants.userMeta
import com.khanalytic.integrations.swiggy.SwiggyConstants.vhcAccessToken
import com.khanalytic.integrations.swiggy.requests.complaintIdsRequestBody
import com.khanalytic.integrations.swiggy.requests.complaintRequestBody
import com.khanalytic.integrations.swiggy.requests.sendEmailReportRequestBody
import com.khanalytic.models.Brand
import io.ktor.client.*
import io.ktor.client.plugins.cookies.cookies
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.encodeToString

class SwiggyApi(
    private val httpClient: HttpClient,
    private val responseParser: SwiggyResponseParser
) : PlatformApi {
    private val serializer = Serialization.serializer

    @Throws(Exception::class)
    override suspend fun getBrands(
        platformId: Long,
        existingRemoteBrandIds: Set<String>
    ): List<Brand> = coroutineScope {
        val brandIdsResponse = httpClient.get(SwiggyConstants.ordersUrl()) {
            commonHeaders()
            acceptHtml()
        }.bodyAsText()

        responseParser.parseRemoteBrandIds(brandIdsResponse)
            .filterNot { remoteBrandId ->  existingRemoteBrandIds.contains(remoteBrandId) }
            .map { remoteBrandId -> async { getBrand(platformId, remoteBrandId) } }
            .awaitAll()
    }

    @Throws(Exception::class)
    override suspend fun getSalesSummary(
        resId: String,
        startDate: String,
        endDate: String
    ): SalesSummary {
        return responseParser.parseSalesSummary(
            httpClient.get(SwiggyConstants.salesSummaryUrl(resId, startDate, endDate)) {
                commonHeaders()
                accept(ContentType.Application.Json)
                accept(ContentType.Text.Plain)
                accept(ContentType.Any)
                referer(SwiggyConstants.restaurantReferrer(resId))
            }.bodyAsText()
        )
    }

    @Throws(Exception::class)
    override suspend fun getMenu(platformBrandId: Long, resId: String): Menu {
        return responseParser.parseMenu(
            platformBrandId,
            httpClient.get(SwiggyConstants.menuUrl(resId)) { rmsHostHeaders() }.bodyAsText()
        )
    }

    @Throws(Exception::class)
    override suspend fun getOrders(
        resId: String,
        startDate: String,
        endDate: String,
        menu: Menu
    ): List<MenuOrder> {
        var page = Page(0, 20)
        val orders = mutableListOf<MenuOrder>()
        while (true) {
            val ordersBatch = ordersBatch(resId, startDate, endDate, menu, page)
            orders.addAll(ordersBatch.batch)
            if (ordersBatch.nextRequestData == null) { break }
            val newOffset = page.offset + minOf(ordersBatch.batch.size, page.limit)
            page = Page(newOffset, page.limit)
        }
        return orders
    }

    @Throws(Exception::class)
    override suspend fun getComplaints(
        resId: String,
        startDate: String,
        endDate: String
    ): List<Complaint> = coroutineScope {
        var nextToken: String? = null
        val complaintIds = mutableListOf<String>()
        while (true) {
            val batch = complaintsBatch(resId, startDate, endDate, nextToken)
            complaintIds.addAll(batch.ids)
            nextToken = batch.nextRequestData
            if (nextToken == null) { break }
        }
        complaintIds.map { async { getComplaint(it) } }.awaitAll()
    }

    @Throws(Exception::class)
    override suspend fun sendEmailReport(
        resId: String,
        startDate: String,
        endDate: String,
        email: String
    ) {
        val requestBody = sendEmailReportRequestBody(resId, startDate, endDate, email)
        val unused = httpClient.post(SwiggyConstants.sendEmailReportUrl()) {
            vhcHostHeaders()
            setBody(serializer.encodeToString(requestBody))
        }.bodyAsText()
    }

    private suspend fun getBrand(platformId: Long, remoteBrandId: String): Brand {
        return responseParser.parseBrand(
            platformId,
            remoteBrandId,
            httpClient.get(SwiggyConstants.restaurantDetailsUrl(remoteBrandId)) {
                rmsHostHeaders()
            }.bodyAsText()
        )
    }

    private suspend fun ordersBatch(
        resId: String,
        startDate: String,
        endDate: String,
        menu: Menu,
        page: Page,
    ): MenuOrdersBatch {
        val url = SwiggyConstants.pastOrdersUrl(resId, startDate, endDate, page)
        return responseParser.parseOrders(httpClient.get(url) {
            rmsHostHeaders()
        }.bodyAsText(), menu)
    }

    private suspend fun complaintsBatch(
        resId: String,
        startDate: String,
        endDate: String,
        nextToken: String?,
    ): ComplaintIdsBatch {
        val requestBody = complaintIdsRequestBody(resId, startDate, endDate, nextToken)
        return responseParser.parseComplaintIdsBatch(
            httpClient.post(SwiggyConstants.complaintIdsUrl()) {
                vhcHostHeaders()
                setBody(serializer.encodeToString(requestBody))
            }.bodyAsText()
        )
    }

    private suspend fun getComplaint(complaintId: String): Complaint {
        val requestBody = complaintRequestBody(complaintId)
        return responseParser.parseComplaint(
            httpClient.post(SwiggyConstants.complaintUrl()) {
                vhcHostHeaders()
                setBody(serializer.encodeToString(requestBody))
            }.bodyAsText()
        )
    }

    private suspend fun HttpRequestBuilder.rmsHostHeaders() {
        commonHeaders()
        accept(ContentType.Any)
        referer("${SwiggyConstants.SELF_CLIENT_HOST}/")
        origin(SwiggyConstants.SELF_CLIENT_HOST)
        contentType(ContentType.Application.Json)
        rmsAccessToken(getAccessToken())
        requestedBy("VENDOR")
        userMeta("{\"source\":\"VENDOR\",\"meta\":{\"updated_by\":\"VENDOR\"}}")
    }

    private suspend fun HttpRequestBuilder.vhcHostHeaders() {
        commonHeaders()
        accept(ContentType.Any)
        referer("${SwiggyConstants.API_HOST}/")
        origin(SwiggyConstants.API_HOST)
        contentType(ContentType.Application.Json)
        vhcAccessToken(getAccessToken())
    }

    private suspend fun getAccessToken(): String {
        return httpClient.cookies("").find {
            it.name == "Swiggy_Session-alpha"
        }?.value ?: throw DataParseException("unable to find access token from cookies")
    }
}