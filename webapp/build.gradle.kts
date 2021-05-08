plugins {
    id("kickstart")
}

dependencies {
    implementation(libs.jmustache)
    implementation(libs.konfig)
    implementation(libs.molecule, dependencyConfiguration = { isChanging = true })

    testRuntime(libs.hamcrest.library)
    testkitImplementation(libs.hamkrest)
}
