rootProject.name = "backend"

pluginManagement {
    plugins {
        id("org.springframework.boot").version("2.4.4")
        id("io.spring.dependency-management").version("1.0.11.RELEASE")
        id("com.netflix.dgs.codegen").version("4.4.1")
        id("org.flywaydb.flyway").version("7.7.0")
        id("nu.studer.jooq").version("5.2.1")
        id("org.sonarqube").version("3.1.1")
    }
}
