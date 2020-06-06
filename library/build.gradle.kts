import org.jetbrains.dokka.gradle.DokkaTask

version = "1.3.4"

plugins {
    id("com.github.johnrengelman.shadow") version "5.2.0"
    id("org.jetbrains.dokka") version "0.9.17"
    kotlin("jvm")
    maven
}

repositories {
    jcenter()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.0.1")
    implementation("org.eclipse.jetty:jetty-server:9.4.25.v20191220")
    implementation("org.eclipse.jetty:jetty-servlet:9.4.25.v20191220")
    implementation("com.squareup.okhttp3:okhttp:3.10.0")
    implementation("com.google.code.gson:gson:2.8.5")
    implementation(kotlin("stdlib-jdk8"))
    testImplementation("junit:junit:4.12")
}

tasks.withType<Jar> {
    buildDir = rootProject.buildDir
}

tasks {
    compileKotlin { kotlinOptions.jvmTarget = "1.8" }
}

val dokka by tasks.getting(DokkaTask::class) {
    outputFormat = "html"
    outputDirectory = "$buildDir/dokka"
}