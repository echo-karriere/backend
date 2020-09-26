package no.echokarriere.user

import io.kotest.assertions.ktor.shouldHaveStatus
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.ktor.application.Application
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.withTestApplication
import io.restassured.path.json.JsonPath
import no.echokarriere.module
import no.echokarriere.utils.DatabaseDescribeSpec
import no.echokarriere.utils.graphqlQuery
import java.util.UUID

class UserResolverSpec : DatabaseDescribeSpec({ body() })

private val body: DescribeSpec.() -> Unit = {
    lateinit var id: UUID

    describe("UserResolver") {
        it("can create a user") {
            withTestApplication(Application::module) {
                val call =
                    graphqlQuery("{\"query\":\"mutation CreateUser {\\n  createUser(input: {email: \\\"test@example.org\\\", name: \\\"Test User\\\", password: \\\"password123\\\", type: USER}) {\\n\\t\\tactive\\n    email\\n    id\\n    name\\n    type\\n  }\\n}\\n\",\"variables\":null,\"operationName\":\"CreateUser\"}")
                call.response shouldHaveStatus HttpStatusCode.OK

                val json = JsonPath(call.response.content).setRootPath("data.createUser")
                val localId = json.getUUID("id")
                localId shouldNotBe null
                id = localId

                json.getBoolean("active") shouldBe true
                json.getString("email") shouldBe "test@example.org"
                json.getString("name") shouldBe "Test User"
                json.getString("type") shouldBe "USER"
            }
        }

        it("can list users") {
            withTestApplication(Application::module) {
                val call =
                    graphqlQuery("{\"query\":\"query Users {\\n  users {\\n    active\\n    email\\n    id\\n    name\\n    type\\n  }\\n}\\n\",\"variables\":null,\"operationName\":\"Users\"}")
                call.response shouldHaveStatus HttpStatusCode.OK

                val json = JsonPath(call.response.content).setRootPath("data")
                json.getUUID("users[0].id") shouldBe id
                json.getString("users[0].email") shouldBe "test@example.org"
                json.getString("users[0].name") shouldBe "Test User"
                json.getString("users[0].type") shouldBe "USER"
            }
        }

        it("can get a single user") {
            withTestApplication(Application::module) {
                val call =
                    graphqlQuery("{\"query\":\"\\nquery User {\\n  user(id: \\\"$id\\\") {\\n    id\\n    name\\n    email\\n    type\\n  }\\n}\",\"variables\":null,\"operationName\":\"User\"}")
                call.response shouldHaveStatus HttpStatusCode.OK

                val json = JsonPath(call.response.content).setRootPath("data.user")
                json.getUUID("id") shouldBe id
                json.getString("email") shouldBe "test@example.org"
                json.getString("name") shouldBe "Test User"
                json.getString("type") shouldBe "USER"
            }
        }
    }
}
