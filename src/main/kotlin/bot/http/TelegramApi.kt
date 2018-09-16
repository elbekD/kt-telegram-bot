package bot.http

import bot.TelegramBot
import bot.types.*
import java.util.concurrent.CompletableFuture

interface TelegramApi {
    fun getMe(): CompletableFuture<User>

    /**
     * @param chatId is `Int` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun sendMessage(chatId: Any, text: String, parseMode: String? = null, preview: Boolean? = null,
                    notification: Boolean? = null, replyTo: Int? = null, markup: ReplyKeyboard? = null):
            CompletableFuture<Message>

    /**
     * @param chatId is `Int` or `String`
     * @param fromId is `Int` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun forwardMessage(chatId: Any, fromId: Any, msgId: Int, notification: Boolean? = null):
            CompletableFuture<Message>

    /**
     * @param chatId is `Int` or `String`
     * @param photo is `java.io.File` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws IllegalArgumentException if `photo` neither file nor string
     * @throws TelegramApiError if error returned in response
     */
    fun sendPhoto(chatId: Any, photo: Any, caption: String? = null, parseMode: String? = null,
                  notification: Boolean? = null, replyTo: Int? = null,
                  markup: ReplyKeyboard? = null): CompletableFuture<Message>

    /**
     * @param chatId is `Int` or `String`
     * @param audio is `java.io.File` or `String` (i.e. file_id)
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws IllegalArgumentException if `audio` neither file nor string
     * @throws TelegramApiError if error returned in response
     */
    fun sendAudio(chatId: Any, audio: Any, caption: String? = null, parseMode: String? = null,
                  duration: Int? = null, performer: String? = null, title: String? = null,
                  notification: Boolean? = null, replyTo: Int? = null, markup: ReplyKeyboard? = null):
            CompletableFuture<Message>

    /**
     * @param chatId is `Int` or `String`
     * @param document is `java.io.File` or `String` (i.e. file_id)
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws IllegalArgumentException if `document` neither file nor string
     * @throws TelegramApiError if error returned in response
     */
    fun sendDocument(chatId: Any, document: Any, caption: String? = null, parseMode: String? = null,
                     notification: Boolean? = null, replyTo: Int? = null, markup: ReplyKeyboard? = null):
            CompletableFuture<Message>

    /**
     * @param chatId is `Int` or `String`
     * @param video is `java.io.File or `String` (i.e. file_id)
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws IllegalArgumentException if `video` neither file nor string
     * @throws TelegramApiError if error returned in response
     */
    fun sendVideo(chatId: Any, video: Any, duration: Int? = null, width: Int? = null, height: Int? = null,
                  caption: String? = null, parseMode: String? = null, streaming: Boolean? = null,
                  notification: Boolean? = null, replyTo: Int? = null, markup: ReplyKeyboard? = null):
            CompletableFuture<Message>

    /**
     * @param chatId is `Int` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws IllegalArgumentException if `voice` neither file nor string
     * @throws TelegramApiError if error returned in response
     */
    fun sendVoice(chatId: Any, voice: Any, caption: String? = null, parseMode: String? = null,
                  duration: Int? = null, notification: Boolean? = null,
                  replyTo: Int? = null, markup: ReplyKeyboard? = null): CompletableFuture<Message>

    /**
     * @param chatId is `Int` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws IllegalArgumentException if `note` neither file nor string
     * @throws TelegramApiError if error returned in response
     */
    fun sendVideoNote(chatId: Any, note: Any, duration: Int? = null, length: Int? = null,
                      notification: Boolean? = null, replyTo: Int? = null, markup: ReplyKeyboard? = null):
            CompletableFuture<Message>

    /**
     * @param chatId is `Int` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun sendMediaGroup(chatId: Any, media: Array<InputMedia>, notification: Boolean? = null, replyTo: Int? = null):
            CompletableFuture<ArrayList<Message>>

    /**
     * @param chatId is `Int` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun sendLocation(chatId: Any, latitude: Double, longitude: Double, period: Int? = null,
                     notification: Boolean? = null, replyTo: Int? = null, markup: ReplyKeyboard? = null):
            CompletableFuture<Message>

    /**
     * @throws IllegalArgumentException if not provided `inlineMessageId` or `chatId` and `messageId`
     * @throws TelegramApiError if error returned in response
     */
    fun editMessageLiveLocation(latitude: Double, longitude: Double, chatId: Any? = null,
                                messageId: Int? = null, inlineMessageId: String? = null, markup: ReplyKeyboard? = null):
            CompletableFuture<Message>

