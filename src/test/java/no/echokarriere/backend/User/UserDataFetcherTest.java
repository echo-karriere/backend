package no.echokarriere.backend.User;

import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import no.echokarriere.graphql.client.*;
import no.echokarriere.graphql.types.CreateUserInput;
import no.echokarriere.graphql.types.UpdateUserInput;
import no.echokarriere.graphql.types.User;
import no.echokarriere.graphql.types.UserType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDataFetcherTest {
    @Autowired
    DgsQueryExecutor dgsQueryExecutor;
    private UUID userId;

    @Test
    @Order(1)
    @DisplayName("Can create a new user")
    void createNewUser() {
        var query = new GraphQLQueryRequest(
                CreateUserGraphQLQuery.newRequest()
                        .input(CreateUserInput.newBuilder()
                                .name("Ola Nordmann")
                                .email("ola@nordmann.no")
                                .userType(UserType.USER)
                                .build())
                        .build(),
                new CreateUserProjectionRoot().id().name().email().userType()
        );

        var result = dgsQueryExecutor.executeAndExtractJsonPathAsObject(query.serialize(), "data.createUser", User.class);
        assertThat(result).isNotNull();

        userId = UUID.fromString(result.getId());

        assertThat(result.getName()).isEqualTo("Ola Nordmann");
        assertThat(result.getEmail()).isEqualTo("ola@nordmann.no");
        assertThat(result.getUserType()).isEqualTo(UserType.USER);
    }

    @Test
    @Order(2)
    @DisplayName("Can get our new user")
    void getSingleUser() {
        var query = new GraphQLQueryRequest(
                UserByIdGraphQLQuery.newRequest()
                        .id(userId.toString())
                        .build(),
                new UserByIdProjectionRoot().id().name().email()
        );

        var result = dgsQueryExecutor.executeAndExtractJsonPathAsObject(query.serialize(), "data.userById", User.class);
        assertThat(result).isNotNull();

        assertThat(result.getId()).isEqualTo(userId.toString());
        assertThat(result.getName()).isEqualTo("Ola Nordmann");
        assertThat(result.getEmail()).isEqualTo("ola@nordmann.no");
    }

    @Test
    @Order(2)
    @DisplayName("Fails for invalid ID")
    void getWrongID() {
        var query = new GraphQLQueryRequest(
                UserByIdGraphQLQuery.newRequest()
                        .id(UUID.randomUUID().toString())
                        .build(),
                new CreateUserProjectionRoot().id().name().email()
        );

        var result = dgsQueryExecutor.execute(query.serialize());
        assertThat(result.getErrors()).isNotEmpty();
    }

    @Test
    @Order(2)
    @DisplayName("User is in all users")
    void getAllUsers() {
        var query = new GraphQLQueryRequest(
                AllUsersGraphQLQuery.newRequest().build(),
                new AllUsersProjectionRoot().id().name().email()
        );

        List<String> result = dgsQueryExecutor.executeAndExtractJsonPath(query.serialize(), "data.allUsers[*].id");
        assertThat(result).isNotEmpty().contains(userId.toString());
    }

    @Test
    @Order(3)
    @DisplayName("Can update our new user")
    void updateExistingUser() {
        var query = new GraphQLQueryRequest(
                UpdateUserGraphQLQuery.newRequest()
                        .id(userId.toString())
                        .input(UpdateUserInput.newBuilder()
                                .name("Kari Nordmann")
                                .email("kari@nordmann.no")
                                .userType(UserType.ADMIN)
                                .build())
                        .build(),
                new UpdateUserProjectionRoot().id().name().email().userType()
        );

        var result = dgsQueryExecutor.executeAndExtractJsonPathAsObject(query.serialize(), "data.updateUser", User.class);
        assertThat(result).isNotNull();

        assertThat(result.getName()).isEqualTo("Kari Nordmann");
        assertThat(result.getEmail()).isEqualTo("kari@nordmann.no");
        assertThat(result.getUserType()).isEqualTo(UserType.ADMIN);
    }

    @Test
    @Order(4)
    @DisplayName("User can be deleted")
    void delete() {
        var query = new GraphQLQueryRequest(
                DeleteUserGraphQLQuery.newRequest().id(userId.toString()).build()
        );

        var result = dgsQueryExecutor.executeAndExtractJsonPathAsObject(query.serialize(), "data.deleteUser", Boolean.class);
        assertThat(result).isNotNull().isTrue();
    }

    @Test
    @Order(4)
    @DisplayName("Deleting with wrong ID does nothing")
    void deleteRandom() {
        var query = new GraphQLQueryRequest(
                DeleteUserGraphQLQuery.newRequest().id(UUID.randomUUID().toString()).build()
        );

        var result = dgsQueryExecutor.executeAndExtractJsonPathAsObject(query.serialize(), "data.deleteUser", Boolean.class);
        assertThat(result).isNotNull().isFalse();
    }

    @Test
    @Order(5)
    @DisplayName("Deleting twice does nothing")
    void deleteAfter() {
        var query = new GraphQLQueryRequest(
                DeleteUserGraphQLQuery.newRequest().id(userId.toString()).build()
        );

        var result = dgsQueryExecutor.executeAndExtractJsonPathAsObject(query.serialize(), "data.deleteUser", Boolean.class);
        assertThat(result).isNotNull().isFalse();
    }
}
