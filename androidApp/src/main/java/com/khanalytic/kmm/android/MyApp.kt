package com.khanalytic.kmm.android

import android.app.Application
import com.khanalytic.kmm.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            // declare used Android context
            androidContext(this@MyApp)
            modules(appModule())
        }
    }
}