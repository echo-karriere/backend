package no.echokarriere.backend.company;

import no.echokarriere.backend.configuration.CrudRepository;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CompanyRepository implements CrudRepository<CompanyEntity, UUID> {
    private final Jdbi jdbi;

    public CompanyRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public Optional<CompanyEntity> create(CompanyEntity entity) {
        return jdbi.withHandle(handle -> handle
                .createUpdate("""
                        insert into company (id, name, homepage, created_at)
                        values (:id, :name, :homepage, :createdAt)
                        returning *
                        """)
                .bindBean(entity)
                .executeAndReturnGeneratedKeys()
                .mapTo(CompanyEntity.class)
                .findOne());
    }

    @Override
    public Optional<CompanyEntity> update(CompanyEntity entity) {
        return jdbi.withHandle(handle -> handle
                .createUpdate("""
                        update company
                        set name = :name, homepage = :homepage, modified_at = :modifiedAt
                        where id = :id
                        returning *
                        """)
                .bindBean(entity)
                .executeAndReturnGeneratedKeys()
                .mapTo(CompanyEntity.class)
                .findOne());
    }

    @Override
    public boolean delete(UUID id) {
        return jdbi.withHandle(handle -> handle
                .createUpdate("delete from company where id = :id")
                .bind("id", id)
                .execute() == 1);
    }

    @Override
    public Optional<CompanyEntity> select(UUID id) {
        return jdbi.withHandle(handle -> handle
                .createQuery("select * from company where id = :id")
                .bind("id", id)
                .mapTo(CompanyEntity.class)
                .findOne());
    }

    @Override
    public List<CompanyEntity> selectAll() {
        return jdbi.withHandle(handle -> handle
                .createQuery("select * from company")
                .mapTo(CompanyEntity.class)
                .list());
    }
}
