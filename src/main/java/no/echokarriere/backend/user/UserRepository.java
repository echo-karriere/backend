package no.echokarriere.backend.user;

import no.echokarriere.backend.configuration.CrudRepository;
import no.echokarriere.db.tables.records.UserRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static no.echokarriere.db.Tables.USER;
import static org.jooq.impl.DSL.row;

@Repository
public class UserRepository implements CrudRepository<UserEntity, UUID> {
    private final DSLContext dsl;

    public UserRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    private static UserEntity map(UserRecord it) {
        return it.into(UserEntity.class);
    }

    @Override
    public Optional<UserEntity> create(UserEntity entity) {
        return dsl.insertInto(USER)
                .columns(USER.ID, USER.NAME, USER.EMAIL, USER.PASSWORD, USER.ACTIVE, USER.TYPE)
                .values(entity.getId(), entity.getName(), entity.getEmail(), entity.getPassword(), entity.isActive(), entity.getUserType())
                .returning()
                .fetchOptional()
                .map(UserRepository::map);
    }

    @Override
    public Optional<UserEntity> update(UserEntity entity) {
        return dsl.update(USER)
                .set(
                        row(USER.NAME, USER.EMAIL, USER.ACTIVE, USER.TYPE, USER.MODIFIED_AT),
                        row(entity.getName(), entity.getEmail(), entity.isActive(), entity.getUserType(), OffsetDateTime.now())
                )
                .where(USER.ID.eq(entity.getId()))
                .returning()
                .fetchOptional()
                .map(UserRepository::map);
    }

    @Override
    public boolean delete(UUID id) {
        return dsl.delete(USER)
                .where(USER.ID.eq(id))
                .execute() == 1;
    }

    @Override
    public Optional<UserEntity> select(UUID id) {
        return dsl.selectFrom(USER)
                .where(USER.ID.eq(id))
                .fetchOptional()
                .map(UserRepository::map);
    }

    @Override
    public List<UserEntity> selectAll() {
        return dsl.selectFrom(USER)
                .fetch()
                .map(UserRepository::map);
    }
}
