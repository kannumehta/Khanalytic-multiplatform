package com.khanalytic.kmm.ui.screens.menu

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.rememberScreenModel
import com.khanalytic.kmm.ui.common.DefaultText
import com.khanalytic.kmm.ui.screens.filter.FilterScreen
import com.khanalytic.kmm.ui.screens.filter.FilterScreenModel

class MenuScreen: FilterScreen() {
    @Composable
    override fun ScreenContent(filterScreenModel: FilterScreenModel) {
        val model = rememberScreenModel { MenuScreenModel(filterScreenModel) }
        DefaultText("Menu Screen")
    }
}