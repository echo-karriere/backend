package no.echokarriere.backend.company;

import no.echokarriere.backend.exception.ResourceNotFoundException;
import no.echokarriere.graphql.types.CreateCompanyInput;
import no.echokarriere.graphql.types.UpdateCompanyInput;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CompanyServiceTest {
    private UUID categoryId;
    @Autowired
    private CompanyService companyService;

    @Test
    @Order(1)
    @DisplayName("Can create a new category")
    void createNewCategory() {
        var actual = companyService.create(new CreateCompanyInput("Bedrift AS", "bedrift.no"));
        categoryId = UUID.fromString(actual.getId());

        assertThat(actual.getName()).isEqualTo("Bedrift AS");
        assertThat(actual.getHomepage()).isEqualTo("bedrift.no");
    }

    @Test
    @Order(2)
    @DisplayName("Can get our created category")
    void getSingleCategory() {
        var actual = companyService.single(categoryId);

        assertThat(actual.getId()).isEqualTo(categoryId.toString());
        assertThat(actual.getName()).isEqualTo("Bedrift AS");
    }

    @Test
    @Order(2)
    @DisplayName("Throws exception when no ID matches")
    void getWrongId() {
        var id = UUID.randomUUID();
        assertThatThrownBy(() -> companyService.single(id)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @Order(2)
    @DisplayName("Category is in all categories")
    void getAllCategories() {
        var actual = companyService.all();

        assertThat(actual)
                .anyMatch(item -> item.getId().equals(categoryId.toString()))
                .anyMatch(item -> item.getName().equals("Bedrift AS"));
    }

    @Test
    @Order(3)
    @DisplayName("Can update category")
    void updateCategory() {
        var updated = new UpdateCompanyInput("Bedrift Inc", "bedrift.com");
        var actual = companyService.update(categoryId, updated);


        assertThat(actual.getName()).isEqualTo("Bedrift Inc");
        assertThat(actual.getHomepage()).isEqualTo("bedrift.com");
    }

    @Test
    @Order(4)
    @DisplayName("Deleting with the correct ID removes it")
    void delete() {
        var actual = companyService.delete(categoryId);

        assertThat(actual).isTrue();
    }

    @Test
    @Order(4)
    @DisplayName("Deleting with an incorrect ID does nothing")
    void deleteRandom() {
        var actual = companyService.delete(UUID.randomUUID());

        assertThat(actual).isFalse();
    }

    @Test
    @Order(5)
    @DisplayName("Deleting twice does nothing")
    void deleteAfter() {
        var actual = companyService.delete(categoryId);

        assertThat(actual).isFalse();
    }
}
