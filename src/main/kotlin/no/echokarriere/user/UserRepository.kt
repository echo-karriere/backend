package no.echokarriere.user

import no.echokarriere.Tables.USER
import no.echokarriere.configuration.Argon2Configuration
import no.echokarriere.configuration.CrudRepository
import no.echokarriere.jdbiQuery
import org.jooq.DSLContext
import java.time.OffsetDateTime
import java.time.ZoneId
import java.util.UUID

class UserRepository(private val argon: Argon2Configuration, private val jooq: DSLContext) :
    CrudRepository<UserEntity, UUID> {
    override suspend fun selectAll(): List<UserEntity> = jdbiQuery {
        jooq.select()
            .from(USER)
            .fetch()
            .into(UserEntity::class.java)
    }

    override suspend fun select(id: UUID): UserEntity? = jdbiQuery {
        jooq.select()
            .from(USER)
            .where(USER.ID.eq(id))
            .fetchOne()
            ?.into(UserEntity::class.java)
    }

    suspend fun selectByEmail(email: String): UserEntity? = jdbiQuery {
        jooq.select()
            .from(USER)
            .where(USER.EMAIL.eq(email))
            .fetchOne()
            ?.into(UserEntity::class.java)
    }

    override suspend fun insert(entity: UserEntity): UserEntity? = jdbiQuery {
        jooq.insertInto(USER)
            .columns(USER.ID, USER.NAME, USER.EMAIL, USER.PASSWORD, USER.ACTIVE, USER.TYPE, USER.CREATED_AT)
            .values(
                entity.id,
                entity.name,
                entity.email,
                argon.hash(entity.password.toCharArray()),
                entity.active,
                entity.type,
                OffsetDateTime.ofInstant(entity.createdAt, ZoneId.systemDefault())
            )
            .returning()
            .fetchOne()
            ?.into(UserEntity::class.java)
    }

    override suspend fun update(entity: UserEntity): UserEntity? {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: UUID): Boolean {
        TODO("Not yet implemented")
    }
}
