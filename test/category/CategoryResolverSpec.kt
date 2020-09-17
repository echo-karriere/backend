package no.echokarriere.category

import io.kotest.assertions.ktor.shouldHaveStatus
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.ktor.application.Application
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import io.restassured.path.json.JsonPath
import no.echokarriere.module
import no.echokarriere.utils.DatabaseDescribeSpec
import java.util.UUID

class CategoryResolverSpec : DatabaseDescribeSpec({
    lateinit var id: UUID

    describe("CategoryResolver") {
        it("can create a new category") {
            withTestApplication(Application::module) {
                val title = "Test Category"
                val description = "A test category"
                val slug = "test-category"

                val call = handleRequest(HttpMethod.Post, "/graphql") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody("{\"query\":\"mutation CreateCategory {\\n  createCategory(input: {title: \\\"$title\\\", description: \\\"$description\\\", slug: \\\"$slug\\\"}) {\\n    id\\n    title\\n    description\\n    slug\\n  }\\n}\\n\",\"variables\":null,\"operationName\":\"CreateCategory\"}")
                }

                call.response shouldHaveStatus HttpStatusCode.OK

                val json = JsonPath(call.response.content!!).setRootPath("data.createCategory")
                val localId = json.getString("id")
                localId shouldNotBe null
                id = UUID.fromString(localId)
                json.getString("title") shouldBe title
                json.getString("description") shouldBe description
                json.getString("slug") shouldBe slug
            }
        }

        it("can update an existing category") {
            withTestApplication(Application::module) {
                val title = "Updated category"
                val description = "New description"
                val slug = "updated-category"

                val update = handleRequest(HttpMethod.Post, "/graphql") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody("{\"query\":\"mutation UpdateCategory {\\n  updateCategory(input: {id: \\\"$id\\\", category: {title: \\\"$title\\\", description: \\\"$description\\\", slug: \\\"$slug\\\"}}) {\\n    title\\n    description\\n    slug\\n  }\\n}\\n\",\"variables\":null,\"operationName\":\"UpdateCategory\"}")
                }

                val json = JsonPath(update.response.content!!).setRootPath("data.updateCategory")
                json.getString("title") shouldBe title
                json.getString("description") shouldBe description
                json.getString("slug") shouldBe slug
            }
        }

        it("can delete a category") {
            withTestApplication(Application::module) {
                val delete = handleRequest(HttpMethod.Post, "/graphql") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody("{\"query\":\"mutation DeleteCategory {\\n  deleteCategory(id:\\\"$id\\\")\\n}\\n\",\"variables\":null,\"operationName\":\"DeleteCategory\"}")
                }

                val json = JsonPath(delete.response.content!!).setRootPath("data.deleteCategory")
                json.getBoolean("") shouldBe true
            }
        }

        it("cannot delete a previously deleted category") {
            withTestApplication(Application::module) {
                val delete = handleRequest(HttpMethod.Post, "/graphql") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody("{\"query\":\"mutation DeleteCategory {\\n  deleteCategory(id:\\\"$id\\\")\\n}\\n\",\"variables\":null,\"operationName\":\"DeleteCategory\"}")
                }

                val json = JsonPath(delete.response.content!!).setRootPath("data.deleteCategory")
                json.getBoolean("") shouldBe false
            }
        }
    }
})
