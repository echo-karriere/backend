package no.echokarriere.category

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

    suspend fun insert(input: CategoryEntity): CategoryEntity? {
        dbQuery {
            Categories
                .insert {
                    it[id] = input.id
                    it[title] = input.title
                    it[description] = input.description
                    it[slug] = input.slug
                }
        }

        return this.selectOne(input.id)
    }

    suspend fun update(input: CategoryEntity): CategoryEntity? {
        dbQuery {
            Categories.update({ Categories.id eq input.id }) {
                it[title] = input.title
                it[description] = input.description
                it[slug] = input.slug
                it[modifiedAt] = input.modifiedAt
            }
        }

        return this.selectOne(input.id)
    }

    suspend fun delete(id: UUID): Boolean = dbQuery {
        Categories.deleteWhere { Categories.id eq id } == 1
    }
}

private fun toNamespace(row: ResultRow): CategoryEntity = CategoryEntity.create(
    id = row[Categories.id],
    title = row[Categories.title],
    description = row[Categories.description],
    slug = row[Categories.slug],
    createdAt = row[Categories.createdAt],
    modifiedAt = row[Categories.modifiedAt]
)
