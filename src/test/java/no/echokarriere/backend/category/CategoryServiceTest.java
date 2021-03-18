package no.echokarriere.backend.category;

import no.echokarriere.backend.exception.NoSuchElementException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CategoryServiceTest {
    private UUID categoryId;
    @Autowired
    private CategoryService categoryService;

    @Test
    @Order(1)
    @DisplayName("Can create a new category")
    void createNewCategory() {
        var actual = categoryService.create(new CategoryDTO("Test Service", "And description", "service"));
        categoryId = actual.getId();

        assertThat(actual.getTitle()).isEqualTo("Test Service");
        assertThat(actual.getDescription()).isEqualTo("And description");
        assertThat(actual.getSlug()).isEqualTo("service");
    }

    @Test
    @Order(2)
    @DisplayName("Can get our created category")
    void getSingleCategory() {
        var actual = categoryService.single(categoryId);

        assertThat(actual.getId()).isEqualTo(categoryId);
        assertThat(actual.getTitle()).isEqualTo("Test Service");
    }

    @Test
    @Order(2)
    @DisplayName("Throws exception when no ID matches")
    void getWrongId() {
        var id = UUID.randomUUID();
        assertThatThrownBy(() -> categoryService.single(id)).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @Order(2)
    @DisplayName("Category is in all categories")
    void getAllCategories() {
        var actual = categoryService.all();

        assertThat(actual)
                .anyMatch(item -> item.getId().equals(categoryId))
                .anyMatch(item -> item.getTitle().equals("Test Service"));
    }

    @Test
    @Order(3)
    @DisplayName("Can update category")
    void updateCategory() {
        var updated = new CategoryDTO("Test Service", "And a new description", "service");
        var actual = categoryService.update(updated, categoryId);


        assertThat(actual.getTitle()).isEqualTo("Test Service");
        assertThat(actual.getDescription()).isEqualTo("And a new description");
        assertThat(actual.getSlug()).isEqualTo("service");
    }

    @Test
    @Order(4)
    @DisplayName("Deleting with the correct ID removes it")
    void delete() {
        var actual = categoryService.delete(categoryId);

        assertThat(actual).isTrue();
    }

    @Test
    @Order(4)
    @DisplayName("Deleting with an incorrect ID does nothing")
    void deleteRandom() {
        var actual = categoryService.delete(UUID.randomUUID());

        assertThat(actual).isFalse();
    }

    @Test
    @Order(5)
    @DisplayName("Deleting twice does nothing")
    void deleteAfter() {
        var actual = categoryService.delete(categoryId);

        assertThat(actual).isFalse();
    }
}
