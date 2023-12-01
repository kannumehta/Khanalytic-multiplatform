package com.khanalytic.kmm.ui.screens.discounts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import com.khanalytic.kmm.http.api.DiscountApi
import com.khanalytic.kmm.http.responses.AnalyticsReport
import com.khanalytic.kmm.subtitleTextColor
import com.khanalytic.kmm.ui.common.CurrencyUtils.toCurrencyValue
import com.khanalytic.kmm.ui.common.DefaultSpacer
import com.khanalytic.kmm.ui.common.DefaultText
import com.khanalytic.kmm.ui.common.ListDivider
import com.khanalytic.kmm.ui.common.SingleLineSubtitleText
import com.khanalytic.kmm.ui.common.SmallSpacer
import com.khanalytic.kmm.ui.screens.anaytics.AnalyticsPagerScreen
import com.khanalytic.kmm.ui.screens.anaytics.AnalyticsPagerScreenModel
import com.khanalytic.kmm.ui.screens.anaytics.ScreenType
import com.khanalytic.models.DiscountReport
import com.khanalytic.models.MenuItem
import com.khanalytic.models.MenuItemStat
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DiscountsScreen : AnalyticsPagerScreen<List<DiscountReport>>(), KoinComponent {
    @Composable
    override fun DrawItem(data: List<AnalyticsReport<List<DiscountReport>>>, selectedIndex: Int) {
        val reports = data[selectedIndex].report
        Column(modifier = Modifier.fillMaxWidth()) {
            LazyColumn (modifier = Modifier.fillMaxSize()) {
                items(items = reports, itemContent = { DrawDiscountReport(it) })
            }
        }
    }

    @Composable
    override fun DrawItemWithoutData() {
        throw NotImplementedError("this should never happen")
    }

    override fun shouldDrawItemWithoutData(): Boolean = false

    @Composable
    override fun getAnalyticsScreenModel(): AnalyticsPagerScreenModel<List<DiscountReport>> =
        getScreenModel()

    @Composable
    private fun getScreenModel(): AnalyticsPagerScreenModel<List<DiscountReport>> {
        val api: DiscountApi by inject()
        val screenType = ScreenType.Discounts(api)
        return rememberScreenModel { AnalyticsPagerScreenModel(screenType) }
    }

    @Composable
    private fun DrawDiscountReport(discountReport: DiscountReport) {
        Column(modifier = Modifier.fillMaxWidth()) {
            SmallSpacer()
            Row(modifier = Modifier.fillMaxWidth()) {
                SingleLineSubtitleText(discountReport.platformName)
                DefaultSpacer()
                SingleLineSubtitleText(
                    discountReport.brandName,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
            }
            DefaultText(discountReport.description, modifier = Modifier.fillMaxWidth())
            DefaultSpacer()
            DiscountStatsRow(discountReport)
            SmallSpacer()
            ListDivider()
        }
    }

    @Composable
    private fun DiscountStatsRow(discountReport: DiscountReport) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DiscountStatColumn("Orders", discountReport.totalOrders.toString(),
                Icons.Outlined.ShoppingCart)
            DiscountStatColumn("Revenue", "₹${discountReport.subtotal.toInt()}",
                Icons.Outlined.AttachMoney)
            DiscountStatColumn("Discounts",
                "₹${discountReport.restaurantPromoDiscount.toInt()}",
                Icons.Outlined.Sell)
        }
    }

    @Composable
    private fun RowScope.DiscountStatColumn(title: String, value: String, icon: ImageVector) {
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = icon,
                    contentDescription = null,
                    tint = subtitleTextColor()
                )
                SingleLineSubtitleText(title)
            }
            SingleLineSubtitleText(value)
        }
    }
}