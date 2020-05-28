package no.echokarriere.backend

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.jwt
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.json
import io.ktor.server.netty.EngineMain
import kotlinx.serialization.Serializable
import java.lang.RuntimeException
import java.util.Collections
import java.util.UUID

fun main(args: Array<String>): Unit = EngineMain.main(args)

open class SimpleJWT(private val secret: String) {
    private val algorithm = Algorithm.HMAC256(secret)
    val verifier: JWTVerifier = JWT.require(algorithm).build()
    fun sign(name: String): String = JWT.create().withClaim("name", name).sign(algorithm)
}

@Serializable
class User(val name: String, val password: String)

val users: MutableMap<String, User> = Collections.synchronizedMap(listOf(User("test", "test")).associateBy { it.name }.toMutableMap())

@Serializable
class LoginRegister(val user: String, val password: String)

@Serializable
data class Snippet(val user: String, val id: Int, val text: String)

@Serializable
data class PostSnippet(val snippet: Text) {
    @Serializable
    data class Text(val text: String)
}

val snippets: MutableList<Snippet> = Collections.synchronizedList(mutableListOf(
    Snippet("test", 1, "hello"),
    Snippet("test",2, "world")
))

@Serializable
class InvalidCredentialsException(override val message: String) : RuntimeException(message)

@Suppress("unused") // Referenced in application.conf
@JvmOverloads
fun Application.module(testing: Boolean = false) {
    val simpleJWT = SimpleJWT("very-secret-ssh")

    install(StatusPages) {
        exception<InvalidCredentialsException> { exception ->
            call.respond(HttpStatusCode.Unauthorized, mapOf("OK" to "false", "error" to (exception.message ?: "")))
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
        route("/") {
            get {
                call.respond(mapOf("snippets" to synchronized(snippets) { snippets.toList() }))
            }
            authenticate {
                post {
                    val post = call.receive<PostSnippet>()
                    val principal = call.principal<UserIdPrincipal>() ?: error("No principal")
                    snippets += Snippet(
                        principal.name,
                        snippets.size + 1,
                        post.snippet.text
                    )
                    call.respond(mapOf("OK" to true))
                }
                delete("{id}") {
                    val id: Int = call.parameters["id"]?.toInt() ?: error("ID went wrong")
                    snippets.removeIf { it.id == id}
                    call.respond(mapOf("OK" to true))
                }
            }
        }
        post("/login-register") {
            val post = call.receive<LoginRegister>()
            val user = users.getOrPut(post.user) { User(post.user, post.password)}
            if (user.password != post.password) throw InvalidCredentialsException("Invalid credentials")
            call.respond(mapOf("token" to simpleJWT.sign(user.name)))
        }
    }
}
