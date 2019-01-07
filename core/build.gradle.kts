plugins {
    id("java-library")
    id("com.github.dcendents.android-maven")
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    api("com.github.contentful", "contentful.java", "${ext["contentful_version"]}")
    implementation("com.google.code.findbugs", "jsr305", "3.0.2")

    testImplementation("junit", "junit", "4.12")
    testImplementation("com.google.truth", "truth", "0.42")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
