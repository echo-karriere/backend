package no.echokarriere.auth

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.jwt
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import no.echokarriere.ServiceRegistry
import no.echokarriere.user.UserPrincipal
import java.util.UUID
import kotlin.collections.set

const val REFRESH_TOKEN_DURATION: Long = 60 * 60 * 24 * 30 // 30 days

fun Application.installAuth(
    serviceRegistry: ServiceRegistry
) {
    val config = serviceRegistry.applicationConfig
    val userRepository = serviceRegistry.userRepository
    val jwtConfiguration = serviceRegistry.jwtConfiguration

    val prod = config.propertyOrNull("prod") != null
    val jwtRealm = config.propertyOrNull("jwt.realm")?.getString() ?: error("Missing `jwt.realm` property")

    install(Sessions) {
        cookie<SessionCookie>("echo_karriere_session") {
            cookie.secure = prod
            cookie.httpOnly = prod
            cookie.extensions["SameSite"] = if (prod) "strict" else "lax"
            cookie.path = "/"
            cookie.maxAgeInSeconds = REFRESH_TOKEN_DURATION
        }
    }

    install(Authentication) {
        jwt {
            realm = jwtRealm
            verifier(jwtConfiguration.makeJwtVerifier())
            validate { credentials ->
                val user = userRepository.select(UUID.fromString(credentials.payload.subject))

                if (user != null) {
                    UserPrincipal(user)
                } else {
                    null
                }
            }
        }
    }
}
