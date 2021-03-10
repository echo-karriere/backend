package no.echokarriere.backend.category.errors;

public class CategoryCreationException extends RuntimeException {
    public CategoryCreationException() {
        super("Could not create category");
    }
}
