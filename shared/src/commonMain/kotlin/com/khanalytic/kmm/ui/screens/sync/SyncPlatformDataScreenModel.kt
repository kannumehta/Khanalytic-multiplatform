package com.khanalytic.kmm.ui.screens.sync

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.khanalytic.kmm.partnersync.SyncJob
import com.khanalytic.kmm.partnersync.SyncService
import com.khanalytic.models.UserPlatformCookie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SyncPlatformDataScreenModel: ScreenModel, KoinComponent {
    private val syncService: SyncService by inject()

    val syncJobsFlow = MutableStateFlow<List<SyncJob>>(listOf())

    private var syncStarted = false
    private val mutex = Mutex()

    fun startSyncingData(
        userId: Long,
        platformId: Long,
        userPlatformCookieId : Long
    ) {
        screenModelScope.launch {
            // TODO(kannumehta@): Use LaunchedEffect here.
            mutex.withLock {
                if (syncStarted) {
                    return@launch
                }
                syncStarted = true
            }

            syncService.sync(userId, platformId, userPlatformCookieId).collect {
                syncJobsFlow.value = it
            }
        }
    }
}