package no.echokarriere.category

import kotlinx.coroutines.runBlocking
import no.echokarriere.utils.DatabaseExtension
import no.echokarriere.utils.TestDatabase
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.extension.ExtendWith
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@ExtendWith(DatabaseExtension::class)
class CategoryRepositoryTests : TestDatabase() {
    private val categoryRepository = CategoryRepository(jdbi())
    private val categoryId = UUID.randomUUID()

    @Test
    @Order(1)
    fun `can create a new category`() = runBlocking {
        val title = "Test Category"
        val description = "A test category"
        val slug = "test-category"

        val category = CategoryEntity.create(
            id = categoryId,
            title,
            description,
            slug
        )

        val created = categoryRepository.insert(category)

        assertNotNull(created)
        assertEquals(categoryId, created.id)
        assertEquals(title, created.title)
        assertEquals(description, created.description)
        assertEquals(slug, created.slug)
    }

    @Test
    @Order(2)
    fun `can find created entity`() = runBlocking {
        val found = categoryRepository.select(categoryId)

        assertNotNull(found)
        assertEquals(categoryId, found.id)
    }

    @Test
    @Order(3)
    fun `contains created entity`() = runBlocking {
        val all = categoryRepository.selectAll()
        val found = all.find { it.id == categoryId }

        assertNotNull(found)
        assertEquals(categoryId, found.id)
    }

    @Test
    @Order(4)
    fun `can update an entity`() = runBlocking {
        val newTitle = "Better Category"
        val newDescription = "The bestest category"
        val newSlug = "the-best"

        val old = categoryRepository.select(categoryId)!!

        val new = old.updateDetails(
            title = newTitle,
            description = newDescription,
            slug = newSlug
        )

        val updated = categoryRepository.update(new)

        assertNotNull(updated)
        assertEquals(categoryId, updated.id)
        assertEquals(newTitle, updated.title)
        assertEquals(newDescription, updated.description)
        assertEquals(newSlug, updated.slug)
    }

    @Test
    @Order(5)
    fun `can delete an entity`() = runBlocking {
        val deleted = categoryRepository.delete(categoryId)

        assertTrue(deleted)

        val doubleDeleted = categoryRepository.delete(categoryId)
        assertTrue(!doubleDeleted)

        val itIsGone = categoryRepository.select(categoryId)
        assertNull(itIsGone)
    }
}
