package no.echokarriere.backend.category;

import lombok.RequiredArgsConstructor;
import lombok.Value;

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

    CategoryEntity(CategoryDTO dto) {
        this(UUID.randomUUID(), dto.getTitle(), dto.getDescription(), dto.getSlug(), null, null);
    }

    CategoryEntity(UUID id, CategoryDTO dto) {
        this(id, dto.getTitle(), dto.getDescription(), dto.getSlug(), null, null);
    }
}
