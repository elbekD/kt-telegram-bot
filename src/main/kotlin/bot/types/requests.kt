package bot.types

internal data class SendMessage(
        val chat_id: String, val text: String, val parse_mode: String?,
        val disable_web_page_preview: Boolean?, val disable_notification: Boolean?,
        val reply_to_message_id: Int?, val reply_markup: ReplyKeyboard?)

internal data class ForwardMessage(val chat_id: String, val from_chat_id: String,
                                   val message_id: Int, val disable_notification: Boolean?)

internal data class SendLocation(val chat_id: String, val latitude: Double, val longitude: Double,
                                 val live_period: Int?, val disable_notification: Boolean?,
                                 val reply_to_message_id: Int?, val reply_markup: ReplyKeyboard?)

internal data class EditLocation(val chat_id: String?, val message_id: Int?, val inline_message_id: String?,
                                 val latitude: Double, val longitude: Double, val reply_markup: ReplyKeyboard?)

internal data class StopLocation(val chat_id: String?, val message_id: Int?,
                                 val inline_message_id: String?, val reply_markup: ReplyKeyboard?)

internal data class SendVenue(val chat_id: String, val latitude: Double, val longitude: Double,
                              val title: String, val address: String, val foursquare_id: String?,
                              val disable_notification: Boolean?, val reply_to_message_id: Int?,
                              val reply_markup: ReplyKeyboard?)

internal data class SendContact(val chat_id: String, val phone_number: String, val first_name: String,
                                val last_name: String?, val disable_notification: Boolean?,
                                val reply_to_message_id: Int?, val reply_markup: ReplyKeyboard?)