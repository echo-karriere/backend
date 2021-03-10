package no.echokarriere.backend.category;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CategoryEntity(
        UUID id,
        String title,
        String description,
        String slug,
        OffsetDateTime createdAt,
        OffsetDateTime modifiedAt
) {
    CategoryEntity(String title, String description, String slug) {
        this(UUID.randomUUID(), title, description, slug, null, null);
    }

    CategoryEntity(UUID id, String title, String description, String slug) {
        this(id, title, description, slug, null, null);
    }
}
