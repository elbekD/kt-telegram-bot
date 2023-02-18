package com.elbekd.bot.model.internal

import com.elbekd.bot.internal.ApiConstants
import com.elbekd.bot.model.ChatId
import com.elbekd.bot.types.*
import com.elbekd.bot.util.Action
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class SendMessage(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId,
    @SerialName(ApiConstants.TEXT) val text: String,
    @SerialName(ApiConstants.MESSAGE_THREAD_ID) val messageThreadId: Long? = null,
    @SerialName(ApiConstants.PARSE_MODE) val parseMode: ParseMode? = null,
    @SerialName(ApiConstants.ENTITIES) val entities: List<MessageEntity>? = null,
    @SerialName(ApiConstants.DISABLE_WEB_PAGE_PREVIEW) val disableWebPagePreview: Boolean? = null,
    @SerialName(ApiConstants.DISABLE_NOTIFICATION) val disableNotification: Boolean? = null,
    @SerialName(ApiConstants.PROTECT_CONTENT) val protectContent: Boolean? = null,
    @SerialName(ApiConstants.REPLY_TO_MESSAGE_ID) val replyToMessageId: Long? = null,
    @SerialName(ApiConstants.ALLOW_SENDING_WITHOUT_REPLY) val allowSendingWithoutReply: Boolean? = null,
    @SerialName(ApiConstants.REPLY_MARKUP) val replyMarkup: ReplyKeyboard? = null,
)

@Serializable
internal class ForwardMessage(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId,
    @SerialName(ApiConstants.FROM_CHAT_ID) val fromChatId: ChatId,
    @SerialName(ApiConstants.MESSAGE_ID) val messageId: Long,
    @SerialName(ApiConstants.MESSAGE_THREAD_ID) val messageThreadId: Long? = null,
    @SerialName(ApiConstants.DISABLE_NOTIFICATION) val disableNotification: Boolean? = null,
    @SerialName(ApiConstants.PROTECT_CONTENT) val protectContent: Boolean? = null,
)

@Serializable
internal class CopyMessage(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId,
    @SerialName(ApiConstants.FROM_CHAT_ID) val fromChatId: ChatId,
    @SerialName(ApiConstants.MESSAGE_ID) val messageId: Long,
    @SerialName(ApiConstants.MESSAGE_THREAD_ID) val messageThreadId: Long? = null,
    @SerialName(ApiConstants.CAPTION) val caption: String? = null,
    @SerialName(ApiConstants.PARSE_MODE) val parseMode: ParseMode? = null,
    @SerialName(ApiConstants.CAPTION_ENTITIES) val captionEntities: List<MessageEntity>? = null,
    @SerialName(ApiConstants.HAS_SPOILER) val hasSpoiler: Boolean? = null,
    @SerialName(ApiConstants.DISABLE_NOTIFICATION) val disableNotification: Boolean? = null,
    @SerialName(ApiConstants.PROTECT_CONTENT) val protectContent: Boolean? = null,
    @SerialName(ApiConstants.REPLY_TO_MESSAGE_ID) val replyToMessageId: Long? = null,
    @SerialName(ApiConstants.ALLOW_SENDING_WITHOUT_REPLY) val allowSendingWithoutReply: Boolean? = null,
    @SerialName(ApiConstants.REPLY_MARKUP) val replyMarkup: ReplyKeyboard? = null
)

@Serializable
internal class SendLocation(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId,
    @SerialName(ApiConstants.LATITUDE) val latitude: Float,
    @SerialName(ApiConstants.LONGITUDE) val longitude: Float,
    @SerialName(ApiConstants.MESSAGE_THREAD_ID) val messageThreadId: Long? = null,
    @SerialName(ApiConstants.HORIZONTAL_ACCURACY) val horizontalAccuracy: Float? = null,
    @SerialName(ApiConstants.LIVE_PERIOD) val livePeriod: Long? = null,
    @SerialName(ApiConstants.HEADING) val heading: Long? = null,
    @SerialName(ApiConstants.PROXIMITY_ALERT_RADIUS) val proximityAlertRadius: Long? = null,
    @SerialName(ApiConstants.DISABLE_NOTIFICATION) val disableNotification: Boolean? = null,
    @SerialName(ApiConstants.PROTECT_CONTENT) val protectContent: Boolean? = null,
    @SerialName(ApiConstants.REPLY_TO_MESSAGE_ID) val replyToMessageId: Long? = null,
    @SerialName(ApiConstants.ALLOW_SENDING_WITHOUT_REPLY) val allowSendingWithoutReply: Boolean? = null,
    @SerialName(ApiConstants.REPLY_MARKUP) val replyMarkup: ReplyKeyboard? = null
)

