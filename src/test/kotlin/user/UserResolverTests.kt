package no.echokarriere.user

import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.withTestApplication
import io.restassured.path.json.JsonPath
import kotlinx.coroutines.runBlocking
import no.echokarriere.module
import no.echokarriere.utils.DatabaseExtension
import no.echokarriere.utils.TestDatabase
import no.echokarriere.utils.graphqlQuery
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
class UserResolverTests : TestDatabase() {
    lateinit var id: UUID

    @Test
    @Order(1)
    fun `can create a user`() = runBlocking {
        withTestApplication({ module(jdbi()) }) {
            val call =
                graphqlQuery("{\"query\":\"mutation CreateUser {\\n  createUser(input: {email: \\\"test@example.org\\\", name: \\\"Test User\\\", password: \\\"password123\\\", type: USER}) {\\n\\t\\tactive\\n    email\\n    id\\n    name\\n    type\\n  }\\n}\\n\",\"variables\":null,\"operationName\":\"CreateUser\"}")
            assertEquals(HttpStatusCode.OK, call.response.status()!!)

            val json = JsonPath(call.response.content).setRootPath("data.createUser")
            val localId = json.getUUID("id")
            assertNotNull(localId)
            id = localId

            println(id)

            assertEquals(true, json.getBoolean("active"))
            assertEquals("test@example.org", json.getString("email"))
            assertEquals("Test User", json.getString("name"))
            assertEquals("USER", json.getString("type"))
        }
    }

    @Test
    @Order(2)
    fun `can list users`() = runBlocking {
        withTestApplication({ module(jdbi()) }) {
            val call =
                graphqlQuery("{\"query\":\"query Users {\\n  users {\\n    active\\n    email\\n    id\\n    name\\n    type\\n  }\\n}\\n\",\"variables\":null,\"operationName\":\"Users\"}")
            assertEquals(HttpStatusCode.OK, call.response.status()!!)

            println(call.response.content)

            val json = JsonPath(call.response.content).setRootPath("data")
            assertEquals(id, json.getUUID("users[0].id"))
            assertEquals("test@example.org", json.getString("users[0].email"))
            assertEquals("Test User", json.getString("users[0].name"))
            assertEquals("USER", json.getString("users[0].type"))
        }
    }

    @Test
    @Order(3)
    fun `can get a single user`() = runBlocking {
        withTestApplication({ module(jdbi()) }) {
            val call =
                graphqlQuery("{\"query\":\"\\nquery User {\\n  user(id: \\\"$id\\\") {\\n    id\\n    name\\n    email\\n    type\\n  }\\n}\",\"variables\":null,\"operationName\":\"User\"}")
            assertEquals(HttpStatusCode.OK, call.response.status()!!)

            val json = JsonPath(call.response.content).setRootPath("data.user")
            assertEquals(id, json.getUUID("id"))
            assertEquals("test@example.org", json.getString("email"))
            assertEquals("Test User", json.getString("name"))
            assertEquals("USER", json.getString("type"))
        }
    }
}
