package no.echokarriere.user

import java.time.Instant
import java.util.UUID
import no.echokarriere.configuration.Argon2Configuration
import no.echokarriere.configuration.CrudRepository
import no.echokarriere.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class UserRepository(private val argon: Argon2Configuration) : CrudRepository<UserEntity, UUID> {
    override suspend fun selectAll(): List<UserEntity> = dbQuery {
        Users.selectAll().map { toUser(it) }
    }

    override suspend fun select(id: UUID): UserEntity? = dbQuery {
        Users
            .select { Users.id eq id }
            .mapNotNull { toUser(it) }
            .singleOrNull()
    }

    suspend fun selectByEmail(email: String): UserEntity? = dbQuery {
        Users
            .select { Users.email eq email }
            .mapNotNull { toUser(it) }
            .singleOrNull()
    }

    override suspend fun insert(value: UserEntity): UserEntity? {
        val created = dbQuery {
            Users
                .insertIgnore {
                    it[id] = value.id
                    it[name] = value.name
                    it[email] = value.email
                    it[password] = argon.hash(value.password.toCharArray())
                    it[active] = true
                    it[type] = value.type
                    it[createdAt] = Instant.now()
                }
                .resultedValues
        }

        if (created.isNullOrEmpty()) {
            throw RuntimeException("User not created")
        }

        return this.select(value.id)
    }

    override suspend fun update(value: UserEntity): UserEntity? {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: UUID): Boolean {
        TODO("Not yet implemented")
    }
}

private fun toUser(row: ResultRow): UserEntity = UserEntity.create(
    id = row[Users.id],
    name = row[Users.name],
    email = row[Users.email],
    password = row[Users.password],
    active = row[Users.active],
    type = row[Users.type],
    createdAt = row[Users.createdAt],
    modifiedAt = row[Users.modifiedAt]
)
