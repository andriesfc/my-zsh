import org.gradle.api.GradleException
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget


val jvmLevelToTarget: Map<JvmTarget, Int> = JvmTarget.values()
    .associateWith { target ->
        when (target) {
            JvmTarget.JVM_1_8 -> 8
            JvmTarget.JVM_9 -> 9
            JvmTarget.JVM_10 -> 10
            JvmTarget.JVM_11 -> 11
            JvmTarget.JVM_12 -> 12
            JvmTarget.JVM_13 -> 13
            JvmTarget.JVM_14 -> 14
            JvmTarget.JVM_15 -> 15
            JvmTarget.JVM_16 -> 16
            JvmTarget.JVM_17 -> 17
            JvmTarget.JVM_18 -> 18
            JvmTarget.JVM_19 -> 19
            JvmTarget.JVM_20 -> 20
        }
    }

val jvmTargetToLevel: Map<Int, JvmTarget> = jvmLevelToTarget.entries.associate { (t, i) -> i to t }

inline val JvmTarget.level: Int get() = jvmLevelToTarget[this]!!
fun JvmTarget.javaLanguageVersion(): JavaLanguageVersion = JavaLanguageVersion.of(level)
fun JvmTarget.Companion.ofLevelOrNull(level: Int): JvmTarget? = jvmTargetToLevel[level]
fun JvmTarget.Companion.ofLevel(level: Int): JvmTarget = ofLevelOrNull(level)
    ?: throw GradleException(
        "Unsupported jdk level ($level). Only the following levels are supported: ${
            JvmTarget.values().joinToString() { target -> target.level.toString() }
        }"
    )