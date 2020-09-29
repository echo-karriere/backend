package no.echokarriere.category

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet
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

    companion object : RowMapper<CategoryEntity> {
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

        override fun map(rs: ResultSet?, ctx: StatementContext?): CategoryEntity? = rs?.let {
            create(
                id = UUID.fromString(it.getString("id")),
                title = it.getString("title"),
                description = it.getString("description"),
                slug = it.getString("slug"),
                createdAt = it.getTimestamp("created_at").toInstant(),
                modifiedAt = it.getTimestamp("modified_at")?.toInstant()
            )
        }
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
