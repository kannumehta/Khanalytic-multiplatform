package com.khanalytic.kmm.ui.screens.user

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material.icons.outlined.Summarize
import androidx.compose.ui.graphics.vector.ImageVector
import cafe.adriel.voyager.core.screen.Screen
import com.khanalytic.kmm.ui.screens.businessreport.BusinessReportScreen
import com.khanalytic.kmm.ui.screens.discounts.DiscountsScreen
import com.khanalytic.kmm.ui.screens.menu.MenuScreen

enum class UserScreenType(
    val screenName: String,
    val icon: ImageVector,
    val screenProducer: () -> Screen
) {
    Menu("Menu", Icons.Outlined.MenuBook, { MenuScreen() }),
    BusinessReport("Business Report", Icons.Outlined.Summarize, { BusinessReportScreen() }),
    Discounts("Discounts", Icons.Outlined.Sell, { DiscountsScreen() }),
    Orders("Orders", Icons.Outlined.Receipt, { throw NotImplementedError() }),
    Complaints("Complaints", Icons.Outlined.BugReport, { throw NotImplementedError() }),
}