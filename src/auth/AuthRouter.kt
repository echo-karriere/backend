package no.echokarriere.auth

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.post
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import io.ktor.util.KtorExperimentalAPI
import java.util.UUID

@KtorExperimentalAPI
fun Routing.authRoutes(jwtConfiguration: JWTConfiguration) {
    post("/login") {
        call.sessions.set(Session(jwtConfiguration.makeToken(UUID.randomUUID().toString())))
        call.respond("test")
    }
}