@Serializable
internal class EditMessageLiveLocation(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId? = null,
    @SerialName(ApiConstants.MESSAGE_ID) val messageId: Long? = null,
    @SerialName(ApiConstants.INLINE_MESSAGE_ID) val inlineMessageId: String? = null,
    @SerialName(ApiConstants.LATITUDE) val latitude: Float,
    @SerialName(ApiConstants.LONGITUDE) val longitude: Float,
    @SerialName(ApiConstants.HORIZONTAL_ACCURACY) val horizontalAccuracy: Float? = null,
    @SerialName(ApiConstants.HEADING) val heading: Long? = null,
    @SerialName(ApiConstants.PROXIMITY_ALERT_RADIUS) val proximityAlertRadius: Long? = null,
    @SerialName(ApiConstants.REPLY_MARKUP) val replyMarkup: ReplyKeyboard? = null
) {
    init {
        if (chatId == null && messageId == null) {
            requireNotNull(
                value = inlineMessageId,
                lazyMessage = { "inlineMessageId is required if chatId and messageId are not provided" }
            )
        }

        if (inlineMessageId == null &&
            (chatId == null || messageId == null)
        ) {
            throw IllegalArgumentException("chatId and messageId are required if inlineMessageId not provided")
        }
    }
}

@Serializable
internal class StopMessageLiveLocation(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId? = null,
    @SerialName(ApiConstants.MESSAGE_ID) val messageId: Long? = null,
    @SerialName(ApiConstants.INLINE_MESSAGE_ID) val inlineMessageId: String? = null,
    @SerialName(ApiConstants.REPLY_MARKUP) val replyMarkup: ReplyKeyboard? = null
) {
    init {
        if (chatId == null && messageId == null) {
            requireNotNull(
                value = inlineMessageId,
                lazyMessage = { "inlineMessageId is required if chatId and messageId are not provided" }
            )
        }

        if (inlineMessageId == null &&
            (chatId == null || messageId == null)
        ) {
            throw IllegalArgumentException("chatId and messageId are required if inlineMessageId not provided")
        }
    }
}

@Serializable
internal class SendVenue(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId,
    @SerialName(ApiConstants.LATITUDE) val latitude: Float,
    @SerialName(ApiConstants.LONGITUDE) val longitude: Float,
    @SerialName(ApiConstants.TITLE) val title: String,
    @SerialName(ApiConstants.ADDRESS) val address: String,
    @SerialName(ApiConstants.MESSAGE_THREAD_ID) val messageThreadId: Long? = null,
    @SerialName(ApiConstants.FOURSQUARE_ID) val foursquareId: String? = null,
    @SerialName(ApiConstants.FOURSQUARE_TYPE) val foursquareType: String? = null,
    @SerialName(ApiConstants.GOOGLE_PLACE_ID) val googlePlaceId: String? = null,
    @SerialName(ApiConstants.GOOGLE_PLACE_TYPE) val googlePlaceType: String? = null,
    @SerialName(ApiConstants.DISABLE_NOTIFICATION) val disableNotification: Boolean? = null,
    @SerialName(ApiConstants.PROTECT_CONTENT) val protectContent: Boolean? = null,
    @SerialName(ApiConstants.REPLY_TO_MESSAGE_ID) val replyToMessageId: Long? = null,
    @SerialName(ApiConstants.ALLOW_SENDING_WITHOUT_REPLY) val allowSendingWithoutReply: Boolean? = null,
    @SerialName(ApiConstants.REPLY_MARKUP) val replyMarkup: ReplyKeyboard? = null
)

