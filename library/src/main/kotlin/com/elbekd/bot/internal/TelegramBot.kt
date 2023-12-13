package com.elbekd.bot.internal

import com.elbekd.bot.Bot
import com.elbekd.bot.model.ChatId
import com.elbekd.bot.types.*
import com.elbekd.bot.util.Action
import com.elbekd.bot.util.AllowedUpdate
import com.elbekd.bot.util.SendingDocument
import java.io.File

internal abstract class TelegramBot protected constructor(username: String?, tk: String) : Bot {
    private val updateHandler = UpdateHandler(username)
    private val client = TelegramClient(tk)

    protected suspend fun onUpdate(updates: List<Update>) = updates.forEach { updateHandler.handle(it) }

    protected suspend fun onUpdate(update: UpdateResponse) {
        updateHandler.handle(update)
    }

    protected fun onStop() {
        client.onStop()
    }

    override fun onMessage(action: (suspend (Message) -> Unit)?) {
        updateHandler.on(AllowedUpdate.Message, action)
    }

    override fun onEditedMessage(action: (suspend (Message) -> Unit)?) {
        updateHandler.on(AllowedUpdate.EditedMessage, action)
    }

    override fun onChannelPost(action: (suspend (Message) -> Unit)?) {
        updateHandler.on(AllowedUpdate.ChannelPost, action)
    }

    override fun onEditedChannelPost(action: (suspend (Message) -> Unit)?) {
        updateHandler.on(AllowedUpdate.EditedChannelPost, action)
    }

    override fun onInlineQuery(action: (suspend (InlineQuery) -> Unit)?) {
        updateHandler.on(AllowedUpdate.InlineQuery, action)
    }

    override fun onChosenInlineQuery(action: (suspend (ChosenInlineResult) -> Unit)?) {
        updateHandler.on(AllowedUpdate.ChosenInlineQuery, action)
    }

    override fun onCallbackQuery(action: (suspend (CallbackQuery) -> Unit)?) {
        updateHandler.on(AllowedUpdate.CallbackQuery, action)
    }

    override fun onShippingQuery(action: (suspend (ShippingQuery) -> Unit)?) {
        updateHandler.on(AllowedUpdate.ShippingQuery, action)
    }

    override fun onPreCheckoutQuery(action: (suspend (PreCheckoutQuery) -> Unit)?) {
        updateHandler.on(AllowedUpdate.PreCheckoutQuery, action)
    }

    override fun onCommand(command: String, action: (suspend (Pair<Message, String?>) -> Unit)?) {
        updateHandler.onCommand(command, action)
    }

    override fun onCallbackQuery(data: String, action: (suspend (CallbackQuery) -> Unit)?) {
        updateHandler.onCallbackQuery(data, action)
    }

    override fun onInlineQuery(query: String, action: (suspend (InlineQuery) -> Unit)?) {
        updateHandler.onInlineQuery(query, action)
    }

    override fun onAnyUpdate(action: (suspend (Update) -> Unit)?) {
        updateHandler.onAnyUpdate(action)
    }

    // Telegram methods
    override suspend fun getMe(): User = client.getMe()

    override suspend fun logOut(): Boolean = client.logOut()

    override suspend fun close(): Boolean = client.close()

    override suspend fun getUpdates(
        offset: Int?, limit: Int?, timeout: Int?, allowedUpdates: List<AllowedUpdate>?
    ): List<Update> = client.getUpdates(
        offset, limit, timeout, allowedUpdates
    )

    override suspend fun getMyCommands(
        scope: BotCommandScope?, languageCode: String?
    ): List<BotCommand> = client.getMyCommands(scope, languageCode)

    override suspend fun setMyCommands(
        commands: List<BotCommand>, scope: BotCommandScope?, languageCode: String?
    ): Boolean = client.setMyCommands(commands, scope, languageCode)

    override suspend fun deleteMyCommands(
        scope: BotCommandScope?, languageCode: String?
    ): Boolean = client.deleteMyCommands(scope, languageCode)

    override suspend fun setWebhook(
        url: String,
        certificate: File?,
        ipAddress: String?,
        maxConnections: Int?,
        allowedUpdates: List<AllowedUpdate>?,
        dropPendingUpdates: Boolean?,
        secretToken: String?,
    ) = client.setWebhook(url, certificate, ipAddress, maxConnections, allowedUpdates, dropPendingUpdates, secretToken)

    override suspend fun deleteWebhook(dropPendingUpdates: Boolean?) = client.deleteWebhook(dropPendingUpdates)

    override suspend fun getWebhookInfo() = client.getWebhookInfo()

