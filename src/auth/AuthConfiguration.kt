package no.echokarriere.auth

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.jwt
import io.ktor.config.HoconApplicationConfig
import io.ktor.routing.routing
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import io.ktor.util.KtorExperimentalAPI
import java.util.UUID
import kotlin.collections.set
import no.echokarriere.configuration.Argon2Configuration
import no.echokarriere.user.UserPrincipal
import no.echokarriere.user.UserRepository

data class Session(val token: String)

@KtorExperimentalAPI
fun Application.installAuth(
    testing: Boolean,
    config: HoconApplicationConfig,
    userRepository: UserRepository,
    authRepository: AuthRepository,
    argon2Configuration: Argon2Configuration
) {
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
                val user = userRepository.select(UUID.fromString(credentials.payload.subject))

                if (user != null) {
                    UserPrincipal(user)
                } else {
                    null
                }
            }
        }
    }

    routing {
        authRoutes(jwtConfiguration, userRepository, authRepository, argon2Configuration)
    }
}
