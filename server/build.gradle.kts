plugins {
    id("kickstart")
    application
    id("com.google.cloud.tools.jib")
}

application {
    mainClass.set("kickstart.CLI")
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":webapp"))
    implementation(libs.konfig)
    implementation(libs.molecule, dependencyConfiguration = { isChanging = true })

    runtimeOnly(libs.simple)

    testkitImplementation(libs.hamkrest)
    testkitImplementation(libs.mario)
    testkitImplementation(libs.selenium.api)
    testkitImplementation(libs.selenium.chromedriver)

    testRuntimeOnly(libs.hamcrest.library)
    testRuntimeOnly(libs.juniversalchardet)
    testRuntimeOnly(libs.simple)
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