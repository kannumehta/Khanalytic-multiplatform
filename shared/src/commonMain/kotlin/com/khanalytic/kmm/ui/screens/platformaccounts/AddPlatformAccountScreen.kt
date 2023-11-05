package com.khanalytic.kmm.ui.screens.platformaccounts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.khanalytic.kmm.MainRes
import com.khanalytic.kmm.swiggy_color
import com.khanalytic.kmm.ui.common.DefaultHeading
import com.khanalytic.kmm.ui.common.DefaultSpacer
import com.khanalytic.kmm.ui.common.DefaultTextButton
import com.khanalytic.kmm.ui.common.ImageButton
import com.khanalytic.kmm.ui.common.collectAsStateMultiplatform
import com.khanalytic.kmm.zomato_color
import com.khanalytic.models.Platform
import io.github.skeptick.libres.images.Image
import kotlinx.datetime.serializers.DateTimePeriodIso8601Serializer

object AddPlatformAccountScreen: Screen {
    private val commonModifier = Modifier.fillMaxWidth()

    @Composable
    override fun Content() {
        val model = getScreenModel<AddPlatformAccountScreenModel>()
        val platforms = model.platformsFlow.collectAsStateMultiplatform().value
        Column (modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            DefaultHeading("Automatically sync your data", commonModifier)
            DefaultSpacer(); DefaultSpacer(); DefaultSpacer()
            platforms.forEach { addPlatformAccountButton(it) }
            DefaultTextButton("Skip", commonModifier) {}
        }
    }

    @Composable
    private fun addPlatformAccountButton(platform: Platform) {
        val text = "Add ${platform.name} account"
        if (platform.name.lowercase() == "zomato") {
            addPlatformAccountButton(text, MainRes.image.zomato, zomato_color)
        } else if (platform.name.lowercase() == "swiggy") {
            addPlatformAccountButton(text, MainRes.image.swiggy, swiggy_color)
        }
    }

    @Composable
    private fun addPlatformAccountButton(text: String, image: Image, color: Color) {
        ImageButton(text, image, color, commonModifier) {}
        DefaultSpacer()
    }
}