@file:Suppress("unused")

package com.elbekd.bot.types

import com.elbekd.bot.model.ChatId
import com.elbekd.bot.util.AllowedUpdate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class WebhookInfo(
    @SerialName("url") val url: String,
    @SerialName("has_custom_certificate") val has_custom_certificate: Boolean,
    @SerialName("pending_update_count") val pending_update_count: Int,
    @SerialName("ip_address") val ipAddress: String? = null,
    @SerialName("last_error_date") val lastErrorDate: Long? = null,
    @SerialName("last_error_message") val lastErrorMessage: String? = null,
    @SerialName("last_synchronization_error_date") val lastSynchronizationErrorDate: Long? = null,
    @SerialName("max_connections") val maxConnections: Int? = null,
    @SerialName("allowed_updates") val allowedUpdates: List<AllowedUpdate>? = null
)

@Serializable
public data class User(
    @SerialName("id") val id: Long,
    @SerialName("is_bot") val is_bot: Boolean,
    @SerialName("first_name") val first_name: String,
    @SerialName("last_name") val lastName: String? = null,
    @SerialName("username") val username: String? = null,
    @SerialName("language_code") val languageCode: String? = null,
    @SerialName("is_premium") val isPremium: Boolean? = null,
    @SerialName("added_to_attachment_menu") val added_to_attachment_menu: Boolean? = null,
    @SerialName("can_join_groups") val canJoinGroups: Boolean? = null,
    @SerialName("can_read_all_group_messages") val canReadAllGroupMessages: Boolean? = null,
    @SerialName("supports_inline_queries") val supportsInlineQueries: Boolean? = null,
)

@Serializable
public data class Message(
    @SerialName("message_id") val messageId: Long,
    @SerialName("from") val from: User? = null,
    @SerialName("sender_chat") val senderChat: Chat? = null,
    @SerialName("date") val date: Long,
    @SerialName("chat") val chat: Chat,
    @SerialName("forward_from") val forwardFrom: User? = null,
    @SerialName("forward_from_chat") val forwardFromChat: Chat? = null,
    @SerialName("forward_from_message_id") val forwardFromMessageId: Long? = null,
    @SerialName("forward_signature") val forwardSignature: String? = null,
    @SerialName("forward_sender_name") val forwardSenderName: String? = null,
    @SerialName("forward_date") val forwardDate: Long? = null,
    @SerialName("is_automatic_forward") val isAutomaticForward: Boolean? = null,
    @SerialName("reply_to_message") val replyToMessage: Message? = null,
    @SerialName("via_bot") val viaBot: User? = null,
    @SerialName("edit_date") val editDate: Long? = null,
    @SerialName("has_protected_content") val hasProtectedContent: Boolean? = null,
    @SerialName("media_group_id") val mediaGroupId: String? = null,
    @SerialName("author_signature") val authorSignature: String? = null,
    @SerialName("text") val text: String? = null,
    @SerialName("entities") val entities: List<MessageEntity> = emptyList(),
    @SerialName("animation") val animation: Animation? = null,
    @SerialName("audio") val audio: Audio? = null,
    @SerialName("document") val document: Document? = null,
    @SerialName("photo") val photo: List<PhotoSize> = emptyList(),
    @SerialName("sticker") val sticker: Sticker? = null,
    @SerialName("video") val video: Video? = null,
    @SerialName("video_note") val videoNote: VideoNote? = null,
    @SerialName("voice") val voice: Voice? = null,
    @SerialName("caption") val caption: String? = null,
    @SerialName("caption_entities") val captionEntities: List<MessageEntity> = emptyList(),
    @SerialName("contact") val contact: Contact? = null,
    @SerialName("dice") val dice: Dice? = null,
    @SerialName("game") val game: Game? = null,
    @SerialName("poll") val poll: Poll? = null,
    @SerialName("venue") val venue: Venue? = null,
    @SerialName("location") val location: Location? = null,
    @SerialName("new_chat_members") val newChatMembers: List<User> = emptyList(),
    @SerialName("left_chat_member") val leftChatMember: User? = null,
    @SerialName("new_chat_title") val newChatTitle: String? = null,
    @SerialName("new_chat_photo") val newChatPhoto: List<PhotoSize> = emptyList(),
    @SerialName("delete_chat_photo") val deleteChatPhoto: Boolean? = null,
    @SerialName("group_chat_created") val groupChatCreated: Boolean? = null,
    @SerialName("supergroup_chat_created") val supergroupChatCreated: Boolean? = null,
    @SerialName("channel_chat_created") val channelChatCreated: Boolean? = null,
    @SerialName("message_auto_delete_timer_changed") val messageAutoDeleteTimerChanged: MessageAutoDeleteTimerChanged? = null,
    @SerialName("migrate_to_chat_id") val migrateToChatId: Long? = null,
    @SerialName("migrate_from_chat_id") val migrateFromChatId: Long? = null,
    @SerialName("pinned_message") val pinnedMessage: Message? = null,
    @SerialName("invoice") val invoice: Invoice? = null,
    @SerialName("successful_payment") val successfulPayment: SuccessfulPayment? = null,
    @SerialName("connected_website") val connectedWebsite: String? = null,
    @SerialName("passport_data") val passportData: PassportData? = null,
    @SerialName("proximity_alert_triggered") val proximityAlertTriggered: ProximityAlertTriggered? = null,
    @SerialName("video_chat_scheduled") val videoChatScheduled: VideoChatScheduled? = null,
    @SerialName("video_chat_started") val videoChatStarted: VideoChatStarted? = null,
    @SerialName("video_chat_ended") val videoChatEnded: VideoChatEnded? = null,
    @SerialName("video_chat_participants_invited") val voiceChatParticipantsInvited: VoiceChatParticipantsInvited? = null,
    @SerialName("web_app_data") val webAppData: WebAppData? = null,
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
)

