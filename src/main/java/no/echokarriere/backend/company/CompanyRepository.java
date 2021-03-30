package no.echokarriere.backend.company;

import no.echokarriere.backend.configuration.CrudRepository;
import no.echokarriere.db.tables.records.CompanyRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static no.echokarriere.db.Tables.COMPANY;
import static org.jooq.impl.DSL.row;

@Repository
public class CompanyRepository implements CrudRepository<CompanyEntity, UUID> {
    private final DSLContext dsl;

    public CompanyRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    private static CompanyEntity map(CompanyRecord it) {
        return it.into(CompanyEntity.class);
    }

    @Override
    public Optional<CompanyEntity> create(CompanyEntity entity) {
        return dsl.insertInto(COMPANY)
                .columns(COMPANY.ID, COMPANY.NAME, COMPANY.HOMEPAGE)
                .values(entity.getId(), entity.getName(), entity.getHomepage())
                .returning()
                .fetchOptional()
                .map(CompanyRepository::map);
    }

    @Override
    public Optional<CompanyEntity> update(CompanyEntity entity) {
        return dsl
                .update(COMPANY)
                .set(
                        row(COMPANY.NAME, COMPANY.HOMEPAGE, COMPANY.MODIFIED_AT),
                        row(entity.getName(), entity.getHomepage(), OffsetDateTime.now())
                )
                .where(COMPANY.ID.eq(entity.getId()))
                .returning()
                .fetchOptional()
                .map(CompanyRepository::map);
    }

    @Override
    public boolean delete(UUID id) {
        return dsl.delete(COMPANY)
                .where(COMPANY.ID.eq(id))
                .execute() == 1;
    }

    @Override
    public Optional<CompanyEntity> select(UUID id) {
        return dsl.selectFrom(COMPANY)
                .where(COMPANY.ID.eq(id))
                .fetchOptional()
                .map(CompanyRepository::map);
    }

    @Override
    public List<CompanyEntity> selectAll() {
        return dsl.selectFrom(COMPANY)
                .fetch()
                .map(CompanyRepository::map);
    }
}
