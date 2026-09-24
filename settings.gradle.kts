pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
    resolutionStrategy {
        eachPlugin {
            // AGP 8.13 is the newest line that runs on Gradle 8.x (the wrapper is pinned to 8.14;
            // Gradle 9 needs AGP 9). androidx.core 1.16+ requires AGP 8.6 or newer.
            if (requested.id.id.startsWith("com.android")) {
                useModule("com.android.tools.build:gradle:8.13.2")
            }
        }
    }
}

rootProject.name = "rich-text-renderer"

include ("android")
include ("android_sample")
include ("core")
include ("html")

