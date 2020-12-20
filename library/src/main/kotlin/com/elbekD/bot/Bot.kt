package com.elbekD.bot

import com.elbekD.bot.http.TelegramApi
import com.elbekD.bot.types.CallbackQuery
import com.elbekD.bot.types.ChosenInlineResult
import com.elbekD.bot.types.InlineQuery
import com.elbekD.bot.types.InputMedia
import com.elbekD.bot.types.Message
import com.elbekD.bot.types.MessageEntity
import com.elbekD.bot.types.PreCheckoutQuery
import com.elbekD.bot.types.ShippingQuery
import com.elbekD.bot.types.Update
import java.io.File

public interface Bot : TelegramApi {
    public companion object {
        /**
         * @param username bot's username without leading @-symbol
         * @param token your bot token
         * @param pollingOptions options used to configure [LongPollingBot]
         */
        @JvmStatic
        public fun createPolling(
            username: String,
            token: String,
            pollingOptions: PollingOptions.() -> Unit = { PollingOptions() }
        ): Bot {
            validateToken(token)
            return LongPollingBot(username, token, PollingOptions().apply(pollingOptions))
        }

        /**
         * @param username bot's username without leading @-symbol
         * @param token your bot token
         * @param webhookOptions options used to configure server and webhook params for [setWebhook] method
         */
        @JvmStatic
        public fun createWebhook(
            username: String,
            token: String,
            webhookOptions: WebhookOptions.() -> Unit = { WebhookOptions() }
        ): Bot {
            validateToken(token)
            return WebhookBot(username, token, WebhookOptions().apply(webhookOptions))
        }

        private fun validateUsername(username: String) {
            if (username.isBlank() || username.startsWith("@"))
                throw IllegalArgumentException("Invalid username. Check and try again")
        }

        private fun validateToken(token: String) {
            if (token.isBlank())
                throw IllegalArgumentException("Invalid token. Check and try again")
        }
    }

    /**
     * Start the bot
     */
    public fun start()

    /**
     * Stop the bot. Manual start needed
     */
    public fun stop()

    /**
     * @param action called on [Message] update
     */
    public fun onMessage(action: suspend (Message) -> Unit)

    /**
     * Removes [Message] update action
     */
    public fun removeMessageAction()

    /**
     * @param action called if [Message] has been edited
     */
    public fun onEditedMessage(action: suspend (Message) -> Unit)

    /**
     * Removes edited [Message] update action
     */
    public fun removeEditedMessageAction()

    /**
     * @param action called on [channel post][Message] update
     */
    public fun onChannelPost(action: suspend (Message) -> Unit)

    /**
     * Removes [channel post][Message] update action
     */
    public fun removeChannelPostAction()

    /**
     * @param action called if [channel post][Message] has been edited
     */
    public fun onEditedChannelPost(action: suspend (Message) -> Unit)

    /**
     * Removes [edited channel post][Message] update action
     */
    public fun removeEditedChannelPostAction()

    /**
     * @param action called on [InlineQuery] update
     */
    public fun onInlineQuery(action: suspend (InlineQuery) -> Unit)

    /**
     * Removes [InlineQuery] update action
     */
    public fun removeInlineQueryAction()

    /**
     * @param action called on [chosen inline query][ChosenInlineResult] update
     */
    public fun onChosenInlineQuery(action: suspend (ChosenInlineResult) -> Unit)

    /**
     * Removes [chosen inline query][ChosenInlineResult] update action
     */
    public fun removeChosenInlineQueryAction()

    /**
     * @param action called on [CallbackQuery] update
     */
    public fun onCallbackQuery(action: suspend (CallbackQuery) -> Unit)

    /**
     * Removes [CallbackQuery] update action
     */
    public fun removeCallbackQueryAction()

    /**
     * @param action called on [ShippingQuery] update
     */
    public fun onShippingQuery(action: suspend (ShippingQuery) -> Unit)

    /**
     * Removes [ShippingQuery] update action
     */
    public fun removeShippingQueryAction()

    /**
     * @param action called on [PreCheckoutQuery] update
     */
    public fun onPreCheckoutQuery(action: suspend (PreCheckoutQuery) -> Unit)

    /**
     * Removes [PreCheckoutQuery] update action
     */
    public fun removePreCheckoutQueryAction()

    /**
     * @param command bot command which starts with `/`
     * @param action callback for the given `command` with [Message] parameter
     * and optional `argument` parameter
     *
     * @throws [IllegalArgumentException] if [command] exceeds constraints.
     * Check [Telegram Bot Commands](https://core.telegram.org/bots#commands)
     */
    public fun onCommand(command: String, action: suspend (Message, String?) -> Unit)

    /**
     * @param data trigger provided via `callback_data` field of [InlineKeyboardButton][com.elbekD.bot.types.InlineKeyboardButton]
     * @param action callback for the given `data` with [CallbackQuery] parameter
     *
     * @throws [IllegalArgumentException] if [data] length not in `[1, 64]` range
     */
    public fun onCallbackQuery(data: String, action: suspend (CallbackQuery) -> Unit)

    /**
     * @param query trigger provided via `query` field of [InlineQuery]
     * @param action callback for the given `query` with [InlineQuery] parameter
     *
     * @throws [IllegalArgumentException] if [query] length not in `[0, 512]` range
     */
    public fun onInlineQuery(query: String, action: suspend (InlineQuery) -> Unit)

    /**
     * Triggered if no matching update handler found.
     */
    public fun onAnyUpdate(action: suspend (Update) -> Unit)

    /**
     * Helper method to create photo media object
     * @param media file_id, url or file_attach_name
     */
    public fun mediaPhoto(
        media: String,
        attachment: File? = null,
        caption: String? = null,
        parseMode: String? = null,
        captionEntities: List<MessageEntity>? = null,
    ): InputMedia

    /**
     * Helper method to create video media object
     * @param media file_id, url or file_attach_name
     */
    public fun mediaVideo(
        media: String,
        attachment: File? = null,
        thumb: File? = null,
        caption: String? = null,
        parseMode: String? = null,
        captionEntities: List<MessageEntity>? = null,
        width: Int? = null,
        height: Int? = null,
        duration: Int? = null,
        supportsStreaming: Boolean? = null
    ): InputMedia

    /**
     * Helper method to create animation media object
     * @param media file_id, url or file_attach_name
     */
    public fun mediaAnimation(
        media: String,
        attachment: File? = null,
        thumb: File? = null,
        caption: String? = null,
        parseMode: String? = null,
        captionEntities: List<MessageEntity>? = null,
        width: Int? = null,
        height: Int? = null,
        duration: Int? = null
    ): InputMedia

    /**
     * Helper method to create audio media object
     * @param media file_id, url or file_attach_name
     */
    public fun mediaAudio(
        media: String,
        attachment: File? = null,
        thumb: File? = null,
        caption: String? = null,
        parseMode: String? = null,
        captionEntities: List<MessageEntity>? = null,
        duration: Int? = null,
        performer: String? = null,
        title: String? = null
    ): InputMedia

    /**
     * Helper method to create document media object
     * @param media file_id, url or file_attach_name
     */
    public fun mediaDocument(
        media: String,
        attachment: File? = null,
        thumb: File? = null,
        caption: String? = null,
        parseMode: String? = null,
        captionEntities: List<MessageEntity>? = null,
        disableContentTypeDetection: Boolean? = null
    ): InputMedia
}