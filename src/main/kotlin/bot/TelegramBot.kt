package bot

import bot.http.TelegramClient
import bot.types.*
import kotlinx.coroutines.experimental.launch
import java.io.File
import java.util.concurrent.CompletableFuture

// Fixme: protect command handlers from overriding after bot has started
abstract class TelegramBot protected constructor(tk: String) : Bot {
    private val commands by lazy { mutableMapOf<String, (Message, String?) -> Unit>() }
    private val callbackQueries by lazy { mutableMapOf<String, (CallbackQuery) -> Unit>() }
    private val inlineQueries by lazy { mutableMapOf<String, (InlineQuery) -> Unit>() }
    private var onAnyUpdateAction: ((Update) -> Unit)? = null
    private val client = TelegramClient(tk)

    companion object {
        private const val ANY_CALLBACK_TRIGGER = "*"

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

        private fun validateInputFileOrString(obj: Any) {
            if (obj !is File && obj !is String)
                throw IllegalArgumentException("$obj is neither file nor string")
        }

        private fun validateIds(chatId: Any?, messageId: Int?, inlineMessageId: String?) {
            if (
                    inlineMessageId != null && (chatId != null || messageId != null)
                    || inlineMessageId == null && (chatId == null || messageId == null)
            ) throw IllegalArgumentException("Provide only inlineMessage or chatId and messageId")
        }

        private fun extractCommandAndArgument(text: String): Pair<String, String?> {
            val cmd = text.substringBefore(' ')
            val arg = cmd.substringAfter(' ', "")
            return Pair(cmd, if (arg.isEmpty()) null else arg)
        }

        private fun Message.isCommand() = text != null && text.split(' ')[0].isCommand()

        private fun String.isCommand() = matches("^/[\\w]{1,32}$".toRegex())
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

    protected fun getUpdates(options: Map<String, Any?>) = client.getUpdates(options)

    protected fun onUpdate(upds: List<Update>) {
        upds.forEach { upd ->
            if (upd.message != null && upd.message.isCommand()) {
                val (cmd, arg) = extractCommandAndArgument(upd.message.text!!)
                val trigger = if (commands[cmd] != null) cmd else ANY_CALLBACK_TRIGGER
                launch { commands[trigger]?.invoke(upd.message, arg) }

            } else if (upd.callback_query != null) {
                upd.callback_query.data?.let {
                    val trigger = if (callbackQueries[it] != null) it else ANY_CALLBACK_TRIGGER
                    launch { callbackQueries[trigger]?.invoke(upd.callback_query) }
                }

            } else if (upd.inline_query != null) {
                val trigger = if (inlineQueries[upd.inline_query.query] != null)
                    upd.inline_query.query
                else ANY_CALLBACK_TRIGGER
                launch { inlineQueries[trigger]?.invoke(upd.inline_query) }

            } else {
                launch { onAnyUpdateAction?.invoke(upd) }
            }
        }
    }

    override fun onCommand(command: String, action: (Message, String?) -> Unit) {
        if (!command.isCommand())
            throw IllegalArgumentException("$command is not a command")
        commands[command] = action
    }

    override fun onCallbackQuery(data: String, action: (CallbackQuery) -> Unit) {
        if (data.length !in 1..64)
            throw IllegalArgumentException("'data' length must be in [1, 64] range")
        callbackQueries[data] = action
    }

    override fun onInlineQuery(query: String, action: (InlineQuery) -> Unit) {
        if (query.length !in 0..512)
            throw IllegalArgumentException("'query' length must be in [1, 512] range")
        inlineQueries[query] = action
    }

    override fun onAnyUpdate(action: ((Update) -> Unit)?) {
        onAnyUpdateAction = action
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

    override fun sendMediaGroup(chatId: Any, media: List<InputMedia>, notification: Boolean?, replyTo: Int?):
            CompletableFuture<ArrayList<Message>> {
        if (media.size < 2) throw IllegalArgumentException("List must include 2-10 items")
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

    override fun getUserProfilePhotos(userId: Long, offset: Int?, limit: Int?) = client.getUserProfilePhotos(userId, offset, limit)

    override fun getFile(fileId: String) = client.getFile(fileId)

    override fun kickChatMember(chatId: Any, userId: Long, untilDate: Int?) =
            client.kickChatMember(chatId, userId, untilDate)

    override fun unbanChatMember(chatId: Any, userId: Long) = client.unbanChatMember(chatId, userId)

    override fun restrictChatMember(chatId: Any, userId: Long, untilDate: Int?,
                                    canSendMessage: Boolean?, canSendMediaMessages: Boolean?,
                                    canSendOtherMessages: Boolean?, canAddWebPagePreview: Boolean?) = client.restrictChatMember(chatId, userId, untilDate,
            canSendMessage, canSendMediaMessages, canSendOtherMessages, canAddWebPagePreview)

    override fun promoteChatMember(chatId: Any, userId: Long, canChangeInfo: Boolean?, canPostMessages: Boolean?,
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

    override fun getChatMember(chatId: Any, userId: Long) = client.getChatMember(chatId, userId)

    override fun setChatStickerSet(chatId: Any, stickerSetName: String) = client.setChatStickerSet(chatId, stickerSetName)

    override fun deleteChatStickerSet(chatId: Any) = client.deleteChatStickerSet(chatId)

    override fun answerCallbackQuery(id: String, text: String?, alert: Boolean?, url: String?, cacheTime: Int?) = client.answerCallbackQuery(id, text, alert, url, cacheTime)

    override fun answerInlineQuery(queryId: String, results: List<InlineQueryResult>, cacheTime: Int?, personal: Boolean?, offset: String?, pmText: String?, pmParameter: String?) = client.answerInlineQuery(queryId, results, cacheTime, personal, offset, pmText, pmParameter)

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

    override fun sendSticker(chatId: Any, sticker: Any, notification: Boolean?, replyTo: Int?, markup: ReplyKeyboard?): CompletableFuture<Message> {
        validateInputFileOrString(sticker)
        return client.sendSticker(chatId, sticker, notification, replyTo, markup)
    }

    override fun getStickerSet(name: String) = client.getStickerSet(name)

    override fun uploadStickerFile(userId: Long, pngSticker: File) = client.uploadStickerFile(userId, pngSticker)

    override fun createNewStickerSet(userId: Long, name: String, title: String, pngSticker: Any, emojis: String, containsMask: Boolean?, maskPosition: MaskPosition?): CompletableFuture<Boolean> {
        validateInputFileOrString(pngSticker)
        return client.createNewStickerSet(userId, name, title, pngSticker, emojis, containsMask, maskPosition)
    }

    override fun addStickerToSet(userId: Long, name: String, pngSticker: Any, emojis: String, maskPosition: MaskPosition?): CompletableFuture<Boolean> {
        validateInputFileOrString(pngSticker)
        return client.addStickerToSet(userId, name, pngSticker, emojis, maskPosition)
    }

    override fun setStickerPositionInSet(sticker: String, position: Int) = client.setStickerPositionInSet(sticker, position)

    override fun deleteStickerFromSet(sticker: String) = client.deleteStickerFromSet(sticker)

    override fun sendGame(chatId: Long, gameShortName: String, notification: Boolean?, replyTo: Int?, markup: InlineKeyboardMarkup?) = client.sendGame(chatId, gameShortName, notification, replyTo, markup)

    override fun setGameScore(userId: Long, score: Int, force: Boolean?, disableEditMessage: Boolean?, chatId: Long?, messageId: Int?, inlineMessageId: String?): CompletableFuture<Message> {
        validateIds(chatId, messageId, inlineMessageId)
        return client.setGameScore(userId, score, force, disableEditMessage, chatId, messageId, inlineMessageId)
    }

    override fun getGameHighScores(userId: Long, chatId: Long?, messageId: Int?, inlineMessageId: String?): CompletableFuture<List<GameHighScore>> {
        validateIds(chatId, messageId, inlineMessageId)
        return client.getGameHighScores(userId, chatId, messageId, inlineMessageId)
    }

    override fun sendInvoice(chatId: Long, title: String, description: String, payload: String, providerToken: String, startParam: String, currency: String, prices: List<LabeledPrice>, providerData: String?, photoUrl: String?, photoSize: Int?, photoWidth: Int?, photoHeight: Int?, needName: Boolean?, needPhoneNumber: Boolean?, needEmail: Boolean?, needShippingAddress: Boolean?, sendPhoneNumberToProvider: Boolean?, sendEmailToProvider: Boolean?, isFlexible: Boolean?, notification: Boolean?, replyTo: Int?, markup: InlineKeyboardMarkup?): CompletableFuture<Message> {
        return client.sendInvoice(chatId, title, description, payload, providerToken, startParam, currency, prices, providerData, photoUrl, photoSize, photoWidth, photoHeight, needName, needPhoneNumber, needEmail, needShippingAddress, sendPhoneNumberToProvider, sendEmailToProvider, isFlexible, notification, replyTo, markup)
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