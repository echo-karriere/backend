package no.echokarriere.backend.category;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import no.echokarriere.graphql.types.CreateCategoryInput;
import no.echokarriere.graphql.types.UpdateCategoryInput;

import java.time.OffsetDateTime;
import java.util.UUID;

@Value
@RequiredArgsConstructor
@Builder
public class CategoryEntity {
    UUID id;
    String title;
    String description;
    String slug;
    OffsetDateTime createdAt;
    OffsetDateTime modifiedAt;

    CategoryEntity(CreateCategoryInput input) {
        this(UUID.randomUUID(), input.getTitle(), input.getDescription(), input.getSlug(), OffsetDateTime.now(), null);
    }

    CategoryEntity(UUID id, UpdateCategoryInput input) {
        this(id, input.getTitle(), input.getDescription(), input.getSlug(), null, OffsetDateTime.now());
    }
}
