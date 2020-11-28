package com.elbekD.bot

import com.elbekD.bot.http.TelegramClient
import com.elbekD.bot.types.BotCommand
import com.elbekD.bot.types.CallbackQuery
import com.elbekD.bot.types.ChatPermissions
import com.elbekD.bot.types.ChosenInlineResult
import com.elbekD.bot.types.GameHighScore
import com.elbekD.bot.types.InlineKeyboardMarkup
import com.elbekD.bot.types.InlineQuery
import com.elbekD.bot.types.InlineQueryResult
import com.elbekD.bot.types.InputMedia
import com.elbekD.bot.types.InputMediaAnimation
import com.elbekD.bot.types.InputMediaAudio
import com.elbekD.bot.types.InputMediaDocument
import com.elbekD.bot.types.InputMediaPhoto
import com.elbekD.bot.types.InputMediaVideo
import com.elbekD.bot.types.LabeledPrice
import com.elbekD.bot.types.MaskPosition
import com.elbekD.bot.types.Message
import com.elbekD.bot.types.PassportElementError
import com.elbekD.bot.types.Poll
import com.elbekD.bot.types.PreCheckoutQuery
import com.elbekD.bot.types.ReplyKeyboard
import com.elbekD.bot.types.ShippingOption
import com.elbekD.bot.types.ShippingQuery
import com.elbekD.bot.types.Update
import com.elbekD.bot.types.User
import com.elbekD.bot.util.Action
import com.elbekD.bot.util.AllowedUpdate
import java.io.File
import java.util.concurrent.CompletableFuture

internal abstract class TelegramBot protected constructor(username: String, tk: String) : Bot {
    private val updateHandler = UpdateHandler(username)
    private val client = TelegramClient(tk)

    companion object {
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
    }

    protected fun onUpdate(upds: List<Update>) = upds.forEach { onUpdate(it) }

    protected fun onUpdate(upd: Update) {
        updateHandler.handle(upd)
    }

    protected fun onStop() = client.onStop()

    override fun onMessage(action: suspend (Message) -> Unit) {
        updateHandler.on(AllowedUpdate.Message, action)
    }

    override fun removeMessageAction() {
        updateHandler.on<Message>(AllowedUpdate.Message, null)
    }

    override fun onEditedMessage(action: suspend (Message) -> Unit) {
        updateHandler.on(AllowedUpdate.EditedMessage, action)
    }

    override fun removeEditedMessageAction() {
        updateHandler.on<Message>(AllowedUpdate.EditedMessage, null)
    }

    override fun onChannelPost(action: suspend (Message) -> Unit) {
        updateHandler.on(AllowedUpdate.ChannelPost, action)
    }

    override fun removeChannelPostAction() {
        updateHandler.on<Message>(AllowedUpdate.ChannelPost, null)
    }

    override fun onEditedChannelPost(action: suspend (Message) -> Unit) {
        updateHandler.on(AllowedUpdate.EditedChannelPost, action)
    }

    override fun removeEditedChannelPostAction() {
        updateHandler.on<Message>(AllowedUpdate.EditedChannelPost, null)
    }

    override fun onInlineQuery(action: suspend (InlineQuery) -> Unit) {
        updateHandler.on(AllowedUpdate.InlineQuery, action)
    }

    override fun removeInlineQueryAction() {
        updateHandler.on<InlineQuery>(AllowedUpdate.InlineQuery, null)
    }

    override fun onChosenInlineQuery(action: suspend (ChosenInlineResult) -> Unit) {
        updateHandler.on(AllowedUpdate.ChosenInlineQuery, action)
    }

    override fun removeChosenInlineQueryAction() {
        updateHandler.on<ChosenInlineResult>(AllowedUpdate.ChosenInlineQuery, null)
    }

    override fun onCallbackQuery(action: suspend (CallbackQuery) -> Unit) {
        updateHandler.on(AllowedUpdate.CallbackQuery, action)
    }

    override fun removeCallbackQueryAction() {
        updateHandler.on<CallbackQuery>(AllowedUpdate.CallbackQuery, null)
    }

    override fun onShippingQuery(action: suspend (ShippingQuery) -> Unit) {
        updateHandler.on(AllowedUpdate.ShippingQuery, action)
    }

