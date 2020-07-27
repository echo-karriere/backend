package no.echokarriere.auth

import no.echokarriere.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class AuthRepository {
    suspend fun selectAll(): List<RefreshTokenEntity> = dbQuery {
        RefreshTokens.selectAll().map { toRefreshToken(it) }
    }

    suspend fun select(id: Int): RefreshTokenEntity? = dbQuery {
        RefreshTokens
            .select { RefreshTokens.id eq id }
            .mapNotNull { toRefreshToken(it) }
            .singleOrNull()
    }

    suspend fun select(token: String): RefreshTokenEntity? = dbQuery {
        RefreshTokens
            .select { RefreshTokens.refreshToken eq token }
            .mapNotNull { toRefreshToken(it) }
            .singleOrNull()
    }

    suspend fun insert(data: RefreshTokenDTO): RefreshTokenEntity? {
        return dbQuery {
            RefreshTokens
                .insert {
                    it[refreshToken] = data.refreshToken
                    it[userId] = data.userId
                    it[expiresAt] = data.expiresAt
                    it[createdAt] = data.createdAt
                }
                .resultedValues
                ?.map { toRefreshToken(it) }
                ?.singleOrNull()
        }
    }

    suspend fun delete(id: Int): Boolean = dbQuery {
        RefreshTokens.deleteWhere { RefreshTokens.id eq id } == 1
    }
}

fun toRefreshToken(row: ResultRow): RefreshTokenEntity = RefreshTokenEntity(
    id = row[RefreshTokens.id],
    refreshToken = row[RefreshTokens.refreshToken],
    userId = row[RefreshTokens.userId],
    expiresAt = row[RefreshTokens.expiresAt],
    createdAt = row[RefreshTokens.createdAt]
)
