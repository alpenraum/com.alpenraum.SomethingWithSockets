package com.alpenraum.ws

import io.ktor.websocket.WebSocketSession
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.ConcurrentHashMap
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class SessionManager {
    val sessions = ConcurrentHashMap<String, WebSocketSession>()

    private val mutex = Mutex()

    @OptIn(ExperimentalUuidApi::class)
    suspend fun addSession(session: WebSocketSession): String {
        mutex.withLock {
            val sessionId = Uuid.random().toString()
            sessions[sessionId] = session
            return sessionId
        }
    }
}
