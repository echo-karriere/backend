@file:Suppress("EXPERIMENTAL_API_USAGE")

package no.echokarriere.backend

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import kotlin.test.assertEquals
import kotlinx.serialization.json.Json
import no.echokarriere.backend.entities.Namespace
import org.junit.Test

class NamespaceTest {
    @Test
    fun `insert and get namespace`() {
        withTestApplication({
            module(testing = true, database = TestDatabase())
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
        }
    }
}
