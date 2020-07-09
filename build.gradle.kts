import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlin_version: String by project
val logback_version: String by project
val jdbi_version: String by project

plugins {
    id("org.springframework.boot") version "2.3.1.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("org.flywaydb.flyway") version "6.5.0"
    kotlin("jvm") version "1.3.72"
    kotlin("plugin.spring") version "1.3.72"
}

group = "no.echokarriere"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")

    implementation("org.flywaydb:flyway-core")
    implementation("org.postgresql:postgresql:42.2.12")

    implementation("com.expediagroup:graphql-kotlin-spring-server:3.3.1")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

sourceSets {
    val flyway by creating {
        compileClasspath += sourceSets.main.get().compileClasspath
        runtimeClasspath += sourceSets.main.get().runtimeClasspath
    }
    main {
        output.dir(flyway.output)
    }
}

val database = mapOf(
    "url" to (System.getenv("DB_URL") ?: "jdbc:postgresql://localhost:5432/echokarriere"),
    "schema" to "public",
    "user" to (System.getenv("DB_USER") ?: "karriere"),
    "password" to (System.getenv("DB_PASSWORD") ?: "password")
)

flyway {
    url = database["url"]
    user = database["user"]
    password = database["password"]
    schemas = arrayOf(database["schema"])
}

tasks.flywayMigrate { dependsOn("flywayClasses") }

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}
