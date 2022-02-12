plugins {
    id("kickstart")
    id("org.flywaydb.flyway") version "8.4.3"
}

dependencies {
    implementation(project(":domain"))
    implementation(libs.kabinet)
    runtimeOnly(libs.postgres)

    testkitImplementation(project(path = ":domain", configuration = "testkit"))
    testkitImplementation(libs.hamkrest)
    testkitImplementation(libs.flyway)
}

tasks.jar {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}
