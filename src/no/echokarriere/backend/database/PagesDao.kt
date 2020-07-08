package no.echokarriere.backend.database

import org.jdbi.v3.sqlobject.statement.SqlUpdate

interface PagesDao {
    @SqlUpdate(
        """
        CREATE TABLE IF NOT EXISTS pages (
            
        )
    """
    )
    fun createTable()
}
