rootProject.name = "backend"

pluginManagement {
    val detektVersion: String by settings
    val flywayVersion: String by settings
    val gradleKtlintVersion: String by settings
    val jooqPluginVersion: String by settings
    val kotlinVersion: String by settings
    val sonarqubeVersion: String by settings

    plugins {
        id("org.jetbrains.kotlin.kapt").version(kotlinVersion)
        id("org.jetbrains.kotlin.jvm").version(kotlinVersion)
        id("org.flywaydb.flyway").version(flywayVersion)
        id("org.jlleitschuh.gradle.ktlint").version(gradleKtlintVersion)
        id("nu.studer.jooq").version(jooqPluginVersion)
        id("org.sonarqube").version(sonarqubeVersion)
        id("io.gitlab.arturbosch.detekt").version(detektVersion)
    }
}
