package no.echokarriere.backend.configuration;


import java.util.List;

public interface CrudService<E, D, I> {
    E create(D dto);

    E update(D dto, I id);

    boolean delete(I id);

    E single(I id);

    List<E> all();
}
