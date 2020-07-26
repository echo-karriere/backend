package no.echokarriere

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import no.echokarriere.auth.UnauthorizedException

data class ErrorResponse(
    val status: Int,
    val code: String,
    val message: String,
    val type: String = "error"
)

fun Application.installExceptionHandling() {
    install(StatusPages) {
        exception<UnauthorizedException> { cause ->
            call.respond(
                HttpStatusCode.Unauthorized,
                ErrorResponse(
                    status = HttpStatusCode.Unauthorized.value,
                    code = HttpStatusCode.Unauthorized.description,
                    message = cause.message
                )
            )
        }
    }
}
