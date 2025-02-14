package com.alpenraum

import com.alpenraum.plugins.configureAdministration
import com.alpenraum.plugins.configureDI
import com.alpenraum.plugins.configureHTTP
import com.alpenraum.plugins.configureRouting
import com.alpenraum.plugins.configureSerialization
import com.alpenraum.plugins.configureSockets
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.doublereceive.DoubleReceive

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain
        .main(args)
}

fun Application.module() {
    configureDI()
    install(DoubleReceive)
    configureAdministration()
    configureSockets()
    configureSerialization()
    // configureDatabases()
    configureHTTP()
    configureRouting()
}

val Application.envKind get() = environment.config.propertyOrNull("ktor.environment")?.getString()
val Application.isDev get() = envKind != null && envKind == "dev"
val Application.isProd get() = envKind != null && envKind != "dev"
