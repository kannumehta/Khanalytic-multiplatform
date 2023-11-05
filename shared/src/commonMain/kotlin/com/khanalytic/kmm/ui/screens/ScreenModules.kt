package com.khanalytic.kmm.ui.screens

import com.khanalytic.kmm.ui.screens.login.registerScreenModule
import com.khanalytic.kmm.ui.screens.main.mainScreenModule
import com.khanalytic.kmm.ui.screens.platformaccounts.addPlatformAccountModule
import com.khanalytic.kmm.ui.screens.sync.syncPlatformDataModule

val screenModules = listOf(
    registerScreenModule,
    mainScreenModule,
    addPlatformAccountModule,
    syncPlatformDataModule
)