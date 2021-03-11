package no.echokarriere.backend.category;

import no.echokarriere.backend.FlywayMigrationConfig;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("integration-test")
@Import(FlywayMigrationConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CategoryRepositoryTest {
    private final UUID categoryId = UUID.randomUUID();
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private FlywayMigrationStrategy strategy;

    @Test
    @Order(1)
    @DisplayName("Can create a new category")
    void createNewCategory() {
        var actual = categoryRepository.create(
                new CategoryEntity(categoryId, "Test Category", "With a description", "test-category")
        );

        assertThat(actual).isNotEmpty();
        assertThat(actual.get().getTitle()).isEqualTo("Test Category");
        assertThat(actual.get().getDescription()).isEqualTo("With a description");
        assertThat(actual.get().getSlug()).isEqualTo("test-category");
    }

    @Test
    @Order(2)
    @DisplayName("Can get our created category")
    void getSingleCategory() {
        var actual = categoryRepository.select(categoryId);

        assertThat(actual).isNotEmpty();
        assertThat(actual.get().getId()).isEqualTo(categoryId);
        assertThat(actual.get().getTitle()).isEqualTo("Test Category");
    }

    @Test
    @Order(2)
    @DisplayName("Returns Optional.empty() when no ID matches")
    void getWrongId() {
        var actual = categoryRepository.select(UUID.randomUUID());

        assertThat(actual).isEmpty();
    }

    @Test
    @Order(2)
    @DisplayName("Category is in all categories query")
    void getAllCategories() {
        var actual = categoryRepository.selectAll();

        assertThat(actual)
                .anyMatch(item -> item.getId().equals(categoryId))
                .anyMatch(item -> item.getTitle().equals("Test Category"));
    }

    @Test
    @Order(3)
    @DisplayName("Can update category")
    void updateCategory() {
        var updated = new CategoryEntity(categoryId, "Test Category", "Updated description", "test");
        var actual = categoryRepository.update(updated);


        assertThat(actual).isNotEmpty();
        assertThat(actual.get().getTitle()).isEqualTo("Test Category");
        assertThat(actual.get().getDescription()).isEqualTo("Updated description");
        assertThat(actual.get().getSlug()).isEqualTo("test");
    }

    @Test
    @Order(4)
    @DisplayName("Deleting with the correct ID removes it")
    void delete() {
        var actual = categoryRepository.delete(categoryId);

        assertThat(actual).isTrue();
    }

    @Test
    @Order(4)
    @DisplayName("Deleting with an incorrect ID does nothing")
    void deleteRandom() {
        var actual = categoryRepository.delete(UUID.randomUUID());

        assertThat(actual).isFalse();
    }

    @Test
    @Order(5)
    @DisplayName("Deleting twice does nothing")
    void deleteAfter() {
        var actual = categoryRepository.delete(categoryId);

        assertThat(actual).isFalse();
    }
}
