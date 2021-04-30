plugins {
    id("kickstart")
}

dependencies {
    implementation(libs.konfig)
    implementation(libs.molecule, dependencyConfiguration = { isChanging = true })

    runtime(libs.simple)

    testRuntime(libs.hamcrest.library)
    testRuntime(libs.simple)
    testkitImplementation(libs.hamkrest)
}
