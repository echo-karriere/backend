package no.echokarriere.auth

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.jwt
import io.ktor.config.ApplicationConfig
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import no.echokarriere.auth.jwt.JWTConfiguration
import no.echokarriere.user.UserPrincipal
import no.echokarriere.user.UserRepository
import org.koin.ktor.ext.inject
import java.util.UUID
import kotlin.collections.set

data class Session(val token: String)

const val REFRESH_TOKEN_DURATION: Long = 60 * 60 * 24 * 30 // 30 days

fun Application.installAuth(
    config: ApplicationConfig
) {
    val prod = config.propertyOrNull("prod") != null
    val jwtRealm = config.propertyOrNull("jwt.realm")?.getString() ?: error("Missing `jwt.realm` property")
    val userRepository: UserRepository by inject()
    val jwtConfiguration: JWTConfiguration by inject()

    install(Sessions) {
        cookie<Session>("echo_karriere_session") {
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
