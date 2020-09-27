package no.echokarriere.utils

import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

class AppPostgreSQLContainer : PostgreSQLContainer<AppPostgreSQLContainer>("postgres:12")

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class JunitTestContainer {
    @Container
    val container = AppPostgreSQLContainer()

    private var flyway: Flyway

    init {
        container.start()

        flyway = Flyway.configure()
            .locations("filesystem:src/main/resources/db/migrations")
            .dataSource(container.jdbcUrl, container.username, container.password)
            .load()

        flyway.migrate()

        Database.connect(
            container.jdbcUrl,
            container.driverClassName,
            container.username,
            container.password,
        )
    }

    @BeforeAll
    fun setup() {
        if (!container.isRunning) {
            container.start()
            flyway.migrate()
        }
    }

    @AfterAll
    fun tearDown() {
        if (container.isRunning) {
            flyway.clean()
            container.stop()
        }
    }
}

class DatabaseExtension : BeforeAllCallback, AfterAllCallback {
    @Container
    val container = AppPostgreSQLContainer()

    private lateinit var flyway: Flyway

    override fun beforeAll(context: ExtensionContext?) {
        container.start()

        flyway = Flyway.configure()
            .locations("filesystem:src/main/resources/db/migrations")
            .dataSource(container.jdbcUrl, container.username, container.password)
            .load()

        flyway.migrate()

        Database.connect(
            container.jdbcUrl,
            container.driverClassName,
            container.username,
            container.password,
        )
    }

    override fun afterAll(context: ExtensionContext?) {
        flyway.clean()
        container.stop()
    }
}
