import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val argonVersion: String by project
val exposedVersion: String by project
val flywayVersion: String by project
val graphqlKotlinVersion: String by project
val graphqlScalarsVersion: String by project
val graphqlVersion: String by project
val hikariVersion: String by project
val jsonPathVersion: String by project
val kotestVersion: String by project
val kotlinLoggingVersion: String by project
val kotlinVersion: String by project
val ktorVersion: String by project
val logbackVersion: String by project
val postgresVersion: String by project
val testContainersVersion: String by project

plugins {
    jacoco
    application
    kotlin("jvm")
    id("org.flywaydb.flyway")
    id("org.jlleitschuh.gradle.ktlint")
    id("org.sonarqube")
}

group = "no.echokarriere"
version = "0.0.1-SNAPSHOT"

tasks.withType<KotlinCompile>().all {
    kotlinOptions {
        jvmTarget = "11"
        // This might unexpectedly break our code in the future, fingers crossed
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=io.ktor.util.KtorExperimentalAPI"
    }
    sourceCompatibility = JavaVersion.VERSION_11.toString()
    targetCompatibility = JavaVersion.VERSION_11.toString()
}

application {
    mainClassName = "no.echokarriere.ApplicationKt"
}

repositories {
    mavenCentral()
    jcenter()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
}

dependencies {
    implementation("io.ktor", "ktor-server-netty", ktorVersion)
    implementation("io.ktor", "ktor-server-core", ktorVersion)
    implementation("io.ktor", "ktor-server-host-common", ktorVersion)
    implementation("io.ktor", "ktor-server-sessions", ktorVersion)
    implementation("io.ktor", "ktor-jackson", ktorVersion)
    implementation("io.ktor", "ktor-auth", ktorVersion)
    implementation("io.ktor", "ktor-auth-jwt", ktorVersion)

    implementation("de.mkammerer", "argon2-jvm", argonVersion)

    implementation("org.jetbrains.exposed", "exposed-core", exposedVersion)
    implementation("org.jetbrains.exposed", "exposed-dao", exposedVersion)
    implementation("org.jetbrains.exposed", "exposed-jdbc", exposedVersion)
    implementation("org.jetbrains.exposed", "exposed-java-time", exposedVersion)

    implementation("org.flywaydb", "flyway-core", flywayVersion)
    implementation("com.zaxxer", "HikariCP", hikariVersion)
    implementation("org.postgresql", "postgresql", postgresVersion)

    implementation("com.graphql-java", "graphql-java", graphqlVersion)
    implementation("com.expediagroup", "graphql-kotlin-schema-generator", graphqlKotlinVersion)
    implementation("com.graphql-java", "graphql-java-extended-scalars", graphqlScalarsVersion)

    implementation("io.github.microutils", "kotlin-logging", kotlinLoggingVersion)
    implementation("ch.qos.logback", "logback-classic", logbackVersion)

    implementation(platform("org.testcontainers:testcontainers-bom:$testContainersVersion"))
    testImplementation("org.testcontainers", "postgresql")
    testImplementation("io.ktor", "ktor-server-tests", ktorVersion)

    testImplementation("io.kotest", "kotest-runner-junit5", kotestVersion)
    testImplementation("io.kotest", "kotest-assertions-core", kotestVersion)
    testImplementation("io.kotest", "kotest-property", kotestVersion)
    testImplementation("io.kotest", "kotest-assertions-ktor", kotestVersion)
    testImplementation("io.kotest", "kotest-assertions-json", kotestVersion)

    testImplementation("io.rest-assured", "json-path", jsonPathVersion)
}

kotlin.sourceSets["main"].kotlin.srcDir("src")
kotlin.sourceSets["test"].kotlin.srcDir("test")

sourceSets["main"].resources.srcDir("resources")
sourceSets["test"].resources.srcDir("testresources")

sourceSets {
    val flyway by creating {
        compileClasspath += sourceSets.main.get().compileClasspath
        runtimeClasspath += sourceSets.main.get().runtimeClasspath
    }
    main {
        output.dir(flyway.output)
    }
}

flyway {
    url = (System.getenv("DB_URL") ?: "jdbc:postgresql://localhost:5432/echokarriere")
    user = (System.getenv("DB_USER") ?: "karriere")
    password = (System.getenv("DB_PASSWORD") ?: "password")
    locations = arrayOf("filesystem:resources/db/migrations")
}

tasks.flywayMigrate { dependsOn("flywayClasses") }
tasks.withType<Test> { useJUnitPlatform() }

tasks.jacocoTestReport {
    reports {
        xml.isEnabled = true
        html.isEnabled = false
    }
}

sonarqube {
    properties {
        property("sonar.projectKey", "echo-karriere_backend")
        property("sonar.organization", "echo-karriere")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}
