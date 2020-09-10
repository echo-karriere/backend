package no.echokarriere

import io.ktor.application.Application
import io.ktor.config.MapApplicationConfig
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.testcontainers.containers.PostgreSQLContainer

fun Application.testWithDatabase(
    databaseConfiguration: () -> TestDatabaseConfiguration
) {
    databaseConfiguration()
    (environment.config as MapApplicationConfig).apply {
        put("jwt.realm", "testing")
    }
    module()
}

class TestDatabaseConfiguration {
    class AppPostgreSQLContainer : PostgreSQLContainer<AppPostgreSQLContainer>("postgres:12.4")

    init {
        val postgreSQLContainer = AppPostgreSQLContainer()

        postgreSQLContainer.start()

        Flyway.configure()
            .locations("filesystem:resources/db/migrations")
            .dataSource(postgreSQLContainer.jdbcUrl, postgreSQLContainer.username, postgreSQLContainer.password)
            .load()
            .migrate()

        Database.connect(
            postgreSQLContainer.jdbcUrl,
            postgreSQLContainer.driverClassName,
            postgreSQLContainer.username,
            postgreSQLContainer.password
        )
    }
}
