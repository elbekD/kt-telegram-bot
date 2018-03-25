package bot

internal class LongPollingBot(token: String, private val options: PollingOptions): TelegramBot(token) {

    override fun start() {
    }

    override fun stop() {
    }
}