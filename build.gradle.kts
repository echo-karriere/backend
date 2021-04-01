import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    jacoco
    java
    id("io.freefair.lombok").version("5.3.0")
    id("org.springframework.boot").version("2.4.4")
    id("io.spring.dependency-management").version("1.0.11.RELEASE")
    id("com.netflix.dgs.codegen").version("4.4.1")
    id("org.sonarqube").version("3.1.1")
    id("org.flywaydb.flyway").version("7.7.0")
    id("nu.studer.jooq").version("5.2.1")
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation("org.springframework.boot", "spring-boot-starter-web")
    implementation("org.springframework.boot", "spring-boot-starter-actuator")
    implementation("org.springframework.boot", "spring-boot-starter-jooq")
    developmentOnly("org.springframework.boot", "spring-boot-devtools")

    jooqGenerator("org.postgresql", "postgresql", "42.2.19")

    implementation("org.flywaydb", "flyway-core", "7.7.0")
    runtimeOnly("org.postgresql", "postgresql", "42.2.19")

    implementation(platform("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:3.10.2"))
    implementation("com.netflix.graphql.dgs", "graphql-dgs-spring-boot-starter")
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
        languageVersion.set(JavaLanguageVersion.of(11))
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

flyway {
    url = (System.getenv("DATABASE_URL") ?: "jdbc:postgresql://localhost:5432/echokarriere")
    user = (System.getenv("DATABASE_USER") ?: "karriere")
    password = (System.getenv("DATABASE_PASSWORD") ?: "password")
}

jooq {
    version.set(dependencyManagement.importedProperties["jooq.version"])
    edition.set(nu.studer.gradle.jooq.JooqEdition.OSS)
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
