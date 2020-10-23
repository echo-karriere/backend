package no.echokarriere.auth

import com.typesafe.config.ConfigFactory
import io.ktor.config.HoconApplicationConfig
import kotlinx.coroutines.runBlocking
import no.echokarriere.configuration.Argon2Configuration
import no.echokarriere.user.UserEntity
import no.echokarriere.user.UserRepository
import no.echokarriere.user.UserType
import no.echokarriere.utils.DatabaseExtension
import no.echokarriere.utils.TestDatabase
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.extension.ExtendWith
import java.security.SecureRandom
import java.time.Instant
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@ExtendWith(DatabaseExtension::class)
class AuthRepositoryTests : TestDatabase() {
    private val applicationConfiguration = HoconApplicationConfig(ConfigFactory.load())
    private val userRepository = UserRepository(Argon2Configuration(applicationConfiguration), jooq())
    private val authRepository = AuthRepository(jdbi())

    private val userId = UUID.randomUUID()
    lateinit var token: String

    @Test
    @Order(1)
    fun `create a token`() = runBlocking {
        val user = UserEntity.create(
            id = userId,
            name = "Test User",
            email = "test@user.com",
            password = "password123",
            active = true,
            type = UserType.USER
        )

        userRepository.insert(user)

        token = SecureRandom().generateByteArray(24).encodeAsBase64()
        val refreshTokenEntity = RefreshTokenEntity.create(
            userId = userId,
            refreshToken = token,
            expiresAt = Instant.now().plusSeconds(REFRESH_TOKEN_DURATION),
            createdAt = Instant.now()
        )

        val created = authRepository.insert(refreshTokenEntity)

        assertNotNull(created)
        assertEquals(userId, created.userId)
        assertEquals(token, created.refreshToken)
    }

    @Test
    @Order(2)
    fun `can select token by user`() = runBlocking {
        val refreshToken = authRepository.select(userId)

        assertNotNull(refreshToken)
        assertEquals(userId, refreshToken.userId)
        assertEquals(token, refreshToken.refreshToken)
    }

    @Test
    @Order(3)
    fun `can select token by token`() = runBlocking {
        val refreshToken = authRepository.selectByToken(token)

        assertNotNull(refreshToken)
        assertEquals(userId, refreshToken.userId)
        assertEquals(token, refreshToken.refreshToken)
    }

    @Test
    @Order(4)
    fun `can select all tokens`() = runBlocking {
        val tokens = authRepository.selectAll()
        val refreshToken = tokens.find { it.userId == userId }

        assertNotNull(refreshToken)
        assertEquals(userId, refreshToken.userId)
        assertEquals(token, refreshToken.refreshToken)
    }

    @Test
    @Order(5)
    fun `can update a token`() = runBlocking {
        val nextToken = SecureRandom().generateByteArray(24).encodeAsBase64()
        val nextRefreshToken = RefreshTokenEntity.create(
            refreshToken = nextToken,
            userId = userId,
            expiresAt = Instant.now().plusSeconds(REFRESH_TOKEN_DURATION),
            createdAt = Instant.now()
        )

        val updated = authRepository.update(nextRefreshToken)

        assertNotNull(updated)
        assertEquals(userId, updated.userId)
        assertEquals(nextToken, updated.refreshToken)
        assertNotEquals(token, updated.refreshToken)
    }

    @Test
    @Order(6)
    fun `can delete a token`() = runBlocking {
        val deleted = authRepository.delete(userId)

        assertEquals(true, deleted)

        val doubleDeleted = authRepository.delete(userId)
        assertEquals(false, doubleDeleted)

        val itIsGone = authRepository.select(userId)
        assertNull(itIsGone)
    }
}
