package no.echokarriere.user

@Suppress("unused")
class UserMutationResolver(
    private val userRepository: UserRepository
) {
    suspend fun createUser(input: CreateUserInput): User? {
        val user = input.createEntity()
        val created = userRepository.insert(user)

        return created?.let { User(it) }
    }
}