@Serializable
internal class SendContact(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId,
    @SerialName(ApiConstants.PHONE_NUMBER) val phone: String,
    @SerialName(ApiConstants.FIRST_NAME) val firstName: String,
    @SerialName(ApiConstants.MESSAGE_THREAD_ID) val messageThreadId: Long? = null,
    @SerialName(ApiConstants.LAST_NAME) val lastName: String? = null,
    @SerialName(ApiConstants.VCARD) val vcard: String? = null,
    @SerialName(ApiConstants.DISABLE_NOTIFICATION) val disableNotification: Boolean? = null,
    @SerialName(ApiConstants.PROTECT_CONTENT) val protectContent: Boolean? = null,
    @SerialName(ApiConstants.REPLY_TO_MESSAGE_ID) val replyToMessageId: Long? = null,
    @SerialName(ApiConstants.ALLOW_SENDING_WITHOUT_REPLY) val allowSendingWithoutReply: Boolean? = null,
    @SerialName(ApiConstants.REPLY_MARKUP) val replyMarkup: ReplyKeyboard? = null
)

@Serializable
internal class SendChatAction(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId,
    @SerialName(ApiConstants.ACTION) val action: Action,
    @SerialName(ApiConstants.MESSAGE_THREAD_ID) val messageThreadId: Long?,
)

@Serializable
internal class BanChatSenderChat(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId,
    @SerialName(ApiConstants.SENDER_CHAT_ID) val senderChatId: Long,
)

@Serializable
internal class UnbanChatSenderChat(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId,
    @SerialName(ApiConstants.SENDER_CHAT_ID) val senderChatId: Long,
)

@Serializable
internal class GetUserProfilePhotos(
    @SerialName(ApiConstants.USER_ID) val userId: Long,
    @SerialName(ApiConstants.OFFSET) val offset: Long? = null,
    @SerialName(ApiConstants.LIMIT) val limit: Long? = null
)

@Serializable
internal class GetFile(
    @SerialName(ApiConstants.FILE_ID) val fileId: String
)

@Serializable
internal class SendPoll(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId,
    @SerialName(ApiConstants.QUESTION) val question: String,
    @SerialName(ApiConstants.OPTIONS) val options: List<String>,
    @SerialName(ApiConstants.MESSAGE_THREAD_ID) val messageThreadId: Long? = null,
    @SerialName(ApiConstants.IS_ANONYMOUS) val isAnonymous: Boolean? = null,
    @SerialName(ApiConstants.TYPE) val type: String? = null,
    @SerialName(ApiConstants.ALLOWS_MULTIPLE_ANSWERS) val allowsMultipleAnswers: Boolean? = null,
    @SerialName(ApiConstants.CORRECT_OPTION_ID) val correctOptionId: Long? = null,
    @SerialName(ApiConstants.EXPLANATION) val explanation: String? = null,
    @SerialName(ApiConstants.EXPLANATION_PARSE_MODE) val explanationParseMode: String? = null,
    @SerialName(ApiConstants.EXPLANATION_ENTITIES) val explanationEntities: List<MessageEntity>? = null,
    @SerialName(ApiConstants.OPEN_PERIOD) val openPeriod: Long? = null,
    @SerialName(ApiConstants.CLOSE_DATE) val closeDate: Long? = null,
    @SerialName(ApiConstants.IS_CLOSED) val isClosed: Boolean? = null,
    @SerialName(ApiConstants.DISABLE_NOTIFICATION) val disableNotification: Boolean? = null,
    @SerialName(ApiConstants.PROTECT_CONTENT) val protectContent: Boolean? = null,
    @SerialName(ApiConstants.REPLY_TO_MESSAGE_ID) val replyToMessageId: Long? = null,
    @SerialName(ApiConstants.ALLOW_SENDING_WITHOUT_REPLY) val allowSendingWithoutReply: Boolean? = null,
    @SerialName(ApiConstants.REPLY_MARKUP) val replyMarkup: ReplyKeyboard? = null
) {
    init {
        if (options.size < 2 || options.size > 10) {
            throw IllegalArgumentException("options size must be in [2, 10]")
        }
    }
}

