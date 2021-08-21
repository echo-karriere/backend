package no.echokarriere.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

// TODO: Remove exclusion once Spring Boot 2.6 is released
@SpringBootApplication(exclude = {R2dbcAutoConfiguration.class})
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class BackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}
