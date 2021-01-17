# kt-telegram-bot
[![](https://jitpack.io/v/elbekD/kt-telegram-bot.svg)](https://jitpack.io/#elbekD/kt-telegram-bot)

Convenient way to build Telegram bots using powerful Kotlin language.
Support for [Telegram Bot API 5.0](https://core.telegram.org/bots/api).
Method names are the same as in [API](https://core.telegram.org/bots/api#available-methods).

## Changelog

#### Version 1.3.8
- Fix [issue](https://github.com/elbekD/kt-telegram-bot/issues/35)

#### Version 1.3.7
- Update Bot API to 5.0

#### Version 1.3.6
- Migrate to Kotlin 1.4.20

[Changelog history](./CHANGELOG.md)

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
Maven

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

```xml
<dependency>
    <groupId>com.github.elbekD</groupId>
    <artifactId>kt-telegram-bot</artifactId>
    <version>{version}</version>
</dependency>
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

#### Chain
It is common case when you need to ask the user several questions sequentially and process user errors. Now you can create such chains easily. Sea the example below. Do not forget to call `build()` method at the end.

```kotlin
fun main() {
    val token = "<TOKEN>"
    val username = "<BOT USERNAME>"
    val bot = Bot.createPolling(username, token)

    bot.chain("/start") { msg -> bot.sendMessage(msg.chat.id, "Hi! What is your name?") }
        .then { msg -> bot.sendMessage(msg.chat.id, "Nice to meet you, ${msg.text}! Send something to me") }
        .then { msg -> bot.sendMessage(msg.chat.id, "Fine! See you soon") }
        .build()

    bot.chain(
        label = "location_chain",
        predicate = { msg -> msg.location != null },
        action = { msg ->
            bot.sendMessage(
                msg.chat.id,
                "Fine, u've sent me a location. Is this where you want to order a taxi?(yes|no)"
            )
        })
        .then("answer_choice") { msg ->
            when (msg.text) {
                "yes" -> bot.jumpToAndFire("order_taxi", msg)
                "no" -> bot.jumpToAndFire("cancel_ordering", msg)
                else -> {
                    bot.sendMessage(msg.chat.id, "Oops, I don't understand you. Just answer yes or no?")
                    bot.jumpTo("answer_choice", msg)
                }
            }
        }
        .then("order_taxi", isTerminal = true) { msg -> 
            bot.sendMessage(msg.chat.id, "Fine! Taxi is coming") 
        }
        .then("cancel_ordering", isTerminal = true) { msg -> 
            bot.sendMessage(msg.chat.id, "Ok! See you next time") 
        }
        .build()

    bot.start()
}
```

## Deployment
Use [ShadowJar](https://github.com/johnrengelman/shadow) plugin or any other way you like. 

## [Examples](/examples/src/main/kotlin)
- [Long polling bot](/examples/src/main/kotlin/LongPollingExample.kt)
- [Webhook bot](/examples/src/main/kotlin/WebhookExample.kt)
and [nginx configuration file](/examples/bot.conf)

## Bot methods
See details in [source code](/library/src/main/kotlin/com/elbekD/bot/Bot.kt).

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

# License
[MIT License](./LICENSE.md)
