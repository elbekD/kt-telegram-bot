package bot

import kotlin.concurrent.timer

internal class LongPollingBot(token: String, private val options: PollingOptions) : TelegramBot(token) {
    private val defaultPeriod = 60_000L

    override fun start() {
        var period = defaultPeriod

        if (options.timeout != null)
            period = options.timeout!!.toLong()

        timer("Long polling bot", period = period) {

        }
    }

    override fun stop() {
    }
}