@Serializable
internal class SendDice(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId,
    @SerialName(ApiConstants.MESSAGE_THREAD_ID) val messageThreadId: Long? = null,
    @SerialName(ApiConstants.EMOJI) val emoji: String? = null,
    @SerialName(ApiConstants.DISABLE_NOTIFICATION) val disableNotification: Boolean? = null,
    @SerialName(ApiConstants.PROTECT_CONTENT) val protectContent: Boolean? = null,
    @SerialName(ApiConstants.REPLY_TO_MESSAGE_ID) val replyToMessageId: Long? = null,
    @SerialName(ApiConstants.ALLOW_SENDING_WITHOUT_REPLY) val allowSendingWithoutReply: Boolean? = null,
    @SerialName(ApiConstants.REPLY_MARKUP) val replyMarkup: ReplyKeyboard? = null
)

@Serializable
internal class BanChatMember(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId,
    @SerialName(ApiConstants.USER_ID) val userId: Long,
    @SerialName(ApiConstants.UNTIL_DATE) val untilDate: Long? = null,
    @SerialName(ApiConstants.REVOKE_MESSAGES) val revokeMessages: Boolean? = null
)

@Serializable
internal class UnbanChatMember(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId,
    @SerialName(ApiConstants.USER_ID) val userId: Long,
    @SerialName(ApiConstants.ONLY_IF_BANNED) val onlyIfBanned: Boolean? = null
)

@Serializable
internal class RestrictChatMember(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId,
    @SerialName(ApiConstants.USER_ID) val userId: Long,
    @SerialName(ApiConstants.PERMISSIONS) val permissions: ChatPermissions,
    @SerialName(ApiConstants.USE_INDEPENDENT_CHAT_PERMISSIONS) val useIndependentChatPermissions: Boolean? = null,
    @SerialName(ApiConstants.UNTIL_DATE) val untilDate: Long? = null,
)

@Serializable
internal class PromoteChatMember(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId,
    @SerialName(ApiConstants.USER_ID) val userId: Long,
    @SerialName(ApiConstants.IS_ANONYMOUS) val isAnonymous: Boolean? = null,
    @SerialName(ApiConstants.CAN_MANAGE_CHAT) val canManageChat: Boolean? = null,
    @SerialName(ApiConstants.CAN_POST_MESSAGES) val canPostMessages: Boolean? = null,
    @SerialName(ApiConstants.CAN_EDIT_MESSAGES) val canEditMessages: Boolean? = null,
    @SerialName(ApiConstants.CAN_DELETE_MESSAGES) val canDeleteMessages: Boolean? = null,
    @SerialName(ApiConstants.CAN_MANAGE_VIDEO_CHATS) val canManageVideoChats: Boolean? = null,
    @SerialName(ApiConstants.CAN_RESTRICT_MEMBERS) val canRestrictMembers: Boolean? = null,
    @SerialName(ApiConstants.CAN_PROMOTE_MEMBERS) val canPromoteMembers: Boolean? = null,
    @SerialName(ApiConstants.CAN_CHANGE_INFO) val canChangeInfo: Boolean? = null,
    @SerialName(ApiConstants.CAN_INVITE_USERS) val canInviteUsers: Boolean? = null,
    @SerialName(ApiConstants.CAN_PIN_MESSAGES) val canPinMessages: Boolean? = null,
    @SerialName(ApiConstants.CAN_MANAGE_TOPICS) val canManageTopics: Boolean? = null,
)

@Serializable
internal class SetChatAdministratorCustomTitle(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId,
    @SerialName(ApiConstants.USER_ID) val userId: Long,
    @SerialName(ApiConstants.CUSTOM_TITLE) val customTitle: String
)

@Serializable
internal class SetChatPermissions(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId,
    @SerialName(ApiConstants.PERMISSIONS) val permissions: ChatPermissions,
    @SerialName(ApiConstants.USE_INDEPENDENT_CHAT_PERMISSIONS) val useIndependentChatPermissions: Boolean? = null,
)

@Serializable
internal class ExportChatInviteLink(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId
)

