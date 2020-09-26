package no.echokarriere.auth

import com.typesafe.config.ConfigFactory
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.ktor.config.HoconApplicationConfig
import no.echokarriere.configuration.Argon2Configuration
import no.echokarriere.user.UserEntity
import no.echokarriere.user.UserRepository
import no.echokarriere.user.UserType
import no.echokarriere.utils.DatabaseDescribeSpec
import java.security.SecureRandom
import java.time.Instant
import java.util.UUID

class AuthRepositorySpec : DatabaseDescribeSpec({ body() })

private val body: DescribeSpec.() -> Unit = {
    val applicationConfiguration = HoconApplicationConfig(ConfigFactory.load())
    val userRepository = UserRepository(Argon2Configuration(applicationConfiguration))
    val authRepository = AuthRepository()

    val userId = UUID.randomUUID()
    lateinit var token: String

    describe("AuthRepository") {
        it("create a token") {
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

            created shouldNotBe null
            created?.userId shouldBe userId
            created?.refreshToken shouldBe token
        }

        it("can select token by user") {
            val refreshToken = authRepository.select(userId)

            refreshToken shouldNotBe null
            refreshToken?.refreshToken shouldBe token
            refreshToken?.userId shouldBe userId
        }

        it("can select token by token") {
            val refreshToken = authRepository.selectByToken(token)

            refreshToken shouldNotBe null
            refreshToken?.refreshToken shouldBe token
            refreshToken?.userId shouldBe userId
        }

        it("can select all tokens") {
            val tokens = authRepository.selectAll()
            val refreshToken = tokens.find { it.userId == userId }

            refreshToken shouldNotBe null
            refreshToken?.refreshToken shouldBe token
            refreshToken?.userId shouldBe userId
        }

        it("can update a token") {
            val nextToken = SecureRandom().generateByteArray(24).encodeAsBase64()
            val nextRefreshToken = RefreshTokenEntity.create(
                refreshToken = nextToken,
                userId = userId,
                expiresAt = Instant.now().plusSeconds(REFRESH_TOKEN_DURATION),
                createdAt = Instant.now()
            )

            val updated = authRepository.update(nextRefreshToken)

            updated shouldNotBe null
            updated?.refreshToken shouldBe nextToken
            updated?.userId shouldBe userId
        }

        it("can delete a token") {
            val deleted = authRepository.delete(userId)

            deleted shouldBe true

            val doubleDeleted = authRepository.delete(userId)
            doubleDeleted shouldBe false

            val itIsGone = authRepository.select(userId)
            itIsGone shouldBe null
        }
    }
}
