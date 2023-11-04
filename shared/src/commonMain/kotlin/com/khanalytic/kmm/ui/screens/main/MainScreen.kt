package com.khanalytic.kmm.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.Navigator
import com.khanalytic.kmm.primaryColor
import com.khanalytic.kmm.ui.common.collectAsStateMultiplatform
import com.khanalytic.kmm.ui.screens.login.RegisterScreen
import com.khanalytic.kmm.ui.screens.platformaccounts.AddPlatformAccountScreen

object MainScreen : Screen {
    @Composable
    override fun Content() {
        val model = getScreenModel<MainScreenModel>()
        val state = model.state.collectAsStateMultiplatform().value

        val modifier = Modifier.fillMaxSize().apply {
            if (state is MainScreenModel.State.Loading) {
                background(primaryColor())
            }
        }
        Column (modifier = modifier) {
            if (state is MainScreenModel.State.UserData) {
                if (state.user != null) {
                    Navigator(AddPlatformAccountScreen)
                } else {
                    Navigator(RegisterScreen)
                }
            }
        }
    }
}