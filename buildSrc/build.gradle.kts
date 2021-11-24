plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}


dependencies {
    implementation(kotlin("gradle-plugin", "1.6.0"))
    implementation("gradle.plugin.com.google.cloud.tools:jib-gradle-plugin:3.0.0")
}
