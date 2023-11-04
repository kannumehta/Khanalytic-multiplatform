package com.khanalytic.kmm.ui.screens.login

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import cafe.adriel.voyager.core.model.screenModelScope
import com.khanalytic.kmm.http.requests.SignUpRequest
import com.khanalytic.kmm.repositories.UserRepository
import com.khanalytic.kmm.ui.common.EmailUtils
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RegisterScreenModel : ScreenModel, KoinComponent {
    private val userRepository: UserRepository by inject()

    val emailTextFlow = MutableStateFlow("")
    val nameTextFlow = MutableStateFlow("")
    val passWordTextFlow = MutableStateFlow("")
    val isEmailInvalidFlow = MutableStateFlow(false)
    val isNameInvalidFlow = MutableStateFlow(false)
    val isPasswordInValidFlow = MutableStateFlow(false)
    val registerTypeFlow = MutableStateFlow(RegisterType.SignUp)

    fun setEmail(email: String) {
        emailTextFlow.value = email
        validateEmail()
    }

    fun setName(name: String) {
        nameTextFlow.value = name
        validateName()
    }

    fun setPassword(password: String) {
        passWordTextFlow.value = password
        validatePassword()
    }

    fun toggleRegisterTypeFlow() {
        registerTypeFlow.value = when(registerTypeFlow.value) {
            RegisterType.SignUp -> RegisterType.SignIn
            else -> RegisterType.SignUp
        }
    }

    fun signUpWithValidation() {
        validateName(); validateEmail(); validatePassword()
        signUp()
    }

    private fun signUp() {
        screenModelScope.launch {
            val request = SignUpRequest(
                email = emailTextFlow.value,
                name = nameTextFlow.value,
                password = passWordTextFlow.value,
            )
        }
    }

    private fun validateEmail() {
        isEmailInvalidFlow.value = !EmailUtils.isEmailValid(emailTextFlow.value)
    }

    private fun validateName() { isNameInvalidFlow.value = nameTextFlow.value.isEmpty() }

    private fun validatePassword() {
        isPasswordInValidFlow.value = passWordTextFlow.value.length < 8
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