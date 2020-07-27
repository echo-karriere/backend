package no.echokarriere.auth

import java.time.Instant
import java.util.UUID
import no.echokarriere.user.Users
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.timestamp

data class RefreshTokenEntity(
    val id: Int,
    val refreshToken: String,
    val userId: UUID,
    val expiresAt: Instant,
    val createdAt: Instant
)

data class RefreshTokenDTO(
    val refreshToken: String,
    val userId: UUID,
    val expiresAt: Instant,
    val createdAt: Instant
)

data class LoginInput(
    val email: String,
    val password: String
)

data class LoginPayload(
    val token: String
)

object RefreshTokens : Table("refresh_token") {
    val id = integer("id").autoIncrement()
    val refreshToken = text("refresh_token")
    val userId = uuid("user_id").references(Users.id)
    val expiresAt = timestamp("expires_at")
    val createdAt = timestamp("created_at")
    override val primaryKey = PrimaryKey(id)
}
