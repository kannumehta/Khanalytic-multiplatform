package com.khanalytic.kmm.ui.screens.login

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow

class RegisterScreenModel : ScreenModel {
    val emailTextFlow = MutableStateFlow("")
    val passWordTextFlow = MutableStateFlow("")
    val isPasswordInValidFlow = MutableStateFlow(false)
    val registerTypeFlow = MutableStateFlow(RegisterType.SignUp)

    fun setEmail(email: String) { emailTextFlow.value = email }

    fun setPassword(password: String) {
        passWordTextFlow.value = password
        isPasswordInValidFlow.value = password.isNotEmpty() && password.length < 8
    }

    fun toggleRegisterTypeFlow() {
        registerTypeFlow.value = when(registerTypeFlow.value) {
            RegisterType.SignUp -> RegisterType.SignIn
            else -> RegisterType.SignUp
        }
    }
}

enum class RegisterType(
    val heading: String,
    val primaryButtonTitle: String,
    val secondaryButtonTitle: String
) {
    SignUp("Create new account", "Create Account", "Login to existing account"),
    SignIn("Login to existing account", "Login", "Create new account")
}