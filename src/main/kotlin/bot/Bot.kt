package bot

import bot.http.TelegramApi
import bot.types.CallbackQuery
import bot.types.InputMedia
import bot.types.Message
import java.io.File

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
     * @param trigger bot command which starts with `/`
     * @param action callback for the given `trigger` with [Message] parameter
     */
    fun on(trigger: String, action: (Message) -> Unit)

    /**
     * To invoke on any callback query set `trigger` to `*`
     * @param trigger `callback_data` provided via [InlineKeyboardButton][bot.types.InlineKeyboardButton]
     * @param action callback for the given `trigger` with [CallbackQuery] parameter
     */
    fun onCallbackQuery(trigger: String, action: (CallbackQuery) -> Unit)

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