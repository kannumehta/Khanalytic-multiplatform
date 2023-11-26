package com.khanalytic.kmm.http.api

import com.khanalytic.kmm.http.requests.AnalyticsReportRequest
import com.khanalytic.kmm.http.requests.UserApiRequest
import com.khanalytic.kmm.http.responses.AnalyticsReportResponse

abstract class AnalyticsReportApi<T> {
    @Throws(Exception::class)
    abstract suspend fun report(
        request: UserApiRequest<AnalyticsReportRequest>
    ): AnalyticsReportResponse<T>
}