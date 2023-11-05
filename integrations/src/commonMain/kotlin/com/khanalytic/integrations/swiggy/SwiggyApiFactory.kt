package com.khanalytic.integrations.swiggy

import com.khanalytic.integrations.PlatformApi
import com.khanalytic.integrations.PlatformApiFactory
import io.ktor.client.HttpClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SwiggyApiFactory: PlatformApiFactory, KoinComponent {
    private val responseParser: SwiggyResponseParser by inject()
    override fun create(httpClient: HttpClient): PlatformApi = SwiggyApi(httpClient, responseParser)

}