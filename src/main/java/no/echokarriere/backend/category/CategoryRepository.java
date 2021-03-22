package no.echokarriere.backend.category;

import no.echokarriere.backend.configuration.CrudRepository;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CategoryRepository implements CrudRepository<CategoryEntity, UUID> {
    private final Jdbi jdbi;

    public CategoryRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public Optional<CategoryEntity> create(CategoryEntity entity) {
        return jdbi.withHandle(handle -> handle
                .createUpdate("""
                        insert into category (id, title, description, slug, created_at)
                        values (:id, :title, :description, :slug, :createdAt)
                        returning *
                        """)
                .bindBean(entity)
                .executeAndReturnGeneratedKeys()
                .mapTo(CategoryEntity.class)
                .findOne());
    }

    @Override
    public Optional<CategoryEntity> update(CategoryEntity entity) {
        return jdbi.withHandle(handle -> handle
                .createUpdate("""
                        update category
                        set title = :title, description = :description, slug = :slug, modified_at = :modifiedAt
                        where id = :id
                        returning *
                        """)
                .bindBean(entity)
                .executeAndReturnGeneratedKeys()
                .mapTo(CategoryEntity.class)
                .findOne());
    }

    @Override
    public boolean delete(UUID id) {
        return jdbi.withHandle(handle -> handle
                .createUpdate("delete from category where id = :id")
                .bind("id", id)
                .execute() == 1);
    }

    @Override
    public Optional<CategoryEntity> select(UUID id) {
        return jdbi.withHandle(handle -> handle
                .createQuery("select * from category where id = :id")
                .bind("id", id)
                .mapTo(CategoryEntity.class)
                .findOne());
    }

    @Override
    public List<CategoryEntity> selectAll() {
        return jdbi.withHandle(handle -> handle
                .createQuery("select * from category")
                .mapTo(CategoryEntity.class)
                .list());
    }
}
