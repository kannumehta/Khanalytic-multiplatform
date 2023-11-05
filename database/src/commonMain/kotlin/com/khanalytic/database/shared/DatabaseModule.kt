package com.khanalytic.database.shared

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val databaseModule = module {
    single { DatabaseDriverFactory() }
    single { Database() }
    single { UserDao() }
    single { PlatformDao() }
    single { UserPlatformCookieDao() }
}