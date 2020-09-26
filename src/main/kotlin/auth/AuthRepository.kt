package no.echokarriere.auth

import no.echokarriere.configuration.CrudRepository
import no.echokarriere.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import java.util.UUID

class AuthRepository : CrudRepository<RefreshTokenEntity, UUID> {
    override suspend fun selectAll(): List<RefreshTokenEntity> = dbQuery {
        RefreshTokens.selectAll().map { toRefreshToken(it) }
    }

    override suspend fun select(id: UUID): RefreshTokenEntity? = dbQuery {
        RefreshTokens
            .select { RefreshTokens.userId eq id }
            .mapNotNull { toRefreshToken(it) }
            .singleOrNull()
    }

    suspend fun selectByToken(token: String): RefreshTokenEntity? = dbQuery {
        RefreshTokens
            .select { RefreshTokens.refreshToken eq token }
            .mapNotNull { toRefreshToken(it) }
            .singleOrNull()
    }

    override suspend fun insert(value: RefreshTokenEntity): RefreshTokenEntity? {
        return dbQuery {
            RefreshTokens
                .insert {
                    it[refreshToken] = value.refreshToken
                    it[userId] = value.userId
                    it[expiresAt] = value.expiresAt
                    it[createdAt] = value.createdAt
                }
                .resultedValues
                ?.map { toRefreshToken(it) }
                ?.singleOrNull()
        }
    }

    override suspend fun delete(id: UUID): Boolean = dbQuery {
        RefreshTokens.deleteWhere { RefreshTokens.userId eq id } == 1
    }

    override suspend fun update(value: RefreshTokenEntity): RefreshTokenEntity? {
        dbQuery {
            RefreshTokens.update({ RefreshTokens.userId eq value.userId }) {
                it[refreshToken] = value.refreshToken
                it[expiresAt] = value.expiresAt
                it[createdAt] = value.createdAt
            }
        }

        return this.select(value.userId)
    }
}

fun toRefreshToken(row: ResultRow): RefreshTokenEntity = RefreshTokenEntity.create(
    refreshToken = row[RefreshTokens.refreshToken],
    userId = row[RefreshTokens.userId],
    expiresAt = row[RefreshTokens.expiresAt],
    createdAt = row[RefreshTokens.createdAt]
)
