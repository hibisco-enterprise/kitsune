package com.hibisco.kitsune.feature

import android.content.Context
import com.hibisco.kitsune.feature.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

object StartKoin {
    fun initKoin(context: Context) {        // Start Koin
        startKoin {
            androidLogger()
            androidContext(context)
            modules(appModule)
        }
    }
    fun stopKoin() { stopKoin()  }
}