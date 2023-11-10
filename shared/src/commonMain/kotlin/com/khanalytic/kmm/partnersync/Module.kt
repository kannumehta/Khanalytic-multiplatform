package com.khanalytic.kmm.partnersync

import org.koin.dsl.module

val partnerSyncModule = module {
    single { BrandsSyncService() }
    single { SyncService() }
}