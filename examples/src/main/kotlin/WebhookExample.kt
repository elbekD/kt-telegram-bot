import com.elbekd.bot.Bot
import com.elbekd.bot.model.toChatId
import com.elbekd.bot.server

suspend fun main() {
    val token = "<TOKEN>"
    val username = "<BOT USERNAME>"
    val bot = Bot.createWebhook(username, token) {
        url = "<URL>"
        // below is optional parameters
        // certificate = Paths.get("<PATH TO CERTIFICATE>").toFile()
        // maxConnections = 20
        // allowedUpdates = listOf(AllowedUpdate.Message)
        // setWebhookAutomatically = true

        /*
            Jetty server is used to listen to incoming request from Telegram servers.
            Recommended way to use webhook is to set configured nginx as proxy server.
        */
        server {
            //   run on localhost
            host = "localhost"

            //   and listen to desired port
            port = 5200

            //   configuring TLS layer if no nginx used
            //   tls {
            //       port = 443
            //       keyStorePath = "<PATH TO KEYSTORE>"
            //       keyStorePassword = "<KEYSTORE PASSWORD>"
            //       keyManagerPassword = "<KEY MANAGER PASSWORD>"
            //   }
        }
    }

    bot.onCommand("/start") { (msg, _) ->
        bot.sendMessage(msg.chat.id.toChatId(), "Hello World!")
    }

    bot.onCommand("/echo") { (msg, opts) ->
        bot.sendMessage(msg.chat.id.toChatId(), "${msg.text} ${opts ?: ""}")
    }

    bot.start()
}