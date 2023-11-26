package com.khanalytic.kmm.ui.screens.menu

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.outlined.Paid
import androidx.compose.material.icons.outlined.RadioButtonChecked
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import com.khanalytic.kmm.http.api.MenuApi
import com.khanalytic.kmm.http.responses.AnalyticsReport
import com.khanalytic.kmm.subtitleTextColor
import com.khanalytic.kmm.ui.common.CurrencyUtils.toCurrencyValue
import com.khanalytic.kmm.ui.common.DefaultListHeader
import com.khanalytic.kmm.ui.common.DefaultSpacer
import com.khanalytic.kmm.ui.common.DefaultText
import com.khanalytic.kmm.ui.common.ListDivider
import com.khanalytic.kmm.ui.common.SingleLineSubtitleText
import com.khanalytic.kmm.ui.common.collectAsStateMultiplatform
import com.khanalytic.kmm.ui.screens.anaytics.AnalyticsPagerScreen
import com.khanalytic.kmm.ui.screens.anaytics.AnalyticsPagerScreenModel
import com.khanalytic.kmm.ui.screens.anaytics.ScreenType
import com.khanalytic.models.MenuCategory
import com.khanalytic.models.MenuItem
import com.khanalytic.models.MenuItemStat
import com.khanalytic.models.MenuItemStats
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MenuScreen: AnalyticsPagerScreen<MenuItemStats>(), KoinComponent {
    @Composable
    override fun DrawItem(data: List<AnalyticsReport<MenuItemStats>>, selectedIndex: Int) {
        val listItems = getMenuScreenModel().listItemsFlow.collectAsStateMultiplatform().value
        DrawMenu(listItems, data[selectedIndex].report)
    }

    @Composable
    override fun DrawItemWithoutData() {
        val listItems = getMenuScreenModel().listItemsFlow.collectAsStateMultiplatform().value
        DrawMenu(listItems, MenuItemStats(listOf()))
    }

    override fun shouldDrawItemWithoutData(): Boolean = true

    @Composable
    override fun getAnalyticsScreenModel(): AnalyticsPagerScreenModel<MenuItemStats> =
        getMenuScreenModel()

    @Composable
    private fun DrawMenu(listItems: List<ListItemType>, menuStats: MenuItemStats) {
        val menuStatsMap = menuStats.stats.associateBy { Pair(it.platformBrandId, it.remoteItemId) }
        val isMagicMenuEnabled =
            getMenuScreenModel().magicMenuFlow.collectAsStateMultiplatform().value
        
        Column(modifier = Modifier.fillMaxSize()) {
            MagicMenuRow()
            LazyColumn (modifier = Modifier.fillMaxSize()) {
                items(items = listItems, itemContent = { item ->
                    when(item) {
                        is ListItemType.Category -> {
                            if (isMagicMenuEnabled) {
                                val platformBrandId = item.menuCategory.platformBrandId
                                val anyItemHasStats =
                                    item.menuCategory.subcategories.flatMap { it.items }.any {
                                        menuStatsMap.containsKey(
                                            Pair(platformBrandId, it.remoteItemId)
                                        )
                                    }
                                if (anyItemHasStats) { DrawMenuCategory(item.menuCategory) }
                            } else {
                                DrawMenuCategory(item.menuCategory)
                            }
                        }
                        is ListItemType.Item -> {
                            val menuItem = item.menuItem
                            DrawMenuItem(
                                menuItem,
                                menuStatsMap[Pair(menuItem.platformBrandId, menuItem.remoteItemId)])
                        }
                    }
                })
            }
        }
    }

    @Composable
    private fun MagicMenuRow() {
        val model = getMenuScreenModel()
        val isMagicMenuChecked = model.magicMenuFlow.collectAsStateMultiplatform().value
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DefaultText("Magic Menu", modifier = Modifier.weight(1f))

            Switch(
                checked = isMagicMenuChecked,
                onCheckedChange = { model.setMagicMenuChecked(it) }
            )
        }
    }

    @Composable
    private fun DrawMenuCategory(category: MenuCategory) {
        DefaultListHeader(category.name, modifier = Modifier.fillMaxWidth())
    }

    @Composable
    private fun DrawMenuItem(item: MenuItem, stat: MenuItemStat?) {
        val magicMenuEnabled =
            getMenuScreenModel().magicMenuFlow.collectAsStateMultiplatform().value
        if (!magicMenuEnabled || stat != null) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
                    MenuItemFirstRow(item)
                    if (item.description.isNotBlank()) {
                        SingleLineSubtitleText(item.description, modifier = Modifier.fillMaxWidth())
                    }
                    if (stat != null) {
                        DefaultSpacer()
                        MenuItemStatsRow(item, stat)
                    }

                }
                ListDivider()
            }
        }
    }

    @Composable
    private fun MenuItemFirstRow(item: MenuItem) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val iconColor = when(item.isVeg) {
                true -> Color.Green
                else -> Color.Red
            }
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = Icons.Outlined.RadioButtonChecked,
                contentDescription = null,
                tint = iconColor
            )
            DefaultSpacer()
            DefaultText(item.name, modifier = Modifier.fillMaxWidth().weight(1f))
            DefaultSpacer()
            DefaultText(item.price.toInt().toString().toCurrencyValue())

        }
    }

    @Composable
    private fun MenuItemStatsRow(item: MenuItem, stat: MenuItemStat) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MenuItemStatColumn("Revenue", stat.revenue.toInt().toString().toCurrencyValue(),
                Icons.Outlined.AttachMoney)
            MenuItemStatColumn("Orders", stat.deliveredOrders.toString(),
                Icons.Outlined.ShoppingCart)
            MenuItemStatColumn("Complaints", stat.complaints.toString(),
                Icons.Outlined.BugReport)
        }
    }

    @Composable
    private fun RowScope.MenuItemStatColumn(title: String, value: String, icon: ImageVector) {
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

    @Composable
    private fun getMenuScreenModel(): MenuScreenModel<MenuItemStats> {
        val menuApi: MenuApi by inject()
        val screenType = ScreenType.Menu(menuApi)
        return rememberScreenModel { MenuScreenModel(screenType) }
    }
}