package no.echokarriere.backend.User;

import no.echokarriere.backend.user.UserEntity;
import no.echokarriere.backend.user.UserRepository;
import no.echokarriere.backend.user.UserType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryTest {
    private final UUID userId = UUID.randomUUID();
    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    @DisplayName("Can create a new User")
    void createNewUser() {
        var actual = userRepository.create(
                new UserEntity(userId, "Test User", "test@example.com", "password", true, UserType.USER, OffsetDateTime.now(), null)
        );

        assertThat(actual).isNotEmpty();
        assertThat(actual.get().getName()).isEqualTo("Test User");
        assertThat(actual.get().getUserType()).isEqualTo(UserType.USER);
        assertThat(actual.get().isActive()).isTrue();
    }

    @Test
    @Order(2)
    @DisplayName("Can get our created user")
    void getSingleUser() {
        var actual = userRepository.select(userId);

        assertThat(actual).isNotEmpty();
        assertThat(actual.get().getId()).isEqualTo(userId);
        assertThat(actual.get().getName()).isEqualTo("Test User");
    }

    @Test
    @Order(2)
    @DisplayName("Returns Optional.empty() when no ID matches")
    void getWrongId() {
        var actual = userRepository.select(UUID.randomUUID());

        assertThat(actual).isEmpty();
    }

    @Test
    @Order(2)
    @DisplayName("User is in all users query")
    void getAllUsers() {
        var actual = userRepository.selectAll();

        assertThat(actual)
                .anyMatch(item -> item.getId().equals(userId))
                .anyMatch(item -> item.getName().equals("Test User"));
    }

    @Test
    @Order(3)
    @DisplayName("Can update user")
    void updateUser() {
        var updated = new UserEntity(userId, "Not Test User", "test@example.com", "hunter42", true, UserType.ADMIN, null, OffsetDateTime.now());
        var actual = userRepository.update(updated);


        assertThat(actual).isNotEmpty();
        assertThat(actual.get().getName()).isEqualTo("Not Test User");
        assertThat(actual.get().getUserType()).isEqualTo(UserType.ADMIN);
    }

    @Test
    @Order(4)
    @DisplayName("Deleting with the correct ID removes it")
    void delete() {
        var actual = userRepository.delete(userId);

        assertThat(actual).isTrue();
    }

    @Test
    @Order(4)
    @DisplayName("Deleting with an incorrect ID does nothing")
    void deleteRandom() {
        var actual = userRepository.delete(UUID.randomUUID());

        assertThat(actual).isFalse();
    }

    @Test
    @Order(5)
    @DisplayName("Deleting twice does nothing")
    void deleteAfter() {
        var actual = userRepository.delete(userId);

        assertThat(actual).isFalse();
    }
}
