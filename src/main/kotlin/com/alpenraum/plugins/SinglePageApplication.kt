package com.alpenraum.plugins

import com.alpenraum.isDev
import com.alpenraum.isProd
import io.ktor.server.http.content.singlePageApplication
import io.ktor.server.http.content.staticResources
import io.ktor.server.http.content.vue
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Routing
import io.ktor.server.routing.application
import io.ktor.server.routing.get
import io.ktor.util.logging.Logger
import org.koin.ktor.ext.inject

fun Routing.configureSinglePageApplication() {
    val logger by inject<Logger>()
    when {
        application.isDev -> {
            // redirect to Vue dev server
            get("/") {
                call.respondRedirect("http://localhost:3006")
            }
            logger.info("Frontend is running on: http://localhost:3006")
        }

        application.isProd -> {
            // serve Vue page with Ktor
            singlePageApplication {
                vue("frontend/SomethingWithSockets_Frontend/dist")
            }
            staticResources("/", "dist")
        }
    }
}
