package no.echokarriere.backend.category;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import no.echokarriere.graphql.types.CreateCategoryInput;
import no.echokarriere.graphql.types.UpdateCategoryInput;

import java.time.OffsetDateTime;
import java.util.UUID;

@Value
@RequiredArgsConstructor
public class CategoryEntity {
    UUID id;
    String title;
    String description;
    String slug;
    OffsetDateTime createdAt;
    OffsetDateTime modifiedAt;

    CategoryEntity(CreateCategoryInput input) {
        this(UUID.randomUUID(), input.getTitle(), input.getDescription(), input.getSlug(), null, null);
    }

    CategoryEntity(UUID id, UpdateCategoryInput input) {
        this(id, input.getTitle(), input.getDescription(), input.getSlug(), null, null);
    }
}
