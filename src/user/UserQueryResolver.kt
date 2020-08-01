package no.echokarriere.user

import java.util.UUID

@Suppress("unused")
class UserQueryResolver(
    private val userRepository: UserRepository
) {
    suspend fun users(): List<UserEntity> = userRepository.selectAll()
    suspend fun user(id: UUID): UserEntity? = userRepository.select(id)
}
