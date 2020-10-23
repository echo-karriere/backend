package no.echokarriere.form

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
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@ExtendWith(DatabaseExtension::class)
class FormRepositoryTests : TestDatabase() {
    private val formRepository = FormRepository(jooq())
    private val formId = UUID.randomUUID()

    @Test
    @Order(1)
    fun `create a new form container`() = runBlocking {
        val entity = FormEntity.create(
            id = formId,
            title = "Test Form",
            description = "A form for all test responses"
        )

        val actual = formRepository.insert(entity)

        assertNotNull(actual)
        assertEquals("Test Form", actual.title)
        assertEquals("A form for all test responses", actual.description)
    }

    @Test
    @Order(2)
    fun `get form container by id`() = runBlocking {
        val actual = formRepository.select(formId)

        assertNotNull(actual)
        assertEquals(formId, actual.id)
        assertEquals("Test Form", actual.title)
    }

    @Test
    @Order(3)
    fun `selectAll contains form container`() = runBlocking {
        val list = formRepository.selectAll()
        val actual = list.find { it.id == formId }

        assertNotNull(actual)
        assertEquals(formId, actual.id)
        assertEquals("Test Form", actual.title)
    }

    @Test
    @Order(4)
    fun `update a form entity`() = runBlocking {
        val entity = formRepository.select(formId)!!
        val expected = entity.updateDetails(
            title = "A new test form",
            description = "A container for the new tests"
        )

        val actual = formRepository.update(expected)

        assertNotNull(actual)
        assertEquals(formId, actual.id)
        assertEquals(expected.id, actual.id)
        assertEquals("A new test form", actual.title)
        assertEquals("A container for the new tests", actual.description)
    }

    @Test
    @Order(5)
    fun `delete a form entity`() = runBlocking {
        var actual = formRepository.delete(formId)
        assertTrue(actual)

        actual = formRepository.delete(formId)
        assertFalse(actual)

        val entity = formRepository.select(formId)
        assertNull(entity)
    }
}
