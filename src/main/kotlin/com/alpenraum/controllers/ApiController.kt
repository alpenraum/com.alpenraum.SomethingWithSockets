package com.alpenraum.controllers

import com.alpenraum.ws.SessionManager
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.host
import io.ktor.server.routing.port
import io.ktor.websocket.*
import org.koin.ktor.ext.inject

fun Routing.apiController(host: String, port: Int){
    val sessionManager: SessionManager by this.inject()
    host("api.$host", port) {
        get("/") {
            call.respondText("Hello TEst")
        }
        post<String>("/test"){
           val body = call.receive<String>()

            sessionManager.sessions.forEach{ (key, value) ->
                value.send(Frame.Text("api update: $body"))
            }
            call.respond(HttpStatusCode.OK)
        }


    }
}