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
import no.echokarriere.auth.jwt.JWTConfiguration
import no.echokarriere.category.CategoryRepository
import no.echokarriere.configuration.Argon2Configuration
import no.echokarriere.configuration.DatabaseConfigurator
import no.echokarriere.graphql.installGraphQL
import no.echokarriere.user.UserRepository

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val applicationConfiguration = HoconApplicationConfig(ConfigFactory.load())
    val argon2Configuration = Argon2Configuration(applicationConfiguration)
    val jwtConfiguration = JWTConfiguration(applicationConfiguration)

    val jdbi = DatabaseConfigurator.create(DatabaseConfigurator.buildDataSource(applicationConfiguration))

    val authRepository = AuthRepository(jdbi)
    val categoryRepository = CategoryRepository(jdbi)
    val userRepository = UserRepository(argon2Configuration, jdbi)

    val serviceRegistry = ServiceRegistry(
        authRepository = authRepository,
        categoryRepository = categoryRepository,
        userRepository = userRepository,
        jwtConfiguration = jwtConfiguration,
        applicationConfig = applicationConfiguration,
        argon2Configuration = argon2Configuration
    )

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

    installAuth(serviceRegistry)

    installGraphQL(serviceRegistry)
}
