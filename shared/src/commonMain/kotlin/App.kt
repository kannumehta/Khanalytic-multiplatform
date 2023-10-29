import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.multiplatform.webview.cookie.Cookie
import com.multiplatform.webview.cookie.WebViewCookieManager
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    Napier.base(DebugAntilog())
    val url = "https://partner.swiggy.com/login"
    MaterialTheme {
        var greetingText by remember { mutableStateOf("Hello, World!") }
        var showImage by remember { mutableStateOf(false) }
        val webViewState = rememberWebViewState(url)
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            val loadedUrl = webViewState.lastLoadedUrl
            if (loadedUrl != null && loadedUrl != url) {
                Napier.v("user logged in: $loadedUrl")
                val cookieManager = webViewState.cookieManager
                CoroutineScope(Job() + Dispatchers.Default).launch {
                    Napier.v("loaded url: ${cookieManager.getCookies(loadedUrl)}")
                }
            } else {
                Napier.v("loaded url: $loadedUrl")
                WebView(state = webViewState, modifier = Modifier.fillMaxSize())
            }
            val state = webViewState.loadingState
            if (state is LoadingState.Loading) {
                Napier.v("showing progress indicator")
                LinearProgressIndicator(
                    progress = state.progress,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Napier.v("hiding progress indicator")
            }
        }
    }
}

