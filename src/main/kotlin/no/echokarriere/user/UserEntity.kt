package no.echokarriere.user

import no.echokarriere.enums.Usertype
import org.jooq.impl.EnumConverter
import java.time.OffsetDateTime
import java.util.UUID

enum class UserType {
    ADMIN,
    STAFF,
    USER
}

@Suppress("unused") // used by Jooq
class UserTypeConverter : EnumConverter<UserType, Usertype>(UserType::class.java, Usertype::class.java)

data class User(private val entity: UserEntity) {
    val id = entity.id
    val name = entity.name
    val email = entity.email
    val active = entity.active
    val type = entity.type
}

class UserEntity private constructor(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val email: String,
    val password: String,
    val active: Boolean,
    val type: UserType,
    val createdAt: OffsetDateTime,
    val modifiedAt: OffsetDateTime? = null
) {
    companion object {
        fun create(
            id: UUID = UUID.randomUUID(),
            name: String,
            email: String,
            password: String,
            active: Boolean,
            type: UserType,
            createdAt: OffsetDateTime = OffsetDateTime.now(),
            modifiedAt: OffsetDateTime? = null
        ): UserEntity = UserEntity(
            id = id,
            name = name,
            email = email,
            password = password,
            active = active,
            type = type,
            createdAt = createdAt,
            modifiedAt = modifiedAt
        )
    }
}

data class CreateUserInput(
    val name: String,
    val email: String,
    val password: String,
    val type: UserType = UserType.USER
) {
    fun createEntity(): UserEntity = UserEntity.create(
        name = name,
        email = email,
        password = password,
        type = type,
        active = true
    )
}
