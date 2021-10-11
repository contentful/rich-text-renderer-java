plugins {
    id("org.jetbrains.kotlin.jvm") version "1.5.0"

    `java-library`
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    api(project(":core"))
    api("com.contentful.java", "java-sdk", "${project.ext["contentful_version"]}")
    implementation("com.google.code.findbugs", "jsr305", "3.0.2")

    testImplementation("junit", "junit", "4.12")
    testImplementation("com.google.truth", "truth", "0.42")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

java.targetCompatibility = JavaVersion.VERSION_1_8