package com.elbekD.bot

import java.util.Timer
import kotlin.concurrent.timer

internal class LongPollingBot(
    username: String,
    token: String,
    private val options: PollingOptions
) : TelegramBot(username, token) {
    private var timer: Timer? = null

    private companion object {
        @Volatile
        private var polling = false
    }

    override fun start() {
        if (options.removeWebhookAutomatically)
            deleteWebhook().join()

        if (!polling) {
            var lastUpdateId = -1L
            timer = timer("LongPollingBot", period = options.period) {
                try {
                    getUpdates(
                        mapOf(
                            "offset" to lastUpdateId + 1,
                            "allowed_updates" to options.allowedUpdates,
                            "timeout" to options.timeout,
                            "limit" to options.limit
                        )
                    ).thenAccept {
                        if (it.isNotEmpty()) {
                            onUpdate(it)
                            lastUpdateId = it.last().update_id
                        }
                    }.join()
                } catch (e: Exception) {
                    e.printStackTrace()
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