package com.khanalytic.kmm.importing.intgerations.swiggy

import com.khanalytic.kmm.http.Cookie.Companion.toCookies
import com.khanalytic.kmm.http.HttpRequestBuilderExtensions.cookies
import com.khanalytic.kmm.http.HttpRequestBuilderExtensions.origin
import com.khanalytic.kmm.http.HttpRequestBuilderExtensions.referer
import com.khanalytic.kmm.importing.intgerations.HttpClientPlatformApi
import com.khanalytic.kmm.importing.intgerations.Page
import com.khanalytic.kmm.importing.intgerations.PlatformResponseParser
import com.khanalytic.kmm.importing.intgerations.models.Complaint
import com.khanalytic.kmm.importing.intgerations.models.Menu
import com.khanalytic.kmm.importing.intgerations.models.MenuOrder
import com.khanalytic.kmm.importing.intgerations.models.SalesSummary
import com.khanalytic.kmm.importing.intgerations.responses.ComplaintIdsBatch
import com.khanalytic.kmm.importing.intgerations.responses.MenuOrdersBatch
import com.khanalytic.kmm.importing.intgerations.swiggy.SwiggyConstants.acceptHtml
import com.khanalytic.kmm.importing.intgerations.swiggy.SwiggyConstants.rmsAccessToken
import com.khanalytic.kmm.importing.intgerations.swiggy.SwiggyConstants.commonHeaders
import com.khanalytic.kmm.importing.intgerations.swiggy.SwiggyConstants.requestedBy
import com.khanalytic.kmm.importing.intgerations.swiggy.SwiggyConstants.userMeta
import com.khanalytic.kmm.importing.intgerations.swiggy.SwiggyConstants.vhcAccessToken
import com.khanalytic.kmm.importing.intgerations.swiggy.requests.complaintIdsRequestBody
import com.khanalytic.kmm.importing.intgerations.swiggy.requests.complaintRequestBody
import com.khanalytic.kmm.importing.intgerations.swiggy.requests.sendEmailReportRequestBody
import com.khanalytic.kmm.importing.intgerations.swiggy.responses.BrandDetail
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SwiggyApi(
    private val serializer: Json,
    private val httpClient: HttpClient,
    private val responseParser: PlatformResponseParser,
    cookie: String
) : HttpClientPlatformApi(httpClient) {

    // TODO(kannumehta@): This should be moved to a common place.
    private val cookies = cookie.toCookies(SwiggyConstants.API_HOST)

    @Throws(Exception::class)
    override suspend fun getBrands(): List<BrandDetail> = coroutineScope {
        val brandIdsResponse = httpClient.get(SwiggyConstants.ordersUrl()) {
            commonHeaders()
            acceptHtml()
            cookies(cookies)
        }.bodyAsText()

        responseParser.parseBrandIds(brandIdsResponse)
            .map { brandId -> async { getBrand(brandId) } }
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
                cookies(cookies)
                referer(SwiggyConstants.restaurantReferrer(resId))
            }.bodyAsText()
        )
    }

    @Throws(Exception::class)
    override suspend fun getMenu(resId: String): Menu {
        return responseParser.parseMenu(
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

    private suspend fun getBrand(resId: String): BrandDetail {
        return responseParser.parseBrand(
            httpClient.get(SwiggyConstants.restaurantDetailsUrl(resId)) {
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

    private fun HttpRequestBuilder.rmsHostHeaders() {
        commonHeaders()
        accept(ContentType.Any)
        referer("${SwiggyConstants.SELF_CLIENT_HOST}/")
        origin(SwiggyConstants.SELF_CLIENT_HOST)
        contentType(ContentType.Application.Json)
        rmsAccessToken(getAccessToken())
        requestedBy("VENDOR")
        userMeta("{\"source\":\"VENDOR\",\"meta\":{\"updated_by\":\"VENDOR\"}}")
    }

    private fun HttpRequestBuilder.vhcHostHeaders() {
        commonHeaders()
        accept(ContentType.Any)
        referer("${SwiggyConstants.API_HOST}/")
        origin(SwiggyConstants.API_HOST)
        contentType(ContentType.Application.Json)
        vhcAccessToken(getAccessToken())
    }

    private fun getAccessToken(): String {
        return cookies.find { it.key == "Swiggy_Session-alpha" }?.value
            ?: throw DataParseException("unable to find access token from cookies")
    }
}