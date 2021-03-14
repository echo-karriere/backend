package no.echokarriere.backend.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class AbstractEntity {
    @Id
    @GeneratedValue
    UUID id;

    @CreatedDate
    OffsetDateTime createdAt;
    @LastModifiedDate
    OffsetDateTime modifiedAt;
}
