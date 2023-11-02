package com.khanalytic.kmm.android

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.google.common.flogger.FluentLogger
import com.khanalytic.kmm.Greeting
import com.khanalytic.kmm.SwiggyApiFactory
import com.khanalytic.integrations.models.Menu
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }

//        val cookie = "WZRK_G=00eb916684f14c91bc6bee4f4707e2fa; WZRK_G=00eb916684f14c91bc6bee4f4707e2fa; WZRK_L=%257B%257D; Swiggy_Session-alpha=30e166d0-f7ff-492b-8a95-f863d64823d9; Swiggy_Session-newSys=1; Swiggy_Session-user=6360188416; Swiggy_user_role=2; Swiggy_change_password=false; isRidBasedLogin=false; is_dot_in=1; WZRK_S_8RK-K65-846Z=%7B%22p%22%3A3%2C%22s%22%3A1697953841%2C%22t%22%3A1697953941%7D; Swiggy_Session-perms=%5B1%2C2%2C4%2C5%2C6%5D"
//        lifecycleScope.launch {
//            val swiggyApi = SwiggyApiFactory().create(cookie)
//            val menu: Menu = swiggyApi.getMenu("42076")
//             logger.atInfo().log("fetched revenue: %s", swiggyApi.getOrders("42076", "2023-10-17", "2023-10-18", menu))
        // logger.atInfo().log("fetched revenue: %s", swiggyApi.getSalesSummary("42076", "2023-10-17", "2023-10-18"))
//             logger.atInfo().log("fetched revenue: %s", swiggyApi.getComplaints("42076", "2023-10-01", "2023-10-18"))
//             logger.atInfo().log("fetched revenue: %s", swiggyApi.sendEmailReport("42076", "2023-07-27", "2023-10-26", "mehtakan@gmail.com"))
        // logger.atInfo().log("fetched revenue: %s", swiggyApi.getBrands())
//            logger.atInfo().log("fetched revenue: %s", swiggyApi.getMenu("42076"))
//        }
    }

    companion object {
        private val logger = FluentLogger.forEnclosingClass()
    }
}
