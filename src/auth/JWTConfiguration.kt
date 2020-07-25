package no.echokarriere.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.config.HoconApplicationConfig
import io.ktor.util.KtorExperimentalAPI
import java.util.Date

@KtorExperimentalAPI
class JWTConfiguration(testing: Boolean, config: HoconApplicationConfig) {
    private val jwtIssuer = config.propertyOrNull("jwt.domain")?.getString() ?: error("Missing `jwt.domain` property")
    private val jwtAudience = config.propertyOrNull("jwt.audience")?.getString() ?: error("Missing `jwt.audience` property")
    private val validity = if (!testing) 900 else 3600
    private val algorithm = Algorithm.HMAC256("secret")

    fun makeJwtVerifier(): JWTVerifier = JWT
        .require(algorithm)
        .withAudience(jwtAudience)
        .withIssuer(jwtIssuer)
        .build()

    fun makeToken(user: String): String {
        return JWT.create()
            .withSubject(user)
            .withIssuer(jwtIssuer)
            .withExpiresAt(expiration())
            .sign(algorithm)
    }

    private fun expiration() = Date(System.currentTimeMillis() + validity)
}
