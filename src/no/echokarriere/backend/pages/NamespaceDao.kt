package no.echokarriere.backend.pages

import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate

interface NamespaceDao {
    @SqlUpdate(
        """
        CREATE TABLE IF NOT EXISTS namespace
            (
                id          serial  NOT NULL PRIMARY KEY,
                title       text    NOT NULL,
                description text    NOT NULL,
                namespace   text    NOT NULL
            )
    """
    )
    fun createTable()

    @SqlUpdate(
        """
        INSERT INTO namespace(title, description, namespace)
        VALUES (:title, :description, :namespace)
    """
    )
    fun insert(@BindBean namespace: Namespace)

    @SqlQuery("SELECT * FROM namespace")
    fun selectAll(): List<Namespace>
}
