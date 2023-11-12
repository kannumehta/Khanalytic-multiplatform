package com.khanalytic.kmm.ui.screens.sync

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.khanalytic.kmm.partnersync.SyncJobNode
import com.khanalytic.kmm.partnersync.SyncJobStatus
import com.khanalytic.kmm.partnersync.status
import com.khanalytic.kmm.partnersync.title
import com.khanalytic.kmm.partnersync.toUiList
import com.khanalytic.kmm.pendingIconColor
import com.khanalytic.kmm.ui.common.DefaultSpacer
import com.khanalytic.kmm.ui.common.DefaultText
import com.khanalytic.kmm.ui.common.ListDivider
import com.khanalytic.kmm.ui.common.collectAsStateMultiplatform
import io.github.aakira.napier.Napier

data class SyncPlatformDataScreen(
    private val userId: Long,
    private val platformId: Long,
    private val userPlatformCookieId : Long
): Screen {
    @Composable
    override fun Content() {
        val model = rememberScreenModel {
            SyncPlatformDataScreenModel(userId, platformId, userPlatformCookieId)
        }
        val node = model.syncJobsFlow.collectAsStateMultiplatform().value
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            if (node != null) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(items = node.toUiList(), itemContent = { pair ->
                        SyncJob(pair.first, pair.second)
                    })
                }
            }
        }
    }

    @Composable
    private fun SyncJob(level: Int, node: SyncJobNode) {
        DefaultSpacer()
        Row(modifier = Modifier.fillMaxWidth().padding(start = (level * 12).dp)) {
            val status = node.status()
            val iconSizeModifier = Modifier.width(24.dp)
            if (status == SyncJobStatus.Processing && node is SyncJobNode.LeafNode) {
                CircularProgressIndicator(modifier = iconSizeModifier)
            } else if (status == SyncJobStatus.Processed) {
                CheckCircleIcon(iconSizeModifier)
            }
            DefaultSpacer()
            DefaultText(node.title())
        }
        DefaultSpacer()
        ListDivider()
    }

    @Composable
    private fun CheckCircleIcon(modifier: Modifier) {
        Icon(
            modifier = modifier,
            imageVector = Icons.Outlined.CheckCircle,
            contentDescription = null,
            tint = Color.Green
        )
    }

}