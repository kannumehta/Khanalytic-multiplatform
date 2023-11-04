package com.khanalytic.kmm

import org.koin.core.module.Module

actual fun platformAppModules(): List<Module> = listOf(contextModule)