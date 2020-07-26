@file:Suppress("EXPERIMENTAL_API_USAGE")

package no.echokarriere

import com.typesafe.config.ConfigFactory
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.config.HoconApplicationConfig
import io.ktor.features.AutoHeadResponse
import io.ktor.features.CORS
import io.ktor.features.Compression
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.deflate
import io.ktor.features.gzip
import io.ktor.features.minimumSize
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.jackson.jackson
import no.echokarriere.auth.AuthRepository
import no.echokarriere.auth.installAuth
import no.echokarriere.category.CategoryRepository
import no.echokarriere.configuration.Argon2Configuration
import no.echokarriere.configuration.DatabaseConfig
import no.echokarriere.configuration.DatabaseConfiguration
import no.echokarriere.graphql.installGraphQL
import no.echokarriere.user.UserRepository

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

val config = HoconApplicationConfig(ConfigFactory.load())

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
// TODO: Change testing to false for production
fun Application.module(testing: Boolean = true, database: DatabaseConfig = DatabaseConfiguration(config)) {
    val argon2Configuration = Argon2Configuration(testing)
    val categoryRepository = CategoryRepository()
    val userRepository = UserRepository(argon2Configuration)
    val authRepository = AuthRepository()

    install(Compression) {
        gzip {
            priority = 1.0
        }
        deflate {
            priority = 10.0
            minimumSize(1024)
        }
    }

    install(AutoHeadResponse)

    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        allowCredentials = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    install(DefaultHeaders) {
        header("Server", "Ktor")
    }

    install(ContentNegotiation) {
        jackson()
    }

    installExceptionHandling()

    installGraphQL(categoryRepository, userRepository)

    installAuth(testing, config, userRepository, authRepository, argon2Configuration)
}
