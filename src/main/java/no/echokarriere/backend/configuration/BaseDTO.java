package no.echokarriere.backend.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseDTO {
    UUID id = UUID.randomUUID();
    OffsetDateTime createdAt = OffsetDateTime.now();
    OffsetDateTime modifiedAt = null;
}
