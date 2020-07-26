package no.echokarriere.auth

import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.post
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import io.ktor.util.KtorExperimentalAPI
import java.security.SecureRandom
import java.time.Instant
import no.echokarriere.configuration.Argon2Configuration
import no.echokarriere.user.UserRepository

data class LoginDTO(
    val email: String,
    val password: String
)

@KtorExperimentalAPI
fun Routing.authRoutes(
    jwtConfiguration: JWTConfiguration,
    userRepository: UserRepository,
    authRepository: AuthRepository,
    argon2Configuration: Argon2Configuration
) {
    post("/login") {
        val (email, password) = call.receive<LoginDTO>()
        val user = userRepository.selectByEmail(email) ?: throw UnauthorizedException("Invalid username or password")

        if (!argon2Configuration.verify(user.password, password.toCharArray())) {
            throw UnauthorizedException("Invalid username or password")
        }

        val refreshToken = SecureRandom().generateByteArray(24).encodeAsBase64()
        val dto = RefreshTokenDTO(
            refreshToken = refreshToken,
            userId = user.id,
            expiresAt = Instant.now().plusSeconds(REFRESH_TOKEN_DURATION),
            createdAt = Instant.now()
        )

        val resp = authRepository.insert(dto)

        call.sessions.set(Session(resp?.refetchToken!!))
        call.respond(mapOf("token" to jwtConfiguration.makeToken(user.id)))
    }

    post("/refresh_token") {
        val token = call.sessions.get<Session>() ?: throw UnauthorizedException("Missing refresh token")

        val previousToken = authRepository.select(token.token) ?: throw UnauthorizedException("Invalid token")
        val nextToken = SecureRandom().generateByteArray(24).encodeAsBase64()
        val refreshToken = RefreshTokenDTO(
            refreshToken = nextToken,
            userId = previousToken.userId,
            expiresAt = Instant.now().plusSeconds(REFRESH_TOKEN_DURATION),
            createdAt = Instant.now()
        )

        val resp = authRepository.insert(refreshToken)
        if (!authRepository.delete(previousToken.id)) throw RuntimeException("Unable to delete token")

        call.sessions.set(Session(resp?.refetchToken!!))
        call.respond(mapOf("token" to jwtConfiguration.makeToken(refreshToken.userId)))
    }
}
