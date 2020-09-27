package no.echokarriere.user

import com.typesafe.config.ConfigFactory
import io.ktor.config.HoconApplicationConfig
import kotlinx.coroutines.runBlocking
import no.echokarriere.configuration.Argon2Configuration
import no.echokarriere.utils.DatabaseExtension
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.extension.ExtendWith
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@ExtendWith(DatabaseExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryTests {
    private val applicationConfiguration = HoconApplicationConfig(ConfigFactory.load())
    private val userRepository = UserRepository(Argon2Configuration(applicationConfiguration))
    private val userId = UUID.randomUUID()

    @Test
    @Order(1)
    fun `can create a new user`() = runBlocking {
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
        assertEquals(userId, created.id)
        assertEquals("Test User", created.name)
        assertEquals(true, created.active)
        assertEquals(UserType.USER, created.type)
    }

    @Test
    fun `can select a single user`() = runBlocking {
        val user = userRepository.select(userId)

        assertNotNull(user)
        assertEquals(userId, user.id)
    }

    @Test
    fun `can select a single user by email`() = runBlocking {
        val user = userRepository.selectByEmail("test@user.com")

        assertNotNull(user)
        assertEquals(userId, user.id)
        assertEquals("test@user.com", user.email)
    }

    @Test
    fun `can select all users`() = runBlocking {
        val users = userRepository.selectAll()
        val user = users.find { it.id == userId }

        assertNotNull(user)
        assertEquals(userId, user.id)
    }
}
