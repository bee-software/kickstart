plugins {
    id("kickstart")
}

dependencies {
    testkitImplementation(libs.hamkrest)
}

tasks.jar {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}
