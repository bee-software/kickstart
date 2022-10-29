
plugins {
    id("kickstart")
    application
    id("com.google.cloud.tools.jib") version "3.2.0"
    id("org.graalvm.buildtools.native") version "0.9.10"
}

application {
    mainClass.set("kickstart.CLI")
}


dependencies {
    implementation(project(":domain"))
    implementation(project(":webapp"))
    implementation(libs.konfig)
    implementation(libs.molecule)

    runtimeOnly(libs.simple)

    testFixturesApi(libs.mario)
    testFixturesApi(libs.selenium.api)
    testFixturesApi(libs.selenium.chromedriver)

    testRuntimeOnly(libs.hamcrest.library)
    testRuntimeOnly(libs.juniversalchardet)
    testRuntimeOnly(libs.simple)
}

graalvmNative {
    binaries {
        named("main") {
            javaLauncher.set(javaToolchains.launcherFor {
                languageVersion.set(JavaLanguageVersion.of(17))
            })

            // Main options
            imageName.set("kickstart") // The name of the native image, defaults to the project name
            //debug.set(true) // Determines if debug info should be generated, defaults to false
            //verbose.set(true) // Add verbose output, defaults to false
            configurationFileDirectories.from(file("src/native"))
            // Advanced options
            buildArgs.add("--allow-incomplete-classpath")
        }
    }
}

jib {
    from {
        image = "openjdk:17-alpine"
    }
    to {
        image = "bee-software/kickstart"
        tags = setOf("$version", "$version.${extra["buildNumber"]}")
    }

    extraDirectories {
        paths {
            path {
                // copies the contents of www.root into '/www' on the container
                setFrom("../webapp/src/www")
                into = "/www"
            }
        }
    }
}