package no.echokarriere.category

import java.util.UUID
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.timestamp
import java.time.Instant

data class CategoryEntity(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val description: String,
    val slug: String,
    val createdAt: Instant,
    val modifiedAt: Instant?
)

data class CategoryDTO(
    val title: String,
    val description: String
)

object Categories : Table("category") {
    val id = uuid("id")
    val title = text("title")
    val description = text("description")
    val slug = text("slug")
    val createdAt = timestamp("created_at")
    val modifiedAt = timestamp("modified_at").nullable()
    override val primaryKey = PrimaryKey(id)
}
