package no.echokarriere.no.echokarriere

import io.ktor.http.ContentType
import io.ktor.http.Cookie
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.encodeURLParameter
import io.ktor.server.testing.TestApplicationCall
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.TestApplicationResponse
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody

private fun buildCookie(cookie: Cookie): String = "${cookie.name}=${cookie.value.encodeURLParameter()}; "

fun TestApplicationEngine.graphqlQuery(request: String, authCookie: Cookie? = null): TestApplicationCall =
    this.handleRequest(HttpMethod.Post, "/graphql") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        authCookie?.let { addHeader("Cookie", buildCookie(authCookie)) }
        setBody(request)
    }

fun TestApplicationEngine.createAdminUserAndLogin(): TestApplicationResponse {
    graphqlQuery("{\"query\":\"mutation CreateUser {\\n  createUser(input: {email: \\\"admin@example.org\\\", name: \\\"Test User\\\", password: \\\"password123\\\", type: ADMIN}) {\\n\\t\\tactive\\n    email\\n    id\\n    name\\n    type\\n  }\\n}\\n\",\"variables\":null,\"operationName\":\"CreateUser\"}")
    val loginQuery =
        graphqlQuery("{\"query\":\"mutation Login {\\n  login(input: {email: \\\"admin@example.org\\\", password: \\\"password123\\\"}) {\\n    token\\n  }\\n}\\n\",\"variables\":null,\"operationName\":\"Login\"}")

    return loginQuery.response
}
