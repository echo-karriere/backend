package no.echokarriere.backend.company;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import no.echokarriere.graphql.types.CreateCompanyInput;
import no.echokarriere.graphql.types.UpdateCompanyInput;

import java.time.OffsetDateTime;
import java.util.UUID;

@Value
@RequiredArgsConstructor
@Builder
public class CompanyEntity {
    UUID id;
    String name;
    String homepage;
    OffsetDateTime createdAt;
    OffsetDateTime modifiedAt;

    public CompanyEntity(CreateCompanyInput input) {
        this.id = UUID.randomUUID();
        this.name = input.getName();
        this.homepage = input.getHomepage();
        this.createdAt = OffsetDateTime.now();
        this.modifiedAt = null;
    }

    public CompanyEntity(UUID id, UpdateCompanyInput input) {
        this.id = id;
        this.name = input.getName();
        this.homepage = input.getHomepage();
        this.createdAt = OffsetDateTime.now();
        this.modifiedAt = null;
    }
}
