import org.gradle.jvm.tasks.Jar

plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile("com.github.elbekD:kt-telegram-bot:1.0.0-alpha")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "LongPollingExampleKt"
    }

    from(configurations.compile.map {
        @Suppress("IMPLICIT_CAST_TO_ANY")
        if (it.isDirectory) it else zipTree(it)
    })
}
