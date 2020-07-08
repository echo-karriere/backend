val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val jdbi_version: String by project

plugins {
    application
    kotlin("jvm") version "1.3.70"
    kotlin("plugin.serialization") version "1.3.70"
}

group = "no.echokarriere"
version = "0.0.1"

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

repositories {
    mavenLocal()
    jcenter()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0")

    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")

    implementation("org.jdbi:jdbi3-bom:$jdbi_version")
    implementation("org.jdbi:jdbi3-sqlobject:$jdbi_version")
    implementation("org.jdbi:jdbi3-kotlin:$jdbi_version")
    implementation("org.jdbi:jdbi3-kotlin-sqlobject:$jdbi_version")
    implementation("org.jdbi:jdbi3-postgres:$jdbi_version")
    implementation("org.postgresql:postgresql:42.2.12.jre7")

    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jdbi:jdbi3-testing:$jdbi_version")
    implementation("com.opentable.components:otj-pg-embedded:0.13.3")
}

kotlin {
    sourceSets["main"].kotlin.srcDirs("src")
    sourceSets["test"].kotlin.srcDirs("test")
}

sourceSets["main"].resources.srcDirs("resources")
sourceSets["test"].resources.srcDirs("testresources")
