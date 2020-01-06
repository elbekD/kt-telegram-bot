package com.elbekD.bot.http

import com.elbekD.bot.types.Chat
import com.elbekD.bot.types.ChatMember
import com.elbekD.bot.types.ChatPermissions
import com.elbekD.bot.types.File
import com.elbekD.bot.types.GameHighScore
import com.elbekD.bot.types.InlineKeyboardMarkup
import com.elbekD.bot.types.InlineQueryResult
import com.elbekD.bot.types.InputMedia
import com.elbekD.bot.types.LabeledPrice
import com.elbekD.bot.types.MaskPosition
import com.elbekD.bot.types.Message
import com.elbekD.bot.types.PassportElementError
import com.elbekD.bot.types.Poll
import com.elbekD.bot.types.ReplyKeyboard
import com.elbekD.bot.types.ShippingOption
import com.elbekD.bot.types.StickerSet
import com.elbekD.bot.types.Update
import com.elbekD.bot.types.User
import com.elbekD.bot.types.UserProfilePhotos
import com.elbekD.bot.types.WebhookInfo
import com.elbekD.bot.util.Action
import com.elbekD.bot.util.AllowedUpdate
import java.util.concurrent.CompletableFuture

interface TelegramApi {
    fun getMe(): CompletableFuture<User>

    fun getUpdates(options: Map<String, Any?>): CompletableFuture<List<Update>>

    fun setWebhook(url: String,
                   certificate: java.io.File? = null,
                   maxConnections: Int? = null,
                   allowedUpdates: List<AllowedUpdate>? = null): CompletableFuture<Boolean>

    fun deleteWebhook(): CompletableFuture<Boolean>

    fun getWebhookInfo(): CompletableFuture<WebhookInfo>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun sendMessage(chatId: Any,
                    text: String,
                    parseMode: String? = null,
                    preview: Boolean? = null,
                    disableNotification: Boolean? = null,
                    replyTo: Int? = null,
                    markup: ReplyKeyboard? = null): CompletableFuture<Message>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @param fromId is `Int` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun forwardMessage(chatId: Any,
                       fromId: Any,
                       msgId: Int,
                       disableNotification: Boolean? = null): CompletableFuture<Message>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @param photo is [java.io.File] or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws IllegalArgumentException if `photo` neither [java.io.File] nor `String`
     * @throws TelegramApiError if error returned in response
     */
    fun sendPhoto(chatId: Any,
                  photo: Any,
                  caption: String? = null,
                  parseMode: String? = null,
                  disableNotification: Boolean? = null,
                  replyTo: Int? = null,
                  markup: ReplyKeyboard? = null): CompletableFuture<Message>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @param audio is [java.io.File] or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws IllegalArgumentException if `audio` neither [java.io.File] nor `String`
     * @throws TelegramApiError if error returned in response
     */
    fun sendAudio(chatId: Any,
                  audio: Any,
                  caption: String? = null,
                  parseMode: String? = null,
                  duration: Int? = null,
                  performer: String? = null,
                  title: String? = null,
                  thumb: java.io.File? = null,
                  disableNotification: Boolean? = null,
                  replyTo: Int? = null,
                  markup: ReplyKeyboard? = null):
            CompletableFuture<Message>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @param document is [java.io.File] or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws IllegalArgumentException if `document` neither [java.io.File] nor `String`
     * @throws TelegramApiError if error returned in response
     */
    fun sendDocument(chatId: Any,
                     document: Any,
                     thumb: java.io.File? = null,
                     caption: String? = null,
                     parseMode: String? = null,
                     disableNotification: Boolean? = null,
                     replyTo: Int? = null,
                     markup: ReplyKeyboard? = null): CompletableFuture<Message>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @param video is [java.io.File] or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws IllegalArgumentException if `video` neither [java.io.File] nor `String`
     * @throws TelegramApiError if error returned in response
     */
    fun sendVideo(chatId: Any,
                  video: Any,
                  duration: Int? = null,
                  width: Int? = null,
                  height: Int? = null,
                  thumb: java.io.File? = null,
                  caption: String? = null,
                  parseMode: String? = null,
                  streaming: Boolean? = null,
                  disableNotification: Boolean? = null,
                  replyTo: Int? = null,
                  markup: ReplyKeyboard? = null): CompletableFuture<Message>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @param animation is [java.io.File] or `String`
     * @param thumb is [java.io.File] or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws IllegalArgumentException if `animation` neither [java.io.File] nor `String`
     * @throws IllegalArgumentException if `thumb` neither [java.io.File] nor `String`
     * @throws TelegramApiError if error returned in response
     */
    fun sendAnimation(chatId: Any,
                      animation: Any,
                      duration: Int? = null,
                      width: Int? = null,
                      height: Int? = null,
                      thumb: java.io.File? = null,
                      caption: String? = null,
                      parseMode: String? = null,
                      disableNotification: Boolean? = null,
                      replyTo: Int? = null,
                      markup: ReplyKeyboard? = null): CompletableFuture<Message>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @param voice is [java.io.File] or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws IllegalArgumentException if `voice` neither [java.io.File] nor `String`
     * @throws TelegramApiError if error returned in response
     */
    fun sendVoice(chatId: Any,
                  voice: Any,
                  caption: String? = null,
                  parseMode: String? = null,
                  duration: Int? = null,
                  disableNotification: Boolean? = null,
                  replyTo: Int? = null,
                  markup: ReplyKeyboard? = null): CompletableFuture<Message>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @param note is [java.io.File] or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws IllegalArgumentException if `note` neither [java.io.File] nor `String`
     * @throws TelegramApiError if error returned in response
     */
    fun sendVideoNote(chatId: Any,
                      note: Any,
                      duration: Int? = null,
                      length: Int? = null,
                      thumb: java.io.File? = null,
                      disableNotification: Boolean? = null,
                      replyTo: Int? = null,
                      markup: ReplyKeyboard? = null): CompletableFuture<Message>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun sendMediaGroup(chatId: Any,
                       media: List<InputMedia>,
                       disableNotification: Boolean? = null,
                       replyTo: Int? = null): CompletableFuture<ArrayList<Message>>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun sendLocation(chatId: Any,
                     latitude: Double,
                     longitude: Double,
                     period: Int? = null,
                     disableNotification: Boolean? = null,
                     replyTo: Int? = null,
                     markup: ReplyKeyboard? = null): CompletableFuture<Message>

