package no.echokarriere.backend.category.dto;

public record UpdateCategoryDTO(
        String title,
        String description,
        String slug
) {
}
