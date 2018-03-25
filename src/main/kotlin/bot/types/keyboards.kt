package bot.types

interface ReplyKeyboard

data class ReplyKeyboardMarkup(val keyboard: Array<Array<KeyboardButton>>,
                               val resize_keyboard: Boolean?,
                               val one_time_keyboard: Boolean?,
                               val selective: Boolean?
) : ReplyKeyboard

data class KeyboardButton(val text: String,
                          val request_contact: Boolean?,
                          val request_location: Boolean?
)

data class ReplyKeyboardRemove(val remove_keyboard: Boolean, val selective: Boolean?) : ReplyKeyboard

data class InlineKeyboardMarkup(val inline_keyboard: Array<InlineKeyboardButton>) : ReplyKeyboard

data class InlineKeyboardButton(val text: String,
                                val url: String?,
                                val callback_data: String?,
                                val switch_inline_query: String?,
                                val switch_inline_query_current_chat: String?,
                                val callback_game: CallbackGame?,
                                val pay: Boolean
)

data class ForceReply(val force_reply: Boolean, val selective: Boolean) : ReplyKeyboard
