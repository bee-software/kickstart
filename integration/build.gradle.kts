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

    testkitImplementation(project(path = ":domain", configuration = "testkit"))
    testkitImplementation(libs.hamkrest)
}

tasks.jar {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}
