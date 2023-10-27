package com.khanalytic.kmm.importing.intgerations.swiggy

import com.khanalytic.kmm.importing.intgerations.Page
import io.ktor.client.request.*

object SwiggyConstants {
    const val SWIGGY_API_HOST = "https://partner.swiggy.com"
    const val SWIGGY_RMS_HOST = "https://rms.swiggy.com"
    const val SWIGGY_SELF_CLIENT_HOST = "https://partner-self-client.swiggy.com"

    fun salesSummaryUrl(
        resId: String,
        startDate: String,
        endDate: String
    ): String = "$SWIGGY_API_HOST/insights/v1/revenue/?endDate=$endDate&groupBy=day" +
            "&restaurantId=$resId&startDate=$startDate"

    fun ordersUrl(): String = "$SWIGGY_API_HOST/orders"
    fun restaurantDetailsUrl(resId: String) =
        "$SWIGGY_RMS_HOST/api/cms/menu-revision/v1/restaurantDetails/$resId?"
    fun menuUrl(resId: String): String =
        "$SWIGGY_RMS_HOST/api/cms/menu-revision/v1/restaurant-menu/$resId?disabled=true&item_holiday_slots=true&type=REGULAR_MENU"
    fun pastOrdersUrl( resId: String, startDate: String, endDate: String, page: Page) =
        "$SWIGGY_RMS_HOST/orders/v1/history?limit=${page.limit}&offset=${page.offset}" +
                "&ordered_time__gte=$startDate&ordered_time__lte=$endDate&restaurant_id=$resId"

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

    fun HttpRequestBuilder.accessToken(value: String) {
        header("Accesstoken", value)
    }

    fun HttpRequestBuilder.requestedBy(value: String) {
        header("Requested_by", value)
    }

    fun HttpRequestBuilder.userMeta(value: String) {
        header("User-Meta", value)
    }
}