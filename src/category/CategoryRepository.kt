package no.echokarriere.category

import no.echokarriere.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import java.time.Instant
import java.util.UUID

class CategoryRepository {
    suspend fun selectAll(): List<CategoryEntity> = dbQuery {
        Categories.selectAll().map { toNamespace(it) }
    }

    suspend fun selectOne(id: UUID): CategoryEntity? = dbQuery {
        Categories
            .select { Categories.id eq id }
            .mapNotNull { toNamespace(it) }
            .singleOrNull()
    }

    suspend fun insert(data: CategoryDTO): CategoryEntity? {
        val generatedId = UUID.randomUUID()
        dbQuery {
            Categories
                .insert {
                    it[id] = generatedId
                    it[title] = data.title
                    it[description] = data.description
                    it[slug] = data.title.toLowerCase()
                }
        }

        return this.selectOne(generatedId)
    }

    suspend fun update(id: UUID, data: CategoryDTO): CategoryEntity? {
        dbQuery {
            Categories.update({ Categories.id eq id }) {
                it[title] = data.title
                it[description] = data.description
                it[slug] = data.title.toLowerCase()
                it[modifiedAt] = Instant.now()
            }
        }

        return this.selectOne(id)
    }

    suspend fun delete(id: UUID): Boolean = dbQuery {
        Categories.deleteWhere { Categories.id eq id } == 1
    }
}

private fun toNamespace(row: ResultRow): CategoryEntity = CategoryEntity(
    id = row[Categories.id],
    title = row[Categories.title],
    description = row[Categories.description],
    slug = row[Categories.slug],
    createdAt = row[Categories.createdAt],
    modifiedAt = row[Categories.modifiedAt]
)
