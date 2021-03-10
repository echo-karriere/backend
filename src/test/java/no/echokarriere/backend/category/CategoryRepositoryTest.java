package no.echokarriere.backend.category;

import no.echokarriere.backend.FlywayMigrationConfig;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(FlywayMigrationConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CategoryRepositoryTest {
    private final UUID categoryId = UUID.randomUUID();
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private FlywayMigrationStrategy strategy;

    @Test
    @Order(1)
    @DisplayName("Can create a new category")
    public void createNewCategory() {
        var actual = categoryRepository.create(
                new CategoryEntity(categoryId, "Test Category", "With a description", "test-category")
        );

        assertThat(actual).isNotEmpty();
        assertThat(actual.get().title()).isEqualTo("Test Category");
        assertThat(actual.get().description()).isEqualTo("With a description");
        assertThat(actual.get().slug()).isEqualTo("test-category");
    }

    @Test
    @Order(2)
    @DisplayName("Can get our created category")
    public void getSingleCategory() {
        var actual = categoryRepository.select(categoryId);

        assertThat(actual).isNotEmpty();
        assertThat(actual.get().id()).isEqualTo(categoryId);
        assertThat(actual.get().title()).isEqualTo("Test Category");
    }

    @Test
    @Order(2)
    @DisplayName("Returns Optional.empty() when no ID matches")
    public void getWrongId() {
        var actual = categoryRepository.select(UUID.randomUUID());

        assertThat(actual).isEmpty();
    }

    @Test
    @Order(2)
    @DisplayName("Category is in all categories query")
    public void getAllCategories() {
        var actual = categoryRepository.selectAll();

        assertThat(actual).anyMatch(item -> item.id().equals(categoryId));
        assertThat(actual).anyMatch(item -> item.title().equals("Test Category"));
    }

    @Test
    @Order(3)
    @DisplayName("Can update category")
    public void updateCategory() {
        var updated = new CategoryEntity(categoryId, "Test Category", "Updated description", "test");
        var actual = categoryRepository.update(updated);


        assertThat(actual).isNotEmpty();
        assertThat(actual.get().title()).isEqualTo("Test Category");
        assertThat(actual.get().description()).isEqualTo("Updated description");
        assertThat(actual.get().slug()).isEqualTo("test");
    }

    @Test
    @Order(4)
    @DisplayName("Deleting with the correct ID removes it")
    public void delete() {
        var actual = categoryRepository.delete(categoryId);

        assertThat(actual).isTrue();
    }

    @Test
    @Order(4)
    @DisplayName("Deleting with an incorrect ID does nothing")
    public void deleteRandom() {
        var actual = categoryRepository.delete(UUID.randomUUID());

        assertThat(actual).isFalse();
    }

    @Test
    @Order(5)
    @DisplayName("Deleting twice does nothing")
    public void deleteAfter() {
        var actual = categoryRepository.delete(categoryId);

        assertThat(actual).isFalse();
    }
}
