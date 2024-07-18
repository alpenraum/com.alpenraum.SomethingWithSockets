package com.alpenraum.plugins

import com.alpenraum.controllers.apiController
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    routing {

        val host = environment?.config?.host?:""
        val port = environment?.config?.port?:0
        host(host, port) {
            this@routing.configureSinglePageApplication()
        }
        apiController(host,port)
    }
}
