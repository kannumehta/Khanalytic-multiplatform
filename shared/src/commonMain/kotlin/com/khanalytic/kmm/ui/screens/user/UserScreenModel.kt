package com.khanalytic.kmm.ui.screens.user

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent

class UserScreenModel: ScreenModel, KoinComponent {
    val selectedScreenTypeFlow = MutableStateFlow(UserScreenType.Discounts)
    val filterScreenVisibleFlow = MutableStateFlow(false)

    fun onScreenSelected(screenType: UserScreenType) {
        selectedScreenTypeFlow.value = screenType
    }

    fun toggleFilterScreenVisibility() {
        filterScreenVisibleFlow.value = !filterScreenVisibleFlow.value
    }
}