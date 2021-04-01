package no.echokarriere.backend.company;

import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import no.echokarriere.graphql.client.*;
import no.echokarriere.graphql.types.Company;
import no.echokarriere.graphql.types.CreateCompanyInput;
import no.echokarriere.graphql.types.UpdateCompanyInput;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CompanyDataFetcherTest {
    @Autowired
    DgsQueryExecutor dgsQueryExecutor;
    private UUID companyId;

    @Test
    @Order(1)
    @DisplayName("Can create a new company")
    void createNewCompany() {
        var query = new GraphQLQueryRequest(
                CreateCompanyGraphQLQuery.newRequest()
                        .input(CreateCompanyInput.newBuilder()
                                .name("Bedrift AS")
                                .homepage("bedrift.no")
                                .build())
                        .build(),
                new CreateCompanyProjectionRoot().id().name().homepage()
        );

        var result = dgsQueryExecutor.executeAndExtractJsonPathAsObject(query.serialize(), "data.createCompany", Company.class);
        assertThat(result).isNotNull();

        companyId = UUID.fromString(result.getId());

        assertThat(result.getName()).isEqualTo("Bedrift AS");
        assertThat(result.getHomepage()).isEqualTo("bedrift.no");
    }

    @Test
    @Order(2)
    @DisplayName("Can get our new company")
    void getSingleCategory() {
        var query = new GraphQLQueryRequest(
                CompanyByIdGraphQLQuery.newRequest()
                        .id(companyId.toString())
                        .build(),
                new CompanyByIdProjectionRoot().id().name().homepage()
        );

        var result = dgsQueryExecutor.executeAndExtractJsonPathAsObject(query.serialize(), "data.companyById", Company.class);
        assertThat(result).isNotNull();

        assertThat(result.getId()).isEqualTo(companyId.toString());
        assertThat(result.getName()).isEqualTo("Bedrift AS");
        assertThat(result.getHomepage()).isEqualTo("bedrift.no");
    }

    @Test
    @Order(2)
    @DisplayName("Fails for invalid ID")
    void getWrongID() {
        var query = new GraphQLQueryRequest(
                CompanyByIdGraphQLQuery.newRequest()
                        .id(UUID.randomUUID().toString())
                        .build(),
                new CreateCompanyProjectionRoot().id().name().homepage()
        );

        var result = dgsQueryExecutor.execute(query.serialize());
        assertThat(result.getErrors()).isNotEmpty();
    }

    @Test
    @Order(2)
    @DisplayName("Company is in all companies")
    void getAllCategories() {
        var query = new GraphQLQueryRequest(
                AllCompaniesGraphQLQuery.newRequest().build(),
                new AllCompaniesProjectionRoot().id().name().homepage()
        );

        List<String> result = dgsQueryExecutor.executeAndExtractJsonPath(query.serialize(), "data.allCompanies[*].id");
        assertThat(result).isNotEmpty().contains(companyId.toString());
    }

    @Test
    @Order(3)
    @DisplayName("Can update our new company")
    void updateExistingCategory() {
        var query = new GraphQLQueryRequest(
                UpdateCompanyGraphQLQuery.newRequest()
                        .id(companyId.toString())
                        .input(UpdateCompanyInput.newBuilder()
                                .name("Bedrift Inc")
                                .homepage("bedrift.com")
                                .build())
                        .build(),
                new UpdateCompanyProjectionRoot().id().name().homepage()
        );

        var result = dgsQueryExecutor.executeAndExtractJsonPathAsObject(query.serialize(), "data.updateCompany", Company.class);
        assertThat(result).isNotNull();

        assertThat(result.getName()).isEqualTo("Bedrift Inc");
        assertThat(result.getHomepage()).isEqualTo("bedrift.com");
    }

    @Test
    @Order(4)
    @DisplayName("Company can be deleted")
    void delete() {
        var query = new GraphQLQueryRequest(
                DeleteCompanyGraphQLQuery.newRequest().id(companyId.toString()).build()
        );

        var result = dgsQueryExecutor.executeAndExtractJsonPathAsObject(query.serialize(), "data.deleteCompany", Boolean.class);
        assertThat(result).isNotNull().isTrue();
    }

    @Test
    @Order(4)
    @DisplayName("Deleting with wrong ID does nothing")
    void deleteRandom() {
        var query = new GraphQLQueryRequest(
                DeleteCompanyGraphQLQuery.newRequest().id(UUID.randomUUID().toString()).build()
        );

        var result = dgsQueryExecutor.executeAndExtractJsonPathAsObject(query.serialize(), "data.deleteCompany", Boolean.class);
        assertThat(result).isNotNull().isFalse();
    }

    @Test
    @Order(5)
    @DisplayName("Deleting twice does nothing")
    void deleteAfter() {
        var query = new GraphQLQueryRequest(
                DeleteCompanyGraphQLQuery.newRequest().id(companyId.toString()).build()
        );

        var result = dgsQueryExecutor.executeAndExtractJsonPathAsObject(query.serialize(), "data.deleteCompany", Boolean.class);
        assertThat(result).isNotNull().isFalse();
    }
}
