package com.khanalytic.kmm.http.api

import com.khanalytic.kmm.http.requests.UpdateComplaintsRequest
import com.khanalytic.kmm.http.requests.UserApiRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ComplaintApi: KoinComponent {
    private val httpClient: HttpClient by inject()

    @Throws(Exception::class)
    suspend fun update(request: UserApiRequest<UpdateComplaintsRequest>) =
        withContext(Dispatchers.Default)  {
            val unused = httpClient.post(Utils.appUrl("complaint/update")) {
                setBody(request)
            }.body<String>()
        }

}