    override suspend fun sendMessage(
        chatId: ChatId,
        text: String,
        messageThreadId: Long?,
        parseMode: ParseMode?,
        entities: List<MessageEntity>?,
        disableWebPagePreview: Boolean?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ) = client.sendMessage(
        chatId = chatId,
        text = text,
        messageThreadId = messageThreadId,
        parseMode = parseMode,
        entities = entities,
        disableWebPagePreview = disableWebPagePreview,
        disableNotification = disableNotification,
        protectContent = protectContent,
        replyToMessageId = replyToMessageId,
        allowSendingWithoutReply = allowSendingWithoutReply,
        replyMarkup = replyMarkup
    )

    override fun sendAsyncMessage(
        chatId: ChatId,
        text: String,
        messageThreadId: Long?,
        parseMode: ParseMode?,
        entities: List<MessageEntity>?,
        disableWebPagePreview: Boolean?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ) {
        client.sendAsyncMessage(
            chatId = chatId,
            text = text,
            messageThreadId = messageThreadId,
            parseMode = parseMode,
            entities = entities,
            disableWebPagePreview = disableWebPagePreview,
            disableNotification = disableNotification,
            protectContent = protectContent,
            replyToMessageId = replyToMessageId,
            allowSendingWithoutReply = allowSendingWithoutReply,
            replyMarkup = replyMarkup
        )
    }

    override suspend fun forwardMessage(
        chatId: ChatId,
        fromChatId: ChatId,
        msgId: Long,
        messageThreadId: Long?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
    ) = client.forwardMessage(
        chatId = chatId,
        fromChatId = fromChatId,
        msgId = msgId,
        messageThreadId = messageThreadId,
        disableNotification = disableNotification,
        protectContent = protectContent,
    )

    override suspend fun copyMessage(
        chatId: ChatId,
        fromChatId: ChatId,
        messageId: Long,
        messageThreadId: Long?,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ) = client.copyMessage(
        chatId = chatId,
        fromChatId = fromChatId,
        messageId = messageId,
        messageThreadId = messageThreadId,
        caption = caption,
        parseMode = parseMode,
        captionEntities = captionEntities,
        disableNotification = disableNotification,
        protectContent = protectContent,
        replyToMessageId = replyToMessageId,
        allowSendingWithoutReply = allowSendingWithoutReply,
        replyMarkup = replyMarkup
    )

    override suspend fun sendPhoto(
        chatId: ChatId,
        photo: SendingDocument,
        messageThreadId: Long?,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        hasSpoiler: Boolean?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ) = client.sendPhoto(
        chatId = chatId,
        photo = photo,
        messageThreadId = messageThreadId,
        caption = caption,
        parseMode = parseMode,
        captionEntities = captionEntities,
        hasSpoiler = hasSpoiler,
        disableNotification = disableNotification,
        protectContent = protectContent,
        replyToMessageId = replyToMessageId,
        allowSendingWithoutReply = allowSendingWithoutReply,
        replyMarkup = replyMarkup
    )

    override suspend fun sendAudio(
        chatId: ChatId,
        audio: SendingDocument,
        messageThreadId: Long?,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        duration: Long?,
        performer: String?,
        title: String?,
        thumbnail: File?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ) = client.sendAudio(
        chatId = chatId,
        audio = audio,
        messageThreadId = messageThreadId,
        caption = caption,
        parseMode = parseMode,
        captionEntities = captionEntities,
        duration = duration,
        performer = performer,
        title = title,
        thumbnail = thumbnail,
        disableNotification = disableNotification,
        protectContent = protectContent,
        replyToMessageId = replyToMessageId,
        allowSendingWithoutReply = allowSendingWithoutReply,
        replyMarkup = replyMarkup
    )

    override suspend fun sendDocument(
        chatId: ChatId,
        document: SendingDocument,
        messageThreadId: Long?,
        thumbnail: File?,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        disableContentTypeDetection: Boolean?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ) = client.sendDocument(
        chatId = chatId,
        document = document,
        messageThreadId = messageThreadId,
        thumbnail = thumbnail,
        caption = caption,
        parseMode = parseMode,
        captionEntities = captionEntities,
        disableContentTypeDetection = disableContentTypeDetection,
        disableNotification = disableNotification,
        protectContent = protectContent,
        replyToMessageId = replyToMessageId,
        allowSendingWithoutReply = allowSendingWithoutReply,
        replyMarkup = replyMarkup
    )

    override suspend fun sendVideo(
        chatId: ChatId,
        video: SendingDocument,
        messageThreadId: Long?,
        duration: Long?,
        width: Long?,
        height: Long?,
        thumbnail: File?,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        hasSpoiler: Boolean?,
        streaming: Boolean?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ) = client.sendVideo(
        chatId = chatId,
        video = video,
        messageThreadId = messageThreadId,
        duration = duration,
        width = width,
        height = height,
        thumbnail = thumbnail,
        caption = caption,
        parseMode = parseMode,
        captionEntities = captionEntities,
        hasSpoiler = hasSpoiler,
        streaming = streaming,
        disableNotification = disableNotification,
        protectContent = protectContent,
        replyToMessageId = replyToMessageId,
        allowSendingWithoutReply = allowSendingWithoutReply,
        replyMarkup = replyMarkup
    )

