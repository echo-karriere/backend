package no.echokarriere.backend.category;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CategoryServiceTest {
    private Category category;
    @Autowired
    private CategoryService categoryService;

    @BeforeAll
    void setup() {
        category = new Category("Test Category", "With a description", "test-category");
    }

    @Test
    @Order(1)
    @DisplayName("Can create a new category")
    void createNewCategory() {
        var actual = categoryService.create(category);

        assertThat(actual.getId()).isEqualTo(categoryId);
        assertThat(actual.getTitle()).isEqualTo("Test Category");
        assertThat(actual.getDescription()).isEqualTo("With a description");
        assertThat(actual.getSlug()).isEqualTo("test-category");
    }

    @Test
    @Order(2)
    @DisplayName("Can get our created category")
    void getSingleCategory() {
        var actual = categoryService.findById(categoryId);

        assertThat(actual).isNotEmpty();
        assertThat(actual.get().getId()).isEqualTo(categoryId);
        assertThat(actual.get().getTitle()).isEqualTo("Test Category");
    }

    @Test
    @Order(2)
    @DisplayName("Returns Optional.empty() when no ID matches")
    void getWrongId() {
        var actual = categoryService.findById(UUID.randomUUID());

        assertThat(actual).isEmpty();
    }

    @Test
    @Order(2)
    @DisplayName("Category is in all categories query")
    void getAllCategories() {
        var actual = categoryService.findAll();

        assertThat(actual)
                .anyMatch(item -> item.getId().equals(categoryId))
                .anyMatch(item -> item.getTitle().equals("Test Category"));
    }

    @Test
    @Order(3)
    @DisplayName("Can update category")
    void updateCategory() {
        var updated = new Category("Test Category", "Updated description", "test");
        updated.setId(categoryId);
        var actual = categoryService.save(updated);

        assertThat(actual.getTitle()).isEqualTo("Test Category");
        assertThat(actual.getDescription()).isEqualTo("Updated description");
        assertThat(actual.getSlug()).isEqualTo("test");
    }

    @Test
    @Order(4)
    @DisplayName("Deleting with the correct ID removes it")
    void delete() {
        categoryService.deleteById(categoryId);
        var actual = categoryService.findById(categoryId);

        assertThat(actual).isEmpty();
    }

    @Test
    @Order(4)
    @DisplayName("Deleting with an incorrect ID does nothing")
    void deleteRandom() {
        var id = UUID.randomUUID();
        categoryService.deleteById(id);
        var actual = categoryService.findById(id);

        assertThat(actual).isEmpty();
    }
}
