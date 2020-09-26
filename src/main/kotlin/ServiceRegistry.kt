package no.echokarriere

import io.ktor.config.HoconApplicationConfig
import no.echokarriere.auth.AuthRepository
import no.echokarriere.auth.jwt.JWTConfiguration
import no.echokarriere.category.CategoryRepository
import no.echokarriere.configuration.Argon2Configuration
import no.echokarriere.user.UserRepository

open class ServiceRegistry(
    // Repositories
    val authRepository: AuthRepository,
    val categoryRepository: CategoryRepository,
    val userRepository: UserRepository,
    // Configurations
    val jwtConfiguration: JWTConfiguration,
    val applicationConfig: HoconApplicationConfig,
    val argon2Configuration: Argon2Configuration
)
