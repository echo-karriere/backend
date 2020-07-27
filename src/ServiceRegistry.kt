package no.echokarriere

import io.ktor.util.KtorExperimentalAPI
import no.echokarriere.auth.AuthRepository
import no.echokarriere.auth.JWTConfiguration
import no.echokarriere.category.CategoryRepository
import no.echokarriere.configuration.Argon2Configuration
import no.echokarriere.user.UserRepository

@KtorExperimentalAPI
open class ServiceRegistry(
    val userRepository: UserRepository,
    val categoryRepository: CategoryRepository,
    val authRepository: AuthRepository,
    val argon2Configuration: Argon2Configuration,
    val jwtConfiguration: JWTConfiguration
)
