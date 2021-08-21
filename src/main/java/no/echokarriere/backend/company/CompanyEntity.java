package no.echokarriere.backend.company;

import no.echokarriere.graphql.types.CreateCompanyInput;
import no.echokarriere.graphql.types.UpdateCompanyInput;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CompanyEntity(
        UUID id,
        String name,
        String homepage,
        OffsetDateTime createdAt,
        OffsetDateTime modifiedAt
) {

    public CompanyEntity(CreateCompanyInput input) {
        this(UUID.randomUUID(), input.getName(), input.getHomepage(), OffsetDateTime.now(), null);
    }

    public CompanyEntity(UUID id, UpdateCompanyInput input) {
        this(id, input.getName(), input.getHomepage(), OffsetDateTime.now(), null);
    }
}
