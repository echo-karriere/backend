package no.echokarriere.auth

import no.echokarriere.getUUID
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
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
                userId = it.getUUID("user_id"),
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
