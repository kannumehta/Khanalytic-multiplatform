package com.khanalytic.kmm

import com.khanalytic.database.shared.databaseModule
import com.khanalytic.integrations.integrationModules
import com.khanalytic.kmm.http.httpModule
import com.khanalytic.kmm.repositories.repositoryModule
import com.khanalytic.kmm.ui.screens.screenModules
import com.khanalytic.kmm.geocoding.geocodingModule
import com.khanalytic.kmm.partnersync.partnerSyncModule
import com.khanalytic.kmm.ui.screens.filter.filterScreenModule
import org.koin.core.module.Module

fun appModule(): List<Module> = platformAppModules()
    .plus(commonModules)

private val commonModules = listOf(
    databaseModule,
    httpModule,
    repositoryModule,
    geocodingModule,
    partnerSyncModule,
    filterScreenModule,
).plus(screenModules).plus(integrationModules)