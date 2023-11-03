package com.khanalytic.kmm.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.khanalytic.kmm.ui.common.DefaultSpacer
import com.khanalytic.kmm.ui.common.buttonPaddingValues
import com.khanalytic.kmm.ui.common.collectAsStateMultiplatform
import com.khanalytic.kmm.ui.common.emailIcon
import com.khanalytic.kmm.ui.common.emailText
import com.khanalytic.kmm.ui.common.invalidPasswordErrorText
import com.khanalytic.kmm.ui.common.passwordIcon
import com.khanalytic.kmm.ui.common.passwordText

object LoginScreen: Screen {


    private val commonModifier = Modifier.fillMaxWidth()

    @Composable
    override fun Content() {
        val model = rememberScreenModel { LoginScreenModel() }
        // TODO(kannumehta@): Provide extension function here to make it lifecycle aware.
        Column (modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            DefaultSpacer()
            OutlinedTextField(
                value = model.emailTextFlow.collectAsStateMultiplatform().value,
                onValueChange = { value: String -> model.setEmail(value) },
                modifier = commonModifier,
                label = emailText,
                placeholder = emailText,
                leadingIcon = emailIcon
            )
            DefaultSpacer()
            val passwordSupportingText =
                if (model.isPasswordInValidFlow.collectAsStateMultiplatform().value) {
                    invalidPasswordErrorText
                } else { {} }
            OutlinedTextField(
                value = model.passWordTextFlow.collectAsStateMultiplatform().value,
                onValueChange = { value: String -> model.setPassword(value) },
                modifier = commonModifier,
                label = passwordText,
                placeholder = passwordText,
                leadingIcon = passwordIcon,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                supportingText = passwordSupportingText
            )
            DefaultSpacer()
            Button(
                modifier = commonModifier,
                onClick = {},
                contentPadding = buttonPaddingValues,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Login")
            }
        }
    }
}