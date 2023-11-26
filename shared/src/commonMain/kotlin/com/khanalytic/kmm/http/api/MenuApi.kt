package com.khanalytic.kmm.http.api

import com.khanalytic.kmm.http.requests.AnalyticsReportRequest
import com.khanalytic.kmm.http.requests.GetMenuRequest
import com.khanalytic.kmm.http.requests.UpdateMenuRequest
import com.khanalytic.kmm.http.requests.UserApiRequest
import com.khanalytic.kmm.http.responses.AnalyticsReportResponse
import com.khanalytic.models.Menu
import com.khanalytic.models.MenuItemStats
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MenuApi : AnalyticsReportApi<MenuItemStats>(), KoinComponent {
    private val httpClient: HttpClient by inject()

    @Throws(Exception::class)
    suspend fun update(request: UserApiRequest<UpdateMenuRequest>) =
        withContext(Dispatchers.Default) {
            val unused = httpClient.post(Utils.appUrl("menu/update")) {
                setBody(request)
            }.body<String>()
        }

    @Throws(Exception::class)
    suspend fun get(request: UserApiRequest<GetMenuRequest>): Menu =
        withContext(Dispatchers.Default) {
            httpClient.post(Utils.appUrl("menu/get")) {
                setBody(request)
            }.body()
        }

    @Throws(Exception::class)
    override suspend fun report(
        request: UserApiRequest<AnalyticsReportRequest>
    ): AnalyticsReportResponse<MenuItemStats> =
        withContext(Dispatchers.Default) {
            httpClient.post(Utils.appUrl("menu/report")) {
                setBody(request)
            }.body()
        }
}