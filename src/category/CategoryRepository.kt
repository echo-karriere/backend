package no.echokarriere.category

import java.time.Instant
import java.util.UUID
import no.echokarriere.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

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

    suspend fun insert(input: CreateCategoryInput): CategoryEntity? {
        val generatedId = UUID.randomUUID()
        dbQuery {
            Categories
                .insert {
                    it[id] = generatedId
                    it[title] = input.title
                    it[description] = input.description
                    it[slug] = input.title.toLowerCase()
                }
        }

        return this.selectOne(generatedId)
    }

    suspend fun update(input: UpdateCategoryInput): CategoryEntity? {
        dbQuery {
            Categories.update({ Categories.id eq input.id }) {
                it[title] = input.category.title
                it[description] = input.category.description
                it[slug] = input.category.title.toLowerCase()
                it[modifiedAt] = Instant.now()
            }
        }

        return this.selectOne(input.id)
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
