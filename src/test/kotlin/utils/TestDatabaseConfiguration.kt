package no.echokarriere.utils

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.postgres.PostgresPlugin
import org.jdbi.v3.sqlobject.kotlin.KotlinSqlObjectPlugin
import org.postgresql.ds.PGSimpleDataSource
import javax.sql.DataSource

object TestDatabaseConfiguration {
    fun create(): Jdbi = Jdbi.create(this.buildDataSource())
        .installPlugin(KotlinPlugin())
        .installPlugin(KotlinSqlObjectPlugin())
        .installPlugin(PostgresPlugin())

    private fun buildDataSource(): DataSource {
        val pgSimpleDataSource = PGSimpleDataSource().apply {
            user = "karriere"
            password = "password" // such safe, much wow
            setURL("jdbc:postgresql://localhost:32782/echokarriere?loggerLevel=OFF")
        }
        val hikariConfig = HikariConfig().apply {
            dataSourceClassName = "org.postgresql.ds.PGSimpleDataSource"
            dataSource = pgSimpleDataSource
        }
        hikariConfig.validate()

        return HikariDataSource(hikariConfig)
    }
}
