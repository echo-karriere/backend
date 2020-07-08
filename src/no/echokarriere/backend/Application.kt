@file:Suppress("EXPERIMENTAL_API_USAGE")

package no.echokarriere.backend

import com.typesafe.config.ConfigFactory
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.Principal
import io.ktor.auth.session
import io.ktor.config.HoconApplicationConfig
import io.ktor.features.CORS
import io.ktor.features.Compression
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.StatusPages
import io.ktor.features.deflate
import io.ktor.features.gzip
import io.ktor.features.minimumSize
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.serialization.json
import io.ktor.server.netty.EngineMain
import io.ktor.sessions.SessionStorageMemory
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import kotlinx.serialization.Serializable
import no.echokarriere.backend.database.Database
import no.echokarriere.backend.database.IDatabase
import no.echokarriere.backend.errors.ErrorResponse
import no.echokarriere.backend.errors.InternalServerErrorException
import no.echokarriere.backend.errors.InvalidCredentialsException
import no.echokarriere.backend.errors.NotFoundException
import no.echokarriere.backend.routing.apiRouter
import java.util.Collections

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Serializable
class User(val name: String, val password: String)

val users: MutableMap<String, User> =
    Collections.synchronizedMap(listOf(User("test", "test")).associateBy { it.name }.toMutableMap())

@Serializable
class LoginRegister(val user: String, val password: String)

@Serializable
data class AuthSession(val token: String) : Principal

val config = HoconApplicationConfig(ConfigFactory.load())

@Suppress("unused") // Referenced in application.conf
@JvmOverloads
fun Application.module(testing: Boolean = false, database: IDatabase = Database(config)) {
    install(DefaultHeaders)

    install(Compression) {
        gzip {
            priority = 1.0
        }
        deflate {
            priority = 10.0
            minimumSize(1024)
        }
    }

    install(StatusPages) {
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

    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        method(HttpMethod.Get)
        method(HttpMethod.Post)
        header(HttpHeaders.Authorization)
        allowCredentials = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    install(Sessions) {
        cookie<AuthSession>("SESSION", storage = SessionStorageMemory())
    }

    install(Authentication) {
        session<AuthSession> {
            challenge {
                call.respond(HttpStatusCode.Unauthorized)
            }
            validate { session ->
                // TODO: do the actual validation
                return@validate AuthSession("token")
            }
        }
    }

    install(ContentNegotiation) {
        json()
    }

    routing {
        get("/") {
            call.respond(mapOf("OK" to true))
        }

        apiRouter(database)

        post("/login/") {
            val post = call.receive<LoginRegister>()
            val user = users.getOrPut(post.user) { User(post.user, post.password) }
            if (user.password != post.password) throw InvalidCredentialsException(
                "Invalid credentials"
            )
            call.respond(mapOf("token" to true))
        }
    }
}
