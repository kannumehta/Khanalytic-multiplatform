package com.khanalytic.kmm.ui.common

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khanalytic.kmm.md_theme_dark_onSurface
import com.khanalytic.kmm.md_theme_light_onSurface

@Composable fun DefaultSpacer() { Spacer(modifier = Modifier.size(24.dp)) }

@Composable
fun DefaultButton(text: String, modifier: Modifier, onClick: () -> Unit) {
    Button(
        modifier = modifier,
        onClick = onClick,
        contentPadding = buttonPaddingValues,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text = text)
    }
}

@Composable
fun DefaultTextButton(text: String, modifier: Modifier, onClick: () -> Unit) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
        contentPadding = buttonPaddingValues,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text = text)
    }
}

@Composable
fun DefaultText(text: String, modifier: Modifier = Modifier) {
    Text(modifier = modifier, text = text, color = textColor())
}

@Composable
fun DefaultHeading(text: String, modifier: Modifier = Modifier) {
    Text(modifier = modifier, text = text, color = textColor(), fontSize = 18.sp)
}

@Composable
fun EmailTextField(text: String, modifier: Modifier, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = text,
        onValueChange = { value: String -> onValueChange(value) },
        modifier = modifier,
        label = emailText,
        placeholder = emailText,
        leadingIcon = emailIcon
    )
}

@Composable
fun PasswordTextField(
    text: String,
    modifier: Modifier,
    showSupportingText: Boolean,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = text,
        onValueChange = { value: String -> onValueChange(value) },
        modifier = modifier,
        label = passwordText,
        placeholder = passwordText,
        leadingIcon = passwordIcon,
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        supportingText = if (showSupportingText) { invalidPasswordErrorText} else { {} }
    )
}

@Composable
private fun textColor(): Color =
    if (isSystemInDarkTheme()) {
        md_theme_dark_onSurface
    } else {
        md_theme_light_onSurface
    }


val emailText: @Composable () -> Unit = { Text(text = "Email") }
val passwordText: @Composable () -> Unit = { Text(text = "Password") }
val invalidPasswordErrorText: @Composable () -> Unit = {
    Text(text = "Use at least 8 characters", color = Color.Red)
}

val emailIcon: @Composable () -> Unit = { Icon(imageVector = Icons.Outlined.Email, contentDescription = null) }
val passwordIcon: @Composable () -> Unit = { Icon(imageVector = Icons.Outlined.Lock, contentDescription = null) }

private val buttonPaddingValues = PaddingValues(16.dp)

