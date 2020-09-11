package no.echokarriere.utils

import org.jetbrains.exposed.sql.Database
import org.testcontainers.containers.PostgreSQLContainer

class TestDbContainer : PostgreSQLContainer<TestDbContainer>() {
    companion object {
        private lateinit var instance: TestDbContainer

        fun start() {
            if (!Companion::instance.isInitialized) {
                instance = TestDbContainer()
                instance.dockerImageName = "postgres:12"
                instance.start()

                System.setProperty("database.default.url", instance.jdbcUrl)
                System.setProperty("database.default.driver", instance.driverClassName)
                System.setProperty("database.default.username", instance.username)
                System.setProperty("database.default.password", instance.password)

                Database.connect(
                    instance.jdbcUrl,
                    instance.driverClassName,
                    instance.username,
                    instance.password,
                )
            }
        }

        fun stop() {
            if (Companion::instance.isInitialized) instance.stop()
        }
    }
}
