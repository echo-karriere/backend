package no.echokarriere.auth

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.Principal
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.jwt.jwt
import io.ktor.auth.session
import io.ktor.config.HoconApplicationConfig
import io.ktor.routing.routing
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.util.KtorExperimentalAPI

data class Session(val token: String)

data class UserPrincipal(val token: String) : Principal

@KtorExperimentalAPI
fun Application.installAuth(testing: Boolean, config: HoconApplicationConfig) {
    val jwtRealm = config.propertyOrNull("jwt.realm")?.getString() ?: error("Missing `jwt.realm` property")
    val jwtConfiguration = JWTConfiguration(testing, config)

    install(Sessions) {
        cookie<Session>("echo_karriere_session") {
            cookie.secure = !testing
            cookie.httpOnly = !testing
            cookie.extensions["SameSite"] = if (!testing) "strict" else "lax"
            cookie.path = "/"
        }
    }

    install(Authentication) {
        jwt {
            realm = jwtRealm
            verifier(jwtConfiguration.makeJwtVerifier())
            validate { credentials ->
                if (credentials.payload.audience.contains(jwtConfiguration.makeToken("test"))) {
                    JWTPrincipal(credentials.payload)
                } else {
                    null
                }
            }
        }

        session<Session>("echo_karriere_session") {
            validate {
                sessions.get<Session>()?.let {
                    val token = it.token
                    UserPrincipal(token)
                }
            }
        }
    }

    routing {
        authRoutes(jwtConfiguration)
    }
}
