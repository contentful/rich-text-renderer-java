pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id.startsWith("com.android")) {
                useModule("com.android.tools.build:gradle:7.4.0-beta02")
            }
            if (requested.id.id.startsWith("org.jetbrains.kotlin")) {
                useVersion("1.8.0")
            }
        }
    }
}

rootProject.name = "rich-text-renderer"

include ("android")
include ("android_sample")
include ("core")
include ("html")

