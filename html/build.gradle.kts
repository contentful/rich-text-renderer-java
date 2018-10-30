plugins {
    id("java-library")
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    api(project(":core"))
    api("com.github.contentful", "contentful.java", "add~rich-text-SNAPSHOT")
    api("com.google.code.findbugs", "jsr305", "3.0.2")

    testImplementation("junit", "junit", "4.12")
    testImplementation("com.google.truth", "truth", "0.42")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}