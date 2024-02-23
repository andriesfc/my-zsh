pluginManagement {
    includeBuild("build-logic")
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

rootProject.name = "mpt"

include(
    "mpt-app",
    "mpt-commons",
    "mpt-framework",
    "mpt-tool-installer",
    "mpt-tool-jdeveloper",
    "mpt-tool-hostfileutils"
)