    /**
     * @throws IllegalArgumentException if not provided `inlineMessageId` or `chatId` and `messageId`
     * @throws TelegramApiError if error returned in response
     */
    fun editMessageLiveLocation(latitude: Double,
                                longitude: Double,
                                chatId: Any? = null,
                                messageId: Int? = null,
                                inlineMessageId: String? = null,
                                markup: InlineKeyboardMarkup? = null): CompletableFuture<Message>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if not provided `inlineMessageId` or `chatId` and `messageId`
     * @throws TelegramApiError if error returned in response
     */
    fun stopMessageLiveLocation(chatId: Any? = null,
                                messageId: Int? = null,
                                inlineMessageId: String? = null,
                                markup: InlineKeyboardMarkup? = null): CompletableFuture<Message>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun sendVenue(chatId: Any, latitude: Double,
                  longitude: Double,
                  title: String,
                  address: String,
                  foursquareId: String? = null,
                  foursquareType: String? = null,
                  disableNotification: Boolean? = null,
                  replyTo: Int? = null,
                  markup: ReplyKeyboard? = null): CompletableFuture<Message>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun sendContact(chatId: Any,
                    phone: String,
                    firstName: String,
                    lastName: String? = null,
                    vcard: String? = null,
                    disableNotification: Boolean? = null,
                    replyTo: Int? = null,
                    markup: ReplyKeyboard? = null): CompletableFuture<Message>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun sendChatAction(chatId: Any,
                       action: Action): CompletableFuture<Boolean>

    fun getUserProfilePhotos(userId: Long,
                             offset: Int? = null,
                             limit: Int? = null): CompletableFuture<UserProfilePhotos>

