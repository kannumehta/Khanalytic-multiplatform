package com.khanalytic.kmm.ui.screens.sync

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.khanalytic.kmm.ui.common.DefaultText

data class SyncPlatformDataScreen(
    private val userId: Long,
    private val platformId: Long,
    private val userPlatformCookieId : Long
): Screen {
    @Composable
    override fun Content() {
        val model = getScreenModel<SyncPlatformDataScreenModel>()
        model.startSyncingData(userId, platformId, userPlatformCookieId)
        Column(modifier = Modifier.fillMaxSize()) {
            DefaultText("Syncing data")
        }
    }

}