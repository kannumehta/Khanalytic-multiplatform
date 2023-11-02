import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
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
    AppTheme {
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "This should work!")
        }
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

