package com.khanalytic.kmm.ui.screens.main

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.khanalytic.kmm.repositories.PlatformRepository
import com.khanalytic.kmm.repositories.UserRepository
import com.khanalytic.kmm.ui.screens.screenModules
import com.khanalytic.models.User
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainScreenModel: ScreenModel, KoinComponent {
    val userRepository: UserRepository by inject()
    private val platformRepository: PlatformRepository by inject()
    val state = MutableStateFlow<State>(State.Loading)

    init {
        Napier.base(DebugAntilog())
        screenModelScope.launch {
            userRepository.getLoggedInUser().collect {
                state.value = State.UserData(it)
            }
        }
        screenModelScope.launch {
            platformRepository.syncPlatforms()
        }
    }

    sealed class State {
        data object Loading: State()
        data class UserData(val user: User?): State()
    }


}