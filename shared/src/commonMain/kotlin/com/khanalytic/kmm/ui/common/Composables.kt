package com.khanalytic.kmm.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khanalytic.kmm.listDividerColor
import com.khanalytic.kmm.md_theme_dark_onSurface
import com.khanalytic.kmm.md_theme_light_onSurface
import com.khanalytic.kmm.subtitleTextColor
import io.github.skeptick.libres.compose.painterResource
import io.github.skeptick.libres.images.Image as LibreImage

@Composable fun DefaultSpacer() { Spacer(modifier = Modifier.size(16.dp)) }

@Composable fun SmallSpacer() { Spacer(modifier = Modifier.size(4.dp)) }

@Composable
fun DefaultButton(text: String, modifier: Modifier, onClick: () -> Unit) {
    Button(
        modifier = modifier,
        onClick = onClick,
        contentPadding = buttonPaddingValues,
        shape = buttonCorner
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
        shape = buttonCorner
    ) {
        Text(text = text)
    }
}

@Composable
fun ImageButton(
    text: String,
    image: LibreImage,
    color: Color,
    modifier: Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        border = BorderStroke(2.dp, color),
        contentPadding = imageButtonPaddingValues,
        shape = buttonCorner
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Image(
                modifier = Modifier.size(32.dp),
                painter = painterResource(image),
                contentDescription = text
            )
            Text(
                text = text,
                color = color,
                modifier = Modifier.fillMaxWidth().align(Alignment.Center),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun DefaultText(text: String, modifier: Modifier = Modifier) {
    Text(modifier = modifier, text = text, color = textColor())
}

@Composable
fun SingleLineSubtitleText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Left) {
    Text(modifier = modifier, text = text, color = subtitleTextColor(),  maxLines = 1,
        fontSize = 14.sp, textAlign = textAlign)
}

@Composable
fun DefaultHeading(text: String, modifier: Modifier = Modifier) {
    Text(modifier = modifier, text = text, color = textColor(), fontSize = 18.sp,
        textAlign = TextAlign.Center)
}

@Composable
fun DefaultListHeader(text: String, modifier: Modifier = Modifier) {
    Text(modifier = modifier.background(listDividerColor()).padding(vertical = 12.dp),
        text = text,
        color = textColor(),
        fontSize = 18.sp,
        textAlign = TextAlign.Center
    )
}

@Composable
fun EmailTextField(
    text: String,
    modifier: Modifier,
    showSupportingText: Boolean,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = text,
        onValueChange = { value: String -> onValueChange(value) },
        modifier = modifier,
        label = emailText,
        placeholder = emailText,
        leadingIcon = emailIcon,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        supportingText = if (showSupportingText) { invalidEmailErrorText } else { {} }

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
        supportingText = if (showSupportingText) { invalidPasswordErrorText } else { {} }
    )
}

@Composable
fun NameTextField(
    text: String,
    modifier: Modifier,
    showSupportingText: Boolean,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = text,
        onValueChange = { value: String -> onValueChange(value) },
        modifier = modifier,
        label = nameText,
        placeholder = nameText,
        leadingIcon = nameIcon,
        supportingText = if (showSupportingText) { invalidNameErrorText } else { {} }
    )
}

@Composable fun ListDivider() {
    Column (modifier = Modifier.height(1.dp).fillMaxWidth().background(listDividerColor())) {}
}

@Composable
fun textColor(): Color =
    if (isSystemInDarkTheme()) {
        md_theme_dark_onSurface
    } else {
        md_theme_light_onSurface
    }


val emailText: @Composable () -> Unit = { Text(text = "Email") }
val passwordText: @Composable () -> Unit = { Text(text = "Password") }
val nameText: @Composable () -> Unit = { Text(text = "Full Name") }
val invalidEmailErrorText: @Composable () -> Unit = {
    Text(text = "Use a valid email", color = Color.Red)
}
val invalidNameErrorText: @Composable () -> Unit = {
    Text(text = "Use a valid name", color = Color.Red)
}
val invalidPasswordErrorText: @Composable () -> Unit = {
    Text(text = "Use at least 8 characters", color = Color.Red)
}

val emailIcon: @Composable () -> Unit = { Icon(imageVector = Icons.Outlined.Email, contentDescription = null) }
val passwordIcon: @Composable () -> Unit = { Icon(imageVector = Icons.Outlined.Lock, contentDescription = null) }
val nameIcon: @Composable () -> Unit = { Icon(imageVector = Icons.Outlined.AccountCircle, contentDescription = null) }

private val buttonPaddingValues = PaddingValues(16.dp)
private val imageButtonPaddingValues = PaddingValues(8.dp)
private val buttonCorner = RoundedCornerShape(8.dp)

