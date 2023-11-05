package com.khanalytic.integrations.http

import org.koin.dsl.module

val integrationsHttpModule = module {
    single { HttpClientFactory() }
}