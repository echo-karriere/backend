package no.echokarriere.category

import no.echokarriere.Tables.CATEGORY
import no.echokarriere.configuration.CrudRepository
import no.echokarriere.dbQuery
import org.jooq.DSLContext
import org.jooq.impl.DSL.row
import java.time.OffsetDateTime
import java.util.UUID

class CategoryRepository(private val jooq: DSLContext) : CrudRepository<CategoryEntity, UUID> {
    override suspend fun selectAll(): List<CategoryEntity> = dbQuery {
        jooq.select()
            .from(CATEGORY)
            .fetch()
            .into(CategoryEntity::class.java)
    }

    override suspend fun select(id: UUID): CategoryEntity? = dbQuery {
        jooq.select()
            .from(CATEGORY)
            .where(CATEGORY.ID.eq(id))
            .fetchOne()
            ?.into(CategoryEntity::class.java)
    }

    override suspend fun insert(entity: CategoryEntity): CategoryEntity? = dbQuery {
        jooq.insertInto(CATEGORY)
            .columns(CATEGORY.ID, CATEGORY.TITLE, CATEGORY.DESCRIPTION, CATEGORY.SLUG)
            .values(entity.id, entity.title, entity.description, entity.slug)
            .returning()
            .fetchOne()
            ?.into(CategoryEntity::class.java)
    }

    override suspend fun update(entity: CategoryEntity): CategoryEntity? = dbQuery {
        jooq.update(CATEGORY)
            .set(
                row(CATEGORY.TITLE, CATEGORY.DESCRIPTION, CATEGORY.SLUG, CATEGORY.MODIFIED_AT),
                row(entity.title, entity.description, entity.slug, OffsetDateTime.now()),
            )
            .where(CATEGORY.ID.eq(entity.id))
            .returning()
            .fetchOne()
            ?.into(CategoryEntity::class.java)
    }

    override suspend fun delete(id: UUID): Boolean = dbQuery {
        jooq.delete(CATEGORY)
            .where(CATEGORY.ID.eq(id))
            .execute() == 1
    }
}
