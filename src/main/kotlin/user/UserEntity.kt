package no.echokarriere.user

import no.echokarriere.category.Categories
import no.echokarriere.configuration.PGEnum
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.timestamp
import java.sql.ResultSet
import java.time.Instant
import java.util.UUID

enum class UserType {
    ADMIN,
    STAFF,
    USER
}

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
    val createdAt: Instant,
    val modifiedAt: Instant? = null
) {
    companion object : RowMapper<UserEntity> {
        fun create(
            id: UUID = UUID.randomUUID(),
            name: String,
            email: String,
            password: String,
            active: Boolean,
            type: UserType,
            createdAt: Instant = Instant.now(),
            modifiedAt: Instant? = null
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

        override fun map(rs: ResultSet?, ctx: StatementContext?): UserEntity? = rs?.let {
            create(
                id = UUID.fromString(it.getString("id")),
                name = it.getString("name"),
                email = it.getString("email"),
                password = it.getString("password"),
                active = it.getBoolean("active"),
                type = UserType.valueOf(it.getString("type")),
                createdAt = it.getTimestamp("created_at").toInstant(),
                modifiedAt = it.getTimestamp("modified_at")?.toInstant()
            )
        }
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

object Users : Table("user") {
    val id = uuid("id")
    val name = text("name")
    val email = text("email")
    val password = text("password")
    val active = bool("active")
    val type = customEnumeration(
        "type",
        "UserType",
        { value -> UserType.valueOf(value as String) },
        { PGEnum("UserType", it) }
    )
    val createdAt = timestamp("created_at")
    val modifiedAt = timestamp("modified_at").nullable()
    override val primaryKey = PrimaryKey(Categories.id)
}
