package no.echokarriere.backend.category;

import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import no.echokarriere.graphql.client.*;
import no.echokarriere.graphql.types.Category;
import no.echokarriere.graphql.types.CreateCategoryInput;
import no.echokarriere.graphql.types.UpdateCategoryInput;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CategoryDataFetcherTest {
    @Autowired
    DgsQueryExecutor dgsQueryExecutor;
    private UUID categoryId;

    @Test
    @Order(1)
    @DisplayName("Can create a new category")
    void createNewCategory() {
        var query = new GraphQLQueryRequest(
                CreateCategoryGraphQLQuery.newRequest()
                        .input(CreateCategoryInput.newBuilder()
                                .title("GraphQL Title")
                                .description("Come on and slam")
                                .slug("graphql")
                                .build())
                        .build(),
                new CreateCategoryProjectionRoot().id().title().description().slug()
        );

        var result = dgsQueryExecutor.executeAndExtractJsonPathAsObject(query.serialize(), "data.createCategory", Category.class);
        assertThat(result).isNotNull();

        categoryId = UUID.fromString(result.getId());

        assertThat(result.getTitle()).isEqualTo("GraphQL Title");
        assertThat(result.getDescription()).isEqualTo("Come on and slam");
        assertThat(result.getSlug()).isEqualTo("graphql");
    }

    @Test
    @Order(2)
    @DisplayName("Can get our new category")
    void getSingleCategory() {
        var query = new GraphQLQueryRequest(
                CategoryByIdGraphQLQuery.newRequest()
                        .id(categoryId.toString())
                        .build(),
                new CreateCategoryProjectionRoot().id().title().description().slug()
        );

        var result = dgsQueryExecutor.executeAndExtractJsonPathAsObject(query.serialize(), "data.categoryById", Category.class);
        assertThat(result).isNotNull();

        assertThat(result.getId()).isEqualTo(categoryId.toString());
        assertThat(result.getTitle()).isEqualTo("GraphQL Title");
        assertThat(result.getDescription()).isEqualTo("Come on and slam");
        assertThat(result.getSlug()).isEqualTo("graphql");
    }

    @Test
    @Order(2)
    @DisplayName("Fails for invalid ID")
    void getWrongID() {
        var query = new GraphQLQueryRequest(
                CategoryByIdGraphQLQuery.newRequest()
                        .id(UUID.randomUUID().toString())
                        .build(),
                new CreateCategoryProjectionRoot().id().title().description().slug()
        );

        var result = dgsQueryExecutor.execute(query.serialize());
        assertThat(result.getErrors()).isNotEmpty();
    }


    @Test
    @Order(2)
    @DisplayName("Category is in all categories")
    void getAllCategories() {
        var query = new GraphQLQueryRequest(
                AllCategoriesGraphQLQuery.newRequest().build(),
                new CreateCategoryProjectionRoot().id().title().description().slug()
        );

        List<String> result = dgsQueryExecutor.executeAndExtractJsonPath(query.serialize(), "data.allCategories[*].id");
        assertThat(result).isNotEmpty().contains(categoryId.toString());
    }

    @Test
    @Order(3)
    @DisplayName("Can update a new category")
    void updateExistingCategory() {
        var query = new GraphQLQueryRequest(
                UpdateCategoryGraphQLQuery.newRequest()
                        .id(categoryId.toString())
                        .input(UpdateCategoryInput.newBuilder()
                                .title("GraphQL Title")
                                .description("Is it working?")
                                .slug("gql")
                                .build())
                        .build(),
                new CreateCategoryProjectionRoot().id().title().description().slug()
        );

        var result = dgsQueryExecutor.executeAndExtractJsonPathAsObject(query.serialize(), "data.updateCategory", Category.class);
        assertThat(result).isNotNull();

        assertThat(result.getTitle()).isEqualTo("GraphQL Title");
        assertThat(result.getDescription()).isEqualTo("Is it working?");
        assertThat(result.getSlug()).isEqualTo("gql");
    }

    @Test
    @Order(4)
    @DisplayName("Category can be deleted")
    void delete() {
        var query = new GraphQLQueryRequest(
                DeleteCategoryGraphQLQuery.newRequest().id(categoryId.toString()).build()
        );

        var result = dgsQueryExecutor.executeAndExtractJsonPathAsObject(query.serialize(), "data.deleteCategory", Boolean.class);
        assertThat(result).isNotNull().isTrue();
    }

    @Test
    @Order(4)
    @DisplayName("Deleting with wrong ID does nothing")
    void deleteRandom() {
        var query = new GraphQLQueryRequest(
                DeleteCategoryGraphQLQuery.newRequest().id(UUID.randomUUID().toString()).build()
        );

        var result = dgsQueryExecutor.executeAndExtractJsonPathAsObject(query.serialize(), "data.deleteCategory", Boolean.class);
        assertThat(result).isNotNull().isFalse();
    }

    @Test
    @Order(5)
    @DisplayName("Deleting twice does nothing")
    void deleteAfter() {
        var query = new GraphQLQueryRequest(
                DeleteCategoryGraphQLQuery.newRequest().id(categoryId.toString()).build()
        );

        var result = dgsQueryExecutor.executeAndExtractJsonPathAsObject(query.serialize(), "data.deleteCategory", Boolean.class);
        assertThat(result).isNotNull().isFalse();
    }
}
