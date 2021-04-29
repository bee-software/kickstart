import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm")
}

group = "software.bee.kickstart"
version = if (project.hasProperty("version")) project.property("version") as String else "dev"

repositories {
    mavenCentral()

    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots")
    }
}

configurations.all {
    // check for updates every build
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

sourceSets {
    create("testkit") {
        java.srcDir(file("src/testkit/java"))
        java.srcDir(file("src/testkit/main"))
        resources.srcDir(file("src/testkit/resources"))
        compileClasspath += sourceSets["main"].output
        runtimeClasspath += sourceSets["main"].output
    }
}

sourceSets.test {
    compileClasspath += sourceSets["testkit"].output
    runtimeClasspath += sourceSets["testkit"].output
}

val testkitImplementation by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

val testImplementation by configurations.getting {
    extendsFrom(configurations["testkitImplementation"])
}

val testkit by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = true
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
        apiVersion = "1.4"
        languageVersion = "1.4"
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    testkitImplementation(libs.hamkrest)
    testkitImplementation(libs.junit.api)
    testkitImplementation(libs.faker)

    testkit(sourceSets["testkit"].output)
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("failed", "standardOut")
    }
}
