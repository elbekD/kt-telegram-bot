package bot

internal class WebhookBot(token: String, private val options: WebhookOptions) : TelegramBot(token) {
    override fun start() {
        println(options)
    }

    override fun stop() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}