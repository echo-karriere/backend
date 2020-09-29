package no.echokarriere.auth

import no.echokarriere.user.Users
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.timestamp
import java.sql.ResultSet
import java.time.Instant
import java.util.UUID

class RefreshTokenEntity private constructor(
    val refreshToken: String,
    val userId: UUID,
    val expiresAt: Instant,
    val createdAt: Instant
) {
    companion object : RowMapper<RefreshTokenEntity> {
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

        override fun map(rs: ResultSet?, ctx: StatementContext?): RefreshTokenEntity? = rs?.let {
            create(
                refreshToken = it.getString("refresh_token"),
                userId = UUID.fromString(it.getString("user_id")),
                expiresAt = it.getTimestamp("expires_at").toInstant(),
                createdAt = it.getTimestamp("created_at").toInstant()
            )
        }
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
