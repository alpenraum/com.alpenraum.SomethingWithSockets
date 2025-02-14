package com.alpenraum.plugins

import com.alpenraum.controllers.apiController
import io.ktor.server.application.Application
import io.ktor.server.application.host
import io.ktor.server.application.port
import io.ktor.server.routing.host
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        val host = environment.config.host
        val port = environment.config.port
        host(host, port) {
            this@routing.configureSinglePageApplication()
        }
        apiController(host, port)
    }
}
