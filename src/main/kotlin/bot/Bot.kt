package bot

import bot.http.TelegramApi
import bot.types.*
import java.io.File

// Todo: regex command handler
interface Bot : TelegramApi {
    /**
     * Start the bot
     */
    fun start()

    /**
     * Stop the bot. Manual start needed
     */
    fun stop()

    /**
     * By default this method is triggered on any command.
     * @param command bot command which starts with `/`
     * @param action callback for the given `command` with [Message] parameter
     * and optional `argument` parameter
     *
     * @throws [IllegalArgumentException] if [command] exceeds constraints.
     * Check [Telegram Bot Commands](https://core.telegram.org/bots#commands)
     */
    fun onCommand(command: String = "/*", action: (Message, String?) -> Unit)

    /**
     * By default this method is triggered on any data
     * @param data trigger provided via `callback_data` field of [InlineKeyboardButton][bot.types.InlineKeyboardButton]
     * @param action callback for the given `data` with [CallbackQuery] parameter
     *
     * @throws [IllegalArgumentException] if [data] length not in `[1, 64]` range
     */
    fun onCallbackQuery(data: String = "*", action: (CallbackQuery) -> Unit)

    /**
     * By default this method is triggered on any query
     * @param query trigger provided via `query` field of [InlineQuery]
     * @param action callback for the given `query` with [InlineQuery] parameter
     *
     * @throws [IllegalArgumentException] if [query] length not in `[0, 512]` range
     */
    fun onInlineQuery(query: String = "*", action: (InlineQuery) -> Unit)

    /**
     * Triggered if no matching update handler found. Pass `null` to remove action
     */
    fun onAnyUpdate(action: ((Update) -> Unit)?)

    /**
     * Helper method to create photo media object
     * @param media file_id, url or file_attach_name
     * @throws [IllegalArgumentException] if `attachment` neither `File` nor `String`
     */
    fun mediaPhoto(media: String, attachment: File? = null, caption: String? = null): InputMedia

    /**
     * Helper method to create video media object
     * @param media file_id, url or file_attach_name
     * @throws [IllegalArgumentException] if `attachment` neither `File` nor `String`
     */
    fun mediaVideo(media: String, width: Int, height: Int, duration: Int,
                   attachment: File? = null, caption: String? = null): InputMedia


}