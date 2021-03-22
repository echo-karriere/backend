package no.echokarriere.backend.company;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CompanyRepositoryTest {
    private final UUID companyId = UUID.randomUUID();
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private FlywayMigrationStrategy strategy;

    @Test
    @Order(1)
    @DisplayName("Can create a new company")
    void createNewCompany() {
        var actual = companyRepository.create(
                new CompanyEntity(companyId, "Norge AS", "http://www.norge.no", OffsetDateTime.now(), null)
        );

        assertThat(actual).isNotEmpty();
        assertThat(actual.get().getName()).isEqualTo("Norge AS");
        assertThat(actual.get().getHomepage()).isEqualTo("http://www.norge.no");
    }

    @Test
    @Order(2)
    @DisplayName("Can get our created company")
    void getSingleCompany() {
        var actual = companyRepository.select(companyId);

        assertThat(actual).isNotEmpty();
        assertThat(actual.get().getId()).isEqualTo(companyId);
        assertThat(actual.get().getName()).isEqualTo("Norge AS");
    }

    @Test
    @Order(2)
    @DisplayName("Returns Optional.empty() when no ID matches")
    void getWrongId() {
        var actual = companyRepository.select(UUID.randomUUID());

        assertThat(actual).isEmpty();
    }

    @Test
    @Order(2)
    @DisplayName("Company is in all categories query")
    void getAllCategories() {
        var actual = companyRepository.selectAll();

        assertThat(actual)
                .anyMatch(item -> item.getId().equals(companyId))
                .anyMatch(item -> item.getName().equals("Norge AS"));
    }

    @Test
    @Order(3)
    @DisplayName("Can update company")
    void updateCompany() {
        var updated = new CompanyEntity(companyId, "Norge Associates", "http://www.norge.nu", null, OffsetDateTime.now());
        var actual = companyRepository.update(updated);


        assertThat(actual).isNotEmpty();
        assertThat(actual.get().getName()).isEqualTo("Norge Associates");
        assertThat(actual.get().getHomepage()).isEqualTo("http://www.norge.nu");
    }

    @Test
    @Order(4)
    @DisplayName("Deleting with the correct ID removes it")
    void delete() {
        var actual = companyRepository.delete(companyId);

        assertThat(actual).isTrue();
    }

    @Test
    @Order(4)
    @DisplayName("Deleting with an incorrect ID does nothing")
    void deleteRandom() {
        var actual = companyRepository.delete(UUID.randomUUID());

        assertThat(actual).isFalse();
    }

    @Test
    @Order(5)
    @DisplayName("Deleting twice does nothing")
    void deleteAfter() {
        var actual = companyRepository.delete(companyId);

        assertThat(actual).isFalse();
    }
}
