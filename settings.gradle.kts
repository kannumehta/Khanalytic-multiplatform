pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://kotlin.bintray.com/kotlinx")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    buildscript {
        repositories {
            mavenCentral()
            maven {
                url = uri("https://storage.googleapis.com/r8-releases/raw")
            }
        }
        dependencies {
            classpath("com.android.tools:r8:8.2.24")
        }
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://kotlin.bintray.com/kotlinx")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

rootProject.name = "Khanalytic"
include(":androidApp")
include(":shared")