package bot.types

data class TelegramObject<out T>(val ok: Boolean,
                                 val result: T?,
                                 val error_code: Int?,
                                 val description: String?
)

data class Update(val update_id: Int,
                  val message: Message?,
                  val edited_message: Message?,
                  val channel_post: Message?,
                  val edited_channel_post: Message?,
                  val inline_query: InlineQuery?,
                  val chosen_inline_result: ChosenInlineResult?,
                  val callback_query: CallbackQuery?,
                  val shipping_query: ShippingQuery,
                  val pre_checkout_query: PreCheckoutQuery?
)

data class User(val id: Int,
                val is_bot: Boolean,
                val first_name: String,
                val last_name: String?,
                val username: String?,
                val language_code: String?
)

data class Chat(val id: Int,
                val type: String,
                val title: String?,
                val username: String?,
                val first_name: String?,
                val last_name: String?,
                val all_members_are_administrators: Boolean,
                val photo: ChatPhoto?,
                val description: String?,
                val invite_link: String?,
                val pinned_message: Message?
)

data class Message(val message_id: Int,
                   val from: User?,
                   val date: Int,
                   val chat: Chat,
                   val forward_from: User?,
                   val forward_from_chat: Chat?,
                   val forward_from_message_id: Int,
                   val forward_signature: String,
                   val forward_date: Int,
                   val reply_to_message: Message?,
                   val edit_date: Int,
                   val author_signature: String,
                   val text: String?,
                   val entities: Array<MessageEntity>?,
                   val caption_entities: Array<MessageEntity>?,
                   val audio: Audio?,
                   val document: Document?,
                   val game: Game?,
                   val photo: Array<PhotoSize>?,
                   val sticker: Sticker?,
                   val video: Video?,
                   val voice: Voice?,
                   val video_note: VideoNote?,
                   val new_chat_members: Array<User>?,
                   val caption: String,
                   val contact: Contact?,
                   val location: Location?,
                   val venue: Venue?,
                   val new_chat_member: User?,
                   val left_chat_member: User?,
                   val new_chat_title: String,
                   val new_chat_photo: Array<PhotoSize>?,
                   val delete_chat_photo: Boolean,
                   val group_chat_created: Boolean,
                   val supergroup_chat_created: Boolean,
                   val channel_chat_created: Boolean,
                   val migrate_to_chat_id: Int,
                   val migrate_from_chat_id: Int,
                   val pinned_message: Message?,
                   val invoice: Invoice?,
                   val successful_payment: SuccessfulPayment?

)

data class CallbackQuery(val id: String,
                         val from: User,
                         val message: Message?,
                         val inline_message_id: String?,
                         val chat_instance: String,
                         val data: String?,
                         val game_short_name: String?
)

data class MessageEntity(val type: String,
                         val offset: Int,
                         val length: Int,
                         val url: String?,
                         val user: User?
)

data class PhotoSize(val file_id: String,
                     val width: Int,
                     val height: Int,
                     val file_size: Int
)

data class Audio(val file_id: String,
                 val duration: Int,
                 val performer: String?,
                 val title: String?,
                 val mime_type: String?,
                 val file_size: Int
)

data class Document(val file_id: String,
                    val thumb: PhotoSize?,
                    val file_name: String?,
                    val mime_type: String?,
                    val file_size: Int
)

data class Video(val file_id: String,
                 val width: Int,
                 val height: Int,
                 val duration: Int,
                 val thumb: PhotoSize?,
                 val mime_type: String?,
                 val file_size: Int
)

data class Voice(val file_id: String,
                 val duration: Int,
                 val mime_type: String,
                 val file_size: Int
)

data class VideoNote(val file_id: String,
                     val length: Int,
                     val duration: Int,
                     val thumb: PhotoSize?,
                     val file_size: Int
)

data class Contact(val phone_number: String,
                   val first_name: String,
                   val last_name: String,
                   val user_id: Int
)

data class Location(val longitude: Float, val latitude: Float)

data class Venue(val location: Location?,
                 val title: String,
                 val address: String,
                 val foursquare_id: String
)

data class UserProfilePhotos(val total_count: Int, val photos: Array<PhotoSize>?)

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
                      val can_add_web_page_previews: Boolean
)

data class ResponseParameters(val migrate_to_chat_id: Long, val retry_after: Int)


interface InputMedia

data class InputMediaPhoto(val type: String, val media: String, val caption: String?) : InputMedia

data class InputMediaVideo(val type: String,
                           val media: String,
                           val caption: String?,
                           val width: Int,
                           val height: Int,
                           val duration: Int) : InputMedia