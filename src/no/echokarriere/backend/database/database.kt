package no.echokarriere.backend.database

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.postgres.PostgresPlugin
import org.jdbi.v3.sqlobject.SqlObjectPlugin
import org.jdbi.v3.sqlobject.kotlin.KotlinSqlObjectPlugin

class Database(host: String, port: Int, database: String) {
    init {
        val jdbi = Jdbi.create("jdbc:postgresql://$host:$port/$database")
            .installPlugin(KotlinSqlObjectPlugin())
            .installPlugin(SqlObjectPlugin())
            .installPlugin(KotlinPlugin())
            .installPlugin(PostgresPlugin())
    }
}