    override suspend fun sendAnimation(
        chatId: ChatId,
        animation: SendingDocument,
        messageThreadId: Long?,
        duration: Long?,
        width: Long?,
        height: Long?,
        thumbnail: File?,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        hasSpoiler: Boolean?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ) = client.sendAnimation(
        chatId = chatId,
        animation = animation,
        messageThreadId = messageThreadId,
        duration = duration,
        width = width,
        height = height,
        thumbnail = thumbnail,
        caption = caption,
        parseMode = parseMode,
        captionEntities = captionEntities,
        hasSpoiler = hasSpoiler,
        disableNotification = disableNotification,
        protectContent = protectContent,
        replyToMessageId = replyToMessageId,
        allowSendingWithoutReply = allowSendingWithoutReply,
        replyMarkup = replyMarkup
    )

    override suspend fun sendVoice(
        chatId: ChatId,
        voice: SendingDocument,
        messageThreadId: Long?,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        duration: Long?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ) = client.sendVoice(
        chatId = chatId,
        voice = voice,
        messageThreadId = messageThreadId,
        caption = caption,
        parseMode = parseMode,
        captionEntities = captionEntities,
        duration = duration,
        disableNotification = disableNotification,
        protectContent = protectContent,
        replyToMessageId = replyToMessageId,
        allowSendingWithoutReply = allowSendingWithoutReply,
        replyMarkup = replyMarkup
    )

    override suspend fun sendVideoNote(
        chatId: ChatId,
        note: SendingDocument,
        messageThreadId: Long?,
        duration: Long?,
        length: Long?,
        thumbnail: File?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ) = client.sendVideoNote(
        chatId = chatId,
        note = note,
        messageThreadId = messageThreadId,
        duration = duration,
        length = length,
        thumbnail = thumbnail,
        disableNotification = disableNotification,
        protectContent = protectContent,
        replyToMessageId = replyToMessageId,
        allowSendingWithoutReply = allowSendingWithoutReply,
        replyMarkup = replyMarkup
    )

    override suspend fun sendMediaGroup(
        chatId: ChatId,
        media: List<InputMedia>,
        messageThreadId: Long?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?
    ): ArrayList<Message> {
        if (media.size < 2) throw IllegalArgumentException("List must include 2-10 items")
        return client.sendMediaGroup(
            chatId = chatId,
            media = media,
            messageThreadId = messageThreadId,
            disableNotification = disableNotification,
            protectContent = protectContent,
            replyToMessageId = replyToMessageId,
            allowSendingWithoutReply = allowSendingWithoutReply
        )
    }

    override suspend fun sendLocation(
        chatId: ChatId,
        latitude: Float,
        longitude: Float,
        messageThreadId: Long?,
        horizontalAccuracy: Float?,
        livePeriod: Long?,
        heading: Long?,
        proximityAlertRadius: Long?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ) = client.sendLocation(
        chatId = chatId,
        latitude = latitude,
        longitude = longitude,
        messageThreadId = messageThreadId,
        horizontalAccuracy = horizontalAccuracy,
        livePeriod = livePeriod,
        heading = heading,
        proximityAlertRadius = proximityAlertRadius,
        disableNotification = disableNotification,
        protectContent = protectContent,
        replyToMessageId = replyToMessageId,
        allowSendingWithoutReply = allowSendingWithoutReply,
        replyMarkup = replyMarkup
    )

