package no.echokarriere.form

import no.echokarriere.getUUID
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet
import java.time.Instant
import java.util.UUID

class FormEntity private constructor(
    val id: UUID,
    val title: String,
    val description: String,
    val createdAt: Instant,
    val modifiedAt: Instant?
) {
    private fun update(
        id: UUID = this.id,
        title: String = this.title,
        description: String = this.description,
        createdAt: Instant = this.createdAt,
        modifiedAt: Instant? = this.modifiedAt
    ): FormEntity = FormEntity(
        id = id,
        title = title,
        description = description,
        createdAt = createdAt,
        modifiedAt = modifiedAt
    )

    fun updateDetails(
        title: String,
        description: String,
    ) = update(
        title = title,
        description = description,
        modifiedAt = Instant.now()
    )

    companion object : RowMapper<FormEntity> {
        fun create(
            id: UUID = UUID.randomUUID(),
            title: String,
            description: String,
            createdAt: Instant = Instant.now(),
            modifiedAt: Instant? = null
        ): FormEntity = FormEntity(
            id = id,
            title = title,
            description = description,
            createdAt = createdAt,
            modifiedAt = modifiedAt
        )

        override fun map(rs: ResultSet?, ctx: StatementContext?): FormEntity? = rs?.let {
            create(
                id = it.getUUID("id"),
                title = it.getString("title"),
                description = it.getString("description"),
                createdAt = it.getTimestamp("created_at").toInstant(),
                modifiedAt = it.getTimestamp("modified_at")?.toInstant()
            )
        }
    }
}
