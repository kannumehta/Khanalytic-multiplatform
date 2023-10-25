package com.khanalytic.kmm.importing.intgerations.swiggy

import com.khanalytic.kmm.importing.intgerations.Cookie.Companion.toCookies
import com.khanalytic.kmm.importing.intgerations.HttpClientPlatformApi
import com.khanalytic.kmm.importing.intgerations.PlatformResponseParser
import com.khanalytic.kmm.importing.intgerations.swiggy.responses.BrandDetail
import io.ktor.client.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class SwiggyApi(
    httpClient: HttpClient,
    private val responseParser: PlatformResponseParser,
    cookie: String
) : HttpClientPlatformApi(httpClient) {

    // TODO(kannumehta@): This should be moved to a common place.
    private val cookies = cookie.toCookies(SwiggyConstants.SWIGGY_API_HOST)

    @Throws(Exception::class)
    override suspend fun getBrands(): List<BrandDetail> = coroutineScope {
        val headers = SwiggyConstants.commonHeaders.plus(mapOf(
            "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/" +
                    "webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
        ))
        return@coroutineScope responseParser.parseBrandIds(get(
            SwiggyConstants.ordersUrl(),
            headers,
            cookies
        )).map { brandId ->
            async { getBrand(brandId) }
        }.awaitAll()
    }

    @Throws(Exception::class)
    override suspend fun getSalesSummary(
        resId: String,
        startDate: String,
        endDate: String
    ): String {
        val headers = SwiggyConstants.commonHeaders.plus(mapOf(
            "Accept" to "application/json, text/plain, */*",
            "Referer" to SwiggyConstants.restaurantReferrer(resId),
        ))
        return get(SwiggyConstants.salesSummaryUrl(resId, startDate, endDate), headers, cookies)
    }

    private suspend fun getBrand(resId: String): BrandDetail {
        val headers = SwiggyConstants.commonHeaders.plus(mapOf(
            "Accept" to "*/*",
            "Accesstoken" to getAccessToken(),
            "Content-Type" to "application/json",
            "Origin" to SwiggyConstants.SWIGGY_SELF_CLIENT_HOST,
            "Referer" to "${SwiggyConstants.SWIGGY_SELF_CLIENT_HOST}/",
            "Requested_by" to "VENDOR",
            "User-Meta" to "{\"source\":\"VENDOR\",\"meta\":{\"updated_by\":\"VENDOR\"}}"
        ))
        return responseParser.parseBrand(
            get(SwiggyConstants.restaurantDetailsUrl(resId), headers, listOf())
        )
    }

    private fun getAccessToken(): String {
        return cookies.find { it.key == "Swiggy_Session-alpha" }?.value
            ?: throw DataParseException("unable to find access token from cookies")
    }

    companion object {


    }
}