
val kotlin_version: String by project
val logback_version: String by project
val mongo_version: String by project
val koin_version: String by project

plugins {
    kotlin("jvm") version "2.0.0"
    id("io.ktor.plugin") version "2.3.12"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
}

group = "example.com"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

var env = "production"

val setDev = tasks.register("setDev") {
    env = "development"
}

tasks.processResources {
    outputs.upToDateWhen { false }
    filesMatching("*.yaml") {
        when (env) {
            "development" -> {
                expand(
                    "KTOR_ENV" to "dev",
                    "KTOR_PORT" to "8081",
                    "KTOR_MODULE" to "build",
                    "KTOR_DEV_MODE" to "true"
                )
            }
            "production" -> {
                expand(
                    "KTOR_ENV" to "production",
                    "KTOR_PORT" to "8080",
                    "KTOR_MODULE" to "",
                    "KTOR_DEV_MODE" to "false"
                )
            }
        }
    }
}

tasks{
    "run" {
        dependsOn(setDev)
    }
}


repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-websockets-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("org.mongodb:mongodb-driver-core:$mongo_version")
    implementation("org.mongodb:mongodb-driver-sync:$mongo_version")
    implementation("org.mongodb:bson:$mongo_version")
    implementation("io.ktor:ktor-server-swagger-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-config-yaml")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    implementation("io.ktor:ktor-server-double-receive")


    implementation(platform("io.insert-koin:koin-bom:$koin_version"))
    implementation( "io.insert-koin:koin-ktor")
    implementation( "io.insert-koin:koin-core")
    implementation ("io.insert-koin:koin-core-coroutines")
}
