package no.echokarriere.backend.configuration;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.Assert;

public class AudienceValidator implements OAuth2TokenValidator<Jwt> {
    private final String audience;

    public AudienceValidator(String audience) {
        Assert.hasText(audience, "Audience is null or empty");
        this.audience = audience;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        var audiences = token.getAudience();
        if (audiences.contains(this.audience)) {
            return OAuth2TokenValidatorResult.success();
        }

        var err = new OAuth2Error(OAuth2ErrorCodes.INVALID_TOKEN);
        return OAuth2TokenValidatorResult.failure(err);
    }
}