    override fun removeShippingQueryAction() {
        updateHandler.on<ShippingQuery>(AllowedUpdate.ShippingQuery, null)
    }

    override fun onPreCheckoutQuery(action: suspend (PreCheckoutQuery) -> Unit) {
        updateHandler.on(AllowedUpdate.PreCheckoutQuery, action)
    }

    override fun removePreCheckoutQueryAction() {
        updateHandler.on<PreCheckoutQuery>(AllowedUpdate.PreCheckoutQuery, null)
    }

    override fun onCommand(command: String, action: suspend (Message, String?) -> Unit) {
        updateHandler.onCommand(command, action)
    }

    override fun onCallbackQuery(data: String, action: suspend (CallbackQuery) -> Unit) {
        updateHandler.onCallbackQuery(data, action)
    }

    override fun onInlineQuery(query: String, action: suspend (InlineQuery) -> Unit) {
        updateHandler.onInlineQuery(query, action)
    }

    override fun onAnyUpdate(action: suspend (Update) -> Unit) {
        updateHandler.onAnyUpdate(action)
    }

    override fun mediaPhoto(
        media: String,
        attachment: File?,
        caption: String?,
        parseMode: String?
    ): InputMedia =
        InputMediaPhoto(media, attachment, caption, parseMode)

    override fun mediaVideo(
        media: String,
        attachment: File?,
        thumb: File?,
        caption: String?,
        parseMode: String?,
        width: Int?,
        height: Int?,
        duration: Int?,
        supportsStreaming: Boolean?
    ): InputMedia =
        InputMediaVideo(media, attachment, thumb, caption, parseMode, width, height, duration, supportsStreaming)

    override fun mediaAnimation(
        media: String,
        attachment: File?,
        thumb: File?,
        caption: String?,
        parseMode: String?,
        width: Int?,
        height: Int?,
        duration: Int?
    ): InputMedia =
        InputMediaAnimation(media, attachment, thumb, caption, parseMode, width, height, duration)

    override fun mediaAudio(
        media: String,
        attachment: File?,
        thumb: File?,
        caption: String?,
        parseMode: String?,
        duration: Int?,
        performer: String?,
        title: String?
    ): InputMedia =
        InputMediaAudio(media, attachment, thumb, caption, parseMode, duration, performer, title)

    override fun mediaDocument(
        media: String,
        attachment: File?,
        thumb: File?,
        caption: String?,
        parseMode: String?
    ): InputMedia =
        InputMediaDocument(media, attachment, thumb, caption, parseMode)

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
    override fun getMe(): CompletableFuture<out User> = client.getMe()

    override fun getUpdates(options: Map<String, Any?>): CompletableFuture<out List<Update>> =
        client.getUpdates(options)

    override fun getMyCommands(): CompletableFuture<out List<BotCommand>> = client.getMyCommands()

    override fun setMyCommands(commands: List<BotCommand>): CompletableFuture<out Boolean> =
        client.setMyCommands(commands)

    override fun setWebhook(
        url: String,
        certificate: File?,
        maxConnections: Int?,
        allowedUpdates: List<AllowedUpdate>?
    ) =
        client.setWebhook(url, certificate, maxConnections, allowedUpdates)

    override fun deleteWebhook() = client.deleteWebhook()

    override fun getWebhookInfo() = client.getWebhookInfo()

    override fun sendMessage(
        chatId: Any,
        text: String,
        parseMode: String?,
        disableWebPagePreview: Boolean?,
        disableNotification: Boolean?,
        replyTo: Int?,
        markup: ReplyKeyboard?
    ) =
        client.sendMessage(chatId, text, parseMode, disableWebPagePreview, disableNotification, replyTo, markup)

    override fun forwardMessage(
        chatId: Any,
        fromId: Any,
        msgId: Int,
        disableNotification: Boolean?
    ) =
        client.forwardMessage(chatId, fromId, msgId, disableNotification)

    override fun sendPhoto(
        chatId: Any,
        photo: Any,
        caption: String?,
        parseMode: String?,
        disableNotification: Boolean?,
        replyTo: Int?,
        markup: ReplyKeyboard?
    ) =
        client.sendPhoto(chatId, photo, caption, parseMode, disableNotification, replyTo, markup)

