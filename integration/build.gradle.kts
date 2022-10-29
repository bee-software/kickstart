plugins {
    id("kickstart")
    id("org.flywaydb.flyway") version "8.4.3"
}

dependencies {
    implementation(project(":domain"))
    implementation(libs.kabinet)
    implementation(libs.flyway)
    implementation(libs.konfig)
    runtimeOnly(libs.postgres)

    testImplementation(testFixtures(project(":domain")))
    testFixturesImplementation(project(":domain"))
    testFixturesImplementation(libs.kabinet)
    testFixturesImplementation(libs.konfig)
}

tasks.jar {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}
