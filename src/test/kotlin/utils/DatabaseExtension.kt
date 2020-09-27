package no.echokarriere.utils

import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext

class DatabaseExtension : BeforeAllCallback, AfterAllCallback {
    private val flyway = Flyway.configure()
        .locations("filesystem:src/main/resources/db/migrations")
        .dataSource("jdbc:postgresql://localhost:32782/echokarriere?loggerLevel=OFF", "karriere", "password")
        .load()

    init {
        Database.connect(
            url = "jdbc:postgresql://localhost:32782/echokarriere?loggerLevel=OFF",
            driver = "org.postgresql.Driver",
            user = "karriere",
            password = "password",
        )
    }

    override fun beforeAll(context: ExtensionContext?) {
        flyway.migrate()
    }

    override fun afterAll(context: ExtensionContext?) {
        flyway.clean()
    }
}
