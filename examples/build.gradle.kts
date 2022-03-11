import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.6.0"
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

repositories {
    mavenCentral()
    // uncomment
//    maven("https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1")
    implementation(project(":library"))
//    comment/remove the line above and uncomment the line below
//    implementation("com.elbekd:kt-telegram-bot:2.0.0-beta")
}

tasks {
    compileKotlin { kotlinOptions.jvmTarget = "1.8" }
}

tasks.withType<ShadowJar> {
    manifest { attributes["Main-Class"] = "LongPollingExampleKt" }
}