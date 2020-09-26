package no.echokarriere.auth

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.ktor.application.Application
import io.ktor.http.Cookie
import io.ktor.server.testing.withTestApplication
import io.restassured.path.json.JsonPath
import no.echokarriere.module
import no.echokarriere.utils.DatabaseDescribeSpec
import no.echokarriere.utils.createAdminUserAndLogin
import no.echokarriere.utils.graphqlQuery

class AuthResolverSpec : DatabaseDescribeSpec({ body() })

private val body: DescribeSpec.() -> Unit = {
    lateinit var loginToken: String
    lateinit var cookie: Cookie

    describe("AuthResolver") {
        it("can login") {
            withTestApplication(Application::module) {
                val resp = createAdminUserAndLogin()
                val json = JsonPath(resp.content).setRootPath("data.login")

                val token = json.getString("token")
                token shouldNotBe null

                loginToken = token
                cookie = resp.cookies["echo_karriere_session"]!!
            }
        }

        it("can refresh tokens when authenticated") {
            withTestApplication(Application::module) {
                val call = graphqlQuery(
                    request = "{\"query\":\"mutation RefreshToken {\\n  refreshToken {\\n    token\\n  }\\n}\\n\",\"variables\":null,\"operationName\":\"RefreshToken\"}",
                    authCookie = cookie
                )

                val json = JsonPath(call.response.content).setRootPath("data.refreshToken")
                val token = json.getString("token")

                token shouldNotBe loginToken
            }
        }

        it("can not refresh tokens when not authenticated") {
            withTestApplication(Application::module) {
                val call = graphqlQuery(
                    request = "{\"query\":\"mutation RefreshToken {\\n  refreshToken {\\n    token\\n  }\\n}\\n\",\"variables\":null,\"operationName\":\"RefreshToken\"}",
                )

                val json = JsonPath(call.response.content).setRootPath("errors[0]")
                val message = json.getString("message")

                message shouldContain "Missing refresh token"

                val data = JsonPath(call.response.content).setRootPath("data")
                data.get<Any>() shouldBe null
            }
        }
    }
}
