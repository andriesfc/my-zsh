import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.0"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.9.0"
    id("com.google.devtools.ksp") version "1.9.0-1.0.11" //"1.8.22-1.0.11"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.micronaut.application") version "4.0.2"
}

version = "0.1"
group = "andriesfc.powertoys"

val kotlinVersion= project.properties["kotlinVersion"]

val mordantVersion = "2.1.0"

repositories {
    mavenCentral()
}

dependencies {

    ksp("info.picocli:picocli-codegen")
    ksp("io.micronaut.serde:micronaut-serde-processor")
    implementation("info.picocli:picocli")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut.picocli:micronaut-picocli")
    implementation("io.micronaut.serde:micronaut-serde-jackson")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Yaml Config
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")
    runtimeOnly("org.yaml:snakeyaml")

    // Test - Mocking
    testImplementation("io.mockk:mockk:1.13.7")

}

val jvmLevel = 20

application {
    mainClass.set("andriesfc.powertoys.RootCommandLauncher")
    applicationName = "mpt"
}
java {
    sourceCompatibility = JavaVersion.toVersion(jvmLevel)
}

tasks {
    compileKotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_20)
        }
    }
    compileTestKotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_20)
        }
    }
}
micronaut {
    testRuntime("kotest5")
    processing {
        incremental(true)
        annotations("andriesfc.powertoys.*")
    }
}



