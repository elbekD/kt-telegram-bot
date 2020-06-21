import com.elbekD.bot.Bot

fun main() {
    val token = "<TOKEN>"
    val username = "<BOT USERNAME>"
    val bot = Bot.createPolling(username, token) {
        // below is optional parameters
        // limit = 50
        // timeout = 30
        // allowedUpdates = listOf(AllowedUpdate.Message)
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
