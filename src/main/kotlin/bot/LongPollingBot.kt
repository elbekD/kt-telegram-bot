package bot

import java.util.*
import kotlin.concurrent.timer

internal class LongPollingBot(token: String, private val options: PollingOptions) : TelegramBot(token) {
    private lateinit var timer: Timer
    private var lastUpdateId = -1
    @Volatile
    private var polling = true

    override fun start() {
        timer = timer("LongPollingBot", period = options.period) {
            try {
                val updates = getUpdates(mapOf("offset" to lastUpdateId + 1,
                        "allowed_updates" to options.allowedUpdates,
                        "timeout" to options.timeout,
                        "limit" to options.limit)).get()
                onUpdate(updates)
                lastUpdateId = updates.last().update_id
            } catch (e: Exception) {
                System.err.println(e.message)
            }
        }
    }

    override fun stop() {
        polling = false
        timer.cancel()
    }
}