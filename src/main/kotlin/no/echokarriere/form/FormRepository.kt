package no.echokarriere.form

import no.echokarriere.Tables.FORM
import no.echokarriere.configuration.CrudRepository
import no.echokarriere.dbQuery
import org.jooq.DSLContext
import org.jooq.impl.DSL
import java.time.OffsetDateTime
import java.util.UUID

class FormRepository(private val jooq: DSLContext) : CrudRepository<FormEntity, UUID> {
    override suspend fun selectAll(): List<FormEntity> = dbQuery {
        jooq.select()
            .from(FORM)
            .fetch()
            .into(FormEntity::class.java)
    }

    override suspend fun select(id: UUID): FormEntity? = dbQuery {
        jooq.select()
            .from(FORM)
            .where(FORM.ID.eq(id))
            .fetchOne()
            ?.into(FormEntity::class.java)
    }

    override suspend fun insert(entity: FormEntity): FormEntity? = dbQuery {
        jooq.insertInto(FORM)
            .columns(FORM.ID, FORM.TITLE, FORM.DESCRIPTION, FORM.CREATED_AT)
            .values(entity.id, entity.title, entity.description, entity.createdAt)
            .returning()
            .fetchOne()
            ?.into(FormEntity::class.java)
    }

    override suspend fun update(entity: FormEntity): FormEntity? = dbQuery {
        jooq.update(FORM)
            .set(
                DSL.row(FORM.TITLE, FORM.DESCRIPTION, FORM.MODIFIED_AT),
                DSL.row(entity.title, entity.description, OffsetDateTime.now())
            )
            .where(FORM.ID.eq(entity.id))
            .returning()
            .fetchOne()
            ?.into(FormEntity::class.java)
    }

    override suspend fun delete(id: UUID): Boolean = dbQuery {
        jooq.delete(FORM)
            .where(FORM.ID.eq(id))
            .execute() == 1
    }
}
