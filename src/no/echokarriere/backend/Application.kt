@file:Suppress("EXPERIMENTAL_API_USAGE")

package no.echokarriere.backend

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.typesafe.config.ConfigFactory
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.jwt.jwt
import io.ktor.config.HoconApplicationConfig
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
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
import java.util.Collections
import kotlinx.serialization.Serializable
import no.echokarriere.backend.database.Database
import no.echokarriere.backend.database.IDatabase
import no.echokarriere.backend.errors.ErrorResponse
import no.echokarriere.backend.errors.InternalServerErrorException
import no.echokarriere.backend.errors.InvalidCredentialsException
import no.echokarriere.backend.errors.NotFoundException
import no.echokarriere.backend.routing.apiRouter

fun main(args: Array<String>): Unit = EngineMain.main(args)

open class SimpleJWT(private val secret: String) {
    private val algorithm = Algorithm.HMAC256(secret)
    val verifier: JWTVerifier = JWT.require(algorithm).build()
    fun sign(name: String): String = JWT.create().withClaim("name", name).sign(algorithm)
}

@Serializable
class User(val name: String, val password: String)

val users: MutableMap<String, User> =
    Collections.synchronizedMap(listOf(User("test", "test")).associateBy { it.name }.toMutableMap())

@Serializable
class LoginRegister(val user: String, val password: String)

val config = HoconApplicationConfig(ConfigFactory.load())

@Suppress("unused") // Referenced in application.conf
@JvmOverloads
fun Application.module(testing: Boolean = false, database: IDatabase = Database(config)) {
    val simpleJWT = SimpleJWT("very-secret-ssh")

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

    install(Authentication) {
        jwt {
            verifier(simpleJWT.verifier)
            validate {
                UserIdPrincipal(it.payload.getClaim("name").asString())
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

        post("/login-register") {
            val post = call.receive<LoginRegister>()
            val user = users.getOrPut(post.user) { User(post.user, post.password) }
            if (user.password != post.password) throw InvalidCredentialsException(
                "Invalid credentials"
            )
            call.respond(mapOf("token" to simpleJWT.sign(user.name)))
        }
    }
}