    override fun sendAudio(
        chatId: Any,
        audio: Any,
        caption: String?,
        parseMode: String?,
        duration: Int?,
        performer: String?,
        title: String?,
        thumb: File?,
        disableNotification: Boolean?,
        replyTo: Int?,
        markup: ReplyKeyboard?
    ) =
        client.sendAudio(
            chatId,
            audio,
            caption,
            parseMode,
            duration,
            performer,
            title,
            thumb,
            disableNotification,
            replyTo,
            markup
        )

    override fun sendDocument(
        chatId: Any,
        document: Any,
        thumb: File?,
        caption: String?,
        parseMode: String?,
        disableNotification: Boolean?,
        replyTo: Int?,
        markup: ReplyKeyboard?
    ) =
        client.sendDocument(chatId, document, thumb, caption, parseMode, disableNotification, replyTo, markup)

    override fun sendVideo(
        chatId: Any,
        video: Any,
        duration: Int?,
        width: Int?,
        height: Int?,
        thumb: File?,
        caption: String?,
        parseMode: String?,
        streaming: Boolean?,
        disableNotification: Boolean?,
        replyTo: Int?,
        markup: ReplyKeyboard?
    ) =
        client.sendVideo(
            chatId,
            video,
            duration,
            width,
            height,
            thumb,
            caption,
            parseMode,
            streaming,
            disableNotification,
            replyTo,
            markup
        )

    override fun sendAnimation(
        chatId: Any,
        animation: Any,
        duration: Int?,
        width: Int?,
        height: Int?,
        thumb: File?,
        caption: String?,
        parseMode: String?,
        disableNotification: Boolean?,
        replyTo: Int?,
        markup: ReplyKeyboard?
    ) =
        client.sendAnimation(
            chatId,
            animation,
            duration,
            width,
            height,
            thumb,
            caption,
            parseMode,
            disableNotification,
            replyTo,
            markup
        )

    override fun sendVoice(
        chatId: Any,
        voice: Any,
        caption: String?,
        parseMode: String?,
        duration: Int?,
        disableNotification: Boolean?,
        replyTo: Int?,
        markup: ReplyKeyboard?
    ) =
        client.sendVoice(chatId, voice, caption, parseMode, duration, disableNotification, replyTo, markup)

    override fun sendVideoNote(
        chatId: Any,
        note: Any,
        duration: Int?,
        length: Int?,
        thumb: File?,
        disableNotification: Boolean?,
        replyTo: Int?,
        markup: ReplyKeyboard?
    ) =
        client.sendVideoNote(chatId, note, duration, length, thumb, disableNotification, replyTo, markup)

    override fun sendMediaGroup(
        chatId: Any,
        media: List<InputMedia>,
        disableNotification: Boolean?,
        replyTo: Int?
    ): CompletableFuture<out ArrayList<Message>> {
        if (media.size < 2) throw IllegalArgumentException("List must include 2-10 items")
        return client.sendMediaGroup(chatId, media, disableNotification, replyTo)
    }

    override fun sendLocation(
        chatId: Any,
        latitude: Double,
        longitude: Double,
        period: Int?,
        disableNotification: Boolean?,
        replyTo: Int?,
        markup: ReplyKeyboard?
    ) =
        client.sendLocation(chatId, latitude, longitude, period, disableNotification, replyTo, markup)

    override fun editMessageLiveLocation(
        latitude: Double,
        longitude: Double,
        chatId: Any?,
        messageId: Int?,
        inlineMessageId: String?,
        markup: InlineKeyboardMarkup?
    ): CompletableFuture<out Message> {
        validateIds(chatId, messageId, inlineMessageId)
        return client.editMessageLiveLocation(latitude, longitude, chatId, messageId, inlineMessageId, markup)
    }

    override fun stopMessageLiveLocation(
        chatId: Any?,
        messageId: Int?,
        inlineMessageId: String?,
        markup: InlineKeyboardMarkup?
    ): CompletableFuture<out Message> {
        validateIds(chatId, messageId, inlineMessageId)
        return client.stopMessageLiveLocation(chatId, messageId, inlineMessageId, markup)
    }

    override fun sendVenue(
        chatId: Any,
        latitude: Double,
        longitude: Double,
        title: String,
        address: String,
        foursquareId: String?,
        foursquareType: String?,
        disableNotification: Boolean?,
        replyTo: Int?,
        markup: ReplyKeyboard?
    ) =
        client.sendVenue(
            chatId,
            latitude,
            longitude,
            title,
            address,
            foursquareId,
            foursquareType,
            disableNotification,
            replyTo,
            markup
        )

