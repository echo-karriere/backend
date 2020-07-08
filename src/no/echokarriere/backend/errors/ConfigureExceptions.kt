package no.echokarriere.backend.errors

import io.ktor.application.call
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond

fun StatusPages.Configuration.configureExceptions() {
    exception<InvalidCredentialsException> { cause ->
        call.respond(
            HttpStatusCode.Unauthorized,
            ErrorResponse(
                HttpStatusCode.Unauthorized.value,
                "unauthorized",
                cause.message
            )
        )
    }
    exception<NotFoundException> { cause ->
        call.respond(
            HttpStatusCode.NotFound,
            ErrorResponse(
                HttpStatusCode.NotFound.value,
                "not_found",
                cause.message ?: "Resource not found"
            )
        )
    }
    exception<InternalServerErrorException> { cause ->
        call.respond(
            HttpStatusCode.InternalServerError,
            ErrorResponse(
                HttpStatusCode.InternalServerError.value,
                "internal_error",
                cause.message
            )
        )
    }
}