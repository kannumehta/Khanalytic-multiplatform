package com.khanalytic.kmm.ui.screens.main

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.khanalytic.database.shared.UserPlatformCookieDao
import com.khanalytic.kmm.repositories.PlatformRepository
import com.khanalytic.kmm.repositories.UserRepository
import com.khanalytic.models.User
import com.khanalytic.models.UserPlatformCookie
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainScreenModel: ScreenModel, KoinComponent {
    private val userRepository: UserRepository by inject()
    private val userPlatformCookieDao: UserPlatformCookieDao by inject()
    private val platformRepository: PlatformRepository by inject()

    val state = MutableStateFlow<State>(State.Loading)
    val userPlatformCookiesFlow = MutableStateFlow(listOf<UserPlatformCookie>())

    init {
        Napier.base(DebugAntilog())
        screenModelScope.launch {
            userRepository.getLoggedInUser().collect {
                state.value = State.UserData(it)
                setupUserPlatformCookies(it)
            }
        }
        screenModelScope.launch {
            platformRepository.syncPlatforms()
        }
    }

    private fun setupUserPlatformCookies(user: User?) {
        if (user != null) {
            screenModelScope.launch {
                userPlatformCookieDao.getFlowByUserId(user.id).collect {
                    userPlatformCookiesFlow.value = it
                }
            }
        } else {
            userPlatformCookiesFlow.value = listOf()
        }
    }

    sealed class State {
        data object Loading: State()
        data class UserData(val user: User?): State()
    }


}