package com.alpenraum.plugins

import com.alpenraum.isDev
import com.alpenraum.isProd
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Routing.configureSinglePageApplication() {
    when {
        application.isDev -> {
            // redirect to Vue dev server
            get("/") {
                call.respondRedirect("http://${this@configureSinglePageApplication.environment?.config?.host}:3006")
            }
        }
        application.isProd -> {
            // serve Vue page with Ktor
            singlePageApplication() {
                vue("frontend/SomethingWithSockets_Frontend/dist")
            }
            static("/") {
                resources("dist")
                resource("/", "dist/index.html")
            }
        }
    }

}
