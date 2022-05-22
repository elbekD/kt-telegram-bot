package com.elbekd.bot

import com.elbekd.bot.api.TelegramApi
import com.elbekd.bot.internal.LongPollingBot
import com.elbekd.bot.internal.WebhookBot
import com.elbekd.bot.types.CallbackQuery
import com.elbekd.bot.types.ChosenInlineResult
import com.elbekd.bot.types.InlineQuery
import com.elbekd.bot.types.Message
import com.elbekd.bot.types.PreCheckoutQuery
import com.elbekd.bot.types.ShippingQuery
import com.elbekd.bot.types.Update

public interface Bot : TelegramApi {
    public companion object {
        /**
         * @param token your bot token
         * @param username bot's username without leading @-symbol
         * @param pollingOptions options used to configure [LongPollingBot]
         */
        public fun createPolling(
            token: String,
            username: String? = null,
            pollingOptions: PollingOptions.() -> Unit = { PollingOptions() }
        ): Bot {
            validateToken(token)
            return LongPollingBot(username, token, PollingOptions().apply(pollingOptions))
        }

        /**
         * @param token your bot token
         * @param username bot's username without leading @-symbol
         * @param webhookOptions options used to configure server and webhook params for [setWebhook] method
         */
        public fun createWebhook(
            token: String,
            username: String? = null,
            webhookOptions: WebhookOptions.() -> Unit = { WebhookOptions() }
        ): Bot {
            validateToken(token)
            return WebhookBot(username, token, WebhookOptions().apply(webhookOptions))
        }

        private fun validateToken(token: String) {
            if (token.isBlank())
                throw IllegalArgumentException("Invalid token. Check and try again")
        }
    }

    public fun start()

    public fun stop()

    /**
     * @param action called on [Message] update.
     * Pass `null` to remove handler
     */
    public fun onMessage(action: (suspend (Message) -> Unit)?)

    /**
     * @param action called if [Message] has been edited
     * Pass `null` to remove handler
     */
    public fun onEditedMessage(action: (suspend (Message) -> Unit)?)

    /**
     * @param action called on [channel post][Message] update
     * Pass `null` to remove handler
     */
    public fun onChannelPost(action: (suspend (Message) -> Unit)?)

    /**
     * @param action called if [channel post][Message] has been edited
     * Pass `null` to remove handler
     */
    public fun onEditedChannelPost(action: (suspend (Message) -> Unit)?)

    /**
     * @param action called on [InlineQuery] update
     * Pass `null` to remove handler
     */
    public fun onInlineQuery(action: (suspend (InlineQuery) -> Unit)?)

    /**
     * @param action called on [chosen inline query][ChosenInlineResult] update
     * Pass `null` to remove handler
     */
    public fun onChosenInlineQuery(action: (suspend (ChosenInlineResult) -> Unit)?)

    /**
     * @param action called on [CallbackQuery] update
     * Pass `null` to remove handler
     */
    public fun onCallbackQuery(action: (suspend (CallbackQuery) -> Unit)?)

    /**
     * @param action called on [ShippingQuery] update
     * Pass `null` to remove handler
     */
    public fun onShippingQuery(action: (suspend (ShippingQuery) -> Unit)?)

    /**
     * @param action called on [PreCheckoutQuery] update
     * Pass `null` to remove handler
     */
    public fun onPreCheckoutQuery(action: (suspend (PreCheckoutQuery) -> Unit)?)

    /**
     * @param command bot command which starts with `/`
     * @param action callback for the given `command` with [Message] parameter
     * and optional `argument` parameter. Pass `null` to remove handler
     *
     * Check [Telegram Bot Commands](https://core.telegram.org/bots#commands)
     */
    public fun onCommand(command: String, action: (suspend (Pair<Message, String?>) -> Unit)?)

    /**
     * @param data trigger provided via `callback_data` field of [InlineKeyboardButton][com.elbekd.bot.types.InlineKeyboardButton]
     * @param action callback for the given `data` with [CallbackQuery] parameter.
     * Pass `null` to remove handler
     */
    public fun onCallbackQuery(data: String, action: (suspend (CallbackQuery) -> Unit)?)

    /**
     * @param query trigger provided via `query` field of [InlineQuery]
     * @param action callback for the given `query` with [InlineQuery] parameter.
     * Pass `null` to remove handler
     */
    public fun onInlineQuery(query: String, action: (suspend (InlineQuery) -> Unit)?)

    /**
     * Triggered if no matching update handler found.
     * Pass `null` to remove handler
     */
    public fun onAnyUpdate(action: (suspend (Update) -> Unit)?)
}