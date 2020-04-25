# kt-telegram-bot
[![](https://jitpack.io/v/elbekD/kt-telegram-bot.svg)](https://jitpack.io/#elbekD/kt-telegram-bot)

Convenient way to build Telegram bots using powerful Kotlin language.
Support for [Telegram Bot API 4.8](https://core.telegram.org/bots/api).
Method names are the same as in [API](https://core.telegram.org/bots/api#available-methods).

## Changelog
#### Version 1.3.0
- Updated API to 4.8

#### Version 1.3.0-beta1
- Updated API to 4.7

#### Version 1.3.0-beta
- Added new feature - Chain. Checkout [changelog](./CHANGELOG.md#version-130-beta) for details.
Please feel free to give any feedback for this version in [issue](https://github.com/elbekD/kt-telegram-bot/issues/22).

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
Use [ShadowJar](https://github.com/johnrengelman/shadow) plugin or any other way you like. 

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

# License
MIT License

Copyright (c) 2020 Elbek Djuraev

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
