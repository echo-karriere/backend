package no.echokarriere

import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.testcontainers.containers.PostgreSQLContainer

class TestDatabaseConfiguration {
    class AppPostgreSQLContainer : PostgreSQLContainer<AppPostgreSQLContainer>("postgres:12")

    init {
        val postgreSQLContainer = AppPostgreSQLContainer()

        postgreSQLContainer.start()
        val flyway = Flyway
            .configure()
            .dataSource(postgreSQLContainer.jdbcUrl, postgreSQLContainer.username, postgreSQLContainer.password)
            .load()

        flyway.migrate()
        Database.connect(
            postgreSQLContainer.jdbcUrl,
            postgreSQLContainer.driverClassName,
            postgreSQLContainer.username,
            postgreSQLContainer.password
        )
    }
}
