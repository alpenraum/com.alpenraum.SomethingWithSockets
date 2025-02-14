package com.alpenraum.di

import com.alpenraum.ws.SessionManager
import io.ktor.server.application.Application
import io.ktor.server.application.log
import io.ktor.util.logging.Logger
import org.koin.dsl.module

fun appModule(application: Application) =
    module {
        single<Logger> { application.log }
        single { SessionManager() }
    }
