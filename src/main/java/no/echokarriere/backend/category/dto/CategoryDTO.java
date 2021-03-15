package no.echokarriere.backend.category.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import no.echokarriere.backend.configuration.BaseDTO;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO extends BaseDTO {
    @NotBlank(message = "Title is required")
    String title;

    @NotBlank(message = "Description is required")
    String description;

    @NotBlank(message = "Slug is required")
    String slug;
}
