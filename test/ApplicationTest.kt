package no.echokarriere

import io.kotest.assertions.ktor.shouldHaveStatus
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.string.shouldContain
import io.ktor.application.Application
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication

class ApplicationTest : DescribeSpec({
    describe("test root") {
        it("can get /playground") {
            withTestApplication(Application::testWithDatabase) {
                handleRequest(HttpMethod.Get, "/playground").apply {
                    response shouldHaveStatus HttpStatusCode.OK
                    response.content shouldContain "GraphQL Playground"
                }
            }
        }
    }
})
