package no.echokarriere.user

import no.echokarriere.Tables.USER
import no.echokarriere.configuration.Argon2Configuration
import no.echokarriere.configuration.CrudRepository
import no.echokarriere.dbQuery
import org.jooq.DSLContext
import java.util.UUID

class UserRepository(private val argon: Argon2Configuration, private val jooq: DSLContext) :
    CrudRepository<UserEntity, UUID> {
    override suspend fun selectAll(): List<UserEntity> = dbQuery {
        jooq.select()
            .from(USER)
            .fetch()
            .into(UserEntity::class.java)
    }

    override suspend fun select(id: UUID): UserEntity? = dbQuery {
        jooq.select()
            .from(USER)
            .where(USER.ID.eq(id))
            .fetchOne()
            ?.into(UserEntity::class.java)
    }

    suspend fun selectByEmail(email: String): UserEntity? = dbQuery {
        jooq.select()
            .from(USER)
            .where(USER.EMAIL.eq(email))
            .fetchOne()
            ?.into(UserEntity::class.java)
    }

    override suspend fun insert(entity: UserEntity): UserEntity? = dbQuery {
        jooq.insertInto(USER)
            .columns(USER.ID, USER.NAME, USER.EMAIL, USER.PASSWORD, USER.ACTIVE, USER.TYPE, USER.CREATED_AT)
            .values(
                entity.id,
                entity.name,
                entity.email,
                argon.hash(entity.password.toCharArray()),
                entity.active,
                entity.type,
                entity.createdAt
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
