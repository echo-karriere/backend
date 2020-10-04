package no.echokarriere.form

import no.echokarriere.configuration.CrudRepository
import no.echokarriere.jdbiQuery
import org.jdbi.v3.core.Jdbi
import java.util.UUID

class FormRepository(private val jdbi: Jdbi) : CrudRepository<FormEntity, UUID> {
    override suspend fun selectAll(): List<FormEntity> = jdbiQuery {
        jdbi.withHandle<List<FormEntity>, Exception> { handle ->
            handle.select(
                """
                SELECT id, title, description, created_at, modified_at
                FROM form
                """.trimIndent()
            )
                .map(FormEntity.Companion::map)
                .filterNotNull()
                .toList()
        }
    }

    override suspend fun select(id: UUID): FormEntity? = jdbiQuery {
        jdbi.withHandle<FormEntity, Exception> { handle ->
            handle.select(
                """
                SELECT id, title, description, created_at, modified_at
                FROM form
                WHERE id = :id
                """.trimIndent()
            )
                .bind("id", id)
                .map(FormEntity.Companion::map)
                .firstOrNull()
        }
    }

    override suspend fun insert(entity: FormEntity): FormEntity? = jdbiQuery {
        jdbi.withHandle<FormEntity, Exception> { handle ->
            handle.createQuery(
                """
                INSERT INTO form
                VALUES (:id, :title, :description, :created_at)
                RETURNING *
                """.trimIndent()
            )
                .bind("id", entity.id)
                .bind("title", entity.title)
                .bind("description", entity.description)
                .bind("created_at", entity.createdAt)
                .map(FormEntity.Companion::map)
                .firstOrNull()
        }
    }

    override suspend fun update(entity: FormEntity): FormEntity? = jdbiQuery {
        jdbi.withHandle<FormEntity?, Exception> { handle ->
            handle.createQuery(
                """
                UPDATE form
                SET title       = :title,
                    description = :description,
                    created_at  = :created_at
                WHERE id = :id
                RETURNING *
                """.trimIndent()
            )
                .bind("id", entity.id)
                .bind("title", entity.title)
                .bind("description", entity.description)
                .bind("created_at", entity.createdAt)
                .map(FormEntity.Companion::map)
                .firstOrNull()
        }
    }

    override suspend fun delete(id: UUID): Boolean = jdbiQuery {
        jdbi.withHandle<Boolean, Exception> { handle ->
            handle.createUpdate(
                """
                DELETE FROM form WHERE id = :id
                """.trimIndent()
            )
                .bind("id", id)
                .execute() == 1
        }
    }
}