    fun getFile(fileId: String): CompletableFuture<File>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun kickChatMember(chatId: Any,
                       userId: Long,
                       untilDate: Int? = null): CompletableFuture<Boolean>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun unbanChatMember(chatId: Any,
                        userId: Long): CompletableFuture<Boolean>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun restrictChatMember(chatId: Any,
                           userId: Long,
                           permissions: ChatPermissions,
                           untilDate: Int? = null): CompletableFuture<Boolean>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun promoteChatMember(chatId: Any,
                          userId: Long,
                          canChangeInfo: Boolean? = null,
                          canPostMessages: Boolean? = null,
                          canEditMessages: Boolean? = null,
                          canDeleteMessages: Boolean? = null,
                          canInviteUsers: Boolean? = null,
                          canRestrictMembers: Boolean? = null,
                          canPinMessages: Boolean? = null,
                          canPromoteMembers: Boolean? = null): CompletableFuture<Boolean>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun exportChatInviteLink(chatId: Any): CompletableFuture<String>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @param photo is `java.io.File` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun setChatPhoto(chatId: Any,
                     photo: Any): CompletableFuture<Boolean>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun deleteChatPhoto(chatId: Any): CompletableFuture<Boolean>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun setChatTitle(chatId: Any,
                     title: String): CompletableFuture<Boolean>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun setChatDescription(chatId: Any,
                           description: String): CompletableFuture<Boolean>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun pinChatMessage(chatId: Any,
                       messageId: Int,
                       disableNotification: Boolean? = null): CompletableFuture<Boolean>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun unpinChatMessage(chatId: Any): CompletableFuture<Boolean>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun leaveChat(chatId: Any): CompletableFuture<Boolean>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun getChat(chatId: Any): CompletableFuture<Chat>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun getChatAdministrators(chatId: Any): CompletableFuture<ArrayList<ChatMember>>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun getChatMembersCount(chatId: Any): CompletableFuture<Int>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun getChatMember(chatId: Any,
                      userId: Long): CompletableFuture<ChatMember>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun setChatStickerSet(chatId: Any,
                          stickerSetName: String): CompletableFuture<Boolean>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun deleteChatStickerSet(chatId: Any): CompletableFuture<Boolean>

    /**
     * @throws TelegramApiError if error returned in response
     */
    fun answerCallbackQuery(id: String,
                            text: String? = null,
                            alert: Boolean? = null,
                            url: String? = null,
                            cacheTime: Int? = null): CompletableFuture<Boolean>


    /**
     * @throws TelegramApiError if error returned in response
     */
    fun answerInlineQuery(queryId: String,
                          results: List<InlineQueryResult>,
                          cacheTime: Int? = null,
                          personal: Boolean? = null,
                          offset: String? = null,
                          pmText: String? = null,
                          pmParameter: String? = null): CompletableFuture<Boolean>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if not provided `inlineMessageId` or `chatId` and `messageId`
     * @throws TelegramApiError if error returned in response
     */
    fun editMessageText(chatId: Any? = null,
                        messageId: Int? = null,
                        inlineMessageId: String? = null,
                        text: String,
                        parseMode: String? = null,
                        preview: Boolean? = null,
                        markup: InlineKeyboardMarkup? = null): CompletableFuture<Message>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if not provided `inlineMessageId` or `chatId` and `messageId`
     * @throws TelegramApiError if error returned in response
     */
    fun editMessageCaption(chatId: Any? = null,
                           messageId: Int? = null,
                           inlineMessageId: String? = null,
                           caption: String? = null,
                           parseMode: String? = null,
                           markup: InlineKeyboardMarkup? = null): CompletableFuture<Message>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if not provided `inlineMessageId` or `chatId` and `messageId`
     * @throws TelegramApiError if error returned in response
     */
    fun editMessageMedia(chatId: Any? = null,
                         messageId: Int? = null,
                         inlineMessageId: String? = null,
                         media: InputMedia,
                         markup: InlineKeyboardMarkup? = null): CompletableFuture<Message>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if not provided `inlineMessageId` or `chatId` and `messageId`
     * @throws TelegramApiError if error returned in response
     */
    fun editMessageReplyMarkup(chatId: Any? = null,
                               messageId: Int? = null,
                               inlineMessageId: String? = null,
                               markup: InlineKeyboardMarkup? = null): CompletableFuture<Message>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @param sticker is [java.io.File] or `String`
     * @throws IllegalArgumentException if not provided `inlineMessageId` or `chatId` and `messageId`
     * @throws IllegalArgumentException if `sticker` neither [java.io.File] nor `String`
     * @throws TelegramApiError if error returned in response
     */
    fun sendSticker(chatId: Any,
                    sticker: Any,
                    disableNotification: Boolean? = null,
                    replyTo: Int? = null,
                    markup: ReplyKeyboard? = null): CompletableFuture<Message>

    /**
     * @throws TelegramApiError if error returned in response
     */
    fun getStickerSet(name: String): CompletableFuture<StickerSet>

    /**
     * @throws TelegramApiError if error returned in response
     */
    fun uploadStickerFile(userId: Long,
                          pngSticker: java.io.File): CompletableFuture<File>

