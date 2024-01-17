package com.neupanesushant.note.koinmodules

import com.neupanesushant.note.domain.usecase.SharedPref
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val domainModule = module {
    single<SharedPref> {
        SharedPref(androidApplication())
    }
}