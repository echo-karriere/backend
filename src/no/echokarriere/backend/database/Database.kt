@file:Suppress("EXPERIMENTAL_API_USAGE")

package no.echokarriere.backend.database

import io.ktor.config.ApplicationConfig
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.postgres.PostgresPlugin
import org.jdbi.v3.sqlobject.SqlObjectPlugin
import org.jdbi.v3.sqlobject.kotlin.KotlinSqlObjectPlugin

interface IDatabase {
    val namespaceDao: NamespaceDao
}

class Database(config: ApplicationConfig) : IDatabase {
    override val namespaceDao: NamespaceDao

    init {
        val url = config.propertyOrNull("database.url")?.getString() ?: error("Missing `database.url`")
        val username = config.propertyOrNull("database.username")?.getString() ?: error("Missing `database.username`")
        val password = config.propertyOrNull("database.password")?.getString() ?: error("Missing `database.password`")

        val jdbi = Jdbi.create(url, username, password)
            .installPlugin(KotlinSqlObjectPlugin())
            .installPlugin(SqlObjectPlugin())
            .installPlugin(KotlinPlugin())
            .installPlugin(PostgresPlugin())

        namespaceDao = jdbi.onDemand(NamespaceDao::class.java)
        namespaceDao.createTable()
    }
}
