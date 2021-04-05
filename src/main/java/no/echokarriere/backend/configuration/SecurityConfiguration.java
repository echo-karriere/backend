package no.echokarriere.backend.configuration;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Value("${auth0.audience}")
    private String audience;
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .cors()
                .configurationSource(corsConfigurationSource())
                .and()
                .authorizeRequests()
                .antMatchers("/graphql")
                .permitAll()
                .antMatchers("/graphiql")
                .permitAll()
                .and()
                .oauth2ResourceServer()
                .jwt()
                .decoder(jwtDecoder())
                .jwtAuthenticationConverter(jwtAuthenticationConverter());
    }

    CorsConfigurationSource corsConfigurationSource() {
        var configuration = new CorsConfiguration();
        configuration.setAllowedMethods(List.of(
                HttpMethod.GET.name(),
                HttpMethod.POST.name()
        ));
        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "https://dev.api.echokarriere.no",
                "https://echokarriere-dev.azurewebsites.net"
        ));

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/graphql", configuration.applyPermitDefaultValues());
        return source;
    }

    JwtDecoder jwtDecoder() {
        OAuth2TokenValidator<Jwt> withAudience = new AudienceValidator(audience);
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
        OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(withAudience, withIssuer);

        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromOidcIssuerLocation(issuer);
        jwtDecoder.setJwtValidator(validator);
        return jwtDecoder;
    }

    JwtAuthenticationConverter jwtAuthenticationConverter() {
        var customAuthoritiesConverter = new CustomAuthoritiesConverter();
        var authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(customAuthoritiesConverter);
        return authenticationConverter;
    }

    private static class CustomAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
        private static final Map<String, String> CLAIMS_TO_AUTH_PREFIX_MAP = Map.of(
                "scope", "SCOPE_",
                "permissions", "PERMISSION_",
                "https://api.echokarriere.no/roles", "ROLE_",
                "https://api.echokarriere.no/group", "GROUP_"
        );

        @Override
        public Collection<GrantedAuthority> convert(@NotNull Jwt source) {
            return CLAIMS_TO_AUTH_PREFIX_MAP.entrySet()
                    .stream()
                    .map(entry -> getAuthorities(source, entry.getKey(), entry.getValue()))
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
        }

        private Collection<GrantedAuthority> getAuthorities(Jwt jwt, String authorityClaimName, String authorityPrefix) {
            var authorities = jwt.getClaim(authorityClaimName);
            if (authorities instanceof String) {
                if (StringUtils.hasText((String) authorities)) {
                    var claims = Arrays.asList(((String) authorities).split(" "));
                    return claims.stream()
                            .map(claim -> new SimpleGrantedAuthority(authorityPrefix + claim))
                            .collect(Collectors.toList());
                } else {
                    return List.of();
                }
            } else if (authorities instanceof Collection) {
                Collection<String> authoritiesCollection = (Collection<String>) authorities;
                return authoritiesCollection.stream()
                        .map(authority -> new SimpleGrantedAuthority(authorityPrefix + authority))
                        .collect(Collectors.toList());
            }
            return List.of();
        }
    }
}
