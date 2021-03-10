package no.echokarriere.backend.configuration;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<E, I> {
    @Transactional
    Optional<E> create(E entity);

    @Transactional
    Optional<E> update(E entity);

    @Transactional
    boolean delete(I id);

    @Transactional
    Optional<E> select(I id);

    @Transactional
    List<E> selectAll();
}
