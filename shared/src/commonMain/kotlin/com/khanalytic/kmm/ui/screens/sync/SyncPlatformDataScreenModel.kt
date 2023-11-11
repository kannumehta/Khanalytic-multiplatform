package com.khanalytic.kmm.ui.screens.sync

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.khanalytic.kmm.partnersync.SyncJobNode
import com.khanalytic.kmm.partnersync.SyncService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SyncPlatformDataScreenModel(
    userId: Long,
    platformId: Long,
    userPlatformCookieId : Long
): ScreenModel, KoinComponent {
    private val syncService: SyncService by inject()

    val syncJobsFlow = MutableStateFlow<SyncJobNode?>(null)

    init {
        screenModelScope.launch {
            syncService.sync(userId, platformId, userPlatformCookieId).collect {
                syncJobsFlow.value = it
            }
        }
    }
}