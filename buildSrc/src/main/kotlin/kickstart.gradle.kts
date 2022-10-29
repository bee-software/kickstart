import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("java-test-fixtures")
}

val buildNumber by extra("0")

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
        apiVersion = "1.7"
        languageVersion = "1.7"
    }
}

val acceptance by sourceSets.creating {
    compileClasspath += sourceSets.main.get().output + sourceSets["testFixtures"].output
    runtimeClasspath += sourceSets.main.get().output + sourceSets["testFixtures"].output
}

val acceptanceImplementation by configurations.getting {
    extendsFrom(configurations.testImplementation.get())
}

configurations[acceptance.runtimeOnlyConfigurationName].extendsFrom(configurations.testRuntimeOnly.get())


tasks.test {
    useJUnitPlatform()

    project.properties["env"]?.let { systemProperty("env.name", it) }

    testLogging {
        events("failed", "standardOut")
        showExceptions = true
        showStackTraces = true
        showCauses = true
        exceptionFormat = TestExceptionFormat.FULL
    }
}

val acceptanceTest = tasks.register<Test>("acceptanceTest") {
    description = "Runs acceptance tests."
    group = "verification"
    useJUnitPlatform()

    testClassesDirs = acceptance.output.classesDirs
    classpath = configurations[acceptance.runtimeClasspathConfigurationName] +
            sourceSets.main.get().output +
            sourceSets["testFixtures"].output +
            acceptance.output

    project.properties["env"]?.let { systemProperty("env.name", it) }

    testLogging {
        events("failed", "standardOut")
        exceptionFormat = TestExceptionFormat.FULL
    }

    shouldRunAfter(tasks.test)
}

tasks.check {
    dependsOn(acceptanceTest)
}

dependencies {
    implementation(kotlin("reflect"))

    testFixturesApi(libs.hamkrest)
    testFixturesApi(libs.junit.api)
    testFixturesApi(libs.konstruct)
    testFixturesApi(libs.minutest)

    testImplementation(kotlin("test"))
}

