package bot

import bot.http.TelegramClient
import bot.types.*
import java.io.File
import java.util.concurrent.CompletableFuture

abstract class TelegramBot protected constructor(tk: String) : Bot {
    private val commands = mutableMapOf<String, suspend (Message) -> Unit>()
    private val client = TelegramClient(tk)

    companion object {
        fun createPolling(token: String, options: PollingOptions.() -> Unit = { PollingOptions() }): Bot {
            validateToken(token)
            return LongPollingBot(token, PollingOptions().apply(options))
        }

        fun createWebhook(token: String, url: String, options: WebhookOptions.() -> Unit = {}): Bot {
            validateToken(token)
            return WebhookBot(token, WebhookOptions(url).apply(options))
        }

        private fun validateToken(token: String) {
            if (token.isBlank())
                throw IllegalArgumentException("Token cannot be empty")
        }
    }

    enum class Actions(val value: String) {
        Typing("typing"),
        UploadPhoto("upload_photo"),
        RecordVideo("record_video"),
        UploadVideo("upload_video"),
        RecordAudio("record_audio"),
        UploadAudio("upload_audio"),
        UploadDocument("upload_document"),
        FindLocation("find_location"),
        RecordVideoNote("record_video_note"),
        UploadVideoNote("upload_video_note ");
    }

    private fun Message.isCommand() = text != null && text.isCommand()
    private fun String.isCommand() = isNotBlank() && split(' ').size == 1 && startsWith("/")

    private fun validateIds(chatId: Any?, messageId: Int?, inlineMessageId: String?) {
        if (
                inlineMessageId != null && (chatId != null || messageId != null)
                || inlineMessageId == null && (chatId == null || messageId == null)
        ) throw IllegalArgumentException("Provide only inlineMessage or chatId and messageId")
    }

    protected suspend fun onUpdate(msg: Message) {
        if (msg.isCommand()) commands[msg.text]?.invoke(msg)
    }

    override fun on(trigger: String, action: suspend (Message) -> Unit) {
        when {
            trigger.isCommand() -> commands[trigger] = action
            else -> throw IllegalArgumentException("$trigger is not a command")
        }
    }

    override fun keyboard(buttons: Array<Array<KeyboardButton>>, resize: Boolean?, once: Boolean?, selective: Boolean?): ReplyKeyboardMarkup {
        return ReplyKeyboardMarkup(buttons, resize, once, selective)
    }

    override fun button(text: String, contact: Boolean?, location: Boolean?): KeyboardButton {
        if (contact != null && contact && location != null && location)
            throw IllegalArgumentException("both contact and location cannot be set")

        return KeyboardButton(text, contact, location)
    }

    override fun removeKeyboard(remove: Boolean, selective: Boolean?): ReplyKeyboard {
        return ReplyKeyboardRemove(remove, selective)
    }

    override fun mediaPhoto(media: String, attachment: File?, caption: String?): InputMedia {
        return InputMediaPhoto(media, attachment, caption)
    }

    override fun mediaVideo(media: String, width: Int, height: Int, duration: Int,
                            attachment: File?, caption: String?): InputMedia {
        return InputMediaVideo(media, attachment, caption, width, height, duration)
    }

    /*
          Telegram methods
                |  |
                |  |
                |  |
                |  |
               \    /
                \  /
                 \/
     */
    override fun getMe() = client.getMe()

    override fun sendMessage(chatId: Any, text: String, parseMode: String?, preview: Boolean?, notification: Boolean?,
                             replyTo: Int?, markup: ReplyKeyboard?) =
            client.sendMessage(chatId, text, parseMode, preview, notification, replyTo, markup)

    override fun forwardMessage(chatId: Any, fromId: Any, msgId: Int, notification: Boolean?) =
            client.forwardMessage(chatId, fromId, msgId, notification)

