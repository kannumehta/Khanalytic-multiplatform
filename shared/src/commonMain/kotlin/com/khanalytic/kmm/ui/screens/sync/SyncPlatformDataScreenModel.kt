package com.khanalytic.kmm.ui.screens.sync

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.khanalytic.integrations.http.HttpClientFactory
import com.khanalytic.integrations.swiggy.SwiggyApiFactory
import com.khanalytic.kmm.http.HttpUserPlatformCookieStorageFactory
import com.khanalytic.models.Menu
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SyncPlatformDataScreenModel: ScreenModel, KoinComponent {
    private val httpUserPlatformCookieStorageFactory: HttpUserPlatformCookieStorageFactory by inject()
    private val httpClientFactory: HttpClientFactory by inject()
    private val swiggyApiFactory: SwiggyApiFactory by inject()

    private var syncStarted = false
    private val mutex = Mutex()

    fun startSyncingData(
        userId: Long,
        platformId: Long,
        userPlatformCookieId : Long
    ) {
        screenModelScope.launch {
            mutex.withLock {
                if (!syncStarted) {
                    syncStarted = true
                    val httpClient = httpClientFactory.create(
                        httpUserPlatformCookieStorageFactory.create(
                            userId, platformId, userPlatformCookieId
                        )
                    )
                    val swiggyApi = swiggyApiFactory.create(httpClient)
//                    val menu: Menu = swiggyApi.getMenu("42076")
                    Napier.v("brands: ${swiggyApi.getComplaints("42076", "2023-10-01", "2023-10-18")}")
                }
            }
        }
    }
}