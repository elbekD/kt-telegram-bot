package com.elbekd.bot.internal

import com.elbekd.bot.PollingOptions
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

internal class LongPollingBot(
    username: String?,
    token: String,
    private val options: PollingOptions
) : TelegramBot(username, token) {

    private val pollingThread = Executors.newSingleThreadExecutor()

    override fun start() {
        if (pollingThread.isShutdown) return
        pollingThread.submit {
            runBlocking {
                deleteWebhook()
                poll()
            }
        }
    }

    override fun stop() {
        pollingThread.shutdown()
        super.onStop()
    }

    private tailrec suspend fun poll() {
        if (pollingThread.isShutdown) return

        try {
            val id = lastUpdateId
            val offset = if (id != Int.MIN_VALUE) {
                id + 1
            } else {
                when (options.autoOffset) {
                    PollingOptions.AutoOffset.EARLIEST -> null
                    PollingOptions.AutoOffset.LATEST -> -1
                }
            }
            val updates = getUpdates(
                offset = offset,
                allowedUpdates = options.allowedUpdates,
                timeout = options.timeout,
                limit = options.limit
            )

            if (updates.isNotEmpty()) {
                onUpdate(updates)
                lastUpdateId = updates.last().updateId
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        poll()
    }

    private companion object {
        @Volatile
        private var lastUpdateId: Int = Int.MIN_VALUE
    }
}