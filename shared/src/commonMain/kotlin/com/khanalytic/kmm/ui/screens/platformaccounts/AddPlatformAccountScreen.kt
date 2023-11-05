package com.khanalytic.kmm.ui.screens.platformaccounts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.khanalytic.kmm.MainRes
import com.khanalytic.kmm.swiggy_color
import com.khanalytic.kmm.ui.common.DefaultHeading
import com.khanalytic.kmm.ui.common.DefaultSpacer
import com.khanalytic.kmm.ui.common.DefaultTextButton
import com.khanalytic.kmm.ui.common.ImageButton
import com.khanalytic.kmm.ui.common.collectAsStateMultiplatform
import com.khanalytic.kmm.zomato_color
import com.khanalytic.models.Platform
import io.github.aakira.napier.Napier
import io.github.skeptick.libres.images.Image

data class AddPlatformAccountScreen(val userId: Long): Screen {
    @Composable
    override fun Content() {
        val model = getScreenModel<AddPlatformAccountScreenModel>()
        val platforms = model.platformsFlow.collectAsStateMultiplatform().value
        Column (modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            DefaultHeading("Automatically sync your data", commonModifier)
            DefaultSpacer(); DefaultSpacer(); DefaultSpacer()
            platforms.filterNot { it.loginUrl.isNullOrEmpty() }.forEach { addPlatformAccountButton(it) }
            DefaultTextButton("Skip", commonModifier) {}
        }
    }

    @Composable
    private fun addPlatformAccountButton(platform: Platform) {
        val text = "Add ${platform.name} account"
        val buttonClicked = remember { mutableStateOf(false) }
        if (buttonClicked.value) {
            Napier.v("$text clicked")
            navigateToPlatformLoginScreen(platform)
            buttonClicked.value = false
        }
        val onClick: () -> Unit = { buttonClicked.value = true }
        if (platform.isZomato()) {
            addPlatformAccountButton(text, MainRes.image.zomato, zomato_color, onClick)
        } else if (platform.isSwiggy()) {
            addPlatformAccountButton(text, MainRes.image.swiggy, swiggy_color, onClick)
        }
    }

    @Composable
    private fun navigateToPlatformLoginScreen(platform: Platform) {
        LocalNavigator.currentOrThrow.push(PlatformLoginScreen(
            userId = userId,
            platformId = platform.id,
            loginUrl = platform.loginUrl!!
        ))
    }

    @Composable
    private fun addPlatformAccountButton(
        text: String,
        image: Image,
        color: Color,
        onClick: () -> Unit
    ) {
        ImageButton(text, image, color, commonModifier, onClick)
        DefaultSpacer()
    }

    companion object {
        private val commonModifier = Modifier.fillMaxWidth()
    }
}