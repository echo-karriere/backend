package no.echokarriere.auth.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.config.HoconApplicationConfig
import java.util.Date
import java.util.UUID

class JWTConfiguration(config: HoconApplicationConfig) {
    private val jwtIssuer = config
        .propertyOrNull("jwt.domain")
        ?.getString()
        ?: error("Missing `jwt.domain` property")

    private val jwtAudience = config
        .propertyOrNull("jwt.audience")
        ?.getString()
        ?: error("Missing `jwt.audience` property")

    private val validity = if (config.propertyOrNull("prod")?.getString().equals("true")) 900 else 3600
    private val algorithm = Algorithm.HMAC256("secret")

    fun makeJwtVerifier(): JWTVerifier = JWT
        .require(algorithm)
        .withAudience(jwtAudience)
        .withIssuer(jwtIssuer)
        .build()

    fun makeToken(user: UUID): String {
        return JWT.create()
            .withSubject(user.toString())
            .withIssuer(jwtIssuer)
            .withExpiresAt(expiration())
            .sign(algorithm)
    }

    private fun expiration() = Date(System.currentTimeMillis() + validity)
}
