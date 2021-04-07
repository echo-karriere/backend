package no.echokarriere.backend.user;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import no.echokarriere.graphql.types.CreateUserInput;
import no.echokarriere.graphql.types.UpdateUserInput;

import java.time.OffsetDateTime;
import java.util.UUID;

@Value
@Builder
@RequiredArgsConstructor
public class UserEntity {
    UUID id;
    String name;
    String email;
    String password;
    boolean active;
    UserType userType;
    OffsetDateTime createdAt;
    OffsetDateTime modifietAt;

    public UserEntity(CreateUserInput input) {
        this.id = UUID.randomUUID();
        this.name = input.getName();
        this.email = input.getEmail();
        // TODO: create random initial password
        this.password = "";
        this.active = true;
        this.userType = convertUserType(input.getUserType());
        this.createdAt = OffsetDateTime.now();
        this.modifietAt = null;
    }

    public UserEntity(UUID id, UpdateUserInput input) {
        this.id = id;
        this.name = input.getName();
        this.email = input.getEmail();
        // TODO: Skip this field when updating?
        this.password = "";
        this.active = true;
        this.userType = convertUserType(input.getUserType());
        this.createdAt = null;
        this.modifietAt = OffsetDateTime.now();
    }

    private UserType convertUserType(no.echokarriere.graphql.types.UserType userType) {
        if (userType == no.echokarriere.graphql.types.UserType.ADMIN) return UserType.ADMIN;
        else if (userType == no.echokarriere.graphql.types.UserType.STAFF) return UserType.STAFF;
        else return UserType.USER;
    }
}
