import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    // uncomment
    kotlin("jvm") // version "1.3.61"
    id("com.github.johnrengelman.shadow") version "5.2.0"

}

repositories {
    mavenCentral()
    // uncomment
//    maven("https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":library"))
    // comment/remove the line above and uncomment the line below
//    implementation("com.github.elbekD:kt-telegram-bot:1.2.0-beta")
}

tasks {
    compileKotlin { kotlinOptions.jvmTarget = "1.8" }
}

tasks.withType<ShadowJar> {
    manifest { attributes["Main-Class"] = "LongPollingExampleKt" }
}
