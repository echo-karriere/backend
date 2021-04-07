package no.echokarriere.backend.user;

import no.echokarriere.graphql.types.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserConverter implements Converter<UserEntity, User> {
    @Override
    public User convert(UserEntity source) {
        return new User(
                source.getId().toString(),
                source.getName(),
                source.getEmail(),
                source.isActive(),
                convertUserType(source.getUserType()),
                source.getCreatedAt(),
                source.getModifietAt()
        );
    }

    private no.echokarriere.graphql.types.UserType convertUserType(UserType source) {
        if (source == UserType.ADMIN) {
            return no.echokarriere.graphql.types.UserType.ADMIN;
        } else if (source == UserType.STAFF) {
            return no.echokarriere.graphql.types.UserType.STAFF;
        } else {
            return no.echokarriere.graphql.types.UserType.USER;
        }
    }
}
