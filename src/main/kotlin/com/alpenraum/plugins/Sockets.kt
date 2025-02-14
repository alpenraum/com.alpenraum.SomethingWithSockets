package com.alpenraum.plugins

import com.alpenraum.controllers.webSocketController
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = 15.toDuration(kotlin.time.DurationUnit.SECONDS)
        timeout = 15.toDuration(kotlin.time.DurationUnit.SECONDS)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    routing {
        webSocketController()
    }
}
