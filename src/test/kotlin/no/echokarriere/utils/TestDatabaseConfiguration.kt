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
