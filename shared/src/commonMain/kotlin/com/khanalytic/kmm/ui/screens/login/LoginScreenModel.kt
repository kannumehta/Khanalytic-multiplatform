package com.khanalytic.kmm.ui.screens.login

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow

class LoginScreenModel : ScreenModel {
    val emailTextFlow = MutableStateFlow("")
    val passWordTextFlow = MutableStateFlow("")
    val isPasswordInValidFlow = MutableStateFlow(false)

    fun setEmail(email: String) {
        emailTextFlow.value = email
    }

    fun setPassword(password: String) {
        passWordTextFlow.value = password
        isPasswordInValidFlow.value = password.isNotEmpty() && password.length < 8
    }
}