package no.echokarriere.category

import java.util.UUID

@Suppress("unused") // Used by GraphQL via reflection
class CategoryQueryResolver(
    private val categoryRepository: CategoryRepository
) {
    suspend fun categories(): List<CategoryEntity> = categoryRepository.selectAll()
    suspend fun categoryById(id: UUID): CategoryEntity? = categoryRepository.selectOne(id)
}

@Suppress("unused") // Used by GraphQL via reflection
class CategoryMutationResolver(
    private val categoryRepository: CategoryRepository
) {
    suspend fun createCategory(data: CategoryDTO): CategoryEntity? = categoryRepository.insert(data)
    suspend fun deleteCategory(id: UUID): Boolean = categoryRepository.delete(id)
    suspend fun updateCategory(id: UUID, data: CategoryDTO): CategoryEntity? = categoryRepository.update(id, data)
}