    override fun sendContact(
        chatId: Any,
        phone: String,
        firstName: String,
        lastName: String?,
        vcard: String?,
        disableNotification: Boolean?,
        replyTo: Int?,
        markup: ReplyKeyboard?
    ) =
        client.sendContact(chatId, phone, firstName, lastName, vcard, disableNotification, replyTo, markup)

    override fun sendChatAction(
        chatId: Any,
        action: Action
    ) = client.sendChatAction(chatId, action)

    override fun getUserProfilePhotos(
        userId: Long,
        offset: Int?,
        limit: Int?
    ) = client.getUserProfilePhotos(userId, offset, limit)

    override fun getFile(fileId: String) = client.getFile(fileId)

    override fun kickChatMember(
        chatId: Any,
        userId: Long,
        untilDate: Int?
    ) = client.kickChatMember(chatId, userId, untilDate)

    override fun unbanChatMember(
        chatId: Any,
        userId: Long
    ) = client.unbanChatMember(chatId, userId)

    override fun restrictChatMember(
        chatId: Any,
        userId: Long,
        permissions: ChatPermissions,
        untilDate: Int?
    ) =
        client.restrictChatMember(chatId, userId, permissions, untilDate)

    override fun promoteChatMember(
        chatId: Any,
        userId: Long,
        canChangeInfo: Boolean?,
        canPostMessages: Boolean?,
        canEditMessages: Boolean?,
        canDeleteMessages: Boolean?,
        canInviteUsers: Boolean?,
        canRestrictMembers: Boolean?,
        canPinMessages: Boolean?,
        canPromoteMembers: Boolean?
    ) =
        client.promoteChatMember(
            chatId,
            userId,
            canChangeInfo,
            canPostMessages,
            canEditMessages,
            canDeleteMessages,
            canInviteUsers,
            canRestrictMembers,
            canPinMessages,
            canPromoteMembers
        )

    override fun exportChatInviteLink(chatId: Any) = client.exportChatInviteLink(chatId)

    override fun setChatPhoto(
        chatId: Any,
        photo: Any
    ) = client.setChatPhoto(chatId, photo)

    override fun deleteChatPhoto(chatId: Any) = client.deleteChatPhoto(chatId)

    override fun setChatTitle(
        chatId: Any,
        title: String
    ) = client.setChatTitle(chatId, title)

    override fun setChatDescription(
        chatId: Any,
        description: String
    ) = client.setChatDescription(chatId, description)

    override fun pinChatMessage(
        chatId: Any,
        messageId: Int,
        disableNotification: Boolean?
    ) = client.pinChatMessage(chatId, messageId, disableNotification)

    override fun unpinChatMessage(chatId: Any) = client.unpinChatMessage(chatId)

    override fun leaveChat(chatId: Any) = client.leaveChat(chatId)

    override fun getChat(chatId: Any) = client.getChat(chatId)

    override fun getChatAdministrators(chatId: Any) = client.getChatAdministrators(chatId)

    override fun getChatMembersCount(chatId: Any) = client.getChatMembersCount(chatId)

    override fun getChatMember(
        chatId: Any,
        userId: Long
    ) = client.getChatMember(chatId, userId)

    override fun setChatStickerSet(
        chatId: Any,
        stickerSetName: String
    ) = client.setChatStickerSet(chatId, stickerSetName)

    override fun deleteChatStickerSet(chatId: Any) = client.deleteChatStickerSet(chatId)

    override fun answerCallbackQuery(
        id: String,
        text: String?,
        alert: Boolean?,
        url: String?,
        cacheTime: Int?
    ) = client.answerCallbackQuery(id, text, alert, url, cacheTime)

    override fun answerInlineQuery(
        queryId: String,
        results: List<InlineQueryResult>,
        cacheTime: Int?,
        personal: Boolean?,
        offset: String?,
        pmText: String?,
        pmParameter: String?
    ) =
        client.answerInlineQuery(queryId, results, cacheTime, personal, offset, pmText, pmParameter)

