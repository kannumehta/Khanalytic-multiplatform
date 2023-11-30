package com.khanalytic.kmm.http.api

import com.khanalytic.kmm.http.requests.AnalyticsReportRequest
import com.khanalytic.kmm.http.requests.UserApiRequest
import com.khanalytic.kmm.http.responses.AnalyticsReportResponse
import com.khanalytic.models.SaleReport
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BusinessReportApi : AnalyticsReportApi<List<SaleReport>>(), KoinComponent {
    private val httpClient: HttpClient by inject()

    @Throws(Exception::class)
    override suspend fun report(
        request: UserApiRequest<AnalyticsReportRequest>
    ): AnalyticsReportResponse<List<SaleReport>> =
        withContext(Dispatchers.Default) {
            httpClient.post(Utils.appUrl("order/sales_report")) {
                setBody(request)
            }.body()
        }

}