package no.echokarriere.utils

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.postgresql.ds.PGSimpleDataSource
import javax.sql.DataSource

object TestDatabaseConfiguration {
    fun create(): DSLContext = DSL.using(buildDataSource(), SQLDialect.POSTGRES)

    fun buildDataSource(): DataSource {
        val pgSimpleDataSource = PGSimpleDataSource().apply {
            user = System.getenv("DB_USER") ?: "karriere"
            password = System.getenv("DB_PASSWORD") ?: "password"
            setURL(
                System.getenv("DB_URL") ?: "jdbc:postgresql://localhost:32782/echokarriere?loggerLevel=OFF"
            )
        }
        val hikariConfig = HikariConfig().apply {
            dataSourceClassName = "org.postgresql.ds.PGSimpleDataSource"
            dataSource = pgSimpleDataSource
        }
        hikariConfig.validate()

        return HikariDataSource(hikariConfig)
    }
}
