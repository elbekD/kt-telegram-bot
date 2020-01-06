package com.elbekD.bot.types

import com.elbekD.bot.util.AllowedUpdate
import java.io.File

data class TelegramObject<out T>(val ok: Boolean,
                                 val result: T?,
                                 val error_code: Int?,
                                 val description: String?)

data class Update(val update_id: Int,
                  val message: Message?,
                  val edited_message: Message?,
                  val channel_post: Message?,
                  val edited_channel_post: Message?,
                  val inline_query: InlineQuery?,
                  val chosen_inline_result: ChosenInlineResult?,
                  val callback_query: CallbackQuery?,
                  val shipping_query: ShippingQuery?,
                  val pre_checkout_query: PreCheckoutQuery?,
                  val poll: Poll?)

data class WebhookInfo(val url: String,
                       val has_custom_certificate: Boolean,
                       val pending_update_count: Int,
                       val last_error_date: Long?,
                       val last_error_message: String?,
                       val max_connections: Int?,
                       val allowed_updates: List<AllowedUpdate>?)

data class User(val id: Int,
                val is_bot: Boolean,
                val first_name: String,
                val last_name: String?,
                val username: String?,
                val language_code: String?)

data class Chat(val id: Long,
                val type: String,
                val title: String?,
                val username: String?,
                val first_name: String?,
                val last_name: String?,
                val all_members_are_administrators: Boolean,
                val photo: ChatPhoto?,
                val description: String?,
                val invite_link: String?,
                val pinned_message: Message?)

data class Message(val message_id: Int,
                   val from: User?,
                   val date: Int,
                   val chat: Chat,
                   val forward_from: User?,
                   val forward_from_chat: Chat?,
                   val forward_from_message_id: Int?,
                   val forward_signature: String?,
                   val forward_date: Int?,
                   val reply_to_message: Message?,
                   val edit_date: Int?,
                   val author_signature: String?,
                   val text: String?,
                   val entities: List<MessageEntity>?,
                   val caption_entities: List<MessageEntity>?,
                   val audio: Audio?,
                   val document: Document?,
                   val animation: Animation?,
                   val game: Game?,
                   val photo: List<PhotoSize>?,
                   val sticker: Sticker?,
                   val video: Video?,
                   val voice: Voice?,
                   val video_note: VideoNote?,
                   val new_chat_members: List<User>?,
                   val caption: String?,
                   val contact: Contact?,
                   val location: Location?,
                   val venue: Venue?,
                   val poll: Poll?,
                   val new_chat_member: User?,
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
                   val passport_data: PassportData?)

data class CallbackQuery(val id: String,
                         val from: User,
                         val message: Message?,
                         val inline_message_id: String?,
                         val chat_instance: String,
                         val data: String?,
                         val game_short_name: String?)

data class MessageEntity(val type: String,
                         val offset: Int,
                         val length: Int,
                         val url: String?,
                         val user: User?) {

    enum class Types(@Transient @Suppress("unused") val type: String) {
        MENTION("mention"),
        HASHTAG("hashtag"),
        CASHTAG("cashtag"),
        BOT_COMMAND("bot_command"),
        URL("url"),
        EMAIL("email"),
        PHONE_NUMBER("phone_number"),
        BOLD("bold"),
        ITALIC("italic"),
        CODE("code"),
        PRE("pre"),
        TEXT_LINK("text_link"),
        TEXT_MENTION("text_mention")
    }
}

data class PhotoSize(val file_id: String,
                     val width: Int,
                     val height: Int,
                     val file_size: Int)

data class Audio(val file_id: String,
                 val duration: Int,
                 val performer: String?,
                 val title: String?,
                 val mime_type: String?,
                 val file_size: Int?,
                 val thumb: PhotoSize?)

data class Document(val file_id: String,
                    val thumb: PhotoSize?,
                    val file_name: String?,
                    val mime_type: String?,
                    val file_size: Int)

