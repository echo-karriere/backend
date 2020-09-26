package no.echokarriere.category

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.timestamp
import java.time.Instant
import java.util.UUID

data class Category(private val entity: CategoryEntity) {
    val id = entity.id
    val title = entity.title
    val description = entity.description
    val slug = entity.slug
}

class CategoryEntity private constructor(
    val id: UUID,
    val title: String,
    val description: String,
    val slug: String,
    val createdAt: Instant,
    val modifiedAt: Instant?
) {
    private fun update(
        id: UUID = this.id,
        title: String = this.title,
        description: String = this.description,
        slug: String = this.slug,
        createdAt: Instant = this.createdAt
    ): CategoryEntity = CategoryEntity(
        id = id,
        title = title,
        description = description,
        slug = slug,
        createdAt = createdAt,
        modifiedAt = Instant.now()
    )

    fun updateDetails(
        title: String,
        description: String,
        slug: String
    ) = update(
        title = title,
        description = description,
        slug = slug
    )

    companion object {
        fun create(
            id: UUID = UUID.randomUUID(),
            title: String,
            description: String,
            slug: String,
            createdAt: Instant = Instant.now(),
            modifiedAt: Instant? = null
        ): CategoryEntity = CategoryEntity(
            id = id,
            title = title,
            description = description,
            slug = slug,
            createdAt = createdAt,
            modifiedAt = modifiedAt
        )
    }
}

data class CreateCategoryInput(
    val title: String,
    val description: String,
    val slug: String
) {
    fun createEntity(): CategoryEntity = CategoryEntity.create(
        title = title,
        description = description,
        slug = slug
    )
}

data class UpdateCategoryInput(
    val id: UUID,
    val category: CategoryData
) {
    data class CategoryData(
        val title: String,
        val description: String,
        val slug: String
    )
}

object Categories : Table("category") {
    val id = uuid("id")
    val title = text("title")
    val description = text("description")
    val slug = text("slug")
    val createdAt = timestamp("created_at")
    val modifiedAt = timestamp("modified_at").nullable()
    override val primaryKey = PrimaryKey(id)
}
