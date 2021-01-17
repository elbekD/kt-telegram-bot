# Changelog

## Version 1.3.8
- Fix [issue](https://github.com/elbekD/kt-telegram-bot/issues/35)

## Version 1.3.7
- Update Bot API to 5.0

## Version 1.3.6
- Migrate to Kotlin 1.4.20

## Version 1.3.5
- Support ByteArray type for file sending

## Version 1.3.4
- Support API 4.9 changes

## Version 1.3.3
- Added some extension functions to work with the keyboard. See [sources](/library/src/main/kotlin/com/elbekD/bot/util/keyboard) for details

## Version 1.3.2
- Apply suggestion from issue #24 for editTextMessage; changed argument order in createNewStickerSet and addStickerToSet

## Version 1.3.1
- Apply suggestion from issue #24

## Version 1.3.0
- Updated API to 4.8

## Version 1.3.0-beta1
- Updated API to 4.7

## Version 1.3.0-beta
Added new feature - Chain. It is common case when you need to ask the user several
questions sequentially and process user errors. Now you can create such chains easily.
Sea the example below. Do not forget to call `build()` method at the end =)

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
