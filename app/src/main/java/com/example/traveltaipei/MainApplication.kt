package com.example.traveltaipei

import android.app.Application
import com.example.traveltaipei.di.diModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(diModules)
        }
    }
}