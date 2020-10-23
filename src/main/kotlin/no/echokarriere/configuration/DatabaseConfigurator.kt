package no.echokarriere.configuration

import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.config.HoconApplicationConfig
import mu.KLogging
import org.flywaydb.core.Flyway
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.postgresql.ds.PGSimpleDataSource
import javax.sql.DataSource

object DatabaseConfigurator : KLogging() {
    fun create(dataSource: DataSource): DSLContext {
        migrate(dataSource)
        return DSL.using(dataSource, SQLDialect.POSTGRES)
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

    fun buildDataSource(): HikariDataSource {
        val config = HoconApplicationConfig(ConfigFactory.load())
        val pgSimpleDataSource = PGSimpleDataSource().apply {
            databaseName = config.propertyOrNull("database.database")?.getString()
                ?: error("Missing `database.database` property")
            user = config.propertyOrNull("database.username")?.getString()
                ?: error("Missing `database.username` property")
            password = config.propertyOrNull("database.password")?.getString()
                ?: error("Missing `database.password` property")
            portNumbers = config.propertyOrNull("database.port")?.getList()?.map { it.toInt() }?.toIntArray()
                ?: error("Missing `database.port` property")
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
