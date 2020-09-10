package no.echokarriere.category

import io.kotest.assertions.ktor.shouldHaveStatus
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import io.restassured.path.json.JsonPath
import no.echokarriere.TestDatabaseConfiguration
import no.echokarriere.testWithDatabase

class CategoryResolverSpec : DescribeSpec({
    val database = TestDatabaseConfiguration()

    describe("CategoryResolver") {
        it("can create a new category") {
            withTestApplication({ testWithDatabase { database } }) {
                val title = "Test Category"
                val description = "A test category"
                val slug = "test-category"

                val call = handleRequest(HttpMethod.Post, "/graphql") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody("{\"query\":\"mutation CreateCategory {\\n  createCategory(input: {title: \\\"$title\\\", description: \\\"$description\\\", slug: \\\"$slug\\\"}) {\\n    id\\n    title\\n    description\\n    slug\\n  }\\n}\\n\",\"variables\":null,\"operationName\":\"CreateCategory\"}")
                }

                call.response shouldHaveStatus HttpStatusCode.OK

                val json = JsonPath(call.response.content!!).setRootPath("data.createCategory")
                json.getString("id") shouldNotBe null
                json.getString("title") shouldBe title
                json.getString("description") shouldBe description
                json.getString("slug") shouldBe slug
            }
        }
    }
})
