plugins {
    id("mpt-cli-application-conventions")
}

dependencies {
    implementation(project(":mpt-foundation"))
    implementation(project(":mpt-fileio"))
    implementation("io.insert-koin:koin-core")
    implementation("io.insert-koin:koin-core-coroutines")
    implementation("io.insert-koin:koin-logger-slf4j")
    implementation("com.github.ajalt.clikt:clikt")
}

application {
    mainClass.set("andriesfc.mpt.cliapp.Launcher")
    applicationName = "mpt"
}