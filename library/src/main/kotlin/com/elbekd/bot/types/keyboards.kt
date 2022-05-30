package com.elbekd.bot.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public sealed class ReplyKeyboard

@Serializable
public data class ReplyKeyboardMarkup(
    @SerialName("keyboard") val keyboard: List<List<KeyboardButton>>,
    @SerialName("resize_keyboard") val resizeKeyboard: Boolean? = null,
    @SerialName("one_time_keyboard") val oneTimeKeyboard: Boolean? = null,
    @SerialName("input_field_placeholder") val inputFieldPlaceholder: String? = null,
    @SerialName("selective") val selective: Boolean? = null
) : ReplyKeyboard()

@Serializable
public data class KeyboardButton(
    @SerialName("text") val text: String,
    @SerialName("request_contact") val requestContact: Boolean? = null,
    @SerialName("request_location") val requestLocation: Boolean? = null,
    @SerialName("request_poll") val requestPoll: KeyboardButtonPollType? = null,
    @SerialName("web_app") val webApp: WebAppInfo? = null,
)

@Serializable
public class KeyboardButtonPollType(
    @SerialName("type")
    public val type: Type
) {
    @Serializable
    public enum class Type {
        @SerialName("quiz")
        Quiz,

        @SerialName("regular")
        Regular,

        @SerialName("any")
        Any
    }
}

@Serializable
public data class ReplyKeyboardRemove(
    @SerialName("remove_keyboard") val removeKeyboard: Boolean,
    @SerialName("selective") val selective: Boolean? = null
) : ReplyKeyboard()

@Serializable
public data class InlineKeyboardMarkup(
    @SerialName("inline_keyboard") val inlineKeyboard: List<List<InlineKeyboardButton>>
) : ReplyKeyboard()

@Serializable
public data class InlineKeyboardButton(
    @SerialName("text") val text: String,
    @SerialName("url") val url: String? = null,
    @SerialName("login_url") val loginUrl: LoginUrl? = null,
    @SerialName("callback_data") val callbackData: String? = null,
    @SerialName("web_app") val webApp: WebAppInfo? = null,
    @SerialName("switch_inline_query") val switchInlineQuery: String? = null,
    @SerialName("switch_inline_query_current_chat") val switchInlineQueryCurrentChat: String? = null,
    @SerialName("callback_game") val callbackGame: CallbackGame? = null,
    @SerialName("pay") val pay: Boolean? = null,
)

@Serializable
public data class ForceReply(
    @SerialName("force_reply") val force_reply: Boolean,
    @SerialName("input_field_placeholder") val inputFieldPlaceholder: String? = null,
    @SerialName("selective") val selective: Boolean? = null,
) : ReplyKeyboard()
