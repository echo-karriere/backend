package no.echokarriere.backend.category;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.UUID;

@Value
@RequiredArgsConstructor
public class Category {
    UUID id;
    String title;
    String description;
    String slug;
    OffsetDateTime createdAt;
    OffsetDateTime modifiedAt;

    Category(CategoryEntity entity) {
        this(entity.getId(), entity.getTitle(), entity.getDescription(), entity.getSlug(), entity.getCreatedAt(), entity.getModifiedAt());
    }
}
