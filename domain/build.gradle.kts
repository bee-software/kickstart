plugins {
    id("kickstart")
}

tasks.jar {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}
