package com.elbekD.bot.http

import com.elbekD.bot.types.BotCommand
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
import com.elbekD.bot.types.MessageEntity
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

public interface TelegramApi {
    public fun getMe(): CompletableFuture<out User>

    public fun logOut(): CompletableFuture<out Boolean>

    public fun close(): CompletableFuture<out Boolean>

    public fun getUpdates(options: Map<String, Any?>): CompletableFuture<out List<Update>>

    /**
     * @throws TelegramApiError if error returned in response
     */
    public fun getMyCommands(): CompletableFuture<out List<BotCommand>>

    /**
     * @throws TelegramApiError if error returned in response
     */
    public fun setMyCommands(commands: List<BotCommand>): CompletableFuture<out Boolean>

    public fun setWebhook(
        url: String,
        certificate: java.io.File? = null,
        ipAddress: String? = null,
        maxConnections: Int? = null,
        allowedUpdates: List<AllowedUpdate>? = null,
        dropPendingUpdates: Boolean? = null
    ): CompletableFuture<out Boolean>

    public fun deleteWebhook(dropPendingUpdates: Boolean? = null): CompletableFuture<out Boolean>

    public fun getWebhookInfo(): CompletableFuture<out WebhookInfo>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    public fun sendMessage(
        chatId: Any,
        text: String,
        parseMode: String? = null,
        disableWebPagePreview: Boolean? = null,
        disableNotification: Boolean? = null,
        replyTo: Int? = null,
        markup: ReplyKeyboard? = null
    ): CompletableFuture<out Message>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @param fromId is `Int` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    public fun forwardMessage(
        chatId: Any,
        fromId: Any,
        msgId: Int,
        disableNotification: Boolean? = null
    ): CompletableFuture<out Message>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @param photo is [java.io.File] or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws IllegalArgumentException if `photo` neither [java.io.File] nor `String`
     * @throws TelegramApiError if error returned in response
     */
    public fun sendPhoto(
        chatId: Any,
        photo: Any,
        caption: String? = null,
        parseMode: String? = null,
        disableNotification: Boolean? = null,
        replyTo: Int? = null,
        markup: ReplyKeyboard? = null
    ): CompletableFuture<out Message>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @param audio is [java.io.File] or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws IllegalArgumentException if `audio` neither [java.io.File] nor `String`
     * @throws TelegramApiError if error returned in response
     */
    public fun sendAudio(
        chatId: Any,
        audio: Any,
        caption: String? = null,
        parseMode: String? = null,
        duration: Int? = null,
        performer: String? = null,
        title: String? = null,
        thumb: java.io.File? = null,
        disableNotification: Boolean? = null,
        replyTo: Int? = null,
        markup: ReplyKeyboard? = null
    ):
            CompletableFuture<out Message>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @param document is [java.io.File] or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws IllegalArgumentException if `document` neither [java.io.File] nor `String`
     * @throws TelegramApiError if error returned in response
     */
    public fun sendDocument(
        chatId: Any,
        document: Any,
        thumb: java.io.File? = null,
        caption: String? = null,
        parseMode: String? = null,
        captionEntities: List<MessageEntity>? = null,
        disableContentTypeDetection: Boolean? = null,
        disableNotification: Boolean? = null,
        replyTo: Int? = null,
        allowSendingWithoutReply: Boolean? = null,
        markup: ReplyKeyboard? = null
    ): CompletableFuture<out Message>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @param video is [java.io.File] or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws IllegalArgumentException if `video` neither [java.io.File] nor `String`
     * @throws TelegramApiError if error returned in response
     */
    public fun sendVideo(
        chatId: Any,
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
        markup: ReplyKeyboard? = null
    ): CompletableFuture<out Message>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @param animation is [java.io.File] or `String`
     * @param thumb is [java.io.File] or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws IllegalArgumentException if `animation` neither [java.io.File] nor `String`
     * @throws IllegalArgumentException if `thumb` neither [java.io.File] nor `String`
     * @throws TelegramApiError if error returned in response
     */
    public fun sendAnimation(
        chatId: Any,
        animation: Any,
        duration: Int? = null,
        width: Int? = null,
        height: Int? = null,
        thumb: java.io.File? = null,
        caption: String? = null,
        parseMode: String? = null,
        disableNotification: Boolean? = null,
        replyTo: Int? = null,
        markup: ReplyKeyboard? = null
    ): CompletableFuture<out Message>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @param voice is [java.io.File] or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws IllegalArgumentException if `voice` neither [java.io.File] nor `String`
     * @throws TelegramApiError if error returned in response
     */
    public fun sendVoice(
        chatId: Any,
        voice: Any,
        caption: String? = null,
        parseMode: String? = null,
        duration: Int? = null,
        disableNotification: Boolean? = null,
        replyTo: Int? = null,
        markup: ReplyKeyboard? = null
    ): CompletableFuture<out Message>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @param note is [java.io.File] or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws IllegalArgumentException if `note` neither [java.io.File] nor `String`
     * @throws TelegramApiError if error returned in response
     */
    public fun sendVideoNote(
        chatId: Any,
        note: Any,
        duration: Int? = null,
        length: Int? = null,
        thumb: java.io.File? = null,
        disableNotification: Boolean? = null,
        replyTo: Int? = null,
        markup: ReplyKeyboard? = null
    ): CompletableFuture<out Message>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    public fun sendMediaGroup(
        chatId: Any,
        media: List<InputMedia>,
        disableNotification: Boolean? = null,
        replyTo: Int? = null,
        allowSendingWithoutReply: Boolean? = null
    ): CompletableFuture<out ArrayList<Message>>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    public fun sendLocation(
        chatId: Any,
        latitude: Double,
        longitude: Double,
        period: Int? = null,
        disableNotification: Boolean? = null,
        replyTo: Int? = null,
        markup: ReplyKeyboard? = null
    ): CompletableFuture<out Message>

