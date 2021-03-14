package no.echokarriere.backend.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import no.echokarriere.backend.configuration.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category")
public class Category extends AbstractEntity {
    @NotEmpty(message = "Category must have a title")
    String title;
    @NotEmpty(message = "Category must have a description")
    String description;
    @NotEmpty(message = "Category must have a slug")
    String slug;
}
