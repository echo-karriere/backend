package no.echokarriere.backend.company.dto;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class CreateCompanyDTO {
    String name;
    String homepage;
}
