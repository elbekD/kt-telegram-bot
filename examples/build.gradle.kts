import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // uncomment
    kotlin("jvm") // version "1.3.61"
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
//    implementation("com.github.elbekD:kt-telegram-bot:1.1.3")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Jar> {
    manifest { attributes["Main-Class"] = "WebhookExampleKt" }
    from(configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) })
}
