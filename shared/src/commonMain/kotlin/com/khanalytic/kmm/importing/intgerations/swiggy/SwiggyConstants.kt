package com.khanalytic.kmm.importing.intgerations.swiggy

object SwiggyConstants {
    const val SWIGGY_API_HOST = "https://partner.swiggy.com"
    const val SWIGGY_RMS_HOST = "https://rms.swiggy.com"
    const val SWIGGY_SELF_CLIENT_HOST = "https://partner-self-client.swiggy.com"

    val commonHeaders = mapOf(
        "Accept-Encoding" to "gzip, deflate, br",
        "Accept-Language" to "en-US,en;q=0.9",
        "Sec-Ch-Ua" to "\"Chromium\";v=\"118\", \"Google Chrome\";v=\"118\", \"Not=A?Brand\";v=\"99\"",
        "Sec-Ch-Ua-Mobile" to "?0",
        "Sec-Ch-Ua-Platform" to "\"macOS\"",
        "Sec-Fetch-Dest" to "empty",
        "Sec-Fetch-Mode" to "cors",
        "Sec-Fetch-Site" to "same-origin",
        "User-Agent" to "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36"
    )

    fun salesSummaryUrl(
        resId: String,
        startDate: String,
        endDate: String
    ): String = "$SWIGGY_API_HOST/insights/v1/revenue/?endDate=$endDate&groupBy=day" +
            "&restaurantId=$resId&startDate=$startDate"

    fun ordersUrl(): String = "$SWIGGY_API_HOST/orders"
    fun pastOrdersUrl(resId: String): String = "$SWIGGY_API_HOST/orders/past/restaurant/$resId"
    fun restaurantDetailsUrl(resId: String) =
        "$SWIGGY_RMS_HOST/api/cms/menu-revision/v1/restaurantDetails/$resId?"

    fun restaurantReferrer(resId: String): String =
        "https://partner.swiggy.com/business-metrics/revenue/restaurant/$resId"
}