    /**
     * @throws IllegalArgumentException if not provided `inlineMessageId` or `chatId` and `messageId`
     * @throws TelegramApiError if error returned in response
     */
    public fun editMessageLiveLocation(
        latitude: Double,
        longitude: Double,
        chatId: Any? = null,
        messageId: Int? = null,
        inlineMessageId: String? = null,
        markup: InlineKeyboardMarkup? = null
    ): CompletableFuture<out Message>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if not provided `inlineMessageId` or `chatId` and `messageId`
     * @throws TelegramApiError if error returned in response
     */
    public fun stopMessageLiveLocation(
        chatId: Any? = null,
        messageId: Int? = null,
        inlineMessageId: String? = null,
        markup: InlineKeyboardMarkup? = null
    ): CompletableFuture<out Message>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    public fun sendVenue(
        chatId: Any, latitude: Double,
        longitude: Double,
        title: String,
        address: String,
        foursquareId: String? = null,
        foursquareType: String? = null,
        disableNotification: Boolean? = null,
        replyTo: Int? = null,
        markup: ReplyKeyboard? = null
    ): CompletableFuture<out Message>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    public fun sendContact(
        chatId: Any,
        phone: String,
        firstName: String,
        lastName: String? = null,
        vcard: String? = null,
        disableNotification: Boolean? = null,
        replyTo: Int? = null,
        markup: ReplyKeyboard? = null
    ): CompletableFuture<out Message>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    public fun sendChatAction(
        chatId: Any,
        action: Action
    ): CompletableFuture<out Boolean>

    public fun getUserProfilePhotos(
        userId: Long,
        offset: Int? = null,
        limit: Int? = null
    ): CompletableFuture<out UserProfilePhotos>

    public fun getFile(fileId: String): CompletableFuture<out File>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    public fun kickChatMember(
        chatId: Any,
        userId: Long,
        untilDate: Int? = null
    ): CompletableFuture<out Boolean>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    public fun unbanChatMember(
        chatId: Any,
        userId: Long,
        onlyIfBanned: Boolean? = null
    ): CompletableFuture<out Boolean>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    public fun restrictChatMember(
        chatId: Any,
        userId: Long,
        permissions: ChatPermissions,
        untilDate: Int? = null
    ): CompletableFuture<out Boolean>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    public fun promoteChatMember(
        chatId: Any,
        userId: Long,
        canChangeInfo: Boolean? = null,
        canPostMessages: Boolean? = null,
        canEditMessages: Boolean? = null,
        canDeleteMessages: Boolean? = null,
        canInviteUsers: Boolean? = null,
        canRestrictMembers: Boolean? = null,
        canPinMessages: Boolean? = null,
        canPromoteMembers: Boolean? = null
    ): CompletableFuture<out Boolean>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    public fun exportChatInviteLink(chatId: Any): CompletableFuture<out String>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @param photo is `java.io.File` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    public fun setChatPhoto(
        chatId: Any,
        photo: Any
    ): CompletableFuture<out Boolean>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    public fun deleteChatPhoto(chatId: Any): CompletableFuture<out Boolean>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    public fun setChatTitle(
        chatId: Any,
        title: String
    ): CompletableFuture<out Boolean>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    public fun setChatDescription(
        chatId: Any,
        description: String
    ): CompletableFuture<out Boolean>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    public fun pinChatMessage(
        chatId: Any,
        messageId: Int,
        disableNotification: Boolean? = null
    ): CompletableFuture<out Boolean>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    public fun unpinChatMessage(chatId: Any, messageId: Int? = null): CompletableFuture<out Boolean>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    public fun unpinAllChatMessages(chatId: Any): CompletableFuture<out Boolean>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    public fun leaveChat(chatId: Any): CompletableFuture<out Boolean>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    public fun getChat(chatId: Any): CompletableFuture<out Chat>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    public fun getChatAdministrators(chatId: Any): CompletableFuture<out ArrayList<ChatMember>>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    public fun getChatMembersCount(chatId: Any): CompletableFuture<out Int>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    public fun getChatMember(
        chatId: Any,
        userId: Long
    ): CompletableFuture<out ChatMember>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    public fun setChatStickerSet(
        chatId: Any,
        stickerSetName: String
    ): CompletableFuture<out Boolean>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    public fun deleteChatStickerSet(chatId: Any): CompletableFuture<out Boolean>

