package com.alpenraum.controllers

import com.alpenraum.ws.SessionManager
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.collectLatest
import org.koin.ktor.ext.inject


fun Routing.webSocketController() {
    val sessionManager: SessionManager by inject()
    webSocket("/ws") { // websocketSession
        val id = sessionManager.addSession(this)
        outgoing.send(Frame.Text("Connected: $id"))
        try {
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val text = frame.readText()
                    sessionManager.sessions.forEach { (key, value) ->
                        value.outgoing.send(Frame.Text("ID SAID: $text"))
                    }

                    if (text.equals("sessions", ignoreCase = true)) {
                        outgoing.send(Frame.Text(sessionManager.sessions.toList().toString()))
                    }

                    if (text.equals("bye", ignoreCase = true)) {
                        close(CloseReason(CloseReason.Codes.NORMAL, "Client said BYE"))
                    }
                }
            }
        } finally {
            sessionManager.sessions.remove(id)
        }
    }
}