package no.echokarriere.backend.category.errors;

import java.util.UUID;

public class CategoryUpdateException extends RuntimeException {
    public CategoryUpdateException(UUID id) {
        super("Could not update category " + id);
    }
}
