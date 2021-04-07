package no.echokarriere.backend.user;

import no.echokarriere.db.enums.Usertype;
import org.jooq.impl.EnumConverter;

@SuppressWarnings("unused") // Used by jOOQ
public class UserTypeConverter extends EnumConverter<Usertype, UserType> {
    public UserTypeConverter(Class<Usertype> fromType, Class<UserType> toType) {
        super(fromType, toType);
    }
}
