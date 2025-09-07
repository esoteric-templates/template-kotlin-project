import java.util.jar.Attributes

plugins {
    alias(libs.plugins.kotlin)
    application
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.kotlin.test)
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "org.example.project.AppKt"
}

version = ProcessBuilder("git", "describe", "--tags", "--always", "--dirty")
    .start()
    .inputStream
    .bufferedReader()
    .readText()
    .trim()

tasks {
    withType<AbstractArchiveTask> {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true

        from("LICENSE")

        filePermissions {
            user.read = true
            user.write = true
            user.execute = false

            group.read = true
            group.write = false
            group.execute = false

            other.read = true
            other.write = false
            other.execute = false
        }

        dirPermissions {
            user.read = true
            user.write = true
            user.execute = true

            group.read = true
            group.write = false
            group.execute = true

            other.read = false
            other.write = false
            other.execute = true
        }
    }

    distTar {
        from("assets/text/licenses/") {
            into("licenses/")
        }
    }

    distZip {
        from("assets/text/licenses/") {
            into("licenses/")
        }
    }

    withType<Jar> {
        manifest {
            attributes[Attributes.Name.MAIN_CLASS.toString()] = application.mainClass
        }
    }

    test {
        useJUnitPlatform()
    }
}

configurations.all {
    resolutionStrategy {
        failOnNonReproducibleResolution()
    }
}
