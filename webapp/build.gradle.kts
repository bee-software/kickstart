plugins {
    id("kickstart")
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":integration"))

    implementation(libs.jmustache)
    implementation(libs.kabinet)
    implementation(libs.konfig)
    implementation(libs.molecule)

    testkitImplementation(project(path = ":domain", configuration = "testkit"))
    testkitImplementation(libs.skrapeit)
    testkitImplementation(libs.hamkrest)

    testImplementation(libs.hamcrest.library)
}

tasks.jar {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}