    override fun sendPhoto(chatId: Any, photo: Any, caption: String?, parseMode: String?, notification: Boolean?,
                           replyTo: Int?, markup: ReplyKeyboard?) =
            client.sendPhoto(chatId, photo, caption, parseMode, notification, replyTo, markup)

    override fun sendAudio(chatId: Any, audio: Any, caption: String?, parseMode: String?, duration: Int?,
                           performer: String?, title: String?, notification: Boolean?, replyTo: Int?,
                           markup: ReplyKeyboard?) =
            client.sendAudio(chatId, audio, caption, parseMode, duration, performer, title, notification, replyTo, markup)

    override fun sendDocument(chatId: Any, document: Any, caption: String?, parseMode: String?,
                              notification: Boolean?, replyTo: Int?, markup: ReplyKeyboard?) =
            client.sendDocument(chatId, document, caption, parseMode, notification, replyTo, markup)

    override fun sendVideo(chatId: Any, video: Any, duration: Int?, width: Int?, height: Int?, caption: String?,
                           parseMode: String?, streaming: Boolean?, notification: Boolean?,
                           replyTo: Int?, markup: ReplyKeyboard?) =
            client.sendVideo(chatId, video, duration, width, height, caption, parseMode, streaming, notification, replyTo, markup)

    override fun sendVoice(chatId: Any, voice: Any, caption: String?, parseMode: String?, duration: Int?,
                           notification: Boolean?, replyTo: Int?, markup: ReplyKeyboard?) =
            client.sendVoice(chatId, voice, caption, parseMode, duration, notification, replyTo, markup)

    override fun sendVideoNote(chatId: Any, note: Any, duration: Int?, length: Int?, notification: Boolean?,
                               replyTo: Int?, markup: ReplyKeyboard?) =
            client.sendVideoNote(chatId, note, duration, length, notification, replyTo, markup)

    override fun sendMediaGroup(chatId: Any, media: Array<InputMedia>, notification: Boolean?, replyTo: Int?):
            CompletableFuture<ArrayList<Message>> {
        if (media.size < 2) throw IllegalArgumentException("Array must include 2-10 items")
        return client.sendMediaGroup(chatId, media, notification, replyTo)
    }

    override fun sendLocation(chatId: Any, latitude: Double, longitude: Double, period: Int?, notification: Boolean?,
                              replyTo: Int?, markup: ReplyKeyboard?) =
            client.sendLocation(chatId, latitude, longitude, period, notification, replyTo, markup)

    override fun editMessageLiveLocation(latitude: Double, longitude: Double, chatId: Any?, messageId: Int?,
                                         inlineMessageId: String?, markup: InlineKeyboardMarkup?):
            CompletableFuture<Message> {
        validateIds(chatId, messageId, inlineMessageId)
        return client.editMessageLiveLocation(latitude, longitude, chatId, messageId, inlineMessageId, markup)
    }

    override fun stopMessageLiveLocation(chatId: Any?, messageId: Int?, inlineMessageId: String?,
                                         markup: InlineKeyboardMarkup?):
            CompletableFuture<Message> {
        validateIds(chatId, messageId, inlineMessageId)
        return client.stopMessageLiveLocation(chatId, messageId, inlineMessageId, markup)
    }

    override fun sendVenue(chatId: Any, latitude: Double, longitude: Double, title: String, address: String,
                           foursquareId: String?, notification: Boolean?, replyTo: Int?, markup: ReplyKeyboard?) =
            client.sendVenue(chatId, latitude, longitude, title, address, foursquareId, notification, replyTo, markup)

    override fun sendContact(chatId: Any, phone: String, firstName: String, lastName: String?,
                             notification: Boolean?, replyTo: Int?, markup: ReplyKeyboard?) =
            client.sendContact(chatId, phone, firstName, lastName, notification, replyTo, markup)

    override fun sendChatAction(chatId: Any, action: Actions) = client.sendChatAction(chatId, action)
    /*
                /\
               /  \
              /    \
               |  |
               |  |
               |  |
               |  |
         Telegram methods
     */
}