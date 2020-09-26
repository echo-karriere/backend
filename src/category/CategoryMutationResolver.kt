package no.echokarriere.category

import io.ktor.features.NotFoundException
import java.util.UUID

@Suppress("unused") // Used by GraphQL via reflection
class CategoryMutationResolver(
    private val categoryRepository: CategoryRepository
) {
    suspend fun createCategory(input: CreateCategoryInput): Category? {
        val category = input.createEntity()
        val created = categoryRepository.insert(category)

        return created?.let { Category(it) }
    }

    suspend fun deleteCategory(id: UUID): Boolean = categoryRepository.delete(id)

    suspend fun updateCategory(input: UpdateCategoryInput): Category? {
        val category = categoryRepository.select(input.id) ?: throw NotFoundException("Category $input.id not found")

        val updated = category.updateDetails(
            title = input.category.title,
            description = input.category.description,
            slug = input.category.slug
        )

        return categoryRepository.update(updated)?.let { Category(it) }
    }
}
