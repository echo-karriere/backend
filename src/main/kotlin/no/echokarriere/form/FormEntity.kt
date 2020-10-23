package no.echokarriere.form

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
        description: String
    ) = update(
        title = title,
        description = description,
        modifiedAt = Instant.now()
    )

    companion object {
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
    }
}
