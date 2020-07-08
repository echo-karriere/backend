@file:Suppress("EXPERIMENTAL_API_USAGE")

package no.echokarriere.backend

import com.opentable.db.postgres.embedded.EmbeddedPostgres
import no.echokarriere.backend.database.IDatabase
import no.echokarriere.backend.database.NamespaceDao
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.sqlobject.SqlObjectPlugin
import org.jdbi.v3.sqlobject.kotlin.KotlinSqlObjectPlugin

class TestDatabase : IDatabase {
    override val namespaceDao: NamespaceDao

    init {
        val pg = EmbeddedPostgres.start()
        val jdbi = Jdbi.create(pg.postgresDatabase)
        jdbi
            .installPlugin(KotlinSqlObjectPlugin())
            .installPlugin(SqlObjectPlugin())
            .installPlugin(KotlinPlugin())

        namespaceDao = jdbi.onDemand(NamespaceDao::class.java)
        namespaceDao.createTable()
    }
}
