package com.khanalytic.kmm.http.api

import com.khanalytic.kmm.http.requests.MissingDatesRequest
import com.khanalytic.kmm.http.requests.UserApiRequest
import com.khanalytic.kmm.http.responses.PlatformBrandsMissingDates
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MissingDatesApi: KoinComponent {
    private val httpClient: HttpClient by inject()

    @Throws(Exception::class)
    suspend fun getMissingDates(
        request: UserApiRequest<MissingDatesRequest>
    ): List<PlatformBrandsMissingDates> =
        httpClient.post(Utils.appUrl("sync/missing_dates")) {
            setBody(request)
        }.body()
}