package no.echokarriere.backend.category.dto;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class UpdateCategoryDTO {
    String title;
    String description;
    String slug;
}
