package bot

import bot.http.TelegramApi
import bot.types.InputMedia
import bot.types.KeyboardButton
import bot.types.Message
import bot.types.ReplyKeyboard
import java.io.File

interface Bot : TelegramApi {
    /**
     * Starts the bot
     */
    fun start()

    /**
     * Stops the bot. Manual start needed
     */
    fun stop()

    /**
     * @param trigger bot command which starts with `/`
     * @param action suspendable callback for the given `trigger` with [Message] parameter
     */
    fun on(trigger: String, action: suspend (Message) -> Unit)

    fun keyboard(buttons: Array<Array<KeyboardButton>>, resize: Boolean? = null, once: Boolean? = null,
                 selective: Boolean? = null): ReplyKeyboard

    fun button(text: String, contact: Boolean? = null, location: Boolean? = null): KeyboardButton
    fun removeKeyboard(remove: Boolean = true, selective: Boolean? = null): ReplyKeyboard

    /**
     * @param media file_id, url or file_attach_name
     * @throws [IllegalArgumentException] if `attachment` neither `File` nor `String`
     */
    fun mediaPhoto(media: String, attachment: File? = null, caption: String? = null): InputMedia

    /**
     * @param media file_id, url or file_attach_name
     * @throws [IllegalArgumentException] if `attachment` neither `File` nor `String`
     */
    fun mediaVideo(media: String, width: Int, height: Int, duration: Int,
                   attachment: File? = null, caption: String? = null): InputMedia
}