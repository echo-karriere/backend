@file:Suppress("EXPERIMENTAL_API_USAGE")

package no.echokarriere.backend

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testRoot() {
        withTestApplication({
            module(testing = true, database = TestDatabase())
        }) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("{\"OK\":true}", response.content)
            }
        }
    }
}
