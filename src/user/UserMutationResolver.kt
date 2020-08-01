package no.echokarriere.user

@Suppress("unused")
class UserMutationResolver(
    private val userRepository: UserRepository
) {
    suspend fun createUser(user: UserDTO): UserEntity? = userRepository.create(user)
}
