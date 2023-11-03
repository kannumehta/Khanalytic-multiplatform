package com.khanalytic.kmm

import com.khanalytic.database.shared.databaseModule
import org.koin.core.module.Module

actual fun appModule(): List<Module> = listOf(contextModule, databaseModule)