    /**
     * @throws TelegramApiError if error returned in response
     */
    public fun answerCallbackQuery(
        id: String,
        text: String? = null,
        alert: Boolean? = null,
        url: String? = null,
        cacheTime: Int? = null
    ): CompletableFuture<out Boolean>


    /**
     * @throws TelegramApiError if error returned in response
     */
    public fun answerInlineQuery(
        queryId: String,
        results: List<InlineQueryResult>,
        cacheTime: Int? = null,
        personal: Boolean? = null,
        offset: String? = null,
        pmText: String? = null,
        pmParameter: String? = null
    ): CompletableFuture<out Boolean>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if not provided `inlineMessageId` or `chatId` and `messageId`
     * @throws TelegramApiError if error returned in response
     */
    public fun editMessageText(
        chatId: Any? = null,
        messageId: Int? = null,
        inlineMessageId: String? = null,
        text: String,
        parseMode: String? = null,
        disableWebPagePreview: Boolean? = null,
        markup: InlineKeyboardMarkup? = null
    ): CompletableFuture<out Message>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if not provided `inlineMessageId` or `chatId` and `messageId`
     * @throws TelegramApiError if error returned in response
     */
    public fun editMessageCaption(
        chatId: Any? = null,
        messageId: Int? = null,
        inlineMessageId: String? = null,
        caption: String? = null,
        parseMode: String? = null,
        markup: InlineKeyboardMarkup? = null
    ): CompletableFuture<out Message>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if not provided `inlineMessageId` or `chatId` and `messageId`
     * @throws TelegramApiError if error returned in response
     */
    public fun editMessageMedia(
        chatId: Any? = null,
        messageId: Int? = null,
        inlineMessageId: String? = null,
        media: InputMedia,
        markup: InlineKeyboardMarkup? = null
    ): CompletableFuture<out Message>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @throws IllegalArgumentException if not provided `inlineMessageId` or `chatId` and `messageId`
     * @throws TelegramApiError if error returned in response
     */
    public fun editMessageReplyMarkup(
        chatId: Any? = null,
        messageId: Int? = null,
        inlineMessageId: String? = null,
        markup: InlineKeyboardMarkup? = null
    ): CompletableFuture<out Message>

    /**
     * @param chatId is `Int`, `Long` or `String`
     * @param sticker is [java.io.File] or `String`
     * @throws IllegalArgumentException if not provided `inlineMessageId` or `chatId` and `messageId`
     * @throws IllegalArgumentException if `sticker` neither [java.io.File] nor `String`
     * @throws TelegramApiError if error returned in response
     */
    public fun sendSticker(
        chatId: Any,
        sticker: Any,
        disableNotification: Boolean? = null,
        replyTo: Int? = null,
        markup: ReplyKeyboard? = null
    ): CompletableFuture<out Message>

    /**
     * @throws TelegramApiError if error returned in response
     */
    public fun getStickerSet(name: String): CompletableFuture<out StickerSet>

    /**
     * @throws TelegramApiError if error returned in response
     */
    public fun uploadStickerFile(
        userId: Long,
        pngSticker: java.io.File
    ): CompletableFuture<out File>

    /**
     * @param pngSticker is [java.io.File] or `String`
     * @throws TelegramApiError if error returned in response
     */
    public fun createNewStickerSet(
        userId: Long,
        name: String,
        title: String,
        emojis: String,
        pngSticker: Any? = null,
        tgsSticker: java.io.File? = null,
        containsMask: Boolean? = null,
        maskPosition: MaskPosition? = null
    ): CompletableFuture<out Boolean>

