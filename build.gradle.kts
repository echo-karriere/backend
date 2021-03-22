import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

val postgresVersion: String by project
val lombokVersion: String by project

plugins {
    jacoco
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("com.netflix.dgs.codegen")
    id("nu.studer.jooq")
    id("org.sonarqube")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot", "spring-boot-starter-web")
    implementation("org.springframework.boot", "spring-boot-starter-actuator")
    implementation("org.springframework.boot", "spring-boot-starter-jooq")
    implementation("org.springframework.boot", "spring-boot-starter-jdbc")
    developmentOnly("org.springframework.boot", "spring-boot-devtools")

    annotationProcessor("org.projectlombok", "lombok", lombokVersion)
    compileOnly("org.projectlombok", "lombok", lombokVersion)

    implementation(platform("org.jdbi:jdbi3-bom:3.18.0"))
    implementation("org.jdbi", "jdbi3-core")
    implementation("org.jdbi", "jdbi3-spring4")
    implementation("org.jdbi", "jdbi3-sqlobject")
    implementation("org.jdbi", "jdbi3-postgres")

    implementation("org.flywaydb", "flyway-core", "7.7.0")
    implementation("org.jooq", "jooq", "3.14.8")
    runtimeOnly("org.postgresql", "postgresql", postgresVersion)
    jooqGenerator("org.postgresql", "postgresql", postgresVersion)

    implementation("com.netflix.graphql.dgs", "graphql-dgs-spring-boot-starter", "3.9.3")
    implementation("com.graphql-java", "graphql-java-extended-scalars", "15.0.0")

    testImplementation("org.springframework.boot", "spring-boot-starter-test") {
        exclude("org.junit.vintage", "junit-vintage-engine")
    }
}

group = "no.echokarriere"
version = "0.0.1-SNAPSHOT"
description = "backend"

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(15))
    }
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

jooq {
    version.set(dependencyManagement.importedProperties["jooq.version"])
    edition.set(nu.studer.gradle.jooq.JooqEdition.OSS)
    configurations {
        create("main") {
            jooqConfiguration.apply {
                logging = org.jooq.meta.jaxb.Logging.WARN
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
                        packageName = "no.echokarriere"
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}

tasks.named<nu.studer.gradle.jooq.JooqGenerate>("generateJooq") {
    allInputsDeclared.set(true)
    outputs.cacheIf { true }
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.isEnabled = true
        xml.destination = File("$buildDir/reports/jacoco/test/jacoco.xml")
        html.isEnabled = true
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
