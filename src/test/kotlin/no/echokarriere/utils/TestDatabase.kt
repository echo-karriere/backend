package no.echokarriere.utils

import org.jdbi.v3.core.Jdbi
import org.jooq.DSLContext

open class TestDatabase {
    companion object {
        private lateinit var jooqInstance: DSLContext
        private lateinit var jdbiInstance: Jdbi

        fun jdbi(): Jdbi {
            if (!this::jdbiInstance.isInitialized) {
                jdbiInstance = TestDatabaseConfiguration.create()
            }

            return jdbiInstance
        }

        fun jooq(): DSLContext {
            if (!this::jooqInstance.isInitialized) {
                jooqInstance = TestDatabaseConfiguration.initialize()
            }

            return jooqInstance
        }
    }
}
