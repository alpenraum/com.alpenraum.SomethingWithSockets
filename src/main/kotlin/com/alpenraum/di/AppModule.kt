package com.alpenraum.di

import com.alpenraum.ws.SessionManager
import org.koin.dsl.module

val appModule = module {
    single { SessionManager() }
}