package no.echokarriere.namespace

import java.util.UUID
import no.echokarriere.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

class NamespaceRepository {
    suspend fun selectAll(): List<NamespaceEntity> = dbQuery {
        Namespaces.selectAll().map { toNamespace(it) }
    }

    suspend fun selectOne(id: UUID): NamespaceEntity? = dbQuery {
        Namespaces
            .select { Namespaces.id eq id }
            .mapNotNull { toNamespace(it) }
            .singleOrNull()
    }

    suspend fun insert(data: NamespaceDTO): NamespaceEntity? {
        val generatedId = UUID.randomUUID()
        dbQuery {
            Namespaces
                .insert {
                    it[id] = generatedId
                    it[title] = data.title
                    it[description] = data.description
                    it[namespace] = data.title.toLowerCase()
                }
        }

        return this.selectOne(generatedId)
    }

    suspend fun update(id: UUID, data: NamespaceDTO): NamespaceEntity? {
        dbQuery {
            Namespaces.update({ Namespaces.id eq id }) {
                it[title] = data.title
                it[description] = data.description
                it[namespace] = data.title.toLowerCase()
            }
        }

        return this.selectOne(id)
    }

    suspend fun delete(id: UUID): Boolean = dbQuery {
        Namespaces.deleteWhere { Namespaces.id eq id } == 1
    }
}

private fun toNamespace(row: ResultRow): NamespaceEntity = NamespaceEntity(
    id = row[Namespaces.id],
    title = row[Namespaces.title],
    description = row[Namespaces.description],
    namespace = row[Namespaces.namespace]
)
