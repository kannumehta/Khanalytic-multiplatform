package com.khanalytic.kmm.ui.screens.platformaccounts

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.khanalytic.kmm.MainRes
import io.github.skeptick.libres.compose.painterResource

object AddPlatformAccountScreen: Screen {
    @Composable
    override fun Content() {
        Image(
            painter = painterResource(MainRes.image.zomato),
            contentDescription = "Zomato Logo",
            modifier = Modifier.fillMaxSize()
        )
    }
}