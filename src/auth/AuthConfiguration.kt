package no.echokarriere.auth

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.jwt
import io.ktor.config.HoconApplicationConfig
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import io.ktor.util.KtorExperimentalAPI
import java.util.UUID
import kotlin.collections.set
import no.echokarriere.ServiceRegistry
import no.echokarriere.user.UserPrincipal

data class Session(val token: String)

const val REFRESH_TOKEN_DURATION: Long = 60 * 60 * 24 * 30 // 30 days

@KtorExperimentalAPI
fun Application.installAuth(
    testing: Boolean,
    config: HoconApplicationConfig,
    serviceRegistry: ServiceRegistry
) {
    val jwtRealm = config.propertyOrNull("jwt.realm")?.getString() ?: error("Missing `jwt.realm` property")
    val userRepository = serviceRegistry.userRepository
    val jwtConfiguration = serviceRegistry.jwtConfiguration

    install(Sessions) {
        cookie<Session>("echo_karriere_session") {
            cookie.secure = !testing
            cookie.httpOnly = !testing
            cookie.extensions["SameSite"] = if (!testing) "strict" else "lax"
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
