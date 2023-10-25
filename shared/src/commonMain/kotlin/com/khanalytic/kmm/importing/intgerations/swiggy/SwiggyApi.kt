package com.khanalytic.kmm.importing.intgerations.swiggy

import com.khanalytic.kmm.http.Cookie.Companion.toCookies
import com.khanalytic.kmm.http.HttpRequestBuilderExtensions.cookies
import com.khanalytic.kmm.http.HttpRequestBuilderExtensions.origin
import com.khanalytic.kmm.http.HttpRequestBuilderExtensions.referer
import com.khanalytic.kmm.importing.intgerations.HttpClientPlatformApi
import com.khanalytic.kmm.importing.intgerations.PlatformResponseParser
import com.khanalytic.kmm.importing.intgerations.models.SalesSummary
import com.khanalytic.kmm.importing.intgerations.swiggy.SwiggyConstants.acceptHtml
import com.khanalytic.kmm.importing.intgerations.swiggy.SwiggyConstants.accessToken
import com.khanalytic.kmm.importing.intgerations.swiggy.SwiggyConstants.commonHeaders
import com.khanalytic.kmm.importing.intgerations.swiggy.SwiggyConstants.requestedBy
import com.khanalytic.kmm.importing.intgerations.swiggy.SwiggyConstants.userMeta
import com.khanalytic.kmm.importing.intgerations.swiggy.responses.BrandDetail
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class SwiggyApi(
    private val httpClient: HttpClient,
    private val responseParser: PlatformResponseParser,
    cookie: String
) : HttpClientPlatformApi(httpClient) {

    // TODO(kannumehta@): This should be moved to a common place.
    private val cookies = cookie.toCookies(SwiggyConstants.SWIGGY_API_HOST)

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

    private suspend fun getBrand(resId: String): BrandDetail {
        return responseParser.parseBrand(
            httpClient.get(SwiggyConstants.restaurantDetailsUrl(resId)) {
                commonHeaders()
                accept(ContentType.Any)
                referer("${SwiggyConstants.SWIGGY_SELF_CLIENT_HOST}/")
                origin(SwiggyConstants.SWIGGY_SELF_CLIENT_HOST)
                contentType(ContentType.Application.Json)
                accessToken(getAccessToken())
                requestedBy("VENDOR")
                userMeta("{\"source\":\"VENDOR\",\"meta\":{\"updated_by\":\"VENDOR\"}}")
            }.bodyAsText()
        )
    }

    private fun getAccessToken(): String {
        return cookies.find { it.key == "Swiggy_Session-alpha" }?.value
            ?: throw DataParseException("unable to find access token from cookies")
    }

    companion object {


    }
}