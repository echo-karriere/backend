package no.echokarriere.namespace

import java.util.UUID
import no.echokarriere.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

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

    fun insert(namespace: NamespaceEntity): NamespaceEntity = TODO()

    fun update(id: Int, namespace: NamespaceEntity): NamespaceEntity? = TODO()

    fun delete(id: Int): Int = TODO()
}

private fun toNamespace(row: ResultRow): NamespaceEntity = NamespaceEntity(
    id = row[Namespaces.id],
    title = row[Namespaces.title],
    description = row[Namespaces.description],
    namespace = row[Namespaces.namespace]
)
