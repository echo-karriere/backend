package no.echokarriere.user

import java.util.UUID

@Suppress("unused")
class UserQueryResolver(
    private val userRepository: UserRepository
) {
    suspend fun users(): List<User> = userRepository.selectAll().map { User(it) }
    suspend fun user(id: UUID): User? = userRepository.select(id)?.let { User(it) }
}
