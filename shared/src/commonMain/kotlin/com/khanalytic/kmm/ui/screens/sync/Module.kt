package com.khanalytic.kmm.ui.screens.sync

import org.koin.dsl.module

val syncPlatformDataModule = module {
    factory { SyncPlatformDataScreenModel() }
}