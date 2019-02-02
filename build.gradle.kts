import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    project.apply { from("config.gradle.kts") }

    repositories {
        jcenter()
        mavenCentral()
    }
}

plugins {
    `build-scan`
    id("org.jetbrains.dokka") version "0.9.17"
    kotlin("jvm") version "1.3.10"
}

buildScan {
    setLicenseAgreementUrl("https://gradle.com/terms-of-service")
    setLicenseAgree("yes")
    publishAlways()
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    testImplementation("junit:junit:${project.extra["junit"]}")
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${project.extra["kotlin_coroutine"]}")
    implementation("com.squareup.okhttp3:okhttp:${project.extra["okhttp"]}")
    implementation("com.google.code.gson:gson:${project.extra["gson"]}")
    implementation("org.eclipse.jetty:jetty-server:${project.extra["jetty_server"]}")
}

val compileKotlin by tasks.getting(KotlinCompile::class) {
    kotlinOptions.jvmTarget = "1.8"
}
val compileTestKotlin by tasks.getting(KotlinCompile::class) {
    kotlinOptions.jvmTarget = "1.8"
}

val dokka by tasks.getting(DokkaTask::class) {
    outputFormat = "html"
    outputDirectory = "$buildDir/dokka"
}