    override fun editMessageText(
        chatId: Any?,
        messageId: Int?,
        inlineMessageId: String?,
        text: String,
        parseMode: String?,
        disableWebPagePreview: Boolean?,
        markup: InlineKeyboardMarkup?
    ): CompletableFuture<out Message> {
        validateIds(chatId, messageId, inlineMessageId)
        return client.editMessageText(
            chatId,
            messageId,
            inlineMessageId,
            text,
            parseMode,
            disableWebPagePreview,
            markup
        )
    }

    override fun editMessageCaption(
        chatId: Any?,
        messageId: Int?,
        inlineMessageId: String?,
        caption: String?,
        parseMode: String?,
        markup: InlineKeyboardMarkup?
    ): CompletableFuture<out Message> {
        validateIds(chatId, messageId, inlineMessageId)
        return client.editMessageCaption(chatId, messageId, inlineMessageId, caption, parseMode, markup)
    }

    override fun editMessageMedia(
        chatId: Any?,
        messageId: Int?,
        inlineMessageId: String?,
        media: InputMedia,
        markup: InlineKeyboardMarkup?
    ): CompletableFuture<out Message> {
        validateIds(chatId, messageId, inlineMessageId)
        return client.editMessageMedia(chatId, messageId, inlineMessageId, media, markup)
    }

    override fun editMessageReplyMarkup(
        chatId: Any?,
        messageId: Int?,
        inlineMessageId: String?,
        markup: InlineKeyboardMarkup?
    ): CompletableFuture<out Message> {
        validateIds(chatId, messageId, inlineMessageId)
        return client.editMessageReplyMarkup(chatId, messageId, inlineMessageId, markup)
    }

    override fun sendSticker(
        chatId: Any,
        sticker: Any,
        disableNotification: Boolean?,
        replyTo: Int?,
        markup: ReplyKeyboard?
    ): CompletableFuture<out Message> {
        validateInputFileOrString(sticker)
        return client.sendSticker(chatId, sticker, disableNotification, replyTo, markup)
    }

    override fun getStickerSet(name: String) = client.getStickerSet(name)

    override fun uploadStickerFile(
        userId: Long,
        pngSticker: File
    ) = client.uploadStickerFile(userId, pngSticker)

    override fun createNewStickerSet(
        userId: Long,
        name: String,
        title: String,
        emojis: String,
        pngSticker: Any?,
        tgsSticker: File?,
        containsMask: Boolean?,
        maskPosition: MaskPosition?
    ): CompletableFuture<out Boolean> {
        if (pngSticker != null && tgsSticker != null) {
            throw IllegalArgumentException("Use exactly one of the fields pngSticker or tgsSticker")
        }

        pngSticker?.let { validateInputFileOrString(it) }
        return client.createNewStickerSet(
            userId,
            name,
            title,
            emojis,
            pngSticker,
            tgsSticker,
            containsMask,
            maskPosition
        )
    }

    override fun addStickerToSet(
        userId: Long,
        name: String,
        emojis: String,
        pngSticker: Any?,
        tgsSticker: File?,
        maskPosition: MaskPosition?
    ): CompletableFuture<out Boolean> {
        if (pngSticker != null && tgsSticker != null) {
            throw IllegalArgumentException("Use exactly one of the fields pngSticker or tgsSticker")
        }

        pngSticker?.let { validateInputFileOrString(it) }
        return client.addStickerToSet(userId, name, emojis, pngSticker, tgsSticker, maskPosition)
    }

    override fun setStickerPositionInSet(
        sticker: String,
        position: Int
    ) = client.setStickerPositionInSet(sticker, position)

    override fun deleteStickerFromSet(sticker: String) = client.deleteStickerFromSet(sticker)

    override fun setStickerSetThumb(name: String, userId: Long, thumb: Any?): CompletableFuture<out Boolean> {
        if (thumb !is File || thumb !is String) {
            throw IllegalArgumentException("Neither file nor string")
        }

        return client.setStickerSetThumb(name, userId, thumb)
    }

    override fun sendGame(
        chatId: Long,
        gameShortName: String,
        disableNotification: Boolean?,
        replyTo: Int?,
        markup: InlineKeyboardMarkup?
    ) = client.sendGame(chatId, gameShortName, disableNotification, replyTo, markup)

