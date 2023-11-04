package com.khanalytic.kmm.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.khanalytic.kmm.ui.common.DefaultButton
import com.khanalytic.kmm.ui.common.DefaultHeading
import com.khanalytic.kmm.ui.common.DefaultSpacer
import com.khanalytic.kmm.ui.common.DefaultTextButton
import com.khanalytic.kmm.ui.common.EmailTextField
import com.khanalytic.kmm.ui.common.NameTextField
import com.khanalytic.kmm.ui.common.PasswordTextField
import com.khanalytic.kmm.ui.common.collectAsStateMultiplatform

object RegisterScreen: Screen {


    private val commonModifier = Modifier.fillMaxWidth()

    @Composable
    override fun Content() {
        val model = getScreenModel<RegisterScreenModel>()
        val registerType = model.registerTypeFlow.collectAsStateMultiplatform().value
        val email = model.emailTextFlow.collectAsStateMultiplatform().value
        val name = model.nameTextFlow.collectAsStateMultiplatform().value
        val password = model.passWordTextFlow.collectAsStateMultiplatform().value
        val isEmailInValid = model.isEmailInvalidFlow.collectAsStateMultiplatform().value
        val isNameInValid = model.isNameInvalidFlow.collectAsStateMultiplatform().value
        val isPasswordInvalid = model.isPasswordInValidFlow.collectAsStateMultiplatform().value
        Column (modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            DefaultHeading(registerType.heading)
            DefaultSpacer()
            if (registerType == RegisterType.SignUp) {
                NameTextField(name, commonModifier, isNameInValid){ model.setName(it) }
                DefaultSpacer()
            }
            EmailTextField(email, commonModifier, isEmailInValid){ model.setEmail(it) }
            DefaultSpacer()
            PasswordTextField(password, commonModifier, isPasswordInvalid) { model.setPassword(it) }
            DefaultSpacer()
            DefaultButton(registerType.primaryButtonTitle, commonModifier) {  }
            DefaultSpacer()
            DefaultTextButton(registerType.secondaryButtonTitle, commonModifier) {
                model.toggleRegisterTypeFlow()
            }
        }
    }
}