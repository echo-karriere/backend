package no.echokarriere.category

import java.util.UUID

@Suppress("unused") // Used by GraphQL via reflection
class CategoryQueryResolver(
    private val categoryRepository: CategoryRepository
) {
    suspend fun categories(): List<Category> {
        return categoryRepository.selectAll().map { Category(it) }
    }

    suspend fun categoryById(id: UUID): Category? {
        return categoryRepository.select(id)?.let { Category(it) }
    }
}
