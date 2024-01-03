
plugins {
    id("application")
    id("mpt-common-conventions")
}


dependencies {

    constraints {
        api("io.insert-koin:koin-core:${mtp_versions.koin}")
        api("io.insert-koin:koin-core-coroutines:${mtp_versions.koin}")
        api("io.insert-koin:koin-logger-slf4j:${mtp_versions.koin}")
        api("com.github.ajalt.clikt:clikt:${mtp_versions.clikt}")
    }
}
