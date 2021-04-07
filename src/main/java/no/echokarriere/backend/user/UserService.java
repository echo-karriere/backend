package no.echokarriere.backend.user;

import no.echokarriere.backend.exception.BadRequestException;
import no.echokarriere.backend.exception.ResourceNotFoundException;
import no.echokarriere.graphql.types.CreateUserInput;
import no.echokarriere.graphql.types.UpdateUserInput;
import no.echokarriere.graphql.types.User;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ConversionService conversionService;

    public UserService(UserRepository userRepository, ConversionService conversionService) {
        this.userRepository = userRepository;
        this.conversionService = conversionService;
    }

    public List<User> all() {
        return userRepository.selectAll()
                .stream()
                .map(it -> conversionService.convert(it, User.class))
                .collect(Collectors.toList());
    }

    public User single(UUID id) {
        return userRepository
                .select(id)
                .map(it -> conversionService.convert(it, User.class))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional
    public User create(CreateUserInput input) {
        var entity = new UserEntity(input);
        return userRepository
                .create(entity)
                .map(it -> conversionService.convert(it, User.class))
                .orElseThrow(() -> new BadRequestException("Could not create user"));
    }

    @Transactional
    public User update(UUID id, UpdateUserInput input) {
        var entity = new UserEntity(id, input);
        return userRepository
                .update(entity)
                .map(it -> conversionService.convert(it, User.class))
                .orElseThrow(() -> new BadRequestException("Could not update user"));
    }

    @Transactional
    public boolean delete(UUID id) {
        return userRepository.delete(id);
    }
}
