import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
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

sourceSets {
    create("testkit") {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
}

val testkit by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = true
}

val testkitImplementation by configurations.getting { extendsFrom(configurations.implementation.get()) }
configurations["testImplementation"].extendsFrom(testkitImplementation)

sourceSets.test {
    compileClasspath += sourceSets["testkit"].output
    runtimeClasspath += sourceSets["testkit"].output
}

val acceptance by sourceSets.creating {
    compileClasspath += sourceSets.main.get().output + sourceSets["testkit"].output
    runtimeClasspath += sourceSets.main.get().output + sourceSets["testkit"].output
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
            sourceSets["testkit"].output +
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

    testkitImplementation(libs.hamkrest)
    testkitImplementation(libs.junit.api)
    testkitImplementation(libs.faker)
    testkitImplementation(libs.minutest)
    testkit(sourceSets["testkit"].output)

    testImplementation(kotlin("test"))
}
