package com.timplifier.ktortest

import android.app.Application
import com.timplifier.ktortest.data.di.DataModule
import com.timplifier.ktortest.di.ViewModelModule
import com.timplifier.ktortest.domain.di.DomainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.ksp.generated.module

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(module {
                includes(DataModule().module, DomainModule().module, ViewModelModule().module)
            })
        }
    }
}