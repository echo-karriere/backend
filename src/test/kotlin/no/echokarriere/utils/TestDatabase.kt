package no.echokarriere.utils

import org.jooq.DSLContext

open class TestDatabase {
    companion object {
        private lateinit var jooqInstance: DSLContext

        fun jooq(): DSLContext {
            if (!this::jooqInstance.isInitialized) {
                jooqInstance = TestDatabaseConfiguration.create()
            }

            return jooqInstance
        }
    }
}