data class Video(val file_id: String,
                 val width: Int,
                 val height: Int,
                 val duration: Int,
                 val thumb: PhotoSize?,
                 val mime_type: String?,
                 val file_size: Int)

data class Animation(val file_id: String,
                     val width: Int,
                     val height: Int,
                     val duration: Int,
                     val thumb: PhotoSize?,
                     val file_name: String?,
                     val mime_type: String?,
                     val file_size: Int?)

data class Voice(val file_id: String,
                 val duration: Int,
                 val mime_type: String,
                 val file_size: Int)

data class VideoNote(val file_id: String,
                     val length: Int,
                     val duration: Int,
                     val thumb: PhotoSize?,
                     val file_size: Int)

data class Contact(val phone_number: String,
                   val first_name: String,
                   val last_name: String?,
                   val user_id: Int?,
                   val vcard: String?)

data class Location(val longitude: Float, val latitude: Float)

data class Venue(val location: Location?,
                 val title: String,
                 val address: String,
                 val foursquare_id: String?,
                 val foursquare_type: String?)

data class UserProfilePhotos(val total_count: Int, val photos: List<List<PhotoSize>>?)

data class File(val file_id: String, val file_size: Int, val file_path: String)

data class ChatPhoto(val small_file_id: String, val big_file_id: String)

data class ChatMember(val user: User,
                      val status: String,
                      val until_date: Int,
                      val can_be_edited: Boolean,
                      val can_change_info: Boolean,
                      val can_post_messages: Boolean,
                      val can_edit_messages: Boolean,
                      val can_delete_messages: Boolean,
                      val can_invite_users: Boolean,
                      val can_restrict_members: Boolean,
                      val can_pin_messages: Boolean,
                      val can_promote_members: Boolean,
                      val can_send_messages: Boolean,
                      val can_send_media_messages: Boolean,
                      val can_send_other_messages: Boolean,
                      val can_add_web_page_previews: Boolean)

@Suppress("unused")
data class ResponseParameters(val migrate_to_chat_id: Long, val retry_after: Int)

interface InputMedia {
    fun media(): String
    fun file(): File?
    fun thumb(): Any?
}

internal data class InputMediaPhoto(val media: String,
                                    @Transient private val attachment: File?,
                                    val caption: String?,
                                    val parse_mode: String?,
                                    val type: String = "photo") : InputMedia {
    override fun media() = media
    override fun file() = attachment
    override fun thumb(): Any? = null
}

internal data class InputMediaVideo(val media: String,
                                    @Transient private val attachment: File?,
                                    @Transient private val thumb: Any?,
                                    val caption: String?,
                                    val parse_mode: String?,
                                    val width: Int?,
                                    val height: Int?,
                                    val duration: Int?,
                                    val supports_streaming: Boolean?,
                                    val type: String = "video") : InputMedia {
    override fun media() = media
    override fun file() = attachment
    override fun thumb() = thumb
}

internal data class InputMediaAnimation(val media: String,
                                        @Transient private val attachment: File?,
                                        @Transient private val thumb: Any?,
                                        val caption: String?,
                                        val parse_mode: String?,
                                        val width: Int?,
                                        val height: Int?,
                                        val duration: Int?,
                                        val type: String = "animation") : InputMedia {
    override fun media() = media
    override fun file() = attachment
    override fun thumb() = thumb
}

internal data class InputMediaAudio(val media: String,
                                    @Transient private val attachment: File?,
                                    @Transient private val thumb: Any?,
                                    val caption: String?,
                                    val parse_mode: String?,
                                    val duration: Int?,
                                    val performer: String?,
                                    val title: String?,
                                    val type: String = "audio") : InputMedia {
    override fun media() = media
    override fun file() = attachment
    override fun thumb() = thumb
}

internal data class InputMediaDocument(val media: String,
                                       @Transient private val attachment: File?,
                                       @Transient private val thumb: Any?,
                                       val caption: String?,
                                       val parse_mode: String?,
                                       val type: String = "document") : InputMedia {
    override fun media() = media
    override fun file() = attachment
    override fun thumb() = thumb
}