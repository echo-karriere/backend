package no.echokarriere.backend.company;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.UUID;

@Value
@RequiredArgsConstructor
public class Company {
    UUID id;
    String name;
    String homepage;
    OffsetDateTime createdAt;
    OffsetDateTime modifiedAt;

    Company(CompanyEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.homepage = entity.getHomepage();
        this.createdAt = entity.getCreatedAt();
        this.modifiedAt = entity.getModifiedAt();
    }
}
