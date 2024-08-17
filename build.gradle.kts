import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.22"
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("org.openjfx.javafxplugin")
}


buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.guardsquare:proguard-gradle:7.3.0")
    }
}

allprojects {
    apply(plugin = "kotlin")
    apply(plugin = "application")
    apply(plugin = "com.github.johnrengelman.shadow")

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
    }

    java {
        setSourceCompatibility(JavaVersion.VERSION_11.toString())
        setTargetCompatibility(JavaVersion.VERSION_11.toString())
    }
}

dependencies {
    implementation(project("game"))
}

tasks.withType<JavaCompile>().configureEach {
    options.isWarnings = false
    options.isDeprecation = false
    options.isIncremental = true
}

tasks {
    jar {
        destinationDirectory.set(file("${rootProject.buildDir}\\"))
    }
}

val fatJar = task("fatJar", type = Jar::class) {
    this.archiveBaseName.set("${project.name}-fat")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes["Implementation-Title"] = "${project.name}"
        attributes["Implementation-Version"] = version
        attributes["Main-Class"] = "Application"
    }
    from(configurations.runtimeClasspath.get().map({ if (it.isDirectory) it else zipTree(it) }))
    with(tasks.jar.get() as CopySpec)
}

tasks {
    "jar" {
        dependsOn(fatJar)
    }
}