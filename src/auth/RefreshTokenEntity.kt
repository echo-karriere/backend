package no.echokarriere.auth

import no.echokarriere.user.Users
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.timestamp
import java.time.Instant
import java.util.UUID

class RefreshTokenEntity private constructor(
    val refreshToken: String,
    val userId: UUID,
    val expiresAt: Instant,
    val createdAt: Instant
) {
    companion object {
        fun create(
            refreshToken: String,
            userId: UUID,
            expiresAt: Instant,
            createdAt: Instant
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

object RefreshTokens : Table("refresh_token") {
    val refreshToken = text("refresh_token")
    val userId = uuid("user_id").references(Users.id)
    val expiresAt = timestamp("expires_at")
    val createdAt = timestamp("created_at")
    override val primaryKey = PrimaryKey(userId)
}
