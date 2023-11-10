package com.khanalytic.kmm.http.api

import com.khanalytic.kmm.http.requests.CreateBrandsRequest
import com.khanalytic.kmm.http.requests.SyncRequest
import com.khanalytic.kmm.http.requests.UserApiRequest
import com.khanalytic.models.Brand
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BrandApi: KoinComponent {
    private val httpClient: HttpClient by inject()

    suspend fun getBrands(request: UserApiRequest<SyncRequest>): List<Brand> =
        httpClient.post(Utils.appUrl("brand/list")) { setBody(request) }.body()

    suspend fun createBrands(request: UserApiRequest<CreateBrandsRequest>): List<Brand> =
        httpClient.post(Utils.appUrl("brand/create")) { setBody(request) }.body()
}