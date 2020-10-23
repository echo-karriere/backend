package no.echokarriere.category

import java.time.OffsetDateTime
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
    val createdAt: OffsetDateTime,
    val modifiedAt: OffsetDateTime?
) {
    private fun update(
        id: UUID = this.id,
        title: String = this.title,
        description: String = this.description,
        slug: String = this.slug,
        createdAt: OffsetDateTime = this.createdAt
    ): CategoryEntity = CategoryEntity(
        id = id,
        title = title,
        description = description,
        slug = slug,
        createdAt = createdAt,
        modifiedAt = OffsetDateTime.now()
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
            createdAt: OffsetDateTime = OffsetDateTime.now(),
            modifiedAt: OffsetDateTime? = null
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
