package com.alpenraum

import com.alpenraum.plugins.*
import io.ktor.server.application.*
import io.ktor.server.plugins.doublereceive.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}
fun Application.module() {
    configureDI()
    install(DoubleReceive)
    configureAdministration()
    configureSockets()
    configureSerialization()
    //configureDatabases()
    configureHTTP()
    configureRouting()
}


val Application.envKind get() = environment.config.propertyOrNull("ktor.environment")?.getString()
val Application.isDev get() = envKind != null && envKind == "dev"
val Application.isProd get() = envKind != null && envKind != "dev"