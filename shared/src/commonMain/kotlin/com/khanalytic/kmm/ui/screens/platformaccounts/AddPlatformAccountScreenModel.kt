package com.khanalytic.kmm.ui.screens.platformaccounts

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.khanalytic.kmm.repositories.PlatformRepository
import com.khanalytic.models.Platform
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AddPlatformAccountScreenModel: ScreenModel, KoinComponent {
    private val platformRepository: PlatformRepository by inject()

    val platformsFlow = MutableStateFlow(listOf<Platform>())

    init {
        screenModelScope.launch {
            platformRepository.getAllPlatformsAsFlow().collect {
                platformsFlow.value = it
            }
        }
    }
}