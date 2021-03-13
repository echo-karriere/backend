package no.echokarriere.backend.company;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import no.echokarriere.backend.company.dto.CreateCompanyDTO;
import no.echokarriere.backend.company.dto.UpdateCompanyDTO;

import java.time.OffsetDateTime;
import java.util.UUID;

@Value
@RequiredArgsConstructor
public class CompanyEntity {
    UUID id;
    String name;
    String homepage;
    OffsetDateTime createdAt;
    OffsetDateTime modifiedAt;

    public CompanyEntity(CreateCompanyDTO dto) {
        this.id = UUID.randomUUID();
        this.name = dto.getName();
        this.homepage = dto.getHomepage();
        this.createdAt = OffsetDateTime.now();
        this.modifiedAt = null;
    }

    public CompanyEntity(UUID id, UpdateCompanyDTO dto) {
        this.id = id;
        this.name = dto.getName();
        this.homepage = dto.getHomepage();
        this.createdAt = OffsetDateTime.now();
        this.modifiedAt = null;
    }
}
