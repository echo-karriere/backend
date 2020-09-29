rootProject.name = "backend"

pluginManagement {
    val detektVersion: String by settings
    val flywayVersion: String by settings
    val gradleDockerComposeVersion: String by settings
    val gradleKtlintVersion: String by settings
    val kotlinVersion: String by settings
    val sonarqubeVersion: String by settings

    plugins {
        id("org.jetbrains.kotlin.jvm").version(kotlinVersion)
        id("org.flywaydb.flyway").version(flywayVersion)
        id("org.jlleitschuh.gradle.ktlint").version(gradleKtlintVersion)
        id("org.sonarqube").version(sonarqubeVersion)
        id("com.avast.gradle.docker-compose").version(gradleDockerComposeVersion)
        id("io.gitlab.arturbosch.detekt").version(detektVersion)
    }
}
