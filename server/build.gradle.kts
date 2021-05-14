plugins {
    id("kickstart")
    application
    id("com.google.cloud.tools.jib")
}

application {
    mainClass.set("kickstart.CLI")
}

dependencies {
    implementation(project(":webapp"))
    implementation(libs.konfig)
    implementation(libs.molecule, dependencyConfiguration = { isChanging = true })

    runtime(libs.simple)

    testRuntime(libs.hamcrest.library)
    testRuntime(libs.juniversalchardet)
    testRuntime(libs.simple)
    testkitImplementation(libs.hamkrest)
}

jib {
    from {
        image = "openjdk:16-alpine"
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