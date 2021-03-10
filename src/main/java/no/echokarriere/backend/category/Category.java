package no.echokarriere.backend.category;

import java.time.OffsetDateTime;
import java.util.UUID;

public record Category(
        UUID id,
        String title,
        String description,
        String slug,
        OffsetDateTime createdAt,
        OffsetDateTime modifiedAt
) {
    Category(CategoryEntity entity) {
        this(entity.id(), entity.title(), entity.description(), entity.slug(), entity.createdAt(), entity.modifiedAt());
    }
}
