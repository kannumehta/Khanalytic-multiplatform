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
import com.khanalytic.kmm.ui.screens.filter.FilterScreen
import com.khanalytic.kmm.ui.screens.login.RegisterScreen
import com.khanalytic.kmm.ui.screens.platformaccounts.AddPlatformAccountScreen
import com.khanalytic.kmm.ui.screens.sync.SyncPlatformDataScreen
import com.khanalytic.kmm.ui.screens.user.UserScreen

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
                    val userPlatformCookies =
                        model.userPlatformCookiesFlow.collectAsStateMultiplatform().value
                    if (userPlatformCookies.isNotEmpty()) {
//                        val userPlatformCookie = userPlatformCookies.first()
//                        Navigator(SyncPlatformDataScreen(
//                            userPlatformCookie.userId,
//                            userPlatformCookie.platformId,
//                            userPlatformCookie.id
//                        ))
                        Navigator(UserScreen())

                    } else {
                        Navigator(AddPlatformAccountScreen(state.user.id))
                    }
                } else {
                    Navigator(RegisterScreen)
                }
            }
        }
    }
}