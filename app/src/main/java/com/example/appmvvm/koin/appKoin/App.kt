package com.example.appmvvm.koin.appKoin

import android.app.Application
import koin.appKoin.DI
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(DI.mainModule)
        }
    }
}