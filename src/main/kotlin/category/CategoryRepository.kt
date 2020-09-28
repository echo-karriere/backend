package no.echokarriere.category

import no.echokarriere.configuration.CrudRepository
import no.echokarriere.jdbiQuery
import org.jdbi.v3.core.Jdbi
import java.util.UUID

class CategoryRepository(private val jdbi: Jdbi) : CrudRepository<CategoryEntity, UUID> {
    override suspend fun selectAll(): List<CategoryEntity> = jdbiQuery {
        jdbi.withHandle<List<CategoryEntity>, Exception> { handle ->
            handle.select(
                """
                SELECT id, title, description, slug, created_at, modified_at
                FROM category
                """.trimIndent()
            )
                .map(CategoryEntity.Companion::map)
                .filterNotNull()
                .toList()
        }
    }

    override suspend fun select(id: UUID): CategoryEntity? = jdbiQuery {
        jdbi.withHandle<CategoryEntity?, Exception> { handle ->
            handle.select(
                """
                SELECT id, title, description, slug, created_at, modified_at
                FROM category
                WHERE id = :id
                """.trimIndent()
            )
                .bind("id", id)
                .map(CategoryEntity.Companion::map)
                .firstOrNull()
        }
    }

    override suspend fun insert(value: CategoryEntity): CategoryEntity? = jdbiQuery {
        jdbi.withHandle<CategoryEntity?, Exception> { handle ->
            handle.createQuery(
                """
                INSERT INTO category
                VALUES (:id, :title, :description, :slug)
                RETURNING *
                """.trimIndent()
            )
                .bind("id", value.id)
                .bind("title", value.title)
                .bind("description", value.description)
                .bind("slug", value.slug)
                .map(CategoryEntity.Companion::map)
                .firstOrNull()
        }
    }

    override suspend fun update(value: CategoryEntity): CategoryEntity? = jdbiQuery {
        jdbi.withHandle<CategoryEntity?, Exception> { handle ->
            handle.createQuery(
                """
                UPDATE category
                SET title = :title, description = :description, slug = :slug, modified_at = now()
                WHERE id = :id
                RETURNING *
                """.trimIndent()
            )
                .bind("id", value.id)
                .bind("title", value.title)
                .bind("description", value.description)
                .bind("slug", value.slug)
                .map(CategoryEntity.Companion::map)
                .firstOrNull()
        }
    }

    override suspend fun delete(id: UUID): Boolean = jdbiQuery {
        jdbi.withHandle<Boolean, Exception> { handle ->
            handle.createUpdate(
                """
                DELETE FROM category WHERE id = :id
                """.trimIndent()
            )
                .bind("id", id)
                .execute() == 1
        }
    }
}
