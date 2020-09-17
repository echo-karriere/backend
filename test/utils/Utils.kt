package no.echokarriere.utils

import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.testing.TestApplicationCall
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody

fun TestApplicationEngine.graphqlQuery(request: String): TestApplicationCall =
    this.handleRequest(HttpMethod.Post, "/graphql") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        setBody(request)
    }
