# kt-telegram-bot
Convenient way to build Telegram bots using powerful Kotlin language.
Support for [Telegram Bot API 4.1](https://core.telegram.org/bots/api).
Method names are the same as in [API](https://core.telegram.org/bots/api#available-methods).

## Getting started

### Prerequisites
- JDK 8 or higher
- Kotlin 1.2 or higher
- Gradle
- IDE (*optionally*)

### Installation
Gradle
```groovy
repositories {
    maven { url "https://jitpack.io" }
}

dependencies {
    compile "com.github.elbekD:kt-telegram-bot:1.0.0-alpha"
}
```
Or Gradle Kotlin DSL
```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    compile("com.github.elbekD:kt-telegram-bot:1.0.0-alpha")
}
```

## Quick start
```kotlin
fun main(args: Array<String>) {
    val token = "<TOKEN>"
    val bot = TelegramBot.createPolling(token)
    bot.onCommand("/start") { msg, _ ->
        bot.sendMessage(msg.chat.id, "Hello World!")
    }
    bot.start()
}
```
Return type of bot's API methods is `CompletableFuture<T>`.
Also it has extension suspend function `await()`.

## Deployment
Add to your `build.gradle` file next task:
```groovy
jar {
    manifest {
        attributes 'Main-Class': '<YOUR MAIN>Kt'
    }

    from { configurations.compile.collect { it.isDirectory() ? it : zipTree(it) } }
}
```
Or add to your `build.gradle.kts` file next task:
```kotlin
tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "<YOUR MAIN>Kt"
    }

    from(configurations.compile.map {
        if (it.isDirectory) it else zipTree(it)
    })
}
```
Then run this task. Checkout your `build/libs` directory where you'll find
`.jar` file. Run this file using next line: `java -jar <PATH TO YOUR FILE>.jar` 

## Bot methods
See details in [source code](/src/main/kotlin/com/github/elbekD/bot/Bot.kt).

### Overview
Common methods
- `start()`
- `stop()`

Event handlers
- `onCommand()`
- `onCallbackQuery()`
- `onInlineQuery()`
- `onAnyUpdate()`

Helper methods
- `mediaPhoto()`
- `mediaVideo()`
- `mediaAnimation()`
- `mediaAudio()`
- `mediaDocument()`
