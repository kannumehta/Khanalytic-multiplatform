package com.khanalytic.kmm.repositories

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    single { UserRepository() }
    single { PlatformRepository() }
}