import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

repositories {
    mavenCentral()
    jcenter()
}

plugins {
    id("org.jetbrains.dokka") version "0.9.17"
    kotlin("jvm") version "1.3.61"
    maven
}
