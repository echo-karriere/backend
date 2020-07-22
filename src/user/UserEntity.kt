package no.echokarriere.user

import java.time.Instant
import java.util.UUID
import no.echokarriere.category.Categories
import no.echokarriere.configuration.PGEnum
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.timestamp

enum class UserType {
    ADMIN,
    STAFF,
    USER
}

data class UserEntity(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val email: String,
    val password: String,
    val type: UserType,
    val createdAt: Instant,
    val modifiedAt: Instant? = null
)

data class UserDTO(
    val name: String,
    val email: String,
    val password: String,
    val type: UserType = UserType.USER
)

object Users : Table("user") {
    val id = uuid("id")
    val name = text("name")
    val email = text("email")
    val password = text("password")
    val type = customEnumeration("type", "UserType", { value -> UserType.valueOf(value as String) }, { PGEnum("UserType", it) })
    val createdAt = timestamp("created_at")
    val modifiedAt = timestamp("modified_at").nullable()
    override val primaryKey = PrimaryKey(Categories.id)
}