@Serializable
public data class MessageId(
    @SerialName("message_id") val message_id: Long
)

@Serializable
public data class CallbackQuery(
    @SerialName("id") val id: String,
    @SerialName("from") val from: User,
    @SerialName("message") val message: Message? = null,
    @SerialName("inline_message_id") val inlineMessageId: String? = null,
    @SerialName("chat_instance") val chatInstance: String,
    @SerialName("data") val data: String? = null,
    @SerialName("game_short_name") val gameShortName: String? = null
)

@Serializable
public data class MessageEntity(
    @SerialName("type") val type: Type,
    @SerialName("offset") val offset: Int,
    @SerialName("length") val length: Int,
    @SerialName("url") val url: String? = null,
    @SerialName("user") val user: User? = null,
    @SerialName("language") val language: String? = null
) {

    @Serializable
    public enum class Type {
        @SerialName("mention")
        MENTION,

        @SerialName("hashtag")
        HASHTAG,

        @SerialName("cashtag")
        CASHTAG,

        @SerialName("bot_command")
        BOT_COMMAND,

        @SerialName("url")
        URL,

        @SerialName("email")
        EMAIL,

        @SerialName("phone_number")
        PHONE_NUMBER,

        @SerialName("bold")
        BOLD,

        @SerialName("italic")
        ITALIC,

        @SerialName("underline")
        UNDERLINE,

        @SerialName("strikethrough")
        STRIKE_THROUGH,

        @SerialName("spoiler")
        SPOILER,

        @SerialName("code")
        CODE,

        @SerialName("pre")
        PRE,

        @SerialName("text_link")
        TEXT_LINK,

        @SerialName("text_mention")
        TEXT_MENTION,
    }
}

@Serializable
public data class PhotoSize(
    @SerialName("file_id") val fileId: String,
    @SerialName("file_unique_id") val fileUniqueId: String,
    @SerialName("width") val width: Int,
    @SerialName("height") val height: Int,
    @SerialName("file_size") val fileSize: Int? = null,
)

@Serializable
public data class Audio(
    @SerialName("file_id") val fileId: String,
    @SerialName("file_unique_id") val fileUniqueId: String,
    @SerialName("duration") val duration: Int,
    @SerialName("performer") val performer: String? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("file_name") val fileName: String? = null,
    @SerialName("mime_type") val mimeType: String? = null,
    @SerialName("file_size") val fileSize: Long? = null,
    @SerialName("thumb") val thumb: PhotoSize? = null
)

@Serializable
public data class Document(
    @SerialName("file_id") val fileId: String,
    @SerialName("file_unique_id") val fileUniqueId: String,
    @SerialName("thumb") val thumb: PhotoSize? = null,
    @SerialName("file_name") val fileName: String? = null,
    @SerialName("mime_type") val mimeType: String? = null,
    @SerialName("file_size") val fileSize: Long? = null,
)

@Serializable
public data class Video(
    @SerialName("file_id") val file_id: String,
    @SerialName("file_unique_id") val file_unique_id: String,
    @SerialName("width") val width: Int,
    @SerialName("height") val height: Int,
    @SerialName("duration") val duration: Int,
    @SerialName("thumb") val thumb: PhotoSize? = null,
    @SerialName("file_name") val fileName: String? = null,
    @SerialName("mime_type") val mimeType: String? = null,
    @SerialName("file_size") val fileSize: Long? = null,
)

@Serializable
public data class Animation(
    @SerialName("file_id") val file_id: String,
    @SerialName("file_unique_id") val file_unique_id: String,
    @SerialName("width") val width: Int,
    @SerialName("height") val height: Int,
    @SerialName("duration") val duration: Int,
    @SerialName("thumb") val thumb: PhotoSize? = null,
    @SerialName("file_name") val fileName: String? = null,
    @SerialName("mime_type") val mimeType: String? = null,
    @SerialName("fil_size") val fileSize: Long? = null
)

@Serializable
public data class Voice(
    @SerialName("file_id") val file_id: String,
    @SerialName("file_unique_id") val file_unique_id: String,
    @SerialName("duration") val duration: Int,
    @SerialName("mim_type") val mimeType: String? = null,
    @SerialName("file_size") val fileSize: Long? = null,
)

