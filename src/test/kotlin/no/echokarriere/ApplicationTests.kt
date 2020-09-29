package no.echokarriere

import io.ktor.application.Application
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ApplicationTests {
    @Test
    fun `can get playground`() {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Get, "/playground").apply {
                assertEquals(HttpStatusCode.OK, response.status()!!)
                response.content?.contains("GraphQL Playground")?.let { assertTrue(it) }
            }
        }
    }
}
