package no.echokarriere.user

import com.typesafe.config.ConfigFactory
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.ktor.config.HoconApplicationConfig
import no.echokarriere.configuration.Argon2Configuration
import no.echokarriere.utils.DatabaseDescribeSpec
import java.util.UUID

class UserRepositorySpec : DatabaseDescribeSpec({ body() })

private val body: DescribeSpec.() -> Unit = {
    val applicationConfiguration = HoconApplicationConfig(ConfigFactory.load())
    val userRepository = UserRepository(Argon2Configuration(applicationConfiguration))
    val userId = UUID.randomUUID()

    describe("UserRepository") {
        it("can create a new user") {
            val user = UserEntity.create(
                id = userId,
                name = "Test User",
                email = "test@user.com",
                password = "password123",
                active = true,
                type = UserType.USER
            )

            val created = userRepository.insert(user)

            created shouldNotBe null
            created?.id shouldBe userId
            created?.name shouldBe "Test User"
            created?.active shouldBe true
            created?.type shouldBe UserType.USER
        }

        it("can select a single user") {
            val user = userRepository.select(userId)

            user shouldNotBe null
            user?.id shouldBe userId
        }

        it("can select a single user by email") {
            val user = userRepository.selectByEmail("test@user.com")

            user shouldNotBe null
            user?.id shouldBe userId
            user?.email shouldBe "test@user.com"
        }

        it("can select all users") {
            val users = userRepository.selectAll()
            val user = users.find { it.id == userId }

            user shouldNotBe null
            user?.id shouldBe userId
        }
    }
}