    /**
     * @param pngSticker is [java.io.File] or `String`
     * @throws TelegramApiError if error returned in response
     */
    public fun addStickerToSet(
        userId: Long,
        name: String,
        emojis: String,
        pngSticker: Any? = null,
        tgsSticker: java.io.File? = null,
        maskPosition: MaskPosition? = null
    ): CompletableFuture<out Boolean>

    /**
     * @throws TelegramApiError if error returned in response
     */
    public fun setStickerPositionInSet(
        sticker: String,
        position: Int
    ): CompletableFuture<out Boolean>

    /**
     * @throws TelegramApiError if error returned in response
     */
    public fun deleteStickerFromSet(sticker: String): CompletableFuture<out Boolean>

    /**
     * @throws TelegramApiError if error returned in response
     */
    public fun setStickerSetThumb(name: String, userId: Long, thumb: Any? = null): CompletableFuture<out Boolean>

    /**
     * @throws TelegramApiError if error returned in response
     */
    public fun sendGame(
        chatId: Long,
        gameShortName: String,
        disableNotification: Boolean? = null,
        replyTo: Int? = null,
        markup: InlineKeyboardMarkup? = null
    ): CompletableFuture<out Message>

    /**
     * @throws IllegalArgumentException if not provided `inlineMessageId` or `chatId` and `messageId`
     * @throws TelegramApiError if error returned in response
     */
    public fun setGameScore(
        userId: Long,
        score: Int,
        force: Boolean? = null,
        disableEditMessage: Boolean? = null,
        chatId: Long? = null,
        messageId: Int? = null,
        inlineMessageId: String? = null
    ): CompletableFuture<out Message>

    /**
     * @throws IllegalArgumentException if not provided `inlineMessageId` or `chatId` and `messageId`
     * @throws TelegramApiError if error returned in response
     */
    public fun getGameHighScores(
        userId: Long,
        chatId: Long? = null,
        messageId: Int? = null,
        inlineMessageId: String? = null
    ): CompletableFuture<out List<GameHighScore>>

    /**
     * @throws TelegramApiError if error returned in response
     */
    public fun sendInvoice(
        chatId: Long,
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
        markup: InlineKeyboardMarkup? = null
    ): CompletableFuture<out Message>

    /**
     * @throws TelegramApiError if error returned in response
     */
    public fun answerShippingQuery(
        shippingQueryId: String,
        ok: Boolean,
        shippingOptions: List<ShippingOption>? = null,
        errorMessage: String? = null
    ): CompletableFuture<out Boolean>

    /**
     * @throws TelegramApiError if error returned in response
     */
    public fun answerPreCheckoutQuery(
        preCheckoutQueryId: String,
        ok: Boolean,
        errorMessage: String? = null
    ): CompletableFuture<out Boolean>

    /**
     * @throws TelegramApiError if error returned in response
     */
    public fun setPassportDataErrors(
        userId: Long,
        errors: List<PassportElementError>
    ): CompletableFuture<out Boolean>

    /**
     * @throws TelegramApiError if error returned in response
     */
    public fun sendPoll(
        chatId: Any,
        question: String,
        options: List<String>,
        anonymous: Boolean? = null,
        type: String? = null,
        allowsMultipleAnswers: Boolean? = null,
        correctOptionId: Int? = null,
        explanation: String? = null,
        explanationParseMode: String? = null,
        openPeriod: Int? = null,
        closeDate: Long? = null,
        closed: Boolean? = null,
        disableNotification: Boolean? = null,
        replyTo: Int? = null,
        markup: ReplyKeyboard? = null
    ): CompletableFuture<out Message>

    /**
     * @throws TelegramApiError if error returned in response
     */
    public fun stopPoll(
        chatId: Any,
        messageId: Int,
        markup: InlineKeyboardMarkup? = null
    ): CompletableFuture<out Poll>

    /**
     * @throws TelegramApiError if error returned in response
     */
    public fun setChatPermissions(chatId: Any, permissions: ChatPermissions): CompletableFuture<out Boolean>

    /**
     * @throws TelegramApiError if error returned in response
     */
    public fun setChatAdministratorCustomTitle(
        chatId: Any,
        userId: Long,
        customTitle: String
    ): CompletableFuture<out Boolean>

    /**
     * @throws TelegramApiError if error returned in response
     */
    public fun deleteMessage(chatId: Any, messageId: Int): CompletableFuture<out Boolean>

    /**
     * @throws TelegramApiError if error returned in response
     */
    public fun sendDice(
        chatId: Any,
        emoji: String? = null,
        disableNotification: Boolean? = null,
        replyTo: Int? = null,
        markup: ReplyKeyboard? = null
    ): CompletableFuture<out Message>
}
