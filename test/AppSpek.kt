package no.echokarriere

import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.withTestApplication
import kotlinx.coroutines.runBlocking
import no.echokarriere.configuration.DatabaseConfig
import org.flywaydb.core.Flyway
import org.spekframework.spek2.Spek
import org.spekframework.spek2.dsl.Root
import org.testcontainers.containers.PostgreSQLContainer

@Suppress("unused") // `appRoot` used via indirection
abstract class AppSpek(val appRoot: Root.() -> Unit) : Spek({
    beforeGroup {
        if (!postgresContainer.isRunning) {
            postgresContainer.start()
            flyway.migrate()
        }
    }
}) {
    companion object {
        class AppPostgreSQLContainer : PostgreSQLContainer<AppPostgreSQLContainer>(), DatabaseConfig
        val postgresContainer = AppPostgreSQLContainer()
        val flyway: Flyway = Flyway
            .configure()
            .dataSource(postgresContainer.jdbcUrl, postgresContainer.username, postgresContainer.password)
            .load()

        fun <R> withApp(test: suspend TestApplicationEngine.() -> R) = withTestApplication({
            module(testing = true, database = postgresContainer)
        }) {
            runBlocking {
                test.invoke(this@withTestApplication)
            }
        }
    }
}
