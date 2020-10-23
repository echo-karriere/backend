package no.echokarriere.auth

import no.echokarriere.configuration.CrudRepository
import no.echokarriere.dbQuery
import org.jooq.DSLContext
import org.jooq.impl.DSL.row
import java.time.OffsetDateTime
import java.util.UUID
import no.echokarriere.Tables.REFRESH_TOKEN as AUTH

class AuthRepository(private val jooq: DSLContext) : CrudRepository<RefreshTokenEntity, UUID> {
    override suspend fun selectAll(): List<RefreshTokenEntity> = dbQuery {
        jooq.select()
            .from(AUTH)
            .fetch()
            .into(RefreshTokenEntity::class.java)
    }

    override suspend fun select(id: UUID): RefreshTokenEntity? = dbQuery {
        jooq.select()
            .from(AUTH)
            .where(AUTH.USER_ID.eq(id))
            .fetchOne()
            ?.into(RefreshTokenEntity::class.java)
    }

    suspend fun selectByToken(token: String): RefreshTokenEntity? = dbQuery {
        jooq.select()
            .from(AUTH)
            .where(AUTH.REFRESH_TOKEN_.eq(token))
            .fetchOne()
            ?.into(RefreshTokenEntity::class.java)
    }

    override suspend fun insert(entity: RefreshTokenEntity): RefreshTokenEntity? = dbQuery {
        jooq.insertInto(AUTH)
            .columns(AUTH.USER_ID, AUTH.REFRESH_TOKEN_, AUTH.EXPIRES_AT, AUTH.CREATED_AT)
            .values(entity.userId, entity.refreshToken, entity.expiresAt, entity.createdAt)
            .onDuplicateKeyUpdate()
            .set(AUTH.REFRESH_TOKEN_, entity.refreshToken)
            .set(AUTH.EXPIRES_AT, entity.expiresAt)
            .set(AUTH.CREATED_AT, entity.createdAt)
            .returning()
            .fetchOne()
            ?.into(RefreshTokenEntity::class.java)
    }

    override suspend fun delete(id: UUID): Boolean = dbQuery {
        jooq.delete(AUTH)
            .where(AUTH.USER_ID.eq(id))
            .execute() == 1
    }

    override suspend fun update(entity: RefreshTokenEntity): RefreshTokenEntity? = dbQuery {
        jooq.update(AUTH)
            .set(
                row(AUTH.USER_ID, AUTH.REFRESH_TOKEN_, AUTH.EXPIRES_AT, AUTH.CREATED_AT),
                row(entity.userId, entity.refreshToken, entity.expiresAt, OffsetDateTime.now())
            )
            .where(AUTH.USER_ID.eq(entity.userId))
            .returning()
            .fetchOne()
            ?.into(RefreshTokenEntity::class.java)
    }
}
