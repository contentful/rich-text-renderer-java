plugins {
    id("java")
}


repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation(project(":core"))

    implementation("com.github.contentful", "contentful.java", "add~rich-text-SNAPSHOT")
    implementation("com.google.code.findbugs", "jsr305", "3.0.2")

    testImplementation("junit", "junit", "4.12")
    testImplementation("com.google.truth", "truth", "0.42")
}