@Serializable
public data class VideoNote(
    @SerialName("file_id") val file_id: String,
    @SerialName("file_unique_id") val file_unique_id: String,
    @SerialName("length") val length: Int,
    @SerialName("duration") val duration: Int,
    @SerialName("thumb") val thumb: PhotoSize? = null,
    @SerialName("file_size") val fileSize: Int? = null,
)

@Serializable
public data class Contact(
    @SerialName("phone_number") val phoneNumber: String,
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String? = null,
    @SerialName("user_id") val userId: Int? = null,
    @SerialName("vcard") val vcard: String? = null
)

@Serializable
public data class Location(
    @SerialName("longitude") val longitude: Float,
    @SerialName("latitude") val latitude: Float,
    @SerialName("horizontal_accuracy") val horizontalAccuracy: Float? = null,
    @SerialName("live_period") val livePeriod: Int? = null,
    @SerialName("heading") val heading: Int? = null,
    @SerialName("proximity_alert_radius") val proximityAlertRadius: Int? = null
)

@Serializable
public data class Venue(
    @SerialName("location") val location: Location? = null,
    @SerialName("title") val title: String,
    @SerialName("address") val address: String,
    @SerialName("foursquare_id") val foursquareId: String? = null,
    @SerialName("foursquare_type") val foursquareType: String? = null,
    @SerialName("google_place_id") val googlePlaceId: String? = null,
    @SerialName("google_place_type") val googlePlaceType: String? = null
)

@Serializable
public data class ProximityAlertTriggered(
    @SerialName("traveler") val traveler: User,
    @SerialName("watcher") val watcher: User,
    @SerialName("distance") val distance: Int
)

@Serializable
public data class UserProfilePhotos(
    @SerialName("total_count") val total_count: Int,
    @SerialName("photos") val photos: List<List<PhotoSize>> = emptyList()
)

@Serializable
public data class File(
    @SerialName("file_id") val fileId: String,
    @SerialName("file_unique_id") val fileUniqueId: String,
    @SerialName("file_size") val fileSize: Long? = null,
    @SerialName("file_path") val filePath: String? = null,
)

@Serializable
public data class ResponseParameters(
    @SerialName("migrate_to_chat_id") val migrateToChatId: Long,
    @SerialName("retry_after") val retryAfter: Int
)

@Serializable
public data class Dice(
    @SerialName("emoji") val emoji: String,
    @SerialName("value") val value: Int
)

@Serializable
public data class BotCommand(
    @SerialName("command") val command: String,
    @SerialName("description") val description: String
)

@Serializable
public data class MessageAutoDeleteTimerChanged(
    @SerialName("message_auto_delete_time") val messageAutoDeleteTime: Long
)

@Serializable
public data class VideoChatScheduled(
    @SerialName("startDate") val start_date: Long
)

@Serializable
public class VideoChatStarted

@Serializable
public data class VideoChatEnded(
    @SerialName("duration") val duration: Long,
)

@Serializable
public data class VoiceChatParticipantsInvited(
    @SerialName("users") val users: List<User> = emptyList(),
)

@Serializable
public sealed class BotCommandScope(
    @SerialName("type") public val type: String
) {
    @Serializable
    @SerialName("default")
    public object BotCommandScopeDefault : BotCommandScope(type = "default")

    @Serializable
    @SerialName("all_private_chats")
    public object BotCommandScopeAllPrivateChats : BotCommandScope(type = "all_private_chats")

    @Serializable
    @SerialName("all_group_chats")
    public object BotCommandScopeAllGroupChats : BotCommandScope(type = "all_group_chats")

    @Serializable
    @SerialName("all_chat_administrators")
    public object BotCommandScopeAllChatAdministrators : BotCommandScope(type = "all_chat_administrators")

    @Serializable
    @SerialName("chat")
    public data class BotCommandScopeChat(
        @SerialName("chat_id") val chatId: ChatId
    ) : BotCommandScope(type = "chat")

    @Serializable
    @SerialName("chat_administrators")
    public data class BotCommandScopeChatAdministrators(
        @SerialName("chat_id") val chatId: ChatId
    ) : BotCommandScope(type = "chat_administrators")

    @Serializable
    @SerialName("chat_member")
    public data class BotCommandScopeChatMember(
        @SerialName("chat_id") val chatId: ChatId
    ) : BotCommandScope(type = "chat_member")
}

@Serializable
public data class LoginUrl(
    @SerialName("url") val url: String,
    @SerialName("forward_text") val forwardText: String? = null,
    @SerialName("bot_username") val botUsername: String? = null,
    @SerialName("request_write_access") val requestWriteAccess: Boolean? = null
)

@Serializable
public enum class ParseMode {

    @SerialName("HTML")
    Html,

    @SerialName("Markdown")
    Markdown,

    @SerialName("MarkdownV2")
    MarkdownV2
}