package com.khanalytic.kmm.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext

// TODO(kannumehta@): Make this lifecycle aware.
@Composable actual fun <T> StateFlow<T>.collectAsStateMultiplatform(
    context: CoroutineContext
): State<T> = collectAsState(context)