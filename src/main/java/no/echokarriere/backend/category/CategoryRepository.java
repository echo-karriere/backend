package no.echokarriere.backend.category;

import no.echokarriere.backend.configuration.CrudRepository;
import no.echokarriere.db.tables.records.CategoryRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static no.echokarriere.db.Tables.CATEGORY;
import static org.jooq.impl.DSL.row;

@Repository
public class CategoryRepository implements CrudRepository<CategoryEntity, UUID> {
    private final DSLContext dsl;

    public CategoryRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    private static CategoryEntity map(CategoryRecord it) {
        return it.into(CategoryEntity.class);
    }

    @Override
    public Optional<CategoryEntity> create(CategoryEntity entity) {
        return dsl.insertInto(CATEGORY)
                .columns(CATEGORY.ID, CATEGORY.TITLE, CATEGORY.DESCRIPTION, CATEGORY.SLUG)
                .values(entity.getId(), entity.getTitle(), entity.getDescription(), entity.getSlug())
                .returning()
                .fetchOptional()
                .map(CategoryRepository::map);
    }

    @Override
    public Optional<CategoryEntity> update(CategoryEntity entity) {
        return dsl.update(CATEGORY)
                .set(
                        row(CATEGORY.TITLE, CATEGORY.DESCRIPTION, CATEGORY.SLUG, CATEGORY.MODIFIED_AT),
                        row(entity.getTitle(), entity.getDescription(), entity.getSlug(), OffsetDateTime.now())
                )
                .where(CATEGORY.ID.eq(entity.getId()))
                .returning()
                .fetchOptional()
                .map(CategoryRepository::map);
    }

    @Override
    public boolean delete(UUID id) {
        return dsl.delete(CATEGORY)
                .where(CATEGORY.ID.eq(id))
                .execute() == 1;
    }

    @Override
    public Optional<CategoryEntity> select(UUID id) {
        return dsl.selectFrom(CATEGORY)
                .where(CATEGORY.ID.eq(id))
                .fetchOptional()
                .map(CategoryRepository::map);
    }

    @Override
    public List<CategoryEntity> selectAll() {
        return dsl.selectFrom(CATEGORY)
                .fetch()
                .map(CategoryRepository::map);
    }
}
