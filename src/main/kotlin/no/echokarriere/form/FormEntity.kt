package no.echokarriere.form

import java.time.OffsetDateTime
import java.util.UUID

class FormEntity private constructor(
    val id: UUID,
    val title: String,
    val description: String,
    val createdAt: OffsetDateTime,
    val modifiedAt: OffsetDateTime?
) {
    private fun update(
        id: UUID = this.id,
        title: String = this.title,
        description: String = this.description,
        createdAt: OffsetDateTime = this.createdAt,
        modifiedAt: OffsetDateTime? = this.modifiedAt
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
        modifiedAt = OffsetDateTime.now()
    )

    companion object {
        fun create(
            id: UUID = UUID.randomUUID(),
            title: String,
            description: String,
            createdAt: OffsetDateTime = OffsetDateTime.now(),
            modifiedAt: OffsetDateTime? = null
        ): FormEntity = FormEntity(
            id = id,
            title = title,
            description = description,
            createdAt = createdAt,
            modifiedAt = modifiedAt
        )
    }
}