@Serializable
internal class CreateChatInviteLink(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId,
    @SerialName(ApiConstants.NAME) val name: String? = null,
    @SerialName(ApiConstants.EXPIRE_DATE) val expireDate: Long? = null,
    @SerialName(ApiConstants.MEMBER_LIMIT) val memberLimit: Long? = null,
    @SerialName(ApiConstants.CREATES_JOIN_REQUEST) val createsJoinRequest: Boolean? = null,
)

@Serializable
internal class EditChatInviteLink(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId,
    @SerialName(ApiConstants.INVITE_LINK) val inviteLink: String,
    @SerialName(ApiConstants.NAME) val name: String? = null,
    @SerialName(ApiConstants.EXPIRE_DATE) val expireDate: Long? = null,
    @SerialName(ApiConstants.MEMBER_LIMIT) val memberLimit: Long? = null,
    @SerialName(ApiConstants.CREATES_JOIN_REQUEST) val createsJoinRequest: Boolean? = null,
)

@Serializable
internal class RevokeChatInviteLink(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId,
    @SerialName(ApiConstants.INVITE_LINK) val inviteLink: String
)

@Serializable
internal class ApproveChatJoinRequest(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId,
    @SerialName(ApiConstants.INVITE_LINK) val inviteLink: String
)

@Serializable
internal class DeclineChatJoinRequest(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId,
    @SerialName(ApiConstants.INVITE_LINK) val inviteLink: String
)

@Serializable
internal class DeleteChatPhoto(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId
)

@Serializable
internal class SetChatTitle(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId,
    @SerialName(ApiConstants.TITLE) val title: String
)

@Serializable
internal class SetChatDescription(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId,
    @SerialName(ApiConstants.DESCRIPTION) val description: String
)

@Serializable
internal class PinChatMessage(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId,
    @SerialName(ApiConstants.MESSAGE_ID) val messageId: Long,
    @SerialName(ApiConstants.DISABLE_NOTIFICATION) val disableNotification: Boolean? = null
)

@Serializable
internal class UnpinChatMessage(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId,
    @SerialName(ApiConstants.MESSAGE_ID) val messageId: Long? = null
)

@Serializable
internal class UnpinAllChatMessages(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId
)

@Serializable
internal class LeaveChat(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId
)

@Serializable
internal class GetChat(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId
)

@Serializable
internal class GetChatAdministrators(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId
)

@Serializable
internal class GetChatMembersCount(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId
)

@Serializable
internal class GetChatMember(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId,
    @SerialName(ApiConstants.USER_ID) val userId: Long
)

@Serializable
internal class SetChatStickerSet(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId,
    @SerialName(ApiConstants.STICKER_SET_NAME) val stickerSetName: String
)

@Serializable
internal class DeleteChatStickerSet(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId
)

@Serializable
internal class AnswerCallbackQuery(
    @SerialName(ApiConstants.CALLBACK_QUERY_ID) val callbackQueryId: String,
    @SerialName(ApiConstants.TEXT) val text: String? = null,
    @SerialName(ApiConstants.SHOW_ALERT) val showAlert: Boolean? = null,
    @SerialName(ApiConstants.URL) val url: String? = null,
    @SerialName(ApiConstants.CACHE_TIME) val cacheTime: Long? = null
)

@Serializable
internal class SetMyCommands(
    @SerialName(ApiConstants.COMMANDS) val commands: List<BotCommand>,
    @SerialName(ApiConstants.SCOPE) val scope: BotCommandScope? = null,
    @SerialName(ApiConstants.LANGUAGE_CODE) val languageCode: String? = null
)

@Serializable
internal class DeleteMyCommands(
    @SerialName(ApiConstants.SCOPE) val scope: BotCommandScope? = null,
    @SerialName(ApiConstants.LANGUAGE_CODE) val languageCode: String? = null
)

@Serializable
internal class GetMyCommands(
    @SerialName(ApiConstants.SCOPE) val scope: BotCommandScope? = null,
    @SerialName(ApiConstants.LANGUAGE_CODE) val languageCode: String? = null
)