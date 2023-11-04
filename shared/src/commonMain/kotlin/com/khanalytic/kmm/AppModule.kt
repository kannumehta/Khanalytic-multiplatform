package com.khanalytic.kmm

import com.khanalytic.database.shared.databaseModule
import com.khanalytic.kmm.http.httpModule
import com.khanalytic.kmm.repositories.repositoryModule
import com.khanalytic.kmm.ui.screens.login.registerModule
import org.koin.core.module.Module

fun appModule(): List<Module> = platformAppModules()
    .plus(commonModules)

private val commonModules = listOf(
    databaseModule,
    httpModule,
    repositoryModule,
    registerModule,
)