    /**
     * @param pngSticker is [java.io.File] or `String`
     * @throws TelegramApiError if error returned in response
     */
    fun createNewStickerSet(userId: Long,
                            name: String,
                            title: String,
                            pngSticker: Any,
                            emojis: String,
                            containsMask: Boolean? = null,
                            maskPosition: MaskPosition? = null): CompletableFuture<Boolean>

    /**
     * @param pngSticker is [java.io.File] or `String`
     * @throws TelegramApiError if error returned in response
     */
    fun addStickerToSet(userId: Long,
                        name: String,
                        pngSticker: Any,
                        emojis: String,
                        maskPosition: MaskPosition? = null): CompletableFuture<Boolean>

    /**
     * @throws TelegramApiError if error returned in response
     */
    fun setStickerPositionInSet(sticker: String,
                                position: Int): CompletableFuture<Boolean>

    /**
     * @throws TelegramApiError if error returned in response
     */
    fun deleteStickerFromSet(sticker: String): CompletableFuture<Boolean>

    /**
     * @throws TelegramApiError if error returned in response
     */
    fun sendGame(chatId: Long,
                 gameShortName: String,
                 disableNotification: Boolean? = null,
                 replyTo: Int? = null,
                 markup: InlineKeyboardMarkup? = null): CompletableFuture<Message>

    /**
     * @throws IllegalArgumentException if not provided `inlineMessageId` or `chatId` and `messageId`
     * @throws TelegramApiError if error returned in response
     */
    fun setGameScore(userId: Long,
                     score: Int,
                     force: Boolean? = null,
                     disableEditMessage: Boolean? = null,
                     chatId: Long? = null,
                     messageId: Int? = null,
                     inlineMessageId: String? = null): CompletableFuture<Message>

    /**
     * @throws IllegalArgumentException if not provided `inlineMessageId` or `chatId` and `messageId`
     * @throws TelegramApiError if error returned in response
     */
    fun getGameHighScores(userId: Long,
                          chatId: Long? = null,
                          messageId: Int? = null,
                          inlineMessageId: String? = null): CompletableFuture<List<GameHighScore>>

    /**
     * @throws TelegramApiError if error returned in response
     */
    fun sendInvoice(chatId: Long,
                    title: String,
                    description: String,
                    payload: String,
                    providerToken: String,
                    startParam: String,
                    currency: String,
                    prices: List<LabeledPrice>,
                    providerData: String? = null,
                    photoUrl: String? = null,
                    photoSize: Int? = null,
                    photoWidth: Int? = null,
                    photoHeight: Int? = null,
                    needName: Boolean? = null,
                    needPhoneNumber: Boolean? = null,
                    needEmail: Boolean? = null,
                    needShippingAddress: Boolean? = null,
                    sendPhoneNumberToProvider: Boolean? = null,
                    sendEmailToProvider: Boolean? = null,
                    isFlexible: Boolean? = null,
                    disableNotification: Boolean? = null,
                    replyTo: Int? = null,
                    markup: InlineKeyboardMarkup? = null): CompletableFuture<Message>

    /**
     * @throws TelegramApiError if error returned in response
     */
    fun answerShippingQuery(shippingQueryId: String,
                            ok: Boolean,
                            shippingOptions: List<ShippingOption>? = null,
                            errorMessage: String? = null): CompletableFuture<Boolean>

    /**
     * @throws TelegramApiError if error returned in response
     */
    fun answerPreCheckoutQuery(preCheckoutQueryId: String,
                               ok: Boolean,
                               errorMessage: String? = null): CompletableFuture<Boolean>

    /**
     * @throws TelegramApiError if error returned in response
     */
    fun setPassportDataErrors(userId: Long,
                              errors: List<PassportElementError>): CompletableFuture<Boolean>

    fun sendPoll(chatId: Any,
                 question: String,
                 options: List<String>,
                 disableNotification: Boolean? = null,
                 replyTo: Int? = null,
                 markup: ReplyKeyboard? = null): CompletableFuture<Message>

    fun stopPoll(chatId: Any,
                 messageId: Int,
                 markup: InlineKeyboardMarkup? = null): CompletableFuture<Poll>

    fun setChatPermissions(chatId: Any, permissions: ChatPermissions): CompletableFuture<Boolean>

    fun setChatAdministratorCustomTitle(chatId: Any, userId: Long, customTitle: String): CompletableFuture<Boolean>
}
