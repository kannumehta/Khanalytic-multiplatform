package com.khanalytic.integrations

import com.khanalytic.integrations.http.integrationsHttpModule
import com.khanalytic.integrations.swiggy.swiggyModule
import org.koin.dsl.module

val integrationModule = module {
    single { Serialization.serializer }
}

val integrationModules = listOf(integrationModule, integrationsHttpModule, swiggyModule)