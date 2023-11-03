package com.khanalytic.kmm

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.khanalytic.kmm.ui.screens.login.RegisterScreen
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    AppTheme {
        Navigator(RegisterScreen)
    }
}


//Napier.base(DebugAntilog())
//val url = "https://partner.swiggy.com/login"
//var greetingText by remember { mutableStateOf("Hello, World!") }
//var showImage by remember { mutableStateOf(false) }
//val webViewState = rememberWebViewState(url)
//Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//    val loadedUrl = webViewState.lastLoadedUrl
//    if (loadedUrl != null && loadedUrl != url) {
//        Napier.v("user logged in: $loadedUrl")
//        val cookieManager = webViewState.cookieManager
//        CoroutineScope(Job() + Dispatchers.Default).launch {
//            Napier.v("loaded url: ${cookieManager.getCookies(loadedUrl)}")
//        }
//    } else {
//        Napier.v("loaded url: $loadedUrl")
//        WebView(state = webViewState, modifier = Modifier.fillMaxSize())
//    }
//    val state = webViewState.loadingState
//    if (state is LoadingState.Loading) {
//        Napier.v("showing progress indicator")
//        LinearProgressIndicator(
//            progress = state.progress,
//            modifier = Modifier.fillMaxWidth()
//        )
//    } else {
//        Napier.v("hiding progress indicator")
//    }
//}

