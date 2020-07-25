package no.echokarriere.auth

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.post
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.sessions.set

fun Routing.authRoutes() {
    post("/login") {
        call.sessions.set(Session(makeToken("test user", "no")))
        call.respond("test")
    }
}
