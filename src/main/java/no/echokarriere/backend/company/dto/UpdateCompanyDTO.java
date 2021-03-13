package no.echokarriere.backend.company.dto;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class UpdateCompanyDTO {
    String name;
    String homepage;
}
