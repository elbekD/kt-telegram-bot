package com.github.elbekD.bot

import java.util.*
import kotlin.concurrent.timer

internal class LongPollingBot(token: String, private val options: PollingOptions) : TelegramBot(token) {
    private var timer: Timer? = null

    companion object {
        @JvmStatic
        @Volatile
        private var polling = false
    }

    override fun start() {
        if (options.removeWebhookAutomatically)
            deleteWebhook().join()
        if (!polling) {
            var lastUpdateId = -1
            timer = timer("LongPollingBot", period = options.period, initialDelay = 2000) {
                try {
                    getUpdates(mapOf(
                            "offset" to lastUpdateId + 1,
                            "allowed_updates" to options.allowedUpdates,
                            "timeout" to options.timeout,
                            "limit" to options.limit)
                    ).thenAccept {
                        onUpdate(it)
                        lastUpdateId = it.last().update_id
                    }.join()
                } catch (e: Exception) {
                    System.err.println(e.message)
                }
            }
            polling = true
        }
    }

    override fun stop() {
        if (polling) {
            super.onStop()
            timer?.cancel()
            timer = null
            polling = false
        }
    }
}