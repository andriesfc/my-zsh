@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion


plugins {
    java
    kotlin("jvm")
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

val mptJvmLevel = project.findProperty("mpt.jvm.version")?.toString()?.toInt() ?: 17
val mptJvmTarget = JvmTarget.ofLevel(mptJvmLevel)
val mtpJavaLanguageVersion = mptJvmTarget.javaLanguageVersion()

java {
    toolchain {
        languageVersion.set(mtpJavaLanguageVersion)
    }
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter(mtp_versions.junit5)
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(mptJvmTarget)
        apiVersion.set(KotlinVersion.KOTLIN_1_9)
    }
}

dependencies {
    testImplementation("io.mockk:mockk:${mtp_versions.mock}")
    testImplementation("io.kotest:kotest-runner-junit5:${mtp_versions.kotest}")
    testImplementation("io.kotest:kotest-assertions-core:${mtp_versions.kotest}")
    testImplementation("net.datafaker:datafaker:${mtp_versions.datafaker}")
}

