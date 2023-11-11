package com.khanalytic.kmm.http.api

import com.khanalytic.kmm.http.requests.CreateMenuRequest
import com.khanalytic.kmm.http.requests.UserApiRequest
import com.khanalytic.models.Menu
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MenuApi : KoinComponent {
    private val httpClient: HttpClient by inject()

    suspend fun update(request: UserApiRequest<CreateMenuRequest>) {
        val unused = httpClient.post(Utils.appUrl("menu/create")) {
            setBody(request)
        }.body<String>()
    }
}