    /**
     * @param chatId is `Int` or `String`
     * @throws IllegalArgumentException if not provided `inlineMessageId` or `chatId` and `messageId`
     * @throws TelegramApiError if error returned in response
     */
    fun stopMessageLiveLocation(chatId: Any? = null, messageId: Int? = null, inlineMessageId: String? = null,
                                markup: ReplyKeyboard? = null):
            CompletableFuture<Message>

    /**
     * @param chatId is `Int` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun sendVenue(chatId: Any, latitude: Double, longitude: Double, title: String, address: String,
                  foursquareId: String? = null, notification: Boolean? = null,
                  replyTo: Int? = null, markup: ReplyKeyboard? = null):
            CompletableFuture<Message>

    /**
     * @param chatId is `Int` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun sendContact(chatId: Any, phone: String, firstName: String, lastName: String? = null,
                    notification: Boolean? = null, replyTo: Int? = null, markup: ReplyKeyboard? = null):
            CompletableFuture<Message>

    /**
     * @param chatId is `Int` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun sendChatAction(chatId: Any, action: TelegramBot.Actions): CompletableFuture<Boolean>

    fun getUserProfilePhotos(userId: Int, offset: Int? = null, limit: Int? = null): CompletableFuture<UserProfilePhotos>

    fun getFile(fileId: String): CompletableFuture<File>

    /**
     * @param chatId is `Int` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun kickChatMember(chatId: Any, userId: Int, untilDate: Int? = null): CompletableFuture<Boolean>

    /**
     * @param chatId is `Int` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun unbanChatMember(chatId: Any, userId: Int): CompletableFuture<Boolean>

    /**
     * @param chatId is `Int` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun restrictChatMember(chatId: Any, userId: Int, untilDate: Int? = null, canSendMessage: Boolean? = null,
                           canSendMediaMessages: Boolean? = null, canSendOtherMessages: Boolean? = null,
                           canAddWebPagePreview: Boolean? = null): CompletableFuture<Boolean>

    /**
     * @param chatId is `Int` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun promoteChatMember(chatId: Any, userId: Int, canChangeInfo: Boolean? = null, canPostMessages: Boolean? = null,
                          canEditMessages: Boolean? = null, canDeleteMessages: Boolean? = null,
                          canInviteUsers: Boolean? = null, canRestrictMembers: Boolean? = null,
                          canPinMessages: Boolean? = null, canPromoteMembers: Boolean? = null): CompletableFuture<Boolean>

    /**
     * @param chatId is `Int` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun exportChatInviteLink(chatId: Any): CompletableFuture<String>

    /**
     * @param chatId is `Int` or `String`
     * @param photo is `java.io.File` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun setChatPhoto(chatId: Any, photo: Any): CompletableFuture<Boolean>

    /**
     * @param chatId is `Int` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun deleteChatPhoto(chatId: Any): CompletableFuture<Boolean>

    /**
     * @param chatId is `Int` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun setChatTitle(chatId: Any, title: String): CompletableFuture<Boolean>

    /**
     * @param chatId is `Int` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun setChatDescription(chatId: Any, description: String): CompletableFuture<Boolean>

    /**
     * @param chatId is `Int` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun pinChatMessage(chatId: Any, messageId: Int, notification: Boolean? = null): CompletableFuture<Boolean>

    /**
     * @param chatId is `Int` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun unpinChatMessage(chatId: Any): CompletableFuture<Boolean>

    /**
     * @param chatId is `Int` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun leaveChat(chatId: Any): CompletableFuture<Boolean>

    /**
     * @param chatId is `Int` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun getChat(chatId: Any): CompletableFuture<Chat>

    /**
     * @param chatId is `Int` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun getChatAdministrators(chatId: Any): CompletableFuture<ArrayList<ChatMember>>

    /**
     * @param chatId is `Int` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun getChatMembersCount(chatId: Any): CompletableFuture<Int>

    /**
     * @param chatId is `Int` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun getChatMember(chatId: Any, userId: Int): CompletableFuture<ChatMember>

    /**
     * @param chatId is `Int` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun setChatStickerSet(chatId: Any, stickerSet: String): CompletableFuture<Boolean>

    /**
     * @param chatId is `Int` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun deleteChatStickerSet(chatId: Any): CompletableFuture<Boolean>

    /**
     * @throws TelegramApiError if error returned in response
     */
    fun answerCallbackQuery(id: String, text: String? = null,
                            alert: Boolean? = null, url: String? = null,
                            cacheTime: Int? = null): CompletableFuture<Boolean>

    fun answerInlineQuery(queryId: String, results: Array<InlineQueryResult>, cacheTime: Int? = null,
                          personal: Boolean? = null, offset: String? = null,
                          pmText: String? = null, pmParameter: String? = null): CompletableFuture<Boolean>
}
