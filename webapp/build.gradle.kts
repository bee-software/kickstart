plugins {
    id("kickstart")
}

dependencies {
    implementation(libs.jmustache)
    implementation(libs.konfig)
    implementation(libs.molecule, dependencyConfiguration = { isChanging = true })

    testkitImplementation(libs.skrapeit)
    testkitImplementation(libs.hamkrest)

    testImplementation(libs.hamcrest.library)
}

tasks.jar {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}
