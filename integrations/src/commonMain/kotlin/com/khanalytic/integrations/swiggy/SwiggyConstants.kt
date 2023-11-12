package com.khanalytic.integrations.swiggy

import com.khanalytic.integrations.Page
import io.ktor.client.request.*
import kotlinx.datetime.LocalDate

object SwiggyConstants {
    const val API_HOST = "https://partner.swiggy.com"
    const val RMS_HOST = "https://rms.swiggy.com"
    const val SELF_CLIENT_HOST = "https://partner-self-client.swiggy.com"
    const val VHC_HOST = "https://vhc-composer.swiggy.com"

    fun salesSummaryUrl(
        resId: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): String = "$API_HOST/insights/v1/revenue/?endDate=$endDate&groupBy=day" +
            "&restaurantId=$resId&startDate=$startDate"

    fun ordersUrl(): String = "$API_HOST/orders"
    fun restaurantDetailsUrl(resId: String) =
        "$RMS_HOST/api/cms/menu-revision/v1/restaurantDetails/$resId?"
    fun menuUrl(resId: String): String =
        "$RMS_HOST/api/cms/menu-revision/v1/restaurant-menu/$resId?disabled=true&item_holiday_slots=true&type=REGULAR_MENU"
    fun pastOrdersUrl( resId: String, startDate: LocalDate, endDate: LocalDate, page: Page) =
        "$RMS_HOST/orders/v1/history?limit=${page.limit}&offset=${page.offset}" +
                "&ordered_time__gte=$startDate&ordered_time__lte=$endDate&restaurant_id=$resId"

    fun complaintIdsUrl(): String = "$VHC_HOST/query?query=Complaints"

    fun complaintUrl(): String = "$VHC_HOST/query?query=Complaints"

    fun sendEmailReportUrl(): String = "$VHC_HOST/query?query=DownloadFinanceReport"

    fun restaurantReferrer(resId: String): String =
        "https://partner.swiggy.com/business-metrics/revenue/restaurant/$resId"

    fun HttpRequestBuilder.commonHeaders() {
        header("Accept-Encoding", "gzip, deflate, br")
        header("Accept-Language", "en-US,en;q=0.9")
        header("Sec-Ch-Ua", "\"Chromium\";v=\"118\", \"Google Chrome\";v=\"118\", \"Not=A?Brand\";v=\"99\"")
        header("Sec-Ch-Ua-Mobile", "?0")
        header("Sec-Ch-Ua-Platform", "\"macOS\"")
        header("Sec-Fetch-Dest", "empty")
        header("Sec-Fetch-Mode", "cors")
        header("Sec-Fetch-Site", "same-origin")
        header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36")
    }

    fun HttpRequestBuilder.acceptHtml() {
        header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/" +
                "webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
    }

    fun HttpRequestBuilder.rmsAccessToken(value: String) {
        header("Accesstoken", value)
    }

    fun HttpRequestBuilder.vhcAccessToken(value: String) {
        header("Access_token", value)
    }

    fun HttpRequestBuilder.requestedBy(value: String) {
        header("Requested_by", value)
    }

    fun HttpRequestBuilder.userMeta(value: String) {
        header("User-Meta", value)
    }
}