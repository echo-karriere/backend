package no.echokarriere.backend.category;

import no.echokarriere.backend.configuration.CrudRepository;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static no.echokarriere.db.tables.Category.CATEGORY;
import static org.jooq.impl.DSL.row;

@Service
public class CategoryRepository implements CrudRepository<CategoryEntity, UUID> {
    private final DSLContext dsl;

    @Autowired
    public CategoryRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    @Transactional
    public Optional<CategoryEntity> create(CategoryEntity entity) {
        var res = dsl
                .insertInto(CATEGORY).columns(CATEGORY.ID, CATEGORY.TITLE, CATEGORY.DESCRIPTION, CATEGORY.SLUG)
                .values(entity.getId(), entity.getTitle(), entity.getDescription(), entity.getSlug())
                .returning()
                .fetchOne();

        if (res == null) return Optional.empty();

        return Optional.of(res.into(CategoryEntity.class));
    }

    @Override
    @Transactional
    public Optional<CategoryEntity> update(CategoryEntity entity) {
        var res = dsl
                .update(CATEGORY)
                .set(
                        row(CATEGORY.TITLE, CATEGORY.DESCRIPTION, CATEGORY.SLUG, CATEGORY.MODIFIED_AT),
                        row(entity.getTitle(), entity.getDescription(), entity.getSlug(), OffsetDateTime.now())
                )
                .where(CATEGORY.ID.eq(entity.getId()))
                .returning()
                .fetchOne();

        if (res == null) return Optional.empty();

        return Optional.of(res.into(CategoryEntity.class));
    }

    @Override
    @Transactional
    public boolean delete(UUID id) {
        return dsl.
                delete(CATEGORY)
                .where(CATEGORY.ID.eq(id))
                .execute() == 1;
    }

    @Override
    @Transactional
    public Optional<CategoryEntity> select(UUID id) {
        var res = dsl
                .select()
                .from(CATEGORY)
                .where(CATEGORY.ID.eq(id))
                .fetchOne();

        if (res == null) return Optional.empty();

        return Optional.of(res.into(CategoryEntity.class));
    }

    @Override
    @Transactional
    public List<CategoryEntity> selectAll() {
        return dsl
                .select()
                .from(CATEGORY)
                .fetch()
                .into(CategoryEntity.class);
    }
}
