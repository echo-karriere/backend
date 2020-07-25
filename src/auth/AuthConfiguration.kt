package no.echokarriere.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
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
import java.util.Date

data class Session(val token: String)

data class UserPrincipal(val token: String) : Principal

@KtorExperimentalAPI
fun Application.installAuth(testing: Boolean, config: HoconApplicationConfig) {
    val jwtIssuer = config.propertyOrNull("jwt.domain")?.getString() ?: error("Missing `jwt.domain` property")
    val jwtAudience = config.propertyOrNull("jwt.audience")?.getString() ?: error("Missing `jwt.audience` property")
    val jwtRealm = config.propertyOrNull("jwt.realm")?.getString() ?: error("Missing `jwt.realm` property")

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
            verifier(makeJwtVerifier(jwtIssuer, jwtIssuer))
            validate { credentials ->
                if (credentials.payload.audience.contains(jwtAudience)) JWTPrincipal(credentials.payload) else null
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
        authRoutes()
    }
}

private val algorithm = Algorithm.HMAC256("secret")
private fun makeJwtVerifier(issuer: String, audience: String): JWTVerifier = JWT
    .require(algorithm)
    .withAudience(audience)
    .withIssuer(issuer)
    .build()

fun makeToken(user: String, issuer: String): String = JWT.create()
    .withSubject(user)
    .withIssuer(issuer)
    .withExpiresAt(Date(System.currentTimeMillis() + 36000))
    .sign(algorithm)
