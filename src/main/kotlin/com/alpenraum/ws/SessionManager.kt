package com.alpenraum.ws

import io.ktor.websocket.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class SessionManager {
    private val sessionCounter = AtomicInteger()
    val sessions = ConcurrentHashMap<Int, WebSocketSession>()

    fun addSession(session: WebSocketSession): Int {
        synchronized(this) {
            val sessionId = sessionCounter.getAndIncrement()
            sessions[sessionId] = session
            return sessionId
        }
    }
}