package no.echokarriere.utils

import org.jdbi.v3.core.Jdbi

open class TestDatabase {
    companion object {
        private lateinit var jdbiInstance: Jdbi

        fun jdbi(): Jdbi {
            if (!this::jdbiInstance.isInitialized) {
                jdbiInstance = TestDatabaseConfiguration.create()
            }

            return jdbiInstance
        }
    }
}