    override fun setGameScore(
        userId: Long,
        score: Int,
        force: Boolean?,
        disableEditMessage: Boolean?,
        chatId: Long?,
        messageId: Int?, inlineMessageId: String?
    ): CompletableFuture<out Message> {
        validateIds(chatId, messageId, inlineMessageId)
        return client.setGameScore(userId, score, force, disableEditMessage, chatId, messageId, inlineMessageId)
    }

    override fun getGameHighScores(
        userId: Long,
        chatId: Long?,
        messageId: Int?,
        inlineMessageId: String?
    ): CompletableFuture<out List<GameHighScore>> {
        validateIds(chatId, messageId, inlineMessageId)
        return client.getGameHighScores(userId, chatId, messageId, inlineMessageId)
    }

    override fun sendInvoice(
        chatId: Long,
        title: String,
        description: String,
        payload: String,
        providerToken: String,
        startParam: String,
        currency: String,
        prices: List<LabeledPrice>,
        providerData: String?,
        photoUrl: String?,
        photoSize: Int?,
        photoWidth: Int?,
        photoHeight: Int?,
        needName: Boolean?,
        needPhoneNumber: Boolean?,
        needEmail: Boolean?,
        needShippingAddress: Boolean?,
        sendPhoneNumberToProvider: Boolean?,
        sendEmailToProvider: Boolean?,
        isFlexible: Boolean?,
        disableNotification: Boolean?,
        replyTo: Int?,
        markup: InlineKeyboardMarkup?
    ) = client.sendInvoice(
        chatId,
        title,
        description,
        payload,
        providerToken,
        startParam,
        currency,
        prices,
        providerData,
        photoUrl,
        photoSize,
        photoWidth,
        photoHeight,
        needName,
        needPhoneNumber,
        needEmail,
        needShippingAddress,
        sendPhoneNumberToProvider,
        sendEmailToProvider,
        isFlexible,
        disableNotification,
        replyTo,
        markup
    )

    override fun answerShippingQuery(
        shippingQueryId: String,
        ok: Boolean,
        shippingOptions: List<ShippingOption>?,
        errorMessage: String?
    ) = client.answerShippingQuery(shippingQueryId, ok, shippingOptions, errorMessage)

    override fun answerPreCheckoutQuery(
        preCheckoutQueryId: String,
        ok: Boolean,
        errorMessage: String?
    ) = client.answerPreCheckoutQuery(preCheckoutQueryId, ok, errorMessage)

    override fun setPassportDataErrors(
        userId: Long,
        errors: List<PassportElementError>
    ) = client.setPassportDataErrors(userId, errors)

    override fun sendPoll(
        chatId: Any,
        question: String,
        options: List<String>,
        anonymous: Boolean?,
        type: String?,
        allowsMultipleAnswers: Boolean?,
        correctOptionId: Int?,
        explanation: String?,
        explanationParseMode: String?,
        openPeriod: Int?,
        closeDate: Long?,
        closed: Boolean?,
        disableNotification: Boolean?,
        replyTo: Int?,
        markup: ReplyKeyboard?
    ): CompletableFuture<out Message> {
        if (openPeriod != null && closeDate != null) {
            throw IllegalArgumentException("openPeriod and closeDate can't be used together")
        }

        return client.sendPoll(
            chatId,
            question,
            options,
            anonymous,
            type,
            allowsMultipleAnswers,
            correctOptionId,
            explanation,
            explanationParseMode,
            openPeriod,
            closeDate,
            closed,
            disableNotification,
            replyTo,
            markup
        )
    }

    override fun stopPoll(chatId: Any, messageId: Int, markup: InlineKeyboardMarkup?): CompletableFuture<out Poll> =
        client.stopPoll(chatId, messageId, markup)

    override fun setChatPermissions(chatId: Any, permissions: ChatPermissions) =
        client.setChatPermissions(chatId, permissions)

    override fun setChatAdministratorCustomTitle(chatId: Any, userId: Long, customTitle: String) =
        client.setChatAdministratorCustomTitle(chatId, userId, customTitle)

    override fun deleteMessage(chatId: Any, messageId: Int): CompletableFuture<out Boolean> =
        client.deleteMessage(chatId, messageId)

    override fun sendDice(
        chatId: Any,
        emoji: String?,
        disableNotification: Boolean?,
        replyTo: Int?,
        markup: ReplyKeyboard?
    ): CompletableFuture<out Message> = client.sendDice(chatId, emoji, disableNotification, replyTo, markup)
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
