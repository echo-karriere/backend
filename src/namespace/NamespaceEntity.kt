package no.echokarriere.namespace

import java.util.UUID
import org.jetbrains.exposed.sql.Table

data class NamespaceEntity(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val description: String,
    val namespace: String
)

object Namespaces : Table("namespace") {
    val id = uuid("id")
    val title = text("title")
    val description = text("description")
    val namespace = text("namespace")
    override val primaryKey = PrimaryKey(id)
}
