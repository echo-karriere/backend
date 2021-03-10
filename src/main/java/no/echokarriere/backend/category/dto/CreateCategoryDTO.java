package no.echokarriere.backend.category.dto;

public record CreateCategoryDTO(
        String title,
        String description,
        String slug
) {
}
