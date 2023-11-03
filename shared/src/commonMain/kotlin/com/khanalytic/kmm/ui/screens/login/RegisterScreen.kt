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
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.khanalytic.kmm.ui.common.DefaultButton
import com.khanalytic.kmm.ui.common.DefaultHeading
import com.khanalytic.kmm.ui.common.DefaultSpacer
import com.khanalytic.kmm.ui.common.DefaultTextButton
import com.khanalytic.kmm.ui.common.EmailTextField
import com.khanalytic.kmm.ui.common.PasswordTextField
import com.khanalytic.kmm.ui.common.collectAsStateMultiplatform

object RegisterScreen: Screen {


    private val commonModifier = Modifier.fillMaxWidth()

    @Composable
    override fun Content() {
        val model = rememberScreenModel { RegisterScreenModel() }
        val registerType = model.registerTypeFlow.collectAsStateMultiplatform().value
        Column (modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            DefaultHeading(registerType.heading)
            DefaultSpacer()
            EmailTextField(model.emailTextFlow.collectAsStateMultiplatform().value, commonModifier){
                model.setEmail(it)
            }
            DefaultSpacer()
            PasswordTextField(
                model.passWordTextFlow.collectAsStateMultiplatform().value,
                commonModifier,
                model.isPasswordInValidFlow.collectAsStateMultiplatform().value
            ) { model.setPassword(it) }
            DefaultSpacer()
            DefaultButton(registerType.primaryButtonTitle, commonModifier) {  }
            DefaultSpacer()
            DefaultTextButton(registerType.secondaryButtonTitle, commonModifier) {
                model.toggleRegisterTypeFlow()
            }
        }
    }
}