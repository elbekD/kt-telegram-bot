package com.github.elbekD.bot.types

interface ReplyKeyboard

data class ReplyKeyboardMarkup(val keyboard: List<List<KeyboardButton>>,
                               val resize_keyboard: Boolean? = null,
                               val one_time_keyboard: Boolean? = null,
                               val selective: Boolean? = null
) : ReplyKeyboard

data class KeyboardButton(val text: String,
                          val request_contact: Boolean? = null,
                          val request_location: Boolean? = null)

data class ReplyKeyboardRemove(val remove_keyboard: Boolean, val selective: Boolean? = null) : ReplyKeyboard

data class InlineKeyboardMarkup(val inline_keyboard: List<List<InlineKeyboardButton>>) : ReplyKeyboard

data class InlineKeyboardButton(val text: String,
                                val url: String? = null,
                                val callback_data: String? = null,
                                val switch_inline_query: String? = null,
                                val switch_inline_query_current_chat: String? = null,
                                val callback_game: Any? = null,
                                val pay: Boolean? = null)

data class ForceReply(val force_reply: Boolean, val selective: Boolean) : ReplyKeyboard
