package com.khanalytic.kmm.ui.screens.sync

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.khanalytic.kmm.partnersync.SyncJobStatus
import com.khanalytic.kmm.ui.common.DefaultSpacer
import com.khanalytic.kmm.ui.common.DefaultText
import com.khanalytic.kmm.ui.common.collectAsStateMultiplatform

data class SyncPlatformDataScreen(
    private val userId: Long,
    private val platformId: Long,
    private val userPlatformCookieId : Long
): Screen {
    @Composable
    override fun Content() {
        val model = getScreenModel<SyncPlatformDataScreenModel>()
        model.startSyncingData(userId, platformId, userPlatformCookieId)
        val syncJobs = model.syncJobsFlow.collectAsStateMultiplatform().value
        Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(items = syncJobs, itemContent = { syncJob ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        if (syncJob.status == SyncJobStatus.Processing) {
                            CircularProgressIndicator(modifier = Modifier.width(32.dp))
                        }
                        DefaultText(syncJob.title)
                    }
                    DefaultSpacer()
                })
            }
        }
    }

}