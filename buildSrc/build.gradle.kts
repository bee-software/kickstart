plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}


dependencies {
    implementation(kotlin("gradle-plugin", "1.5.30"))
    implementation("gradle.plugin.com.google.cloud.tools:jib-gradle-plugin:3.0.0")
}
