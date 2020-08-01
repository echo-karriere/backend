package no.echokarriere.category

import java.util.UUID

@Suppress("unused") // Used by GraphQL via reflection
class CategoryQueryResolver(
    private val categoryRepository: CategoryRepository
) {
    suspend fun categories(): List<CategoryEntity> = categoryRepository.selectAll()
    suspend fun categoryById(id: UUID): CategoryEntity? = categoryRepository.selectOne(id)
}
