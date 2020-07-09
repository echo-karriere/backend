package no.echokarriere.backend

import com.zaxxer.hikari.HikariDataSource
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.postgres.PostgresPlugin
import org.jdbi.v3.sqlobject.SqlObjectPlugin
import org.jdbi.v3.sqlobject.kotlin.KotlinSqlObjectPlugin
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
class DatabaseConfiguration {
    @Bean
    @Qualifier("spring.datasource")
    @ConfigurationProperties(prefix = "spring.datasource")
    fun dataSource(): HikariDataSource = DataSourceBuilder.create().type(HikariDataSource::class.java).build()

    @Bean
    fun dataSourceTransactionManager(@Qualifier("spring.datasource") dataSource: DataSource): DataSourceTransactionManager {
        val dataSourceTransactionManager = DataSourceTransactionManager()
        dataSourceTransactionManager.dataSource = dataSource
        return dataSourceTransactionManager
    }

    @Bean
    fun jdbi(@Qualifier("spring.datasource") dataSource: DataSource): Jdbi = Jdbi
        .create(dataSource)
        .installPlugin(KotlinSqlObjectPlugin())
        .installPlugin(SqlObjectPlugin())
        .installPlugin(KotlinPlugin())
        .installPlugin(PostgresPlugin())
}
