package no.echokarriere.backend.category;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CategoryRepositoryTest {
    private final UUID categoryId = UUID.randomUUID();
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @Order(1)
    @DisplayName("Can create a new category")
    void createNewCategory() {
        var actual = categoryRepository.create(
                new CategoryEntity(categoryId, "Test Category", "With a description", "test-category", OffsetDateTime.now(), null)
        );

        assertThat(actual).isNotEmpty();
        assertThat(actual.get().title()).isEqualTo("Test Category");
        assertThat(actual.get().description()).isEqualTo("With a description");
        assertThat(actual.get().slug()).isEqualTo("test-category");
    }

    @Test
    @Order(2)
    @DisplayName("Can get our created category")
    void getSingleCategory() {
        var actual = categoryRepository.select(categoryId);

        assertThat(actual).isNotEmpty();
        assertThat(actual.get().id()).isEqualTo(categoryId);
        assertThat(actual.get().title()).isEqualTo("Test Category");
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
                .anyMatch(item -> item.id().equals(categoryId))
                .anyMatch(item -> item.title().equals("Test Category"));
    }

    @Test
    @Order(3)
    @DisplayName("Can update category")
    void updateCategory() {
        var updated = new CategoryEntity(categoryId, "Test Category", "Updated description", "test", null, OffsetDateTime.now());
        var actual = categoryRepository.update(updated);


        assertThat(actual).isNotEmpty();
        assertThat(actual.get().title()).isEqualTo("Test Category");
        assertThat(actual.get().description()).isEqualTo("Updated description");
        assertThat(actual.get().slug()).isEqualTo("test");
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
