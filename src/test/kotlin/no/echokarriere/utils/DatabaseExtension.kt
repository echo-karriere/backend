package no.echokarriere.utils

import org.flywaydb.core.Flyway
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext

class DatabaseExtension : BeforeAllCallback, AfterAllCallback {
    private val flyway = Flyway.configure()
        .locations("filesystem:src/main/resources/db/migrations")
        .dataSource(TestDatabaseConfiguration.buildDataSource())
        .load()

    override fun beforeAll(context: ExtensionContext?) {
        flyway.migrate()
    }

    override fun afterAll(context: ExtensionContext?) {
        flyway.clean()
    }
}
