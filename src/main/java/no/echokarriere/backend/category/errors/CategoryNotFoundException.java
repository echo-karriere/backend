package no.echokarriere.backend.category.errors;

import java.util.UUID;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(UUID id) {
        super("Could not find category " + id);
    }
}
