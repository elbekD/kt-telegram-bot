package bot

import java.util.*
import kotlin.concurrent.timer

internal class LongPollingBot(token: String, private val options: PollingOptions) : TelegramBot(token) {
    private var timer: Timer? = null
    private var lastUpdateId = -1

    companion object {
        @JvmStatic
        @Volatile
        private var polling = false
    }

    override fun start() {
        if (!polling) {
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
            polling = true
        }
    }

    override fun stop() {
        if (polling) {
            timer?.cancel()
            timer = null
            polling = false
        }
    }
}