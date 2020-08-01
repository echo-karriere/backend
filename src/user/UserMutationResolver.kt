package no.echokarriere.user

@Suppress("unused")
class UserMutationResolver(
    private val userRepository: UserRepository
) {
    suspend fun createUser(input: CreateUserInput): UserEntity? = userRepository.create(input)
}
