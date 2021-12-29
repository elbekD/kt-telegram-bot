@file:Suppress("unused")

package com.elbekD.bot.types

import com.elbekD.bot.util.AllowedUpdate
import java.io.File

public data class TelegramObject<out T>(
    val ok: Boolean,
    val result: T?,
    val error_code: Int?,
    val description: String?
)

public data class Update(
    val update_id: Long,
    val message: Message?,
    val edited_message: Message?,
    val channel_post: Message?,
    val edited_channel_post: Message?,
    val inline_query: InlineQuery?,
    val chosen_inline_result: ChosenInlineResult?,
    val callback_query: CallbackQuery?,
    val shipping_query: ShippingQuery?,
    val pre_checkout_query: PreCheckoutQuery?,
    val poll: Poll?,
    val poll_answer: PollAnswer?
)

public data class WebhookInfo(
    val url: String,
    val has_custom_certificate: Boolean,
    val pending_update_count: Int,
    val ip_address: String?,
    val last_error_date: Long?,
    val last_error_message: String?,
    val max_connections: Int?,
    val allowed_updates: List<AllowedUpdate>?
)

public data class User(
    val id: Long,
    val is_bot: Boolean,
    val first_name: String,
    val last_name: String?,
    val username: String?,
    val language_code: String?,
    val can_join_groups: Boolean?,
    val can_read_all_group_messages: Boolean?,
    val supports_inline_queries: Boolean?
)

public data class Chat(
    val id: Long,
    val type: String,
    val title: String?,
    val username: String?,
    val first_name: String?,
    val last_name: String?,
    val photo: ChatPhoto?,
    val bio: String?,
    val description: String?,
    val invite_link: String?,
    val pinned_message: Message?,
    val permissions: ChatPermissions?,
    val slow_mode_delay: Boolean?,
    val sticker_set_name: String?,
    val can_set_sticker_set: Boolean?,
    val linked_chat_id: Long?,
    val location: ChatLocation?
)

public data class ChatLocation(
    val location: Location,
    val address: String
)

public data class ChatPermissions(
    val can_send_messages: Boolean? = null,
    val can_send_media_messages: Boolean? = null,
    val can_send_polls: Boolean? = null,
    val can_send_other_messages: Boolean? = null,
    val can_add_web_page_previews: Boolean? = null,
    val can_change_info: Boolean? = null,
    val can_invite_users: Boolean? = null,
    val can_pin_messages: Boolean? = null
)

public data class Message(
    val message_id: Long,
    val from: User?,
    val sender_chat: Chat?,
    val date: Int,
    val chat: Chat,
    val forward_from: User?,
    val forward_from_chat: Chat?,
    val forward_from_message_id: Long?,
    val forward_signature: String?,
    val forward_sender_name: String?,
    val forward_date: Int?,
    val reply_to_message: Message?,
    val via_bot: User?,
    val edit_date: Int?,
    val media_group_id: String?,
    val author_signature: String?,
    val text: String?,
    val entities: List<MessageEntity>?,
    val animation: Animation?,
    val audio: Audio?,
    val document: Document?,
    val photo: List<PhotoSize>?,
    val sticker: Sticker?,
    val video: Video?,
    val video_note: VideoNote?,
    val voice: Voice?,
    val caption: String?,
    val caption_entities: List<MessageEntity>?,
    val contact: Contact?,
    val dice: Dice?,
    val game: Game?,
    val poll: Poll?,
    val venue: Venue?,
    val location: Location?,
    val new_chat_members: List<User>?,
    val left_chat_member: User?,
    val new_chat_title: String?,
    val new_chat_photo: List<PhotoSize>?,
    val delete_chat_photo: Boolean?,
    val group_chat_created: Boolean?,
    val supergroup_chat_created: Boolean?,
    val channel_chat_created: Boolean?,
    val migrate_to_chat_id: Long?,
    val migrate_from_chat_id: Long?,
    val pinned_message: Message?,
    val invoice: Invoice?,
    val successful_payment: SuccessfulPayment?,
    val connected_website: String?,
    val passport_data: PassportData?,
    val proximity_alert_triggered: ProximityAlertTriggered?,
    val reply_markup: InlineKeyboardMarkup?
)

public data class MessageId(
    val message_id: Long
)

public data class CallbackQuery(
    val id: String,
    val from: User,
    val message: Message?,
    val inline_message_id: String?,
    val chat_instance: String,
    val data: String?,
    val game_short_name: String?
)

public data class MessageEntity(
    val type: String,
    val offset: Int,
    val length: Int,
    val url: String? = null,
    val user: User? = null,
    val language: String? = null
) {

    public enum class Types(@Transient @Suppress("unused") public val type: String) {
        MENTION("mention"),
        HASHTAG("hashtag"),
        CASHTAG("cashtag"),
        BOT_COMMAND("bot_command"),
        URL("url"),
        EMAIL("email"),
        PHONE_NUMBER("phone_number"),
        BOLD("bold"),
        ITALIC("italic"),
        UNDERLINE("underline"),
        STRIKE_THROUGH("strikethrough"),
        CODE("code"),
        PRE("pre"),
        TEXT_LINK("text_link"),
        TEXT_MENTION("text_mention")
    }
}

public data class PhotoSize(
    val file_id: String,
    val width: Int,
    val height: Int,
    val file_size: Int
)

