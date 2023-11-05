package com.khanalytic.kmm.ui.screens.platformaccounts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.khanalytic.kmm.surfaceColor
import com.khanalytic.kmm.ui.common.DefaultText
import com.khanalytic.kmm.ui.common.collectAsStateMultiplatform
import com.khanalytic.kmm.ui.screens.sync.SyncPlatformDataScreen
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState
import io.github.aakira.napier.Napier

data class PlatformLoginScreen(
    val userId: Long,
    val platformId: Long,
    val loginUrl: String,
    val userPlatformCookieId: Long? = null
): Screen {
    @Composable
    override fun Content() {
        val model = getScreenModel<PlatformLoginScreenModel>()
        val userPlatformCookie = model.userPlatformCookieFlow.collectAsStateMultiplatform().value
        val webViewState = rememberWebViewState(loginUrl)

        Column(modifier = Modifier.fillMaxSize().background(surfaceColor())) {
            if (userPlatformCookie != null) {
                val navigator = LocalNavigator.currentOrThrow
                navigator.replace(SyncPlatformDataScreen(
                    userPlatformCookie.userId,
                    userPlatformCookie.platformId,
                    userPlatformCookie.id
                ))
            } else {
                val loadedUrl = webViewState.lastLoadedUrl
                webViewState.webSettings.apply {
                    isJavaScriptEnabled = true
                    androidWebSettings.apply {
                        isAlgorithmicDarkeningAllowed = false
                        safeBrowsingEnabled = true
                        domStorageEnabled = true
                        useWideViewPort = true
                    }
                }
                if (!loadedUrl.isNullOrEmpty() && loadedUrl != loginUrl) {
                    Napier.v("saving cookies")
                    DefaultText("Preparing to sync your data", Modifier.fillMaxWidth())
                    val cookieManager = webViewState.cookieManager
                    model.saveCookies(userId, platformId, cookieManager, loadedUrl,
                        userPlatformCookieId)
                } else {
                    val state = webViewState.loadingState
                    if (state is LoadingState.Loading) {
                        Napier.v("showing progress indicator")
                        LinearProgressIndicator(
                            progress = state.progress,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    WebView(state = webViewState, modifier = Modifier.fillMaxSize().background(
                        surfaceColor()
                    ))
                }
            }
        }
    }
}