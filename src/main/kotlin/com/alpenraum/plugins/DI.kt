package com.alpenraum.plugins

import com.alpenraum.di.appModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureDI() {
    install(Koin) {
        modules(appModule(this@configureDI))
    }
}
