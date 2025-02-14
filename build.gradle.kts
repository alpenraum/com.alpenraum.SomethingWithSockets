val kotlin_version: String by project
val logback_version: String by project
val mongo_version: String by project
val koin_version: String by project

val ktorAppName: String by project
val ktorAppVersion: String by project

plugins {
    application
    kotlin("jvm") version "2.1.0"
    id("io.ktor.plugin") version "3.1.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.0"
}

group = "alpenraum.com"
version = ktorAppVersion

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

var env: ReleaseTypes = ReleaseTypes.PRODUCTION

val setDev =
    tasks.register("setDev") {
        env = ReleaseTypes.DEVELOPMENT
    }

task<Exec>("buildDocker") {
    dependsOn("buildFatJar")

    // Get the build variant (default to "release")
    val buildVariant = project.findProperty("buildVariant") ?: "release"

    // Run the Docker build command with the build argument for the variant
    commandLine(
        "docker",
        "build",
        "--build-arg",
        "KTOR_APP_NAME=$ktorAppName",
        "--build-arg",
        "KTOR_APP_VERSION=$ktorAppVersion",
        "--build-arg",
        "BUILD_VARIANT=$buildVariant",
        "-t",
        "${ktorAppName.lowercase()}:$ktorAppVersion",
        ".",
    )

    doLast {
        val buildDir =
            project.layout.buildDirectory
                .get()
                .asFile
        buildDir.mkdirs() // Ensure the build directory exists

        println("Saving Docker image to ${buildDir.absolutePath}}:${ktorAppName.lowercase()}.tar")
        exec {
            commandLine(
                "docker",
                "save",
                "-o",
                "${buildDir.absolutePath}}:${ktorAppName.lowercase()}.tar",
                ktorAppName.lowercase(),
            )
        }
        exec {
            commandLine("rm", "-f", "build}:somethingwithsockets.tar")
        }
//        println("Removing Docker image from local storage...")
//        exec {
//            commandLine("docker", "rmi", ktorAppName.lowercase())
//        }
    }
}

enum class ReleaseTypes {
    DEVELOPMENT,
    PRODUCTION,
}

tasks.processResources {
    outputs.upToDateWhen { false }
    filesMatching("*.yaml") {
        when (env) {
            ReleaseTypes.DEVELOPMENT -> {
                expand(
                    "KTOR_ENV" to "dev",
                    "KTOR_PORT" to "8081",
                    "KTOR_MODULE" to "build",
                    "KTOR_DEV_MODE" to "true",
                )
            }

            ReleaseTypes.PRODUCTION -> {
                expand(
                    "KTOR_ENV" to "production",
                    "KTOR_PORT" to "8080",
                    "KTOR_MODULE" to "",
                    "KTOR_DEV_MODE" to "false",
                )
            }
        }
    }
}

tasks {
    "run" {
        // dependsOn(setDev)
    }
    jar {
        archiveBaseName.set(ktorAppName) // Set your desired name here
        archiveVersion.set(ktorAppVersion) // Optionally, set a version
    }
}

ktor {
    fatJar {
        archiveFileName.set("fat.jar")
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
    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    implementation("io.ktor:ktor-server-double-receive")

    implementation(platform("io.insert-koin:koin-bom:$koin_version"))
    implementation("io.insert-koin:koin-ktor3")
    implementation("io.insert-koin:koin-core")
    implementation("io.insert-koin:koin-core-coroutines")
}
