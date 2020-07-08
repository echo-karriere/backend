@file:Suppress("EXPERIMENTAL_API_USAGE")

package no.echokarriere.backend

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import kotlinx.serialization.json.Json
import no.echokarriere.backend.entities.Namespace
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class NamespaceTest {
    private val db = TestDatabase()

    @Test
    fun `get whole namespace`() {
        withTestApplication({
            module(testing = true, database = db)
        }) {
            handleRequest(HttpMethod.Get, "/api/namespace").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("[]", response.content)
            }

            handleRequest(HttpMethod.Post, "/api/namespace") {
                addHeader("Content-Type", "application/json")
                setBody(Json.stringify(Namespace.serializer(), Namespace(title = "Test", description = "Some strings", namespace = "test")))
            }

            handleRequest(HttpMethod.Post, "/api/namespace") {
                addHeader("Content-Type", "application/json")
                setBody(Json.stringify(Namespace.serializer(), Namespace(title = "Something", description = "Stuff go here", namespace = "something")))
            }

            val expected = "[{\"id\":1,\"title\":\"Test\",\"description\":\"Some strings\",\"namespace\":\"test\"},{\"id\":2,\"title\":\"Something\",\"description\":\"Stuff go here\",\"namespace\":\"something\"}]"
            handleRequest(HttpMethod.Get, "/api/namespace").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(expected, response.content)
            }
        }
    }

    @Test
    fun `insert, get and put namespace`() {
        withTestApplication({
            module(testing = true, database = db)
        }) {
            val namespace = Namespace(title = "Test", description = "Test namespace", namespace = "test")
            val expected = "{\"id\":1,\"title\":\"Test\",\"description\":\"Test namespace\",\"namespace\":\"test\"}"

            handleRequest(HttpMethod.Post, "/api/namespace") {
                addHeader("Content-Type", "application/json")
                setBody(Json.stringify(Namespace.serializer(), namespace))
            }.apply {
                assertEquals(HttpStatusCode.Created, response.status())
                assertEquals(expected, response.content)
            }

            handleRequest(HttpMethod.Get, "/api/namespace/1").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(expected, response.content)
            }

            val update = Namespace(title = "About", description = "About things", namespace = "about")
            val expectedUpdate = "{\"id\":1,\"title\":\"About\",\"description\":\"About things\",\"namespace\":\"about\"}"
            handleRequest(HttpMethod.Put, "/api/namespace/1") {
                addHeader("Content-Type", "application/json")
                setBody(Json.stringify(Namespace.serializer(), update))
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(expectedUpdate, response.content)
            }

            handleRequest(HttpMethod.Get, "/api/namespace/1").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertNotEquals(expected, response.content)
                assertEquals(expectedUpdate, response.content)
            }
        }
    }

    @Test
    fun `insert and delete`() {
        withTestApplication({
            module(testing = true, database = db)
        }) {
            handleRequest(HttpMethod.Post, "/api/namespace") {
                addHeader("Content-Type", "application/json")
                setBody(Json.stringify(Namespace.serializer(), Namespace(title = "A", description = "B", namespace = "C")))
            }

            handleRequest(HttpMethod.Delete, "/api/namespace/1").apply {
                assertEquals(HttpStatusCode.NoContent, response.status())
            }

            handleRequest(HttpMethod.Delete, "/api/namespace/1").apply {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
        }
    }
}
