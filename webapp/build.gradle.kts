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

    testFixturesApi(libs.skrapeit)
    testFixturesImplementation(libs.molecule)
    testImplementation(testFixtures(project(":domain")))
    testImplementation(libs.hamcrest.library)
}

tasks.jar {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}
