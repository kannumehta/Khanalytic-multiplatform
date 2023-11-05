package com.khanalytic.kmm.ui.screens.sync

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.khanalytic.kmm.ui.common.DefaultText

object SyncPlatformDataScreen: Screen {
    @Composable
    override fun Content() {
        Column(modifier = Modifier.fillMaxSize()) {
            DefaultText("Syncing data")
        }
    }

}