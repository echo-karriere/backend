package no.echokarriere.auth

import io.ktor.http.Cookie
import io.ktor.server.testing.withTestApplication
import io.restassured.path.json.JsonPath
import kotlinx.coroutines.runBlocking
import no.echokarriere.module
import no.echokarriere.utils.DatabaseExtension
import no.echokarriere.utils.TestDatabase
import no.echokarriere.no.echokarriere.createAdminUserAndLogin
import no.echokarriere.no.echokarriere.graphqlQuery
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@ExtendWith(DatabaseExtension::class)
class AuthResolverTests : TestDatabase() {
    lateinit var loginToken: String
    lateinit var cookie: Cookie

    @Test
    @Order(1)
    fun `can login`() = runBlocking {
        withTestApplication({ module(jdbi()) }) {
            val resp = createAdminUserAndLogin()
            val json = JsonPath(resp.content).setRootPath("data.login")

            val token = json.getString("token")
            assertNotNull(token)

            loginToken = token
            cookie = resp.cookies["echo_karriere_session"]!!
        }
    }

    @Test
    @Order(2)
    fun `can refresh tokens when authenticated`() = runBlocking {
        withTestApplication({ module(jdbi()) }) {
            // Required because JWT generates "duplicate" tokens if they are both created
            // with the same payload and within the same second, this was a bitch to figure out
            Thread.sleep(1000)
            val call = graphqlQuery(
                request = "{\"query\":\"mutation RefreshToken {\\n  refreshToken {\\n    token\\n  }\\n}\\n\",\"variables\":null,\"operationName\":\"RefreshToken\"}",
                authCookie = cookie
            )

            val json = JsonPath(call.response.content).setRootPath("data.refreshToken")
            val token = json.getString("token")

            assertNotEquals(token.trim(), loginToken.trim())
        }
    }

    @Test
    @Order(3)
    fun `can not refresh tokens when not authenticated`() = runBlocking {
        withTestApplication({ module(jdbi()) }) {
            val call = graphqlQuery(
                request = "{\"query\":\"mutation RefreshToken {\\n  refreshToken {\\n    token\\n  }\\n}\\n\",\"variables\":null,\"operationName\":\"RefreshToken\"}",
            )

            val json = JsonPath(call.response.content).setRootPath("errors[0]")
            val message = json.getString("message")

            assertTrue(message.contains("Missing refresh token"))

            val data = JsonPath(call.response.content).setRootPath("data")
            assertNull(data.get())
        }
    }
}
