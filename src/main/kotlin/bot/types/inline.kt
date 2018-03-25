package bot.types

data class InlineQuery(val id: String,
                       val from: User,
                       val location: Location,
                       val query: String,
                       val offset: String
)

interface InlineQueryResult

data class InlineQueryResultArticle(val type: String,
                                    val id: String,
                                    val title: String,
                                    val input_message_content: InputMessageContent,
                                    val reply_markup: InlineKeyboardMarkup,
                                    val url: String,
                                    val hide_url: Boolean,
                                    val description: String,
                                    val thumb_url: String,
                                    val thumb_width: Int,
                                    val thumb_height: Int
) : InlineQueryResult

data class InlineQueryResultPhoto(val type: String,
                                  val id: String,
                                  val photo_url: String,
                                  val thumb_url: String,
                                  val photo_width: Int,
                                  val photo_height: Int,
                                  val title: String,
                                  val description: String,
                                  val caption: String,
                                  val reply_markup: InlineKeyboardMarkup,
                                  val input_message_content: InputMessageContent
) : InlineQueryResult

data class InlineQueryResultGif(val type: String,
                                val id: String,
                                val gif_url: String,
                                val gif_width: Int,
                                val gif_height: Int,
                                val gif_duration: Int,
                                val thumb_url: String,
                                val title: String,
                                val caption: String,
                                val reply_markup: InlineKeyboardMarkup,
                                val input_message_content: InputMessageContent
) : InlineQueryResult

data class InlineQueryResultMpeg4Gif(val type: String,
                                     val id: String,
                                     val mpeg4_url: String,
                                     val mpeg4_width: Int,
                                     val mpeg4_height: Int,
                                     val mpeg4_duration: Int,
                                     val thumb_url: String,
                                     val caption: String,
                                     val reply_markup: InlineKeyboardMarkup,
                                     val input_message_content: InputMessageContent
) : InlineQueryResult

data class InlineQueryResultVideo(val type: String,
                                  val id: String,
                                  val video_url: String,
                                  val mime_type: String,
                                  val thumb_url: String,
                                  val title: String,
                                  val caption: String,
                                  val video_width: Int,
                                  val video_height: Int,
                                  val video_duration: Int,
                                  val description: String,
                                  val reply_markup: InlineKeyboardMarkup,
                                  val input_message_content: InputMessageContent
) : InlineQueryResult

data class InlineQueryResultAudio(val type: String,
                                  val id: String,
                                  val audio_url: String,
                                  val title: String,
                                  val caption: String,
                                  val performer: String,
                                  val audio_duration: Int,
                                  val reply_markup: InlineKeyboardMarkup,
                                  val input_message_content: InputMessageContent
) : InlineQueryResult

data class InlineQueryResultVoice(val type: String,
                                  val id: String,
                                  val voice_url: String,
                                  val title: String,
                                  val caption: String,
                                  val voice_duration: Int,
                                  val reply_markup: InlineKeyboardMarkup,
                                  val input_message_content: InputMessageContent
)

data class InlineQueryResultDocument(val type: String,
                                     val id: String,
                                     val title: String,
                                     val caption: String,
                                     val document_url: String,
                                     val mime_type: String,
                                     val description: String,
                                     val reply_markup: InlineKeyboardMarkup,
                                     val input_message_content: InputMessageContent,
                                     val thumb_url: String,
                                     val thumb_width: Int,
                                     val thumb_height: Int
) : InlineQueryResult

data class InlineQueryResultLocation(val type: String,
                                     val id: String,
                                     val latitude: Float,
                                     val longitude: Float,
                                     val title: String,
                                     val reply_markup: InlineKeyboardMarkup,
                                     val input_message_content: InputMessageContent,
                                     val thumb_url: String,
                                     val thumb_width: Int,
                                     val thumb_height: Int
) : InlineQueryResult

data class InlineQueryResultVenue(val type: String,
                                  val id: String,
                                  val latitude: Float,
                                  val longitude: Float,
                                  val title: String,
                                  val address: String,
                                  val foursquare_id: String,
                                  val reply_markup: InlineKeyboardMarkup,
                                  val input_message_content: InputMessageContent,
                                  val thumb_url: String,
                                  val thumb_width: Int,
                                  val thumb_height: Int
) : InlineQueryResult

data class InlineQueryResultContact(val type: String,
                                    val id: String,
                                    val phone_number: String,
                                    val first_name: String,
                                    val last_name: String,
                                    val reply_markup: InlineKeyboardMarkup,
                                    val input_message_content: InputMessageContent,
                                    val thumb_url: String,
                                    val thumb_width: Int,
                                    val thumb_height: Int
) : InlineQueryResult

data class InlineQueryResultGame(val type: String,
                                 val id: String,
                                 val game_short_name: String,
                                 val reply_markup: InlineKeyboardMarkup,
                                 val input_message_content: InputMessageContent
) : InlineQueryResult

data class InlineQueryResultCachedGif(val type: String,
                                      val id: String,
                                      val gif_file_id: String,
                                      val title: String,
                                      val caption: String,
                                      val reply_markup: InlineKeyboardMarkup,
                                      val input_message_content: InputMessageContent
) : InlineQueryResult

data class InlineQueryResultCachedMpeg4Gif(val type: String,
                                           val id: String,
                                           val mpeg4_file_id: String,
                                           val title: String,
                                           val caption: String,
                                           val reply_markup: InlineKeyboardMarkup,
                                           val input_message_content: InputMessageContent
) : InlineQueryResult

data class InlineQueryResultCachedSticker(val type: String,
                                          val id: String,
                                          val sticker_file_id: String,
                                          val reply_markup: InlineKeyboardMarkup,
                                          val input_message_content: InputMessageContent
) : InlineQueryResult

data class InlineQueryResultCachedDocument(val type: String,
                                           val id: String,
                                           val title: String,
                                           val document_file_id: String,
                                           val description: String,
                                           val caption: String,
                                           val reply_markup: InlineKeyboardMarkup,
                                           val input_message_content: InputMessageContent
) : InlineQueryResult

data class InlineQueryResultCachedVideo(val type: String,
                                        val id: String,
                                        val title: String,
                                        val video_file_id: String,
                                        val description: String,
                                        val caption: String,
                                        val reply_markup: InlineKeyboardMarkup,
                                        val input_message_content: InputMessageContent
) : InlineQueryResult

data class InlineQueryResultCachedVoice(val type: String,
                                        val id: String,
                                        val voice_file_id: String,
                                        val description: String,
                                        val caption: String,
                                        val reply_markup: InlineKeyboardMarkup,
                                        val input_message_content: InputMessageContent
) : InlineQueryResult

data class InlineQueryResultCachedAudio(val type: String,
                                        val id: String,
                                        val audio_file_id: String,
                                        val caption: String,
                                        val reply_markup: InlineKeyboardMarkup, val input_message_content: InputMessageContent
) : InlineQueryResult

interface InputMessageContent

data class InputTextMessageContent(val message_text: String,
                                   val parse_mode: String,
                                   val disable_web_page_preview: Boolean
) : InputMessageContent

data class InputLocationMessageContent(val latitude: Float, val longitude: Float) : InputMessageContent

data class InputVenueMessageContent(val latitude: Float,
                                    val longitude: Float,
                                    val title: String,
                                    val address: String,
                                    val foursquare_id: String
) : InputMessageContent

data class InputContactMessageContent(val phone_number: String, val first_name: String, val last_name: String) : InputMessageContent

data class ChosenInlineResult(val result_id: String,
                              val from: User,
                              val location: Location,
                              val inline_message_id: String,
                              val query: String
)