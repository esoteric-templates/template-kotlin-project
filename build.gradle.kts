import java.util.jar.Attributes

plugins {
    alias(libs.plugins.kotlin)
    application

    alias(libs.plugins.shadow)
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
    mainClass = "org.example.AppKt"
}

tasks {
    withType<AbstractArchiveTask> {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true

        from("LICENSE")
        from("assets/text/licenses/") {
            into("licenses/")
        }

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

    withType<Jar> {
        manifest {
            attributes[Attributes.Name.MAIN_CLASS.toString()] = application.mainClass
        }
    }

    shadowJar {
        minimize()

        enableAutoRelocation = true
        relocationPrefix = "org.example.dependencies"
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
