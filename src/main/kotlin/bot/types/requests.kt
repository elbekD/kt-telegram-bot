package bot.types

data class Send(
        val chat_id: String, val text: String, val parse_mode: String?,
        val disable_web_page_preview: Boolean?, val disable_notification: Boolean?,
        val reply_to_message_id: Int?, val reply_markup: ReplyKeyboard?)