# kt-telegram-bot
Telegram Bot Library for Kotlin language

# Installation
Gradle
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compile 'com.github.elbekd:kt-telegram-bot:0.2.0-alpha'
}
```

# Usage
```kotlin
fun main(argv: Array<Strign>) {
    val token = "<TOKEN>"
    val bot = TelegramBot.createPolling(token) {
            limit = 75
            allowedUpdates = arrayOf(AllowedUpdates.Message, AllowedUpdates.ChannelPost)
            timeout = 60
        }
        
    bot.on("/start") { msg ->
            bot.sendMessage(msg.from!!.id, "<i><b>Hello</i></b>", parseMode = "html")
            val m = bot.sendMessage(msg.from.id, "*World*", parseMode = "markdown").await()
            println(m)
        }
         
    bot.start()
}
```
Return type of bot methods is `CompletableFuture<T>`. Also it has extension function `await()`
