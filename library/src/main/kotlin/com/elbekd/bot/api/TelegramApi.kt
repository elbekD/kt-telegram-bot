package com.elbekd.bot.api

import com.elbekd.bot.model.ChatId
import com.elbekd.bot.types.*
import com.elbekd.bot.util.Action

public interface TelegramApi :
    TelegramInlineModeApi,
    TelegramUpdatingMessagesApi,
    TelegramStickerApi,
    TelegramPaymentApi,
    TelegramPassportApi,
    TelegramGameApi,
    TelegramUpdatesApi,
    TelegramChatApi {

    public suspend fun getMe(): User

    public suspend fun logOut(): Boolean

    public suspend fun close(): Boolean

    public suspend fun getMyCommands(
        scope: BotCommandScope? = null,
        languageCode: String? = null
    ): List<BotCommand>

    public suspend fun setMyCommands(
        commands: List<BotCommand>,
        scope: BotCommandScope? = null,
        languageCode: String? = null
    ): Boolean

    public suspend fun deleteMyCommands(
        scope: BotCommandScope? = null,
        languageCode: String? = null
    ): Boolean

    public suspend fun sendMessage(
        chatId: ChatId,
        text: String,
        parseMode: ParseMode? = null,
        entities: List<MessageEntity>? = null,
        disableWebPagePreview: Boolean? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyToMessageId: Long? = null,
        allowSendingWithoutReply: Boolean? = null,
        replyMarkup: ReplyKeyboard? = null
    ): Message

    public suspend fun forwardMessage(
        chatId: ChatId,
        fromChatId: ChatId,
        msgId: Long,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
    ): Message

    public suspend fun copyMessage(
        chatId: ChatId,
        fromChatId: ChatId,
        messageId: Long,
        caption: String? = null,
        parseMode: ParseMode? = null,
        captionEntities: List<MessageEntity>? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyToMessageId: Long? = null,
        allowSendingWithoutReply: Boolean? = null,
        replyMarkup: ReplyKeyboard? = null
    ): MessageId

    public suspend fun sendPhoto(
        chatId: ChatId,
        photo: Any,
        caption: String? = null,
        parseMode: ParseMode? = null,
        captionEntities: List<MessageEntity>? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyToMessageId: Long? = null,
        allowSendingWithoutReply: Boolean? = null,
        replyMarkup: ReplyKeyboard? = null
    ): Message

    public suspend fun sendAudio(
        chatId: ChatId,
        audio: Any,
        caption: String? = null,
        parseMode: ParseMode? = null,
        captionEntities: List<MessageEntity>? = null,
        duration: Long? = null,
        performer: String? = null,
        title: String? = null,
        thumb: java.io.File? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyToMessageId: Long? = null,
        allowSendingWithoutReply: Boolean? = null,
        replyMarkup: ReplyKeyboard? = null
    ): Message

    public suspend fun sendDocument(
        chatId: ChatId,
        document: Any,
        thumb: java.io.File? = null,
        caption: String? = null,
        parseMode: ParseMode? = null,
        captionEntities: List<MessageEntity>? = null,
        disableContentTypeDetection: Boolean? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyToMessageId: Long? = null,
        allowSendingWithoutReply: Boolean? = null,
        replyMarkup: ReplyKeyboard? = null
    ): Message

    public suspend fun sendVideo(
        chatId: ChatId,
        video: Any,
        duration: Long? = null,
        width: Long? = null,
        height: Long? = null,
        thumb: java.io.File? = null,
        caption: String? = null,
        parseMode: ParseMode? = null,
        captionEntities: List<MessageEntity>? = null,
        streaming: Boolean? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyToMessageId: Long? = null,
        allowSendingWithoutReply: Boolean? = null,
        replyMarkup: ReplyKeyboard? = null
    ): Message

    public suspend fun sendAnimation(
        chatId: ChatId,
        animation: Any,
        duration: Long? = null,
        width: Long? = null,
        height: Long? = null,
        thumb: java.io.File? = null,
        caption: String? = null,
        parseMode: ParseMode? = null,
        captionEntities: List<MessageEntity>? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyToMessageId: Long? = null,
        allowSendingWithoutReply: Boolean? = null,
        replyMarkup: ReplyKeyboard? = null
    ): Message

    public suspend fun sendVoice(
        chatId: ChatId,
        voice: Any,
        caption: String? = null,
        parseMode: ParseMode? = null,
        captionEntities: List<MessageEntity>? = null,
        duration: Long? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyToMessageId: Long? = null,
        allowSendingWithoutReply: Boolean? = null,
        replyMarkup: ReplyKeyboard? = null
    ): Message

    public suspend fun sendVideoNote(
        chatId: ChatId,
        note: Any,
        duration: Long? = null,
        length: Long? = null,
        thumb: java.io.File? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyToMessageId: Long? = null,
        allowSendingWithoutReply: Boolean? = null,
        replyMarkup: ReplyKeyboard? = null
    ): Message

    public suspend fun sendMediaGroup(
        chatId: ChatId,
        media: List<InputMedia>,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyToMessageId: Long? = null,
        allowSendingWithoutReply: Boolean? = null
    ): ArrayList<Message>

    public suspend fun sendLocation(
        chatId: ChatId,
        latitude: Float,
        longitude: Float,
        horizontalAccuracy: Float? = null,
        livePeriod: Long? = null,
        heading: Long? = null,
        proximityAlertRadius: Long? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyToMessageId: Long? = null,
        allowSendingWithoutReply: Boolean? = false,
        replyMarkup: ReplyKeyboard? = null
    ): Message

    public suspend fun editMessageLiveLocation(
        latitude: Float,
        longitude: Float,
        horizontalAccuracy: Float? = null,
        heading: Long? = null,
        proximityAlertRadius: Long? = null,
        chatId: ChatId? = null,
        messageId: Long? = null,
        inlineMessageId: String? = null,
        replyMarkup: InlineKeyboardMarkup? = null
    ): Message

    public suspend fun stopMessageLiveLocation(
        chatId: ChatId? = null,
        messageId: Long? = null,
        inlineMessageId: String? = null,
        replyMarkup: InlineKeyboardMarkup? = null
    ): Message

    public suspend fun sendVenue(
        chatId: ChatId,
        latitude: Float,
        longitude: Float,
        title: String,
        address: String,
        foursquareId: String? = null,
        foursquareType: String? = null,
        googlePlaceId: String? = null,
        googlePlaceType: String? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyToMessageId: Long? = null,
        allowSendingWithoutReply: Boolean? = null,
        replyMarkup: ReplyKeyboard? = null
    ): Message

    public suspend fun sendContact(
        chatId: ChatId,
        phoneNumber: String,
        firstName: String,
        lastName: String? = null,
        vcard: String? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyToMessageId: Long? = null,
        allowSendingWithoutReply: Boolean? = null,
        replyMarkup: ReplyKeyboard? = null
    ): Message

    public suspend fun sendPoll(
        chatId: ChatId,
        question: String,
        options: List<String>,
        isAnonymous: Boolean? = null,
        type: String? = null,
        allowsMultipleAnswers: Boolean? = null,
        correctOptionId: Long? = null,
        explanation: String? = null,
        explanationParseMode: String? = null,
        explanationEntities: List<MessageEntity>? = null,
        openPeriod: Long? = null,
        closeDate: Long? = null,
        isClosed: Boolean? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyToMessageId: Long? = null,
        allowSendingWithoutReply: Boolean? = null,
        replyMarkup: ReplyKeyboard? = null
    ): Message

    public suspend fun sendDice(
        chatId: ChatId,
        emoji: String? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyToMessageId: Long? = null,
        allowSendingWithoutReply: Boolean? = null,
        replyMarkup: ReplyKeyboard? = null
    ): Message

    public suspend fun sendChatAction(
        chatId: ChatId,
        action: Action
    ): Boolean

    public suspend fun banChatSenderChat(
        chatId: ChatId,
        senderChatId: Long
    ): Boolean

    public suspend fun unbanChatSenderChat(
        chatId: ChatId,
        senderChatId: Long
    ): Boolean

    public suspend fun getUserProfilePhotos(
        userId: Long,
        offset: Long? = null,
        limit: Long? = null
    ): UserProfilePhotos

    public suspend fun getFile(fileId: String): File

    public suspend fun banChatMember(
        chatId: ChatId,
        userId: Long,
        untilDate: Long? = null,
        revokeMessages: Boolean? = null
    ): Boolean

    public suspend fun unbanChatMember(
        chatId: ChatId,
        userId: Long,
        onlyIfBanned: Boolean? = null
    ): Boolean

    public suspend fun restrictChatMember(
        chatId: ChatId,
        userId: Long,
        permissions: ChatPermissions,
        untilDate: Long? = null
    ): Boolean

    public suspend fun promoteChatMember(
        chatId: ChatId,
        userId: Long,
        isAnonymous: Boolean? = null,
        canManageChat: Boolean? = null,
        canPostMessages: Boolean? = null,
        canEditMessages: Boolean? = null,
        canDeleteMessages: Boolean? = null,
        canManageVideoChats: Boolean? = null,
        canRestrictMembers: Boolean? = null,
        canPromoteMembers: Boolean? = null,
        canChangeInfo: Boolean? = null,
        canInviteUsers: Boolean? = null,
        canPinMessages: Boolean? = null
    ): Boolean

    public suspend fun setChatAdministratorCustomTitle(
        chatId: ChatId,
        userId: Long,
        customTitle: String
    ): Boolean

    public suspend fun setChatPermissions(
        chatId: ChatId,
        permissions: ChatPermissions
    ): Boolean

    public suspend fun createChatInviteLink(
        chatId: ChatId,
        name: String? = null,
        expireDate: Long? = null,
        memberLimit: Long? = null,
        createsJoinRequest: Boolean? = null,
    ): ChatInviteLink

    public suspend fun editChatInviteLink(
        chatId: ChatId,
        inviteLink: String,
        name: String? = null,
        expireDate: Long? = null,
        memberLimit: Long? = null,
        createsJoinRequest: Boolean? = null,
    ): ChatInviteLink

    public suspend fun revokeChatInviteLink(
        chatId: ChatId,
        inviteLink: String
    ): ChatInviteLink

    public suspend fun approveChatJoinRequest(
        chatId: ChatId,
        inviteLink: String
    ): Boolean

    public suspend fun declineChatJoinRequest(
        chatId: ChatId,
        inviteLink: String
    ): Boolean

    public suspend fun exportChatInviteLink(chatId: ChatId): String

    public suspend fun setChatPhoto(
        chatId: ChatId,
        photo: Any
    ): Boolean

    public suspend fun deleteChatPhoto(chatId: ChatId): Boolean

    public suspend fun setChatTitle(
        chatId: ChatId,
        title: String
    ): Boolean

    public suspend fun setChatDescription(
        chatId: ChatId,
        description: String
    ): Boolean

    public suspend fun pinChatMessage(
        chatId: ChatId,
        messageId: Long,
        disableNotification: Boolean? = null
    ): Boolean

    public suspend fun unpinChatMessage(chatId: ChatId, messageId: Long? = null): Boolean

    public suspend fun unpinAllChatMessages(chatId: ChatId): Boolean

    public suspend fun leaveChat(chatId: ChatId): Boolean

    public suspend fun getChat(chatId: ChatId): Chat

    public suspend fun getChatAdministrators(chatId: ChatId): ArrayList<ChatMember>

    public suspend fun getChatMemberCount(chatId: ChatId): Long

    public suspend fun getChatMember(
        chatId: ChatId,
        userId: Long
    ): ChatMember

    public suspend fun setChatStickerSet(
        chatId: ChatId,
        stickerSetName: String
    ): Boolean

    public suspend fun deleteChatStickerSet(chatId: ChatId): Boolean

    public suspend fun answerCallbackQuery(
        callbackQueryId: String,
        text: String? = null,
        showAlert: Boolean? = null,
        url: String? = null,
        cacheTime: Long? = null
    ): Boolean
}