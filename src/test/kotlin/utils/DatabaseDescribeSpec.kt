package no.echokarriere.utils

import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.DescribeSpec
import org.flywaydb.core.Flyway

abstract class DatabaseDescribeSpec(body: DescribeSpec.() -> Unit = {}) : DescribeSpec(body) {
    private val flyway: Flyway = Flyway
        .configure()
        .locations("filesystem:src/main/resources/db/migrations")
        .dataSource(
            System.getProperty("database.default.url"),
            System.getProperty("database.default.username"),
            System.getProperty("database.default.password"),
        ).load()

    override fun beforeSpec(spec: Spec) {
        flyway.migrate()
        super.beforeSpec(spec)
    }

    override fun afterSpec(spec: Spec) {
        flyway.clean()
        super.afterSpec(spec)
    }
}
