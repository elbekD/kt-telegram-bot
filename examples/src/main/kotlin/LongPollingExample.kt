import com.elbekD.bot.Bot

fun main() {
    val token = "<TOKEN>"
    val bot = Bot.createPolling(token) {
        // below is optional parameters
        // limit = 50
        // timeout = 30
        // allowedUpdates = listOf(AllowedUpdates.Message)
        // removeWebhookAutomatically = true
        // period = 1000
    }

    bot.onCommand("/start") { msg, _ ->
        bot.sendMessage(msg.chat.id, "Hello World!")
    }

    bot.onCommand("/echo") { msg, opts ->
        bot.sendMessage(msg.chat.id, "${msg.text} ${opts ?: ""}")
    }

    bot.start()
}