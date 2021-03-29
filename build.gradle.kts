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
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot", "spring-boot-starter-web")
    implementation("org.springframework.boot", "spring-boot-starter-actuator")
    implementation("org.springframework.boot", "spring-boot-starter-jdbc")
    developmentOnly("org.springframework.boot", "spring-boot-devtools")

    implementation(platform("org.jdbi:jdbi3-bom:3.18.1"))
    implementation("org.jdbi", "jdbi3-core")
    implementation("org.jdbi", "jdbi3-spring4")
    implementation("org.jdbi", "jdbi3-sqlobject")
    implementation("org.jdbi", "jdbi3-postgres")

    implementation("org.flywaydb", "flyway-core", "7.7.0")
    runtimeOnly("org.postgresql", "postgresql", "42.2.19")

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
