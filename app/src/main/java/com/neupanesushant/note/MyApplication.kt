package com.neupanesushant.note

import android.app.Application
import com.neupanesushant.note.koinmodules.dataModule
import com.neupanesushant.note.koinmodules.domainModule
import com.neupanesushant.note.koinmodules.vmModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@MyApplication)
            modules(dataModule, vmModule)
        }

    }
}