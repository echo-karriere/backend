package no.echokarriere.configuration

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.config.ApplicationConfig
import mu.KLogging
import org.flywaydb.core.Flyway
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.postgres.PostgresPlugin
import org.jdbi.v3.sqlobject.kotlin.KotlinSqlObjectPlugin
import org.postgresql.ds.PGSimpleDataSource
import javax.sql.DataSource

object DatabaseConfigurator : KLogging() {
    fun create(dataSource: DataSource): Jdbi {
        val jdbi = Jdbi.create(dataSource)
            .installPlugin(KotlinPlugin())
            .installPlugin(KotlinSqlObjectPlugin())
            .installPlugin(PostgresPlugin())

        migrate(dataSource)

        return jdbi
    }

    private fun migrate(dataSource: DataSource) {
        val flyway = Flyway.configure()
            .baselineOnMigrate(true)
            .baselineDescription("InitialMigration")
            .dataSource(dataSource)
            .locations("filesystem:src/main/resources/db/migrations")
            .load()

        logger.info { "Migrating database" }
        flyway.migrate()
    }

    fun buildDataSource(config: ApplicationConfig): HikariDataSource {
        val pgSimpleDataSource = PGSimpleDataSource().apply {
            databaseName = config.propertyOrNull("database.database")?.getString()
                ?: error("Missing `database.database` property")
            user = config.propertyOrNull("database.username")?.getString()
                ?: error("Missing `database.username` property")
            password = config.propertyOrNull("database.password")?.getString()
                ?: error("Missing `database.password` property")
        }
        val hikariConfig = HikariConfig().apply {
            dataSourceClassName = config.propertyOrNull("database.datasource")?.getString()
                ?: error("Missing `database.datasource` property")
            dataSource = pgSimpleDataSource
        }
        hikariConfig.validate()

        return HikariDataSource(hikariConfig)
    }
}
