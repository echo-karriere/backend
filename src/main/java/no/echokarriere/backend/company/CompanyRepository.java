package no.echokarriere.backend.company;

import no.echokarriere.backend.configuration.CrudRepository;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static no.echokarriere.db.tables.Company.COMPANY;
import static org.jooq.impl.DSL.row;

@Repository
public class CompanyRepository implements CrudRepository<CompanyEntity, UUID> {
    private final DSLContext dsl;

    public CompanyRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    @Transactional
    public Optional<CompanyEntity> create(CompanyEntity entity) {
        var res = dsl
                .insertInto(COMPANY).columns(COMPANY.ID, COMPANY.NAME, COMPANY.HOMEPAGE)
                .values(entity.getId(), entity.getName(), entity.getHomepage())
                .returning()
                .fetchOne();

        if (res == null) return Optional.empty();

        return Optional.of(res.into(CompanyEntity.class));
    }

    @Override
    @Transactional
    public Optional<CompanyEntity> update(CompanyEntity entity) {
        var res = dsl
                .update(COMPANY)
                .set(
                        row(COMPANY.NAME, COMPANY.HOMEPAGE, COMPANY.MODIFIED_AT),
                        row(entity.getName(), entity.getHomepage(), OffsetDateTime.now())
                )
                .where(COMPANY.ID.eq(entity.getId()))
                .returning()
                .fetchOne();

        if (res == null) return Optional.empty();

        return Optional.of(res.into(CompanyEntity.class));
    }

    @Override
    @Transactional
    public boolean delete(UUID id) {
        return dsl.
                delete(COMPANY)
                .where(COMPANY.ID.eq(id))
                .execute() == 1;
    }

    @Override
    @Transactional
    public Optional<CompanyEntity> select(UUID id) {
        var res = dsl
                .select()
                .from(COMPANY)
                .where(COMPANY.ID.eq(id))
                .fetchOne();

        if (res == null) return Optional.empty();

        return Optional.of(res.into(CompanyEntity.class));
    }

    @Override
    @Transactional
    public List<CompanyEntity> selectAll() {
        return dsl
                .select()
                .from(COMPANY)
                .fetch()
                .into(CompanyEntity.class);
    }
}