    override suspend fun editMessageLiveLocation(
        latitude: Float,
        longitude: Float,
        horizontalAccuracy: Float?,
        heading: Long?,
        proximityAlertRadius: Long?,
        chatId: ChatId?,
        messageId: Long?,
        inlineMessageId: String?,
        replyMarkup: InlineKeyboardMarkup?
    ): Message {
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
            replyMarkup = replyMarkup
        )
    }

    override suspend fun stopMessageLiveLocation(
        chatId: ChatId?, messageId: Long?, inlineMessageId: String?, replyMarkup: InlineKeyboardMarkup?
    ): Message {
        validateIds(chatId, messageId, inlineMessageId)
        return client.stopMessageLiveLocation(chatId, messageId, inlineMessageId, replyMarkup)
    }

    override suspend fun sendVenue(
        chatId: ChatId,
        latitude: Float,
        longitude: Float,
        title: String,
        address: String,
        messageThreadId: Long?,
        foursquareId: String?,
        foursquareType: String?,
        googlePlaceId: String?,
        googlePlaceType: String?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ) = client.sendVenue(
        chatId = chatId,
        latitude = latitude,
        longitude = longitude,
        title = title,
        address = address,
        messageThreadId = messageThreadId,
        foursquareId = foursquareId,
        foursquareType = foursquareType,
        googlePlaceId = googlePlaceId,
        googlePlaceType = googlePlaceType,
        disableNotification = disableNotification,
        protectContent = protectContent,
        replyToMessageId = replyToMessageId,
        allowSendingWithoutReply = allowSendingWithoutReply,
        replyMarkup = replyMarkup
    )

    override suspend fun sendContact(
        chatId: ChatId,
        phoneNumber: String,
        firstName: String,
        messageThreadId: Long?,
        lastName: String?,
        vcard: String?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ) = client.sendContact(
        chatId = chatId,
        phoneNumber = phoneNumber,
        firstName = firstName,
        messageThreadId = messageThreadId,
        lastName = lastName,
        vcard = vcard,
        disableNotification = disableNotification,
        protectContent = protectContent,
        replyToMessageId = replyToMessageId,
        allowSendingWithoutReply = allowSendingWithoutReply,
        replyMarkup = replyMarkup
    )

    override suspend fun sendChatAction(
        chatId: ChatId,
        action: Action,
        messageThreadId: Long?,
    ) = client.sendChatAction(
        chatId = chatId, action = action, messageThreadId = messageThreadId
    )

    override suspend fun setChatMenuButton(chatId: Long?, menuButton: MenuButton?) =
        client.setChatMenuButton(chatId = chatId, menuButton = menuButton)

    override suspend fun getChatMenuButton(chatId: Long?) = client.getChatMenuButton(chatId = chatId)

    override suspend fun setMyDefaultAdministratorRights(
        rights: ChatAdministratorRights?, forChannels: Boolean?
    ) = client.setMyDefaultAdministratorRights(rights = rights, forChannels = forChannels)

    override suspend fun getMyDefaultAdministratorRights(
        forChannels: Boolean?
    ) = client.getMyDefaultAdministratorRights(forChannels = forChannels)

    override suspend fun getForumTopicIconStickers() = client.getForumTopicIconStickers()

    override suspend fun createForumTopic(
        chatId: ChatId, name: String, iconColor: Int?, iconCustomEmojiId: String?
    ) = client.createForumTopic(
        chatId = chatId,
        name = name,
        iconColor = iconColor,
        iconCustomEmojiId = iconCustomEmojiId,
    )

    override suspend fun editForumTopic(
        chatId: ChatId, messageThreadId: Long, name: String?, iconCustomEmojiId: String?
    ) = client.editForumTopic(
        chatId = chatId,
        messageThreadId = messageThreadId,
        name = name,
        iconCustomEmojiId = iconCustomEmojiId,
    )

    override suspend fun closeForumTopic(
        chatId: ChatId, messageThreadId: Long
    ) = client.closeForumTopic(
        chatId = chatId,
        messageThreadId = messageThreadId,
    )

    override suspend fun reopenForumTopic(
        chatId: ChatId, messageThreadId: Long
    ) = client.reopenForumTopic(
        chatId = chatId,
        messageThreadId = messageThreadId,
    )

    override suspend fun deleteForumTopic(
        chatId: ChatId, messageThreadId: Long
    ) = client.deleteForumTopic(
        chatId = chatId,
        messageThreadId = messageThreadId,
    )

    override suspend fun unpinAllForumTopicMessages(
        chatId: ChatId, messageThreadId: Long
    ) = client.unpinAllForumTopicMessages(
        chatId = chatId,
        messageThreadId = messageThreadId,
    )

    override suspend fun editGeneralForumTopic(
        chatId: ChatId, name: String
    ) = client.editGeneralForumTopic(
        chatId = chatId,
        name = name,
    )

    override suspend fun closeGeneralForumTopic(
        chatId: ChatId,
    ) = client.closeGeneralForumTopic(
        chatId = chatId,
    )

    override suspend fun reopenGeneralForumTopic(
        chatId: ChatId,
    ) = client.reopenGeneralForumTopic(
        chatId = chatId,
    )

    override suspend fun hideGeneralForumTopic(
        chatId: ChatId,
    ) = client.hideGeneralForumTopic(
        chatId = chatId,
    )

    override suspend fun unhideGeneralForumTopic(
        chatId: ChatId,
    ) = client.unhideGeneralForumTopic(
        chatId = chatId,
    )

    override suspend fun getUserProfilePhotos(
        userId: Long, offset: Long?, limit: Long?
    ) = client.getUserProfilePhotos(
        userId = userId, offset = offset, limit = limit
    )

    override suspend fun banChatSenderChat(
        chatId: ChatId, senderChatId: Long
    ): Boolean = client.banChatSenderChat(chatId, senderChatId)

    override suspend fun unbanChatSenderChat(
        chatId: ChatId, senderChatId: Long
    ): Boolean = client.unbanChatSenderChat(chatId, senderChatId)

    override suspend fun getFile(fileId: String) = client.getFile(fileId)

    override suspend fun banChatMember(
        chatId: ChatId, userId: Long, untilDate: Long?, revokeMessages: Boolean?
    ) = client.banChatMember(chatId, userId, untilDate, revokeMessages)

    override suspend fun unbanChatMember(
        chatId: ChatId, userId: Long, onlyIfBanned: Boolean?
    ) = client.unbanChatMember(chatId, userId, onlyIfBanned)

    override suspend fun restrictChatMember(
        chatId: ChatId,
        userId: Long,
        permissions: ChatPermissions,
        useIndependentChatPermissions: Boolean?,
        untilDate: Long?,
    ) = client.restrictChatMember(
        chatId = chatId,
        userId = userId,
        permissions = permissions,
        useIndependentChatPermissions = useIndependentChatPermissions,
        untilDate = untilDate,
    )

    override suspend fun promoteChatMember(
        chatId: ChatId,
        userId: Long,
        isAnonymous: Boolean?,
        canManageChat: Boolean?,
        canPostMessages: Boolean?,
        canEditMessages: Boolean?,
        canDeleteMessages: Boolean?,
        canManageVideoChats: Boolean?,
        canRestrictMembers: Boolean?,
        canPromoteMembers: Boolean?,
        canChangeInfo: Boolean?,
        canInviteUsers: Boolean?,
        canPinMessages: Boolean?,
        canManageTopics: Boolean?,
    ): Boolean = client.promoteChatMember(
        chatId = chatId,
        userId = userId,
        isAnonymous = isAnonymous,
        canManageChat = canManageChat,
        canPostMessages = canPostMessages,
        canEditMessages = canEditMessages,
        canDeleteMessages = canDeleteMessages,
        canManageVideoChats = canManageVideoChats,
        canRestrictMembers = canRestrictMembers,
        canPromoteMembers = canPromoteMembers,
        canChangeInfo = canChangeInfo,
        canInviteUsers = canInviteUsers,
        canPinMessages = canPinMessages,
        canManageTopics = canManageTopics,
    )

    override suspend fun exportChatInviteLink(chatId: ChatId) = client.exportChatInviteLink(chatId)

    override suspend fun setChatPhoto(
        chatId: ChatId, photo: Any
    ) = client.setChatPhoto(chatId, photo)

    override suspend fun deleteChatPhoto(chatId: ChatId) = client.deleteChatPhoto(chatId)

    override suspend fun setChatTitle(
        chatId: ChatId, title: String
    ) = client.setChatTitle(chatId, title)

    override suspend fun setChatDescription(
        chatId: ChatId, description: String
    ) = client.setChatDescription(chatId, description)

    override suspend fun pinChatMessage(
        chatId: ChatId, messageId: Long, disableNotification: Boolean?
    ) = client.pinChatMessage(chatId, messageId, disableNotification)

    override suspend fun unpinChatMessage(
        chatId: ChatId, messageId: Long?
    ) = client.unpinChatMessage(chatId, messageId)

    override suspend fun unpinAllChatMessages(chatId: ChatId): Boolean = client.unpinAllChatMessages(chatId)

    override suspend fun leaveChat(chatId: ChatId) = client.leaveChat(chatId)

    override suspend fun getChat(chatId: ChatId) = client.getChat(chatId)

    override suspend fun getChatAdministrators(chatId: ChatId) = client.getChatAdministrators(chatId)

    override suspend fun getChatMemberCount(chatId: ChatId) = client.getChatMemberCount(chatId)

    override suspend fun getChatMember(
        chatId: ChatId, userId: Long
    ) = client.getChatMember(chatId, userId)

    override suspend fun setChatStickerSet(
        chatId: ChatId, stickerSetName: String
    ) = client.setChatStickerSet(chatId, stickerSetName)

    override suspend fun deleteChatStickerSet(chatId: ChatId) = client.deleteChatStickerSet(chatId)

    override suspend fun answerCallbackQuery(
        callbackQueryId: String, text: String?, showAlert: Boolean?, url: String?, cacheTime: Long?
    ) = client.answerCallbackQuery(
        callbackQueryId = callbackQueryId, text = text, showAlert = showAlert, url = url, cacheTime = cacheTime
    )

    override suspend fun answerInlineQuery(
        inlineQueryId: String,
        results: List<InlineQueryResult>,
        cacheTime: Int?,
        isPersonal: Boolean?,
        nextOffset: String?,
        switchPmText: String?,
        switchPmParameter: String?
    ) = client.answerInlineQuery(
        inlineQueryId = inlineQueryId,
        results = results,
        cacheTime = cacheTime,
        isPersonal = isPersonal,
        nextOffset = nextOffset,
        switchPmText = switchPmText,
        switchPmParameter = switchPmParameter
    )

    override suspend fun answerWebAppQuery(webAppQueryId: String, result: InlineQueryResult) = client.answerWebAppQuery(
        webAppQueryId = webAppQueryId, result = result
    )

    override suspend fun editMessageText(
        chatId: ChatId?,
        messageId: Long?,
        inlineMessageId: String?,
        text: String,
        parseMode: ParseMode?,
        entities: List<MessageEntity>?,
        disableWebPagePreview: Boolean?,
        replyMarkup: InlineKeyboardMarkup?
    ): Message {
        validateIds(chatId, messageId, inlineMessageId)
        return client.editMessageText(
            chatId = chatId,
            messageId = messageId,
            inlineMessageId = inlineMessageId,
            text = text,
            parseMode = parseMode,
            entities = entities,
            disableWebPagePreview = disableWebPagePreview,
            replyMarkup = replyMarkup
        )
    }

    override suspend fun editMessageCaption(
        chatId: ChatId?,
        messageId: Long?,
        inlineMessageId: String?,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        replyMarkup: InlineKeyboardMarkup?
    ): Message {
        validateIds(chatId, messageId, inlineMessageId)
        return client.editMessageCaption(
            chatId = chatId,
            messageId = messageId,
            inlineMessageId = inlineMessageId,
            caption = caption,
            parseMode = parseMode,
            captionEntities = captionEntities,
            replyMarkup = replyMarkup
        )
    }

    override suspend fun editMessageMedia(
        chatId: ChatId?,
        messageId: Long?,
        inlineMessageId: String?,
        media: InputMedia,
        replyMarkup: InlineKeyboardMarkup?
    ): Message {
        validateIds(chatId, messageId, inlineMessageId)
        return client.editMessageMedia(chatId, messageId, inlineMessageId, media, replyMarkup)
    }

    override suspend fun editMessageReplyMarkup(
        chatId: ChatId?, messageId: Long?, inlineMessageId: String?, replyMarkup: InlineKeyboardMarkup?
    ): Message {
        return client.editMessageReplyMarkup(chatId, messageId, inlineMessageId, replyMarkup)
    }

    override suspend fun sendSticker(
        chatId: ChatId,
        sticker: Any,
        messageThreadId: Long?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ): Message {
        validateInputFileOrString(sticker)
        return client.sendSticker(
            chatId = chatId,
            sticker = sticker,
            messageThreadId = messageThreadId,
            disableNotification = disableNotification,
            protectContent = protectContent,
            replyToMessageId = replyToMessageId,
            allowSendingWithoutReply = allowSendingWithoutReply,
            replyMarkup = replyMarkup
        )
    }

    override suspend fun getStickerSet(name: String) = client.getStickerSet(name)

    override suspend fun getCustomEmojiStickers(customEmojiIds: List<String>) =
        client.getCustomEmojiStickers(customEmojiIds)

    override suspend fun uploadStickerFile(
        userId: Long,
        sticker: File,
        stickerFormat: String
    ): com.elbekd.bot.types.File = client.uploadStickerFile(userId = userId, sticker = sticker, stickerFormat = stickerFormat)

    override suspend fun createNewStickerSet(
        userId: Long,
        name: String,
        title: String,
        emojis: String,
        pngSticker: Any?,
        tgsSticker: File?,
        webmSticker: File?,
        stickerType: String?,
        containsMask: Boolean?,
        maskPosition: MaskPosition?
    ): Boolean {
        validateExactlyOneNonNull(
            pngSticker, tgsSticker, webmSticker
        )

        pngSticker?.let { validateInputFileOrString(it) }
        return client.createNewStickerSet(
            userId = userId,
            name = name,
            title = title,
            emojis = emojis,
            pngSticker = pngSticker,
            tgsSticker = tgsSticker,
            webmSticker = webmSticker,
            stickerType = stickerType,
            containsMask = containsMask,
            maskPosition = maskPosition,
        )
    }

    override suspend fun addStickerToSet(
        userId: Long,
        name: String,
        emojis: String,
        pngSticker: Any?,
        tgsSticker: File?,
        webmSticker: File?,
        maskPosition: MaskPosition?
    ): Boolean {
        validateExactlyOneNonNull(
            pngSticker, tgsSticker, webmSticker
        )

        pngSticker?.let { validateInputFileOrString(it) }
        return client.addStickerToSet(
            userId = userId,
            name = name,
            emojis = emojis,
            pngSticker = pngSticker,
            tgsSticker = tgsSticker,
            webmSticker = webmSticker,
            maskPosition = maskPosition,
        )
    }

    override suspend fun setStickerPositionInSet(
        sticker: String, position: Int
    ) = client.setStickerPositionInSet(sticker, position)

    override suspend fun deleteStickerFromSet(sticker: String) = client.deleteStickerFromSet(sticker)

    override suspend fun setStickerMaskPosition(sticker: String, maskPosition: MaskPosition): Boolean =
        client.setStickerMaskPosition(sticker = sticker, maskPosition = maskPosition)

    override suspend fun deleteStickerSet(name: String): Boolean = client.deleteStickerSet(name)

    override suspend fun setCustomEmojiStickerSetThumbnail(name: String, customEmojiId: String?): Boolean =
        client.setCustomEmojiStickerSetThumbnail(name = name, customEmojiId = customEmojiId)

    override suspend fun setStickerSetTitle(name: String, title: String): Boolean =
        client.setStickerSetTitle(name = name, title = title)

    override suspend fun setStickerEmojiList(sticker: String, emojiList: Collection<String>): Boolean =
        client.setStickerEmojiList(sticker = sticker, emojiList = emojiList)

    override suspend fun setStickerKeywords(sticker: String, keywords: Collection<String>): Boolean =
        client.setStickerKeywords(sticker = sticker, keywords = keywords)

    override suspend fun setStickerSetThumbnail(name: String, userId: Long, thumbnail: Any?): Boolean {
        if (thumbnail !is File || thumbnail !is String) {
            throw IllegalArgumentException("Neither file nor string")
        }

        return client.setStickerSetThumbnail(name, userId, thumbnail)
    }

    override suspend fun sendGame(
        chatId: Long,
        gameShortName: String,
        messageThreadId: Long?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: InlineKeyboardMarkup?
    ) = client.sendGame(
        chatId = chatId,
        gameShortName = gameShortName,
        messageThreadId = messageThreadId,
        disableNotification = disableNotification,
        protectContent = protectContent,
        replyToMessageId = replyToMessageId,
        allowSendingWithoutReply = allowSendingWithoutReply,
        replyMarkup = replyMarkup
    )

    override suspend fun setGameScore(
        userId: Long,
        score: Long,
        force: Boolean?,
        disableEditMessage: Boolean?,
        chatId: ChatId.IntegerId?,
        messageId: Long?,
        inlineMessageId: String?
    ): Message {
        validateIds(chatId, messageId, inlineMessageId)
        return client.setGameScore(userId, score, force, disableEditMessage, chatId, messageId, inlineMessageId)
    }

    override suspend fun getGameHighScores(
        userId: Long, chatId: ChatId.IntegerId?, messageId: Long?, inlineMessageId: String?
    ): List<GameHighScore> {
        validateIds(chatId, messageId, inlineMessageId)
        return client.getGameHighScores(userId, chatId, messageId, inlineMessageId)
    }

    override suspend fun sendInvoice(
        chatId: ChatId,
        title: String,
        description: String,
        payload: String,
        providerToken: String,
        currency: String,
        prices: List<LabeledPrice>,
        messageThreadId: Long?,
        maxTipAmount: Int?,
        suggestedTipAmount: List<Int>?,
        startParameter: String?,
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
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: InlineKeyboardMarkup?
    ): Message = client.sendInvoice(
        chatId = chatId,
        title = title,
        description = description,
        payload = payload,
        providerToken = providerToken,
        currency = currency,
        prices = prices,
        messageThreadId = messageThreadId,
        maxTipAmount = maxTipAmount,
        suggestedTipAmount = suggestedTipAmount,
        startParameter = startParameter,
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
        protectContent = protectContent,
        disableNotification = disableNotification,
        replyToMessageId = replyToMessageId
    )

    override suspend fun createInvoiceLink(
        title: String,
        description: String,
        payload: String,
        providerToken: String,
        currency: String,
        prices: List<LabeledPrice>,
        maxTipAmount: Int?,
        suggestedTipAmount: List<Int>?,
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
        isFlexible: Boolean?
    ): String = client.createInvoiceLink(
        title = title,
        description = description,
        payload = payload,
        providerToken = providerToken,
        currency = currency,
        prices = prices,
        maxTipAmount = maxTipAmount,
        suggestedTipAmount = suggestedTipAmount,
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
        isFlexible = isFlexible
    )

    override suspend fun answerShippingQuery(
        shippingQueryId: String, ok: Boolean, shippingOptions: List<ShippingOption>?, errorMessage: String?
    ) = client.answerShippingQuery(shippingQueryId, ok, shippingOptions, errorMessage)

    override suspend fun answerPreCheckoutQuery(
        preCheckoutQueryId: String, ok: Boolean, errorMessage: String?
    ) = client.answerPreCheckoutQuery(preCheckoutQueryId, ok, errorMessage)

    override suspend fun setPassportDataErrors(
        userId: Long, errors: List<PassportElementError>
    ) = client.setPassportDataErrors(userId, errors)

    override suspend fun sendPoll(
        chatId: ChatId,
        question: String,
        options: List<String>,
        messageThreadId: Long?,
        isAnonymous: Boolean?,
        type: String?,
        allowsMultipleAnswers: Boolean?,
        correctOptionId: Long?,
        explanation: String?,
        explanationParseMode: String?,
        explanationEntities: List<MessageEntity>?,
        openPeriod: Long?,
        closeDate: Long?,
        isClosed: Boolean?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ): Message {
        if (openPeriod != null && closeDate != null) {
            throw IllegalArgumentException("openPeriod and closeDate can't be used together")
        }

        return client.sendPoll(
            chatId = chatId,
            question = question,
            options = options,
            messageThreadId = messageThreadId,
            isAnonymous = isAnonymous,
            type = type,
            allowsMultipleAnswers = allowsMultipleAnswers,
            correctOptionId = correctOptionId,
            explanation = explanation,
            explanationParseMode = explanationParseMode,
            explanationEntities = explanationEntities,
            openPeriod = openPeriod,
            closeDate = closeDate,
            isClosed = isClosed,
            disableNotification = disableNotification,
            protectContent = protectContent,
            replyToMessageId = replyToMessageId,
            allowSendingWithoutReply = allowSendingWithoutReply,
            replyMarkup = replyMarkup
        )
    }

    override suspend fun stopPoll(chatId: ChatId, messageId: Long, replyMarkup: InlineKeyboardMarkup?): Poll =
        client.stopPoll(chatId, messageId, replyMarkup)

    override suspend fun setChatPermissions(
        chatId: ChatId,
        permissions: ChatPermissions,
        useIndependentChatPermissions: Boolean?,
    ) = client.setChatPermissions(
        chatId = chatId,
        permissions = permissions,
        useIndependentChatPermissions = useIndependentChatPermissions,
    )

    override suspend fun createChatInviteLink(
        chatId: ChatId,
        name: String?,
        expireDate: Long?,
        memberLimit: Long?,
        createsJoinRequest: Boolean?,
    ): ChatInviteLink = client.createChatInviteLink(
        chatId,
        name,
        expireDate,
        memberLimit,
        createsJoinRequest,
    )

    override suspend fun editChatInviteLink(
        chatId: ChatId,
        inviteLink: String,
        name: String?,
        expireDate: Long?,
        memberLimit: Long?,
        createsJoinRequest: Boolean?,
    ): ChatInviteLink = client.editChatInviteLink(
        chatId, inviteLink, name, expireDate, memberLimit, createsJoinRequest
    )

    override suspend fun revokeChatInviteLink(
        chatId: ChatId, inviteLink: String
    ): ChatInviteLink = client.revokeChatInviteLink(
        chatId, inviteLink
    )

    override suspend fun approveChatJoinRequest(
        chatId: ChatId, inviteLink: String
    ): Boolean = client.approveChatJoinRequest(
        chatId, inviteLink
    )

    override suspend fun declineChatJoinRequest(
        chatId: ChatId, inviteLink: String
    ): Boolean = client.declineChatJoinRequest(
        chatId, inviteLink
    )

    override suspend fun setChatAdministratorCustomTitle(chatId: ChatId, userId: Long, customTitle: String) =
        client.setChatAdministratorCustomTitle(chatId, userId, customTitle)

    override suspend fun deleteMessage(chatId: ChatId, messageId: Long): Boolean =
        client.deleteMessage(chatId, messageId)

    override suspend fun sendDice(
        chatId: ChatId,
        messageThreadId: Long?,
        emoji: String?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ) = client.sendDice(
        chatId = chatId,
        messageThreadId = messageThreadId,
        emoji = emoji,
        disableNotification = disableNotification,
        protectContent = protectContent,
        replyToMessageId = replyToMessageId,
        allowSendingWithoutReply = allowSendingWithoutReply,
        replyMarkup = replyMarkup
    )
    // Telegram methods end

    private fun validateInputFileOrString(obj: Any) {
        if (obj !is File && obj !is String) throw IllegalArgumentException("$obj is neither file nor string")
    }

    private fun validateIds(chatId: ChatId?, messageId: Long?, inlineMessageId: String?) {
        if (inlineMessageId != null && (chatId != null || messageId != null) || inlineMessageId == null && (chatId == null || messageId == null)) throw IllegalArgumentException(
            "Provide only inlineMessage or chatId and messageId"
        )
    }

    private fun validateExactlyOneNonNull(vararg objects: Any?) {
        if (objects.count { it != null } != 1) {
            throw IllegalArgumentException("Provide exactly 1 non null object")
        }
    }
}
