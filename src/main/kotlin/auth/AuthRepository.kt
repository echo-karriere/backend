package no.echokarriere.auth

import no.echokarriere.configuration.CrudRepository
import no.echokarriere.jdbiQuery
import org.jdbi.v3.core.Jdbi
import java.util.UUID

class AuthRepository(private val jdbi: Jdbi) : CrudRepository<RefreshTokenEntity, UUID> {
    override suspend fun selectAll(): List<RefreshTokenEntity> = jdbiQuery {
        jdbi.withHandle<List<RefreshTokenEntity>, Exception> {
            it.select(
                """
                SELECT user_id, refresh_token, expires_at, created_at
                FROM refresh_token
                """.trimIndent()
            )
                .map(RefreshTokenEntity.Companion::map)
                .filterNotNull()
                .toList()
        }
    }

    override suspend fun select(id: UUID): RefreshTokenEntity? = jdbiQuery {
        jdbi.withHandle<RefreshTokenEntity?, Exception> {
            it.select(
                """
                SELECT user_id, refresh_token, expires_at, created_at
                FROM refresh_token
                WHERE user_id = :id
                """.trimIndent()
            )
                .bind("id", id)
                .map(RefreshTokenEntity.Companion::map)
                .firstOrNull()
        }
    }

    suspend fun selectByToken(token: String): RefreshTokenEntity? = jdbiQuery {
        jdbi.withHandle<RefreshTokenEntity?, Exception> {
            it.select(
                """
                SELECT user_id, refresh_token, expires_at, created_at
                FROM refresh_token
                WHERE refresh_token = :token
                """.trimIndent()
            )
                .bind("token", token)
                .map(RefreshTokenEntity.Companion::map)
                .firstOrNull()
        }
    }

    override suspend fun insert(value: RefreshTokenEntity): RefreshTokenEntity? = jdbiQuery {
        jdbi.withHandle<RefreshTokenEntity?, Exception> {
            it.createQuery(
                """
                INSERT INTO refresh_token
                VALUES (:user_id, :refresh_token, :expires_at, :created_at)
                RETURNING *
                """.trimIndent()
            )
                .bind("user_id", value.userId)
                .bind("refresh_token", value.refreshToken)
                .bind("expires_at", value.expiresAt)
                .bind("created_at", value.createdAt)
                .map(RefreshTokenEntity.Companion::map)
                .firstOrNull()
        }
    }

    override suspend fun delete(id: UUID): Boolean = jdbiQuery {
        jdbi.withHandle<Boolean, Exception> { handle ->
            handle.createUpdate(
                """
                DELETE FROM refresh_token WHERE user_id = :id
                """.trimIndent()
            )
                .bind("id", id)
                .execute() == 1
        }
    }

    override suspend fun update(value: RefreshTokenEntity): RefreshTokenEntity? = jdbiQuery {
        jdbi.withHandle<RefreshTokenEntity?, Exception> {
            it.createQuery(
                """
                UPDATE refresh_token
                SET user_id = :user_id, refresh_token = :refresh_token, expires_at = :expires_at, created_at = :created_at
                WHERE user_id = :user_id
                RETURNING *
                """.trimIndent()
            )
                .bind("user_id", value.userId)
                .bind("refresh_token", value.refreshToken)
                .bind("expires_at", value.expiresAt)
                .bind("created_at", value.createdAt)
                .map(RefreshTokenEntity.Companion::map)
                .firstOrNull()
        }
    }
}
