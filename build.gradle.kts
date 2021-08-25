import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    jacoco
    id("application")
    id("org.springframework.boot").version("2.5.4")
    id("io.spring.dependency-management").version("1.0.11.RELEASE")
    id("com.netflix.dgs.codegen").version("5.0.5")
    id("org.sonarqube").version("3.3")
    id("org.flywaydb.flyway").version("7.7.0")
    id("nu.studer.jooq").version("6.0")
    id("com.github.ben-manes.versions").version("0.39.0")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot", "spring-boot-starter-web")
    implementation("org.springframework.boot", "spring-boot-starter-actuator")
    implementation("org.springframework.boot", "spring-boot-starter-jooq")
    implementation("org.springframework.boot", "spring-boot-starter-security")
    developmentOnly("org.springframework.boot", "spring-boot-devtools")

    jooqGenerator("org.postgresql", "postgresql", "42.2.23")

    implementation("org.flywaydb", "flyway-core", "7.14.0")
    runtimeOnly("org.postgresql", "postgresql", "42.2.23")

    implementation(platform("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:4.5.1"))
    implementation("com.netflix.graphql.dgs", "graphql-dgs-spring-boot-starter")
    implementation("com.netflix.graphql.dgs", "graphql-dgs-extended-scalars")

    testImplementation("org.springframework.boot", "spring-boot-starter-test") {
        exclude("org.junit.vintage", "junit-vintage-engine")
    }
}

group = "no.echokarriere"
version = "0.0.1-SNAPSHOT"
description = "backend"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(16))
    }
}

tasks.jar {
    enabled = false
}

tasks.withType<Test> {
    useJUnitPlatform()
    failFast = true
    systemProperty("junit.jupiter.testinstance.lifecycle.default", "per_class")
    testLogging {
        events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        exceptionFormat = TestExceptionFormat.FULL
        showExceptions = true
        showStackTraces = true
        showCauses = true
    }
    finalizedBy(tasks.jacocoTestReport)
}

tasks.withType<com.netflix.graphql.dgs.codegen.gradle.GenerateJavaTask> {
    packageName = "no.echokarriere.graphql"
    generateClient = true
}

flyway {
    url = (System.getenv("DATABASE_URL") ?: "jdbc:postgresql://localhost:5432/echokarriere")
    user = (System.getenv("DATABASE_USER") ?: "karriere")
    password = (System.getenv("DATABASE_PASSWORD") ?: "password")
}

jooq {
    configurations {
        create("main") {
            jooqConfiguration.apply {
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = (System.getenv("DATABASE_URL") ?: "jdbc:postgresql://localhost:5432/echokarriere")
                    user = (System.getenv("DATABASE_USER") ?: "karriere")
                    password = (System.getenv("DATABASE_PASSWORD") ?: "password")
                }
                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                    }
                    generate.apply {
                        isDeprecated = false
                        isPojos = false
                        isJavaTimeTypes = true
                    }
                    target.apply {
                        packageName = "no.echokarriere.db"
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}

tasks.named<nu.studer.gradle.jooq.JooqGenerate>("generateJooq") {
    dependsOn(tasks.flywayMigrate)
    inputs.files(fileTree("src/main/resources/db/migration"))
        .withPropertyName("migrations")
        .withPathSensitivity(PathSensitivity.RELATIVE)
    allInputsDeclared.set(true)
    outputs.cacheIf { true }
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        xml.outputLocation.set(File("$buildDir/reports/jacoco/test/jacoco.xml"))
        html.required.set(true)
    }
}

tasks.sonarqube {
    dependsOn(tasks.test)
}

sonarqube {
    properties {
        property("sonar.projectKey", "echo-karriere_backend")
        property("sonar.organization", "echo-karriere")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.sources", "$rootDir/src/main/java")
        property("sonar.coverage.jacoco.xmlReportPaths", "$buildDir/reports/jacoco/test/jacoco.xml")
    }
}
