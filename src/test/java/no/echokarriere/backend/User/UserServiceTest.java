package no.echokarriere.backend.User;

import no.echokarriere.backend.exception.ResourceNotFoundException;
import no.echokarriere.backend.user.UserService;
import no.echokarriere.graphql.types.CreateUserInput;
import no.echokarriere.graphql.types.UpdateUserInput;
import no.echokarriere.graphql.types.UserType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {
    private UUID userId;
    @Autowired
    private UserService userService;

    @Test
    @Order(1)
    @DisplayName("Can create a new user")
    void createNewUser() {
        var actual = userService.create(new CreateUserInput("Test Karriere", "test@karriere.no", UserType.USER));
        userId = UUID.fromString(actual.getId());

        assertThat(actual.getName()).isEqualTo("Test Karriere");
        assertThat(actual.getEmail()).isEqualTo("test@karriere.no");
        assertThat(actual.getActive()).isTrue();
        assertThat(actual.getUserType()).isEqualTo(UserType.USER);
    }

    @Test
    @Order(2)
    @DisplayName("Can get our created user")
    void getSingleUser() {
        var actual = userService.single(userId);

        assertThat(actual.getId()).isEqualTo(userId.toString());
        assertThat(actual.getName()).isEqualTo("Test Karriere");
    }

    @Test
    @Order(2)
    @DisplayName("Throws exception when no ID matches")
    void getWrongId() {
        var id = UUID.randomUUID();
        assertThatThrownBy(() -> userService.single(id)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @Order(2)
    @DisplayName("User is in all categories")
    void getAllUsers() {
        var actual = userService.all();

        assertThat(actual)
                .anyMatch(item -> item.getId().equals(userId.toString()))
                .anyMatch(item -> item.getName().equals("Test Karriere"));
    }

    @Test
    @Order(3)
    @DisplayName("Can update category")
    void updateUser() {
        var updated = new UpdateUserInput("Test Student", "test@student.com", UserType.STAFF);
        var actual = userService.update(userId, updated);


        assertThat(actual.getName()).isEqualTo("Test Student");
        assertThat(actual.getEmail()).isEqualTo("test@student.com");
        assertThat(actual.getUserType()).isEqualTo(UserType.STAFF);
    }

    @Test
    @Order(4)
    @DisplayName("Deleting with the correct ID removes it")
    void delete() {
        var actual = userService.delete(userId);

        assertThat(actual).isTrue();
    }

    @Test
    @Order(4)
    @DisplayName("Deleting with an incorrect ID does nothing")
    void deleteRandom() {
        var actual = userService.delete(UUID.randomUUID());

        assertThat(actual).isFalse();
    }

    @Test
    @Order(5)
    @DisplayName("Deleting twice does nothing")
    void deleteAfter() {
        var actual = userService.delete(userId);

        assertThat(actual).isFalse();
    }
}
