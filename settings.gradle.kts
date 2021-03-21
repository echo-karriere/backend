rootProject.name = "backend"

pluginManagement {
    val flywayVersion: String by settings
    val jooqPluginVersion: String by settings
    val sonarqubeVersion: String by settings
    val springBootVersion: String by settings
    val springDependencyVersion: String by settings

    plugins {
        id("org.springframework.boot").version(springBootVersion)
        id("io.spring.dependency-management").version(springDependencyVersion)
        id("org.flywaydb.flyway").version(flywayVersion)
        id("nu.studer.jooq").version(jooqPluginVersion)
        id("org.sonarqube").version(sonarqubeVersion)
    }
}
