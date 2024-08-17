import proguard.gradle.ProGuardTask

group = "com.client"
version = "0.0.1"

plugins {
    id("org.openjfx.javafxplugin")
}

javafx {
    modules("javafx.base")
}

dependencies {
    implementation(project(":runelite"))
    implementation("org.slf4j:slf4j-api:1.7.25")
    implementation("com.google.guava:guava:30.0-android")
    implementation("ch.qos.logback:logback-classic:1.2.9")
    implementation("commons-io:commons-io:2.7")
    // https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp

    implementation(group = "com.squareup.okhttp3", name = "okhttp", version = "4.9.1")
    testImplementation(group = "com.squareup.okhttp3", name = "mockwebserver", version = "4.9.1")

    implementation("org.apache.commons:commons-lang3:3.9")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.7.1")
    implementation("com.dorkbox:Notify:3.7")
    implementation("com.google.code.gson:gson:2.8.9")
// https://mvnrepository.com/artifact/it.unimi.dsi/fastutil
    implementation("it.unimi.dsi:fastutil:8.5.13")
// https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1-Beta")

    // https://github.com/aschoerk/reflections8
    implementation("net.oneandone.reflections8:reflections8:0.11.7")

    // Jackson
    implementation("com.fasterxml.jackson.core:jackson-core:2.8.6")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.7.1")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.8.6")

    implementation(group = "com.google.inject", name = "guice", version = "5.0.1")
    implementation(libs.netty)
    // https://mvnrepository.com/artifact/me.tongfei/progressbar
    implementation("me.tongfei:progressbar:0.9.5")
    implementation("org.jetbrains:annotations:24.0.0")

    // https://mvnrepository.com/artifact/com.thoughtworks.xstream/xstream
    implementation("com.thoughtworks.xstream:xstream:1.4.20")

    implementation("com.displee:rs-cache-library:6.9")
    implementation("one.util:streamex:0.8.1")

    val lombok = module("org.projectlombok", "lombok", "1.18.30")
    compileOnly(lombok)
    annotationProcessor(lombok)
    testCompileOnly(lombok)
    testAnnotationProcessor(lombok)
}

tasks {

    register<JavaExec>("Run-Normal") {
        group = "Runelite"
        description = "Run Runelite in Normal Mode"
        classpath = project.sourceSets.main.get().runtimeClasspath
        mainClass.set("Application")
    }

    register<JavaExec>("Run-Development") {
        group = "Runelite"
        description = "Run Runelite in Development Mode"
        enableAssertions = true
        args = listOf("--developer-mode")
        classpath = project.sourceSets.main.get().runtimeClasspath
        mainClass.set("Application")
    }

}