package com.khanalytic.kmm.ui.screens.menu

import cafe.adriel.voyager.core.model.ScreenModel
import com.khanalytic.kmm.ui.screens.filter.FilterScreenModel
import org.koin.core.component.KoinComponent

class MenuScreenModel(val filterScreenModel: FilterScreenModel): ScreenModel, KoinComponent {

}