package bot

import bot.http.TelegramClient
import bot.types.*
import kotlinx.coroutines.experimental.launch
import java.io.File
import java.util.concurrent.CompletableFuture

abstract class TelegramBot protected constructor(tk: String) : Bot {
    private val callbackQueries = mutableMapOf<String, (CallbackQuery) -> Unit>()
    private val commands = mutableMapOf<String, (Message) -> Unit>()
    private val client = TelegramClient(tk)

    companion object {
        private const val ANY_CALLBACK_QUERY_TRIGGER = "*"
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

    private fun Message.isCommand() = text != null && text.split(' ')[0].isCommand()
    private fun String.isCommand() = isNotBlank() && split(' ').size == 1 && startsWith("/")
//    private fun String.isEvent() =

    private fun validateIds(chatId: Any?, messageId: Int?, inlineMessageId: String?) {
        if (
                inlineMessageId != null && (chatId != null || messageId != null)
                || inlineMessageId == null && (chatId == null || messageId == null)
        ) throw IllegalArgumentException("Provide only inlineMessage or chatId and messageId")
    }

    protected fun getUpdates(options: Map<String, Any?>) = client.getUpdates(options)

    protected fun onUpdate(upds: List<Update>) {
        upds.forEach { upd ->
            if (upd.message != null && upd.message.isCommand())
                launch { commands[upd.message.text]?.invoke(upd.message) }
            else if (upd.callback_query != null) {
                upd.callback_query.data?.let {
                    val trigger = if (callbackQueries[it] != null) it else ANY_CALLBACK_QUERY_TRIGGER
                    launch { callbackQueries[trigger]?.invoke(upd.callback_query) }
                }
            }
        }
    }

    override fun on(trigger: String, action: (Message) -> Unit) {
        when {
            trigger.isCommand() -> commands[trigger] = action
            else -> throw IllegalArgumentException("$trigger is not a command")
        }
    }

    override fun onCallbackQuery(trigger: String, action: (CallbackQuery) -> Unit) {
        if (trigger.length !in 1..64)
            throw IllegalArgumentException("$trigger length must be in [1, 64] range")
        callbackQueries[trigger] = action
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

    override fun getUserProfilePhotos(userId: Int, offset: Int?, limit: Int?) = client.getUserProfilePhotos(userId, offset, limit)

    override fun getFile(fileId: String) = client.getFile(fileId)

    override fun kickChatMember(chatId: Any, userId: Int, untilDate: Int?) =
            client.kickChatMember(chatId, userId, untilDate)

    override fun unbanChatMember(chatId: Any, userId: Int) = client.unbanChatMember(chatId, userId)

    override fun restrictChatMember(chatId: Any, userId: Int, untilDate: Int?,
                                    canSendMessage: Boolean?, canSendMediaMessages: Boolean?,
                                    canSendOtherMessages: Boolean?, canAddWebPagePreview: Boolean?) = client.restrictChatMember(chatId, userId, untilDate,
            canSendMessage, canSendMediaMessages, canSendOtherMessages, canAddWebPagePreview)

    override fun promoteChatMember(chatId: Any, userId: Int, canChangeInfo: Boolean?, canPostMessages: Boolean?,
                                   canEditMessages: Boolean?, canDeleteMessages: Boolean?, canInviteUsers: Boolean?,
                                   canRestrictMembers: Boolean?, canPinMessages: Boolean?, canPromoteMembers: Boolean?) = client.promoteChatMember(chatId, userId, canChangeInfo, canPostMessages, canEditMessages, canDeleteMessages, canInviteUsers, canRestrictMembers, canPinMessages, canPromoteMembers)

    override fun exportChatInviteLink(chatId: Any) = client.exportChatInviteLink(chatId)

    override fun setChatPhoto(chatId: Any, photo: Any) = client.setChatPhoto(chatId, photo)

    override fun deleteChatPhoto(chatId: Any) = client.deleteChatPhoto(chatId)

    override fun setChatTitle(chatId: Any, title: String) = client.setChatTitle(chatId, title)

    override fun setChatDescription(chatId: Any, description: String) = client.setChatDescription(chatId, description)

    override fun pinChatMessage(chatId: Any, messageId: Int, notification: Boolean?) = client.pinChatMessage(chatId, messageId, notification)

    override fun unpinChatMessage(chatId: Any) = client.unpinChatMessage(chatId)

    override fun leaveChat(chatId: Any) = client.leaveChat(chatId)

    override fun getChat(chatId: Any) = client.getChat(chatId)

    override fun getChatAdministrators(chatId: Any) = client.getChatAdministrators(chatId)

    override fun getChatMembersCount(chatId: Any) = client.getChatMembersCount(chatId)

    override fun getChatMember(chatId: Any, userId: Int) = client.getChatMember(chatId, userId)

    override fun setChatStickerSet(chatId: Any, stickerSet: String) = client.setChatStickerSet(chatId, stickerSet)

    override fun deleteChatStickerSet(chatId: Any) = client.deleteChatStickerSet(chatId)

    override fun answerCallbackQuery(id: String, text: String?, alert: Boolean?, url: String?, cacheTime: Int?) = client.answerCallbackQuery(id, text, alert, url, cacheTime)

    override fun answerInlineQuery(queryId: String, results: Array<InlineQueryResult>, cacheTime: Int?, personal: Boolean?, offset: String?, pmText: String?, pmParameter: String?) = client.answerInlineQuery(queryId, results, cacheTime, personal, offset, pmText, pmParameter)

    override fun editMessageText(chatId: Any?, messageId: Int?, inlineMessageId: String?, text: String, parseMode: String?, preview: Boolean?, markup: InlineKeyboardMarkup?): CompletableFuture<Message> {
        validateIds(chatId, messageId, inlineMessageId)
        return client.editMessageText(chatId, messageId, inlineMessageId, text, parseMode, preview, markup)
    }

    override fun editMessageCaption(chatId: Any?, messageId: Int?, inlineMessageId: String?, caption: String?, parseMode: String?, markup: InlineKeyboardMarkup?): CompletableFuture<Message> {
        validateIds(chatId, messageId, inlineMessageId)
        return client.editMessageCaption(chatId, messageId, inlineMessageId, caption, parseMode, markup)
    }

    override fun editMessageMedia(chatId: Any?, messageId: Int?, inlineMessageId: String?, media: InputMedia, markup: InlineKeyboardMarkup?): CompletableFuture<Message> {
        validateIds(chatId, messageId, inlineMessageId)
        return client.editMessageMedia(chatId, messageId, inlineMessageId, media, markup)
    }

    override fun editMessageReplyMarkup(chatId: Any?, messageId: Int?, inlineMessageId: String?, markup: InlineKeyboardMarkup?): CompletableFuture<Message> {
        validateIds(chatId, messageId, inlineMessageId)
        return client.editMessageReplyMarkup(chatId, messageId, inlineMessageId, markup)
    }
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