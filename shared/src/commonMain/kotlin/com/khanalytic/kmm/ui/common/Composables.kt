package com.khanalytic.kmm.ui.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DefaultSpacer() {
    Spacer(modifier = Modifier.size(24.dp))
}

val emailText: @Composable () -> Unit = { Text(text = "Email") }
val passwordText: @Composable () -> Unit = { Text(text = "Password") }
val invalidPasswordErrorText: @Composable () -> Unit = {
    Text(text = "Use at least 8 characters", color = Color.Red)
}

val emailIcon: @Composable () -> Unit = { Icon(imageVector = Icons.Outlined.Email, contentDescription = null) }
val passwordIcon: @Composable () -> Unit = { Icon(imageVector = Icons.Outlined.Lock, contentDescription = null) }

val buttonPaddingValues = PaddingValues(16.dp)

