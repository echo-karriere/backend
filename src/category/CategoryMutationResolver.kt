package no.echokarriere.category

import java.util.UUID

@Suppress("unused") // Used by GraphQL via reflection
class CategoryMutationResolver(
    private val categoryRepository: CategoryRepository
) {
    suspend fun createCategory(input: CreateCategoryInput): CategoryEntity? = categoryRepository.insert(input)
    suspend fun deleteCategory(id: UUID): Boolean = categoryRepository.delete(id)
    suspend fun updateCategory(input: UpdateCategoryInput): CategoryEntity? = categoryRepository.update(input)
}
