package no.echokarriere.configuration

import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.config.ApplicationConfig
import io.ktor.config.HoconApplicationConfig
import org.jetbrains.exposed.sql.Database
import org.postgresql.ds.PGSimpleDataSource
import org.postgresql.util.PGobject

class DatabaseConfiguration {
    init {
        val config = HoconApplicationConfig(ConfigFactory.load())
        Database.connect(this.dataSource(config))
    }

    private fun dataSource(config: ApplicationConfig): HikariDataSource {
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

class PGEnum<T : Enum<T>>(enumTypeName: String, enumValue: T?) : PGobject() {
    init {
        value = enumValue?.name
        type = enumTypeName
    }
}
