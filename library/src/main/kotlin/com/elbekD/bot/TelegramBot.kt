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
import com.elbekD.bot.types.MessageEntity
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
        parseMode: String?,
        captionEntities: List<MessageEntity>?
    ): InputMedia = InputMediaPhoto(
        media = media,
        attachment = attachment,
        caption = caption,
        parse_mode = parseMode,
        caption_entities = captionEntities
    )

    override fun mediaVideo(
        media: String,
        attachment: File?,
        thumb: File?,
        caption: String?,
        parseMode: String?,
        captionEntities: List<MessageEntity>?,
        width: Int?,
        height: Int?,
        duration: Int?,
        supportsStreaming: Boolean?
    ): InputMedia = InputMediaVideo(
        media = media,
        attachment = attachment,
        thumb = thumb,
        caption = caption,
        parse_mode = parseMode,
        caption_entities = captionEntities,
        width = width,
        height = height,
        duration = duration,
        supports_streaming = supportsStreaming
    )

    override fun mediaAnimation(
        media: String,
        attachment: File?,
        thumb: File?,
        caption: String?,
        parseMode: String?,
        captionEntities: List<MessageEntity>?,
        width: Int?,
        height: Int?,
        duration: Int?
    ): InputMedia = InputMediaAnimation(
        media = media,
        attachment = attachment,
        thumb = thumb,
        caption = caption,
        parse_mode = parseMode,
        caption_entities = captionEntities,
        width = width,
        height = height,
        duration = duration
    )

    override fun mediaAudio(
        media: String,
        attachment: File?,
        thumb: File?,
        caption: String?,
        parseMode: String?,
        captionEntities: List<MessageEntity>?,
        duration: Int?,
        performer: String?,
        title: String?
    ): InputMedia = InputMediaAudio(
        media = media,
        attachment = attachment,
        thumb = thumb,
        caption = caption,
        parse_mode = parseMode,
        caption_entities = captionEntities,
        duration = duration,
        performer = performer,
        title = title
    )

    override fun mediaDocument(
        media: String,
        attachment: File?,
        thumb: File?,
        caption: String?,
        parseMode: String?,
        captionEntities: List<MessageEntity>?,
        disableContentTypeDetection: Boolean?
    ): InputMedia = InputMediaDocument(
        media = media,
        attachment = attachment,
        thumb = thumb,
        caption = caption,
        parse_mode = parseMode,
        caption_entities = captionEntities,
        disable_content_type_detection = disableContentTypeDetection
    )

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

    override fun logOut(): CompletableFuture<out Boolean> = client.logOut()

    override fun close(): CompletableFuture<out Boolean> = client.close()

    override fun getUpdates(options: Map<String, Any?>): CompletableFuture<out List<Update>> =
        client.getUpdates(options)

    override fun getMyCommands(): CompletableFuture<out List<BotCommand>> = client.getMyCommands()

    override fun setMyCommands(commands: List<BotCommand>): CompletableFuture<out Boolean> =
        client.setMyCommands(commands)

    override fun setWebhook(
        url: String,
        certificate: File?,
        ipAddress: String?,
        maxConnections: Int?,
        allowedUpdates: List<AllowedUpdate>?,
        dropPendingUpdates: Boolean?
    ) = client.setWebhook(url, certificate, ipAddress, maxConnections, allowedUpdates, dropPendingUpdates)

    override fun deleteWebhook(dropPendingUpdates: Boolean?) = client.deleteWebhook(dropPendingUpdates)

    override fun getWebhookInfo() = client.getWebhookInfo()

    override fun sendMessage(
        chatId: Any,
        text: String,
        parseMode: String?,
        entities: List<MessageEntity>?,
        disableWebPagePreview: Boolean?,
        disableNotification: Boolean?,
        replyTo: Int?,
        allowSendingWithoutReply: Boolean?,
        markup: ReplyKeyboard?
    ) = client.sendMessage(
        chatId = chatId,
        text = text,
        parseMode = parseMode,
        entities = entities,
        disableWebPagePreview = disableWebPagePreview,
        disableNotification = disableNotification,
        replyTo = replyTo,
        allowSendingWithoutReply = allowSendingWithoutReply,
        markup = markup
    )

    override fun forwardMessage(
        chatId: Any,
        fromId: Any,
        msgId: Int,
        disableNotification: Boolean?
    ) = client.forwardMessage(
        chatId = chatId,
        fromId = fromId,
        msgId = msgId,
        disableNotification = disableNotification
    )

    override fun copyMessage(
        chatId: Any,
        fromChatId: Any,
        messageId: Int,
        caption: String?,
        parseMode: String?,
        captionEntities: List<MessageEntity>?,
        disableNotification: Boolean?,
        replyToMessageId: Int?,
        allowSendingWithoutReply: Boolean?,
        markup: ReplyKeyboard?
    ) = client.copyMessage(
        chatId = chatId,
        fromChatId = fromChatId,
        messageId = messageId,
        caption = caption,
        parseMode = parseMode,
        captionEntities = captionEntities,
        disableNotification = disableNotification,
        replyToMessageId = replyToMessageId,
        allowSendingWithoutReply = allowSendingWithoutReply,
        markup = markup
    )

    override fun sendPhoto(
        chatId: Any,
        photo: Any,
        caption: String?,
        parseMode: String?,
        captionEntities: List<MessageEntity>?,
        disableNotification: Boolean?,
        replyTo: Int?,
        allowSendingWithoutReply: Boolean?,
        markup: ReplyKeyboard?
    ) = client.sendPhoto(
        chatId = chatId,
        photo = photo,
        caption = caption,
        parseMode = parseMode,
        captionEntities = captionEntities,
        disableNotification = disableNotification,
        replyTo = replyTo,
        allowSendingWithoutReply = allowSendingWithoutReply,
        markup = markup
    )

    override fun sendAudio(
        chatId: Any,
        audio: Any,
        caption: String?,
        parseMode: String?,
        captionEntities: List<MessageEntity>?,
        duration: Int?,
        performer: String?,
        title: String?,
        thumb: File?,
        disableNotification: Boolean?,
        replyTo: Int?,
        allowSendingWithoutReply: Boolean?,
        markup: ReplyKeyboard?
    ) = client.sendAudio(
        chatId = chatId,
        audio = audio,
        caption = caption,
        parseMode = parseMode,
        captionEntities = captionEntities,
        duration = duration,
        performer = performer,
        title = title,
        thumb = thumb,
        disableNotification = disableNotification,
        replyTo = replyTo,
        allowSendingWithoutReply = allowSendingWithoutReply,
        markup = markup
    )

    override fun sendDocument(
        chatId: Any,
        document: Any,
        thumb: File?,
        caption: String?,
        parseMode: String?,
        captionEntities: List<MessageEntity>?,
        disableContentTypeDetection: Boolean?,
        disableNotification: Boolean?,
        replyTo: Int?,
        allowSendingWithoutReply: Boolean?,
        markup: ReplyKeyboard?
    ) = client.sendDocument(
        chatId,
        document,
        thumb,
        caption,
        parseMode,
        captionEntities,
        disableContentTypeDetection,
        disableNotification,
        replyTo,
        allowSendingWithoutReply,
        markup
    )

    override fun sendVideo(
        chatId: Any,
        video: Any,
        duration: Int?,
        width: Int?,
        height: Int?,
        thumb: File?,
        caption: String?,
        parseMode: String?,
        captionEntities: List<MessageEntity>?,
        streaming: Boolean?,
        disableNotification: Boolean?,
        replyTo: Int?,
        allowSendingWithoutReply: Boolean?,
        markup: ReplyKeyboard?
    ) = client.sendVideo(
        chatId = chatId,
        video = video,
        duration = duration,
        width = width,
        height = height,
        thumb = thumb,
        caption = caption,
        parseMode = parseMode,
        captionEntities = captionEntities,
        streaming = streaming,
        disableNotification = disableNotification,
        replyTo = replyTo,
        allowSendingWithoutReply = allowSendingWithoutReply,
        markup = markup
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
        captionEntities: List<MessageEntity>?,
        disableNotification: Boolean?,
        replyTo: Int?,
        allowSendingWithoutReply: Boolean?,
        markup: ReplyKeyboard?
    ) = client.sendAnimation(
        chatId = chatId,
        animation = animation,
        duration = duration,
        width = width,
        height = height,
        thumb = thumb,
        caption = caption,
        parseMode = parseMode,
        captionEntities = captionEntities,
        disableNotification = disableNotification,
        replyTo = replyTo,
        allowSendingWithoutReply = allowSendingWithoutReply,
        markup = markup
    )

    override fun sendVoice(
        chatId: Any,
        voice: Any,
        caption: String?,
        parseMode: String?,
        captionEntities: List<MessageEntity>?,
        duration: Int?,
        disableNotification: Boolean?,
        replyTo: Int?,
        allowSendingWithoutReply: Boolean?,
        markup: ReplyKeyboard?
    ) = client.sendVoice(
        chatId = chatId,
        voice = voice,
        caption = caption,
        parseMode = parseMode,
        captionEntities = captionEntities,
        duration = duration,
        disableNotification = disableNotification,
        replyTo = replyTo,
        allowSendingWithoutReply = allowSendingWithoutReply,
        markup = markup
    )

    override fun sendVideoNote(
        chatId: Any,
        note: Any,
        duration: Int?,
        length: Int?,
        thumb: File?,
        disableNotification: Boolean?,
        replyTo: Int?,
        allowSendingWithoutReply: Boolean?,
        markup: ReplyKeyboard?
    ) = client.sendVideoNote(
        chatId = chatId,
        note = note,
        duration = duration,
        length = length,
        thumb = thumb,
        disableNotification = disableNotification,
        replyTo = replyTo,
        allowSendingWithoutReply = allowSendingWithoutReply,
        markup = markup
    )

    override fun sendMediaGroup(
        chatId: Any,
        media: List<InputMedia>,
        disableNotification: Boolean?,
        replyTo: Int?,
        allowSendingWithoutReply: Boolean?
    ): CompletableFuture<out ArrayList<Message>> {
        if (media.size < 2) throw IllegalArgumentException("List must include 2-10 items")
        return client.sendMediaGroup(
            chatId,
            media,
            disableNotification,
            replyTo,
            allowSendingWithoutReply
        )
    }

    override fun sendLocation(
        chatId: Any,
        latitude: Double,
        longitude: Double,
        horizontalAccuracy: Float?,
        period: Int?,
        heading: Int?,
        proximityAlertRadius: Int?,
        disableNotification: Boolean?,
        replyTo: Int?,
        allowSendingWithoutReply: Boolean?,
        markup: ReplyKeyboard?
    ) = client.sendLocation(
        chatId = chatId,
        latitude = latitude,
        longitude = longitude,
        horizontalAccuracy = horizontalAccuracy,
        period = period,
        heading = heading,
        proximityAlertRadius = proximityAlertRadius,
        disableNotification = disableNotification,
        replyTo = replyTo,
        allowSendingWithoutReply = allowSendingWithoutReply,
        markup = markup
    )

    override fun editMessageLiveLocation(
        latitude: Double,
        longitude: Double,
        horizontalAccuracy: Float?,
        heading: Int?,
        proximityAlertRadius: Int?,
        chatId: Any?,
        messageId: Int?,
        inlineMessageId: String?,
        markup: InlineKeyboardMarkup?
    ): CompletableFuture<out Message> {
        validateIds(chatId, messageId, inlineMessageId)
        return client.editMessageLiveLocation(
            latitude = latitude,
            longitude = longitude,
            horizontalAccuracy = horizontalAccuracy,
            heading = heading,
            proximityAlertRadius = proximityAlertRadius,
            chatId = chatId,
            messageId = messageId,
            inlineMessageId = inlineMessageId,
            markup = markup
        )
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
        googlePlaceId: String?,
        googlePlaceType: String?,
        disableNotification: Boolean?,
        replyTo: Int?,
        allowSendingWithoutReply: Boolean?,
        markup: ReplyKeyboard?
    ) = client.sendVenue(
        chatId = chatId,
        latitude = latitude,
        longitude = longitude,
        title = title,
        address = address,
        foursquareId = foursquareId,
        foursquareType = foursquareType,
        googlePlaceId = googlePlaceId,
        googlePlaceType = googlePlaceType,
        disableNotification = disableNotification,
        replyTo = replyTo,
        allowSendingWithoutReply = allowSendingWithoutReply,
        markup = markup
    )

    override fun sendContact(
        chatId: Any,
        phone: String,
        firstName: String,
        lastName: String?,
        vcard: String?,
        disableNotification: Boolean?,
        replyTo: Int?,
        allowSendingWithoutReply: Boolean?,
        markup: ReplyKeyboard?
    ) = client.sendContact(
        chatId = chatId,
        phone = phone,
        firstName = firstName,
        lastName = lastName,
        vcard = vcard,
        disableNotification = disableNotification,
        replyTo = replyTo,
        allowSendingWithoutReply = allowSendingWithoutReply,
        markup = markup
    )

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
        userId: Long,
        onlyIfBanned: Boolean?
    ) = client.unbanChatMember(chatId, userId, onlyIfBanned)

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
        isAnonymous: Boolean?,
        canChangeInfo: Boolean?,
        canPostMessages: Boolean?,
        canEditMessages: Boolean?,
        canDeleteMessages: Boolean?,
        canInviteUsers: Boolean?,
        canRestrictMembers: Boolean?,
        canPinMessages: Boolean?,
        canPromoteMembers: Boolean?
    ) = client.promoteChatMember(
        chatId = chatId,
        userId = userId,
        isAnonymous = isAnonymous,
        canChangeInfo = canChangeInfo,
        canPostMessages = canPostMessages,
        canEditMessages = canEditMessages,
        canDeleteMessages = canDeleteMessages,
        canInviteUsers = canInviteUsers,
        canRestrictMembers = canRestrictMembers,
        canPinMessages = canPinMessages,
        canPromoteMembers = canPromoteMembers
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

    override fun unpinChatMessage(chatId: Any, messageId: Int?) = client.unpinChatMessage(chatId, messageId)

    override fun unpinAllChatMessages(chatId: Any): CompletableFuture<out Boolean> = client.unpinAllChatMessages(chatId)

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
        entities: List<MessageEntity>?,
        disableWebPagePreview: Boolean?,
        markup: InlineKeyboardMarkup?
    ): CompletableFuture<out Message> {
        validateIds(chatId, messageId, inlineMessageId)
        return client.editMessageText(
            chatId = chatId,
            messageId = messageId,
            inlineMessageId = inlineMessageId,
            text = text,
            parseMode = parseMode,
            entities = entities,
            disableWebPagePreview = disableWebPagePreview,
            markup = markup
        )
    }

    override fun editMessageCaption(
        chatId: Any?,
        messageId: Int?,
        inlineMessageId: String?,
        caption: String?,
        parseMode: String?,
        captionEntities: List<MessageEntity>?,
        markup: InlineKeyboardMarkup?
    ): CompletableFuture<out Message> {
        validateIds(chatId, messageId, inlineMessageId)
        return client.editMessageCaption(
            chatId = chatId,
            messageId = messageId,
            inlineMessageId = inlineMessageId,
            caption = caption,
            parseMode = parseMode,
            captionEntities = captionEntities,
            markup = markup
        )
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
        allowSendingWithoutReply: Boolean?,
        markup: ReplyKeyboard?
    ): CompletableFuture<out Message> {
        validateInputFileOrString(sticker)
        return client.sendSticker(
            chatId = chatId,
            sticker = sticker,
            disableNotification = disableNotification,
            replyTo = replyTo,
            allowSendingWithoutReply = allowSendingWithoutReply,
            markup = markup
        )
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
        allowSendingWithoutReply: Boolean?,
        markup: InlineKeyboardMarkup?
    ) = client.sendGame(
        chatId = chatId,
        gameShortName = gameShortName,
        disableNotification = disableNotification,
        replyTo = replyTo,
        allowSendingWithoutReply = allowSendingWithoutReply,
        markup = markup
    )

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
        allowSendingWithoutReply: Boolean?,
        markup: InlineKeyboardMarkup?
    ) = client.sendInvoice(
        chatId = chatId,
        title = title,
        description = description,
        payload = payload,
        providerToken = providerToken,
        startParam = startParam,
        currency = currency,
        prices = prices,
        providerData = providerData,
        photoUrl = photoUrl,
        photoSize = photoSize,
        photoWidth = photoWidth,
        photoHeight = photoHeight,
        needName = needName,
        needPhoneNumber = needPhoneNumber,
        needEmail = needEmail,
        needShippingAddress = needShippingAddress,
        sendPhoneNumberToProvider = sendPhoneNumberToProvider,
        sendEmailToProvider = sendEmailToProvider,
        isFlexible = isFlexible,
        disableNotification = disableNotification,
        replyTo = replyTo,
        allowSendingWithoutReply = allowSendingWithoutReply,
        markup = markup
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
        explanationEntities: List<MessageEntity>?,
        openPeriod: Int?,
        closeDate: Long?,
        closed: Boolean?,
        disableNotification: Boolean?,
        replyTo: Int?,
        allowSendingWithoutReply: Boolean?,
        markup: ReplyKeyboard?
    ): CompletableFuture<out Message> {
        if (openPeriod != null && closeDate != null) {
            throw IllegalArgumentException("openPeriod and closeDate can't be used together")
        }

        return client.sendPoll(
            chatId = chatId,
            question = question,
            options = options,
            anonymous = anonymous,
            type = type,
            allowsMultipleAnswers = allowsMultipleAnswers,
            correctOptionId = correctOptionId,
            explanation = explanation,
            explanationParseMode = explanationParseMode,
            explanationEntities = explanationEntities,
            openPeriod = openPeriod,
            closeDate = closeDate,
            closed = closed,
            disableNotification = disableNotification,
            replyTo = replyTo,
            allowSendingWithoutReply = allowSendingWithoutReply,
            markup = markup
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
        allowSendingWithoutReply: Boolean?,
        markup: ReplyKeyboard?
    ) = client.sendDice(
        chatId = chatId,
        emoji = emoji,
        disableNotification = disableNotification,
        replyTo = replyTo,
        allowSendingWithoutReply = allowSendingWithoutReply,
        markup = markup
    )
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
