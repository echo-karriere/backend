package no.echokarriere.backend.category.dto;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
@RequiredArgsConstructor
public class CreateCategoryDTO {
    @NotBlank(message = "Title is required")
    String title;

    @NotBlank(message = "Description is required")
    String description;

    @NotBlank(message = "Slug is required")
    String slug;
}
