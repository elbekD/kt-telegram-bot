# kt-telegram-bot
[![](https://jitpack.io/v/elbekD/kt-telegram-bot.svg)](https://jitpack.io/#elbekD/kt-telegram-bot)

Convenient way to build Telegram bots using powerful Kotlin language.
Support for [Telegram Bot API 4.1](https://core.telegram.org/bots/api).
Method names are the same as in [API](https://core.telegram.org/bots/api#available-methods).

## Getting started

### Prerequisites
- JDK 8 or higher
- Kotlin 1.3 or higher
- Gradle
- IDE (*optionally*)

### Installation
Gradle
```groovy
repositories {
    maven { url "https://jitpack.io" }
}

dependencies {
    implementation "com.github.elbekD:kt-telegram-bot:$version"
}
```
Or Gradle Kotlin DSL
```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    compile("com.github.elbekD:kt-telegram-bot:${version}")
}
```

## Quick start
```kotlin
fun main(args: Array<String>) {
    val token = "<TOKEN>"
    val bot = Bot.createPolling(token)
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

## [Examples](/examples/src/main/kotlin)
- [Long polling bot](/examples/src/main/kotlin/LongPollingExample.kt)
- [Webhook bot](/examples/src/main/kotlin/WebhookExample.kt)
and [nginx configuration file](/examples/bot.conf)

## Bot methods
See details in [source code](/library/src/main/kotlin/com/github/elbekD/bot/Bot.kt).

### Overview
Common methods
- `start()`
- `stop()`

Event handlers
- `onMessage` -- called on any message
- `removeMessageAction`
- `onEditedMessage` -- called on any edited message
- `removeEditedMessageAction`
- `onChannelPost` -- called on any channel post
- `removeChannelPostAction`
- `onEditedChannelPost` -- called on any edited channel post
- `removeEditedChannelPostAction`
- `onInlineQuery` -- called on any inline query or on specific `query` provided in [`InlineQuery`](/library/src/main/kotlin/com/github/elbekD/bot/types/inline.kt)
- `removeInlineQueryAction`
- `onChosenInlineQuery` -- called on chosen inline query event
- `removeChosenInlineQueryAction`
- `onCallbackQuery` -- called on any callback query or on specific `callback_data` provided in [`InlineKeyboardButton`](/library/src/main/kotlin/com/github/elbekD/bot/types/inline.kt)
- `removeCallbackQueryAction`
- `onShippingQuery` -- called on any shipping query
- `removeShippingQueryAction`
- `onPreCheckoutQuery` -- called on any pre checkout query
- `removePreCheckoutQueryAction`
- `onCommand` -- called on specific command
- `onAnyUpdate` -- called on any update

Helper methods
- `mediaPhoto`
- `mediaVideo`
- `mediaAnimation`
- `mediaAudio`
- `mediaDocument`
