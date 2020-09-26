package no.echokarriere.category

import no.echokarriere.configuration.CrudDatabase
import java.util.UUID
import no.echokarriere.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

class CategoryRepository : CrudDatabase<CategoryEntity> {
    override suspend fun selectAll(): List<CategoryEntity> = dbQuery {
        Categories.selectAll().map { toNamespace(it) }
    }

    override suspend fun select(id: UUID): CategoryEntity? = dbQuery {
        Categories
            .select { Categories.id eq id }
            .mapNotNull { toNamespace(it) }
            .singleOrNull()
    }

    override suspend fun insert(value: CategoryEntity): CategoryEntity? {
        dbQuery {
            Categories
                .insert {
                    it[id] = value.id
                    it[title] = value.title
                    it[description] = value.description
                    it[slug] = value.slug
                }
        }

        return this.select(value.id)
    }

    override suspend fun update(value: CategoryEntity): CategoryEntity? {
        dbQuery {
            Categories.update({ Categories.id eq value.id }) {
                it[title] = value.title
                it[description] = value.description
                it[slug] = value.slug
                it[modifiedAt] = value.modifiedAt
            }
        }

        return this.select(value.id)
    }

    override suspend fun delete(id: UUID): Boolean = dbQuery {
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
