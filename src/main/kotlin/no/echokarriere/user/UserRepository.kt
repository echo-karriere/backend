package no.echokarriere.user

import no.echokarriere.configuration.Argon2Configuration
import no.echokarriere.configuration.CrudRepository
import no.echokarriere.jdbiQuery
import org.jdbi.v3.core.Jdbi
import java.util.UUID

class UserRepository(private val argon: Argon2Configuration, private val jdbi: Jdbi) :
    CrudRepository<UserEntity, UUID> {
    override suspend fun selectAll(): List<UserEntity> = jdbiQuery {
        jdbi.withHandle<List<UserEntity>, Exception> { handle ->
            handle.select(
                """
                SELECT id, name, email, password, active, type, created_at, modified_at
                FROM "user"
                """.trimIndent()
            )
                .map(UserEntity.Companion::map)
                .filterNotNull()
                .toList()
        }
    }

    override suspend fun select(id: UUID): UserEntity? = jdbiQuery {
        jdbi.withHandle<UserEntity, Exception> { handle ->
            handle.select(
                """
                SELECT id, name, email, password, active, type, created_at, modified_at
                FROM "user"
                WHERE id = :id
                """.trimIndent()
            )
                .bind("id", id)
                .map(UserEntity.Companion::map)
                .firstOrNull()
        }
    }

    suspend fun selectByEmail(email: String): UserEntity? = jdbiQuery {
        jdbi.withHandle<UserEntity, Exception> { handle ->
            handle.select(
                """
                SELECT id, name, email, password, active, type, created_at, modified_at
                FROM "user"
                WHERE email = :email
                """.trimIndent()
            )
                .bind("email", email)
                .map(UserEntity.Companion::map)
                .firstOrNull()
        }
    }

    override suspend fun insert(value: UserEntity): UserEntity? = jdbiQuery {
        jdbi.withHandle<UserEntity, Exception> { handle ->
            handle.createQuery(
                """
                INSERT INTO "user"
                VALUES (:id, :name, :email, :password, :active, :type, :created_at)
                RETURNING *
                """.trimIndent()
            )
                .bind("id", value.id)
                .bind("name", value.name)
                .bind("email", value.email)
                .bind("password", argon.hash(value.password.toCharArray()))
                .bind("active", value.active)
                .bind("type", value.type)
                .bind("created_at", value.createdAt)
                .map(UserEntity.Companion::map)
                .firstOrNull()
        }
    }

    override suspend fun update(value: UserEntity): UserEntity? {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: UUID): Boolean {
        TODO("Not yet implemented")
    }
}
