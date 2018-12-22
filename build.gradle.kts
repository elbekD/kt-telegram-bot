import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    project.apply { from("config.gradle.kts") }

    repositories {
        jcenter()
        mavenCentral()
    }
}

plugins {
    kotlin("jvm") version "1.3.10"
    maven
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    testCompileOnly("junit:junit:${project.extra["junit"]}")
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${project.extra["kotlin_version"]}")
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-core:${project.extra["kotlin_coroutine"]}")
    compile("com.squareup.okhttp3:okhttp:${project.extra["okhttp"]}")
    compile("com.google.code.gson:gson:${project.extra["gson"]}")
    compile("org.eclipse.jetty:jetty-server:${project.extra["jetty_server"]}")
}

val compileKotlin by tasks.getting(KotlinCompile::class) {
    kotlinOptions.jvmTarget = "1.8"
}
val compileTestKotlin by tasks.getting(KotlinCompile::class) {
    kotlinOptions.jvmTarget = "1.8"
}
