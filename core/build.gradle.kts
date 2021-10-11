plugins {
    id("java-library")
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    api("com.contentful.java", "java-sdk", "10.5.2")
    implementation("com.google.code.findbugs", "jsr305", "3.0.2")

    testImplementation("junit", "junit", "4.12")
    testImplementation("com.google.truth", "truth", "0.42")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

java.targetCompatibility = JavaVersion.VERSION_1_8