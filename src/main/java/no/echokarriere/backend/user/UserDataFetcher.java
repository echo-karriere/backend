package no.echokarriere.backend.user;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import no.echokarriere.graphql.types.CreateUserInput;
import no.echokarriere.graphql.types.UpdateUserInput;
import no.echokarriere.graphql.types.User;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.UUID;

@RolesAllowed({"ADMIN", "STAFF"})
@DgsComponent
@SuppressWarnings("unused") // Autowired by GraphQL
public class UserDataFetcher {
    private final UserService userService;

    public UserDataFetcher(UserService userService) {
        this.userService = userService;
    }

    @DgsQuery
    public List<User> allUsers() {
        return userService.all();
    }

    @DgsQuery
    public User userById(@InputArgument("id") UUID id) {
        return userService.single(id);
    }

    @DgsMutation
    public User createUser(@InputArgument("input") CreateUserInput input) {
        return userService.create(input);
    }

    @DgsMutation
    public User updateUser(@InputArgument("id") UUID id, @InputArgument("input") UpdateUserInput input) {
        return userService.update(id, input);
    }

    @DgsMutation
    public boolean deleteUser(@InputArgument("id") UUID id) {
        return userService.delete(id);
    }
}
