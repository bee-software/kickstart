import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

val buildNumber by extra("0")

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
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
        apiVersion = "1.6"
        languageVersion = "1.6"
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

    testLogging {
        events("failed", "standardOut")
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
    testkit(sourceSets["testkit"].output)

    testImplementation(kotlin("test"))
}
