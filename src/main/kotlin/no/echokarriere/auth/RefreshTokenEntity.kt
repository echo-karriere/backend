package no.echokarriere.auth

import java.time.OffsetDateTime
import java.util.UUID

class RefreshTokenEntity private constructor(
    val userId: UUID,
    val refreshToken: String,
    val expiresAt: OffsetDateTime,
    val createdAt: OffsetDateTime
) {
    companion object {
        fun create(
            refreshToken: String,
            userId: UUID,
            expiresAt: OffsetDateTime,
            createdAt: OffsetDateTime
        ): RefreshTokenEntity = RefreshTokenEntity(
            refreshToken = refreshToken,
            userId = userId,
            expiresAt = expiresAt,
            createdAt = createdAt
        )
    }
}

data class LoginInput(
    val email: String,
    val password: String
)

data class LoginPayload(
    val token: String
)
