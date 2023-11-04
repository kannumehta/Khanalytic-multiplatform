package com.khanalytic.kmm.ui.screens.platformaccounts

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.khanalytic.kmm.ui.common.DefaultText

object AddPlatformAccountScreen: Screen {
    @Composable
    override fun Content() {
        DefaultText("Add platform accounts")
    }
}