public data class Audio(
    val file_id: String,
    val file_unique_id: String,
    val duration: Int,
    val performer: String?,
    val title: String?,
    val file_name: String?,
    val mime_type: String?,
    val file_size: Int?,
    val thumb: PhotoSize?
)

public data class Document(
    val file_id: String,
    val file_unique_id: String,
    val thumb: PhotoSize?,
    val file_name: String?,
    val mime_type: String?,
    val file_size: Int
)

public data class Video(
    val file_id: String,
    val file_unique_id: String,
    val width: Int,
    val height: Int,
    val duration: Int,
    val thumb: PhotoSize?,
    val file_name: String?,
    val mime_type: String?,
    val file_size: Int
)

public data class Animation(
    val file_id: String,
    val file_unique_id: String,
    val width: Int,
    val height: Int,
    val duration: Int,
    val thumb: PhotoSize?,
    val file_name: String?,
    val mime_type: String?,
    val file_size: Int?
)

public data class Voice(
    val file_id: String,
    val file_unique_id: String,
    val duration: Int,
    val mime_type: String,
    val file_size: Int
)

public data class VideoNote(
    val file_id: String,
    val file_unique_id: String,
    val length: Int,
    val duration: Int,
    val thumb: PhotoSize?,
    val file_size: Int
)

public data class Contact(
    val phone_number: String,
    val first_name: String,
    val last_name: String?,
    val user_id: Long?,
    val vcard: String?
)

public data class Location(
    val longitude: Float,
    val latitude: Float,
    val horizontal_accuracy: Float?,
    val live_period: Int?,
    val heading: Int?,
    val proximity_alert_radius: Int?
)

public data class Venue(
    val location: Location?,
    val title: String,
    val address: String,
    val foursquare_id: String?,
    val foursquare_type: String?,
    val google_place_id: String?,
    val google_place_type: String?
)

public data class ProximityAlertTriggered(
    val traveler: User,
    val watcher: User,
    val distance: Int
)

public data class UserProfilePhotos(val total_count: Int, val photos: List<List<PhotoSize>>?)

public data class File(
    val file_id: String,
    val file_unique_id: String,
    val file_size: Int,
    val file_path: String
)

public data class ChatPhoto(
    val small_file_id: String,
    val small_file_unique_id: String,
    val big_file_id: String,
    val big_file_unique_id: String
)

public data class ChatMember(
    val user: User,
    val status: String,
    val custom_title: String?,
    val is_anonymous: Boolean?,
    val can_be_edited: Boolean?,
    val can_post_messages: Boolean?,
    val can_edit_messages: Boolean?,
    val can_delete_messages: Boolean?,
    val can_restrict_members: Boolean?,
    val can_promote_members: Boolean?,
    val can_change_info: Boolean?,
    val can_invite_users: Boolean?,
    val can_pin_messages: Boolean?,
    val is_member: Boolean?,
    val can_send_messages: Boolean?,
    val can_send_media_messages: Boolean?,
    val can_send_polls: Boolean?,
    val can_send_other_messages: Boolean?,
    val can_add_web_page_previews: Boolean?,
    val until_date: Int?
)

public data class ResponseParameters(val migrate_to_chat_id: Long, val retry_after: Int)

public data class Dice(val emoji: String, val value: Int)

public data class BotCommand(val command: String, val description: String)

public interface InputMedia {
    public fun media(): String
    public fun file(): File?
    public fun thumb(): Any?
}

internal data class InputMediaPhoto(
    val media: String,
    @Transient private val attachment: File?,
    val caption: String?,
    val parse_mode: String?,
    val caption_entities: List<MessageEntity>?,
    val type: String = "photo"
) : InputMedia {
    override fun media() = media
    override fun file() = attachment
    override fun thumb(): Any? = null
}

internal data class InputMediaVideo(
    val media: String,
    @Transient private val attachment: File?,
    @Transient private val thumb: Any?,
    val caption: String?,
    val parse_mode: String?,
    val caption_entities: List<MessageEntity>?,
    val width: Int?,
    val height: Int?,
    val duration: Int?,
    val supports_streaming: Boolean?,
    val type: String = "video"
) : InputMedia {
    override fun media() = media
    override fun file() = attachment
    override fun thumb() = thumb
}

internal data class InputMediaAnimation(
    val media: String,
    @Transient private val attachment: File?,
    @Transient private val thumb: Any?,
    val caption: String?,
    val parse_mode: String?,
    val caption_entities: List<MessageEntity>?,
    val width: Int?,
    val height: Int?,
    val duration: Int?,
    val type: String = "animation"
) : InputMedia {
    override fun media() = media
    override fun file() = attachment
    override fun thumb() = thumb
}

internal data class InputMediaAudio(
    val media: String,
    @Transient private val attachment: File?,
    @Transient private val thumb: Any?,
    val caption: String?,
    val parse_mode: String?,
    val caption_entities: List<MessageEntity>?,
    val duration: Int?,
    val performer: String?,
    val title: String?,
    val type: String = "audio"
) : InputMedia {
    override fun media() = media
    override fun file() = attachment
    override fun thumb() = thumb
}

internal data class InputMediaDocument(
    val media: String,
    @Transient private val attachment: File?,
    @Transient private val thumb: Any?,
    val caption: String?,
    val parse_mode: String?,
    val caption_entities: List<MessageEntity>?,
    val disable_content_type_detection: Boolean?,
    val type: String = "document"
) : InputMedia {
    override fun media() = media
    override fun file() = attachment
    override fun thumb() = thumb
}