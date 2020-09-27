package no.echokarriere.user

import com.typesafe.config.ConfigFactory
import io.ktor.config.HoconApplicationConfig
import kotlinx.coroutines.runBlocking
import no.echokarriere.configuration.Argon2Configuration
import no.echokarriere.utils.DatabaseExtension
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.extension.ExtendWith
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@ExtendWith(DatabaseExtension::class)
class UserRepositoryTests {
    private val applicationConfiguration = HoconApplicationConfig(ConfigFactory.load())
    private val userRepository = UserRepository(Argon2Configuration(applicationConfiguration))
    private val userId: UUID = UUID.fromString("9a1ac48d-c9b3-4db2-9832-95bf89697d3a")

    @Test
    @Order(1)
    fun `it can create a new user`(): Unit = runBlocking {
        val user = UserEntity.create(
            id = userId,
            name = "Test User",
            email = "test@user.com",
            password = "password123",
            active = true,
            type = UserType.USER
        )

        val created = userRepository.insert(user)
        assertNotNull(created)
        assertEquals(created.id, userId)
        assertEquals(created.name, "Test User")
        assertEquals(created.active, true)
        assertEquals(created.type, UserType.USER)
    }

    @Test
    @Order(2)
    fun `can select a single user`() = runBlocking {
        val user = userRepository.select(userId)

        assertNotNull(user)
        assertEquals(user.id, userId)
    }

    @Test
    @Order(3)
    fun `can select a single user by email`() = runBlocking {
        val user = userRepository.selectByEmail("test@user.com")

        assertNotNull(user)
        assertEquals(user.id, userId)
        assertEquals(user.email, "test@user.com")
    }

    @Test
    @Order(4)
    fun `can select all users`() = runBlocking {
        val users = userRepository.selectAll()
        val user = users.find { it.id == userId }

        assertNotNull(user)
        assertEquals(user.id, userId)
    }
}
