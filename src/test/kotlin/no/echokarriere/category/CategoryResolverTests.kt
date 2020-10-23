package no.echokarriere.category

import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.withTestApplication
import io.restassured.path.json.JsonPath
import kotlinx.coroutines.runBlocking
import no.echokarriere.module
import no.echokarriere.no.echokarriere.graphqlQuery
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

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@ExtendWith(DatabaseExtension::class)
class CategoryResolverTests : TestDatabase() {
    lateinit var id: UUID

    @Test
    @Order(1)
    fun `can create a new category`() = runBlocking {
        withTestApplication({ module(jdbi(), jooq()) }) {
            val title = "Test Category"
            val description = "A test category"
            val slug = "test-category"

            val call =
                graphqlQuery("{\"query\":\"mutation CreateCategory {\\n  createCategory(input: {title: \\\"$title\\\", description: \\\"$description\\\", slug: \\\"$slug\\\"}) {\\n    id\\n    title\\n    description\\n    slug\\n  }\\n}\\n\",\"variables\":null,\"operationName\":\"CreateCategory\"}")
            assertEquals(HttpStatusCode.OK, call.response.status()!!)

            val json = JsonPath(call.response.content!!).setRootPath("data.createCategory")
            val localId = json.getUUID("id")
            assertNotNull(localId)
            id = localId

            assertEquals(title, json.getString("title"))
            assertEquals(description, json.getString("description"))
            assertEquals(slug, json.getString("slug"))
        }
    }

    @Test
    @Order(2)
    fun `can update an existing category`() = runBlocking {
        withTestApplication({ module(jdbi(), jooq()) }) {
            val title = "Updated category"
            val description = "New description"
            val slug = "updated-category"

            val update =
                graphqlQuery("{\"query\":\"mutation UpdateCategory {\\n  updateCategory(input: {id: \\\"$id\\\", category: {title: \\\"$title\\\", description: \\\"$description\\\", slug: \\\"$slug\\\"}}) {\\n    title\\n    description\\n    slug\\n  }\\n}\\n\",\"variables\":null,\"operationName\":\"UpdateCategory\"}")

            val json = JsonPath(update.response.content!!).setRootPath("data.updateCategory")
            assertEquals(title, json.getString("title"))
            assertEquals(description, json.getString("description"))
            assertEquals(slug, json.getString("slug"))
        }
    }

    @Test
    @Order(3)
    fun `can delete a category`() = runBlocking {
        withTestApplication({ module(jdbi(), jooq()) }) {
            val delete =
                graphqlQuery("{\"query\":\"mutation DeleteCategory {\\n  deleteCategory(id:\\\"$id\\\")\\n}\\n\",\"variables\":null,\"operationName\":\"DeleteCategory\"}")

            val json = JsonPath(delete.response.content!!).setRootPath("data.deleteCategory")
            assertEquals(true, json.getBoolean(""))
        }
    }

    @Test
    @Order(4)
    fun `cannot delete a previously deleted category`() = runBlocking {
        withTestApplication({ module(jdbi(), jooq()) }) {
            val delete =
                graphqlQuery("{\"query\":\"mutation DeleteCategory {\\n  deleteCategory(id:\\\"$id\\\")\\n}\\n\",\"variables\":null,\"operationName\":\"DeleteCategory\"}")

            val json = JsonPath(delete.response.content!!).setRootPath("data.deleteCategory")
            assertEquals(false, json.getBoolean(""))
        }
    }
}
