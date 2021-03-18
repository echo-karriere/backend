package no.echokarriere.backend;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class DatabaseExtension implements BeforeAllCallback, AfterAllCallback {
    private Flyway flyway() {
        var config = new FluentConfiguration()
                .dataSource("jdbc:postgresql://localhost:32789/test_db", "karriere", "password");
        return new Flyway(config);
    }

    @Override
    public void afterAll(ExtensionContext context) {
        flyway().clean();
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        flyway().migrate();
    }
}
