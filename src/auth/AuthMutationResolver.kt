package no.echokarriere.auth

import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import java.security.SecureRandom
import java.time.Instant
import no.echokarriere.auth.jwt.JWTConfiguration
import no.echokarriere.configuration.Argon2Configuration
import no.echokarriere.graphql.ApplicationCallContext
import no.echokarriere.user.UserRepository

@Suppress("unused") // Used by GraphQL via reflection
class AuthMutationResolver(
    private val jwtConfiguration: JWTConfiguration,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val argon2Configuration: Argon2Configuration
) {
    suspend fun login(context: ApplicationCallContext, input: LoginInput): LoginPayload {
        val user = userRepository.selectByEmail(input.email) ?: throw UnauthorizedException("Invalid username or password")

        if (!argon2Configuration.verify(user.password, input.password.toCharArray())) {
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

        context.call.sessions.set(Session(resp?.refreshToken!!))
        return LoginPayload(jwtConfiguration.makeToken(user.id))
    }

    suspend fun refreshToken(context: ApplicationCallContext): LoginPayload {
        val token = context.call.sessions.get<Session>() ?: throw UnauthorizedException("Missing refresh token")

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

        context.call.sessions.set(Session(resp?.refreshToken!!))
        return LoginPayload(jwtConfiguration.makeToken(previousToken.userId))
    }
}
