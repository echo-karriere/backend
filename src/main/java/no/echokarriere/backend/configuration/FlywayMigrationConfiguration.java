package no.echokarriere.backend.configuration;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class FlywayMigrationConfiguration {

    @Bean
    @Primary
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return flyway -> {
            // If we are in a development environment, clean out the database on
            // launch to get a clean slate.
            if (System.getenv("DEV_MODE") != null) {
                flyway.clean();
            }

            flyway.migrate();
        };
    }
}