package no.echokarriere

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import kotlin.test.assertEquals
import org.spekframework.spek2.style.specification.describe

object ApplicationTest : AppSpek({
    describe("test root") {
        it("can get /playground") {
            withApp {
                handleRequest(HttpMethod.Get, "/playground").apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                    response.content?.contains("GraphQL Playground")?.let { assert(it) }
                }
            }
        }
    }
})
