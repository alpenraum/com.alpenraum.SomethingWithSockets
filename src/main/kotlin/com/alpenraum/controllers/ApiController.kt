package com.alpenraum.controllers

import com.alpenraum.ws.SessionManager
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.host
import io.ktor.server.routing.post
import io.ktor.websocket.Frame
import org.koin.ktor.ext.inject

fun Routing.apiController(
    host: String,
    port: Int,
) {
    val sessionManager: SessionManager by this.inject()
    host("api.$host", port) {
        get("/") {
            call.respondText("Hello Test")
        }
        post<String>("/test") {
            val body = call.receive<String>()

            sessionManager.sessions.forEach { (key, value) ->
                value.send(Frame.Text("api update: $body"))
            }
            call.respond(HttpStatusCode.OK, null)
        }
    }
}
