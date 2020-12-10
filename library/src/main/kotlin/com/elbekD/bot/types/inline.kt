package com.elbekD.bot.types

public interface InlineQueryResult
public interface InputMessageContent

public data class InlineQuery(
    val id: String,
    val from: User,
    val location: Location?,
    val query: String,
    val offset: String
)

public data class InlineQueryResultArticle(
    val id: String,
    val title: String,
    val input_message_content: InputMessageContent,
    val reply_markup: InlineKeyboardMarkup? = null,
    val url: String? = null,
    val hide_url: Boolean? = null,
    val description: String? = null,
    val thumb_url: String? = null,
    val thumb_width: Int? = null,
    val thumb_height: Int? = null
) : InlineQueryResult {
    val type: String = "article"
}

public data class InlineQueryResultPhoto(
    val id: String,
    val photo_url: String,
    val thumb_url: String,
    val photo_width: Int? = null,
    val photo_height: Int? = null,
    val title: String? = null,
    val description: String? = null,
    val caption: String? = null,
    val parse_mode: String? = null,
    val reply_markup: InlineKeyboardMarkup? = null,
    val input_message_content: InputMessageContent? = null
) : InlineQueryResult {
    val type: String = "photo"
}

public data class InlineQueryResultGif(
    val id: String,
    val gif_url: String,
    val thumb_url: String,
    val gif_width: Int? = null,
    val gif_height: Int? = null,
    val gif_duration: Int? = null,
    val thumb_mime_type: String? = null,
    val title: String? = null,
    val caption: String? = null,
    val parse_mode: String? = null,
    val reply_markup: InlineKeyboardMarkup? = null,
    val input_message_content: InputMessageContent? = null
) : InlineQueryResult {
    val type: String = "gif"
}

public data class InlineQueryResultMpeg4Gif(
    val id: String,
    val mpeg4_url: String,
    val thumb_url: String,
    val mpeg4_width: Int? = null,
    val mpeg4_height: Int? = null,
    val mpeg4_duration: Int? = null,
    val thumb_mime_type: String? = null,
    val caption: String? = null,
    val parse_mode: String? = null,
    val reply_markup: InlineKeyboardMarkup? = null,
    val input_message_content: InputMessageContent? = null
) : InlineQueryResult {
    val type: String = "mpeg4_gif"
}

public data class InlineQueryResultVideo(
    val id: String,
    val video_url: String,
    val mime_type: String,
    val thumb_url: String,
    val title: String,
    val caption: String? = null,
    val parse_mode: String? = null,
    val video_width: Int? = null,
    val video_height: Int? = null,
    val video_duration: Int? = null,
    val description: String? = null,
    val reply_markup: InlineKeyboardMarkup? = null,
    val input_message_content: InputMessageContent? = null
) : InlineQueryResult {
    val type: String = "video"
}

public data class InlineQueryResultAudio(
    val id: String,
    val audio_url: String,
    val title: String,
    val caption: String? = null,
    val parse_mode: String? = null,
    val performer: String? = null,
    val audio_duration: Int? = null,
    val reply_markup: InlineKeyboardMarkup? = null,
    val input_message_content: InputMessageContent? = null
) : InlineQueryResult {
    val type: String = "audio"
}

public data class InlineQueryResultVoice(
    val id: String,
    val voice_url: String,
    val title: String,
    val caption: String? = null,
    val parse_mode: String? = null,
    val voice_duration: Int? = null,
    val reply_markup: InlineKeyboardMarkup? = null,
    val input_message_content: InputMessageContent? = null
) : InlineQueryResult {
    val type: String = "voice"
}

public data class InlineQueryResultDocument(
    val id: String,
    val title: String,
    val document_url: String,
    val mime_type: String,
    val caption: String? = null,
    val parse_mode: String? = null,
    val description: String? = null,
    val reply_markup: InlineKeyboardMarkup? = null,
    val input_message_content: InputMessageContent? = null,
    val thumb_url: String? = null,
    val thumb_width: Int? = null,
    val thumb_height: Int? = null
) : InlineQueryResult {
    val type: String = "document"
}

public data class InlineQueryResultLocation(
    val id: String,
    val latitude: Float,
    val longitude: Float,
    val title: String,
    val live_period: Int? = null,
    val reply_markup: InlineKeyboardMarkup? = null,
    val input_message_content: InputMessageContent? = null,
    val thumb_url: String? = null,
    val thumb_width: Int? = null,
    val thumb_height: Int? = null
) : InlineQueryResult {
    val type: String = "location"
}

public data class InlineQueryResultVenue(
    val id: String,
    val latitude: Float,
    val longitude: Float,
    val title: String,
    val address: String,
    val foursquare_id: String? = null,
    val foursquare_type: String? = null,
    val reply_markup: InlineKeyboardMarkup? = null,
    val input_message_content: InputMessageContent? = null,
    val thumb_url: String? = null,
    val thumb_width: Int? = null,
    val thumb_height: Int? = null
) : InlineQueryResult {
    val type: String = "venue"
}

public data class InlineQueryResultContact(
    val id: String,
    val phone_number: String,
    val first_name: String,
    val last_name: String? = null,
    val vcard: String? = null,
    val reply_markup: InlineKeyboardMarkup? = null,
    val input_message_content: InputMessageContent? = null,
    val thumb_url: String? = null,
    val thumb_width: Int? = null,
    val thumb_height: Int? = null
) : InlineQueryResult {
    val type: String = "contact"
}

public data class InlineQueryResultGame(
    val id: String,
    val game_short_name: String,
    val reply_markup: InlineKeyboardMarkup? = null
) : InlineQueryResult {
    val type: String = "game"
}

public data class InlineQueryResultCachedPhoto(
    val id: String,
    val photo_file_id: String,
    val title: String? = null,
    val description: String? = null,
    val caption: String? = null,
    val parse_mode: String? = null,
    val reply_markup: InlineKeyboardMarkup? = null,
    val input_message_content: InputMessageContent? = null
) : InlineQueryResult {
    val type: String = "photo"
}

public data class InlineQueryResultCachedGif(
    val id: String,
    val gif_file_id: String,
    val title: String? = null,
    val caption: String? = null,
    val parse_mode: String? = null,
    val reply_markup: InlineKeyboardMarkup? = null,
    val input_message_content: InputMessageContent? = null
) : InlineQueryResult {
    val type: String = "gif"
}

public data class InlineQueryResultCachedMpeg4Gif(
    val id: String,
    val mpeg4_file_id: String,
    val title: String? = null,
    val caption: String? = null,
    val parse_mode: String? = null,
    val reply_markup: InlineKeyboardMarkup? = null,
    val input_message_content: InputMessageContent? = null
) : InlineQueryResult {
    val type: String = "mpeg4_gif"
}

public data class InlineQueryResultCachedSticker(
    val id: String,
    val sticker_file_id: String,
    val reply_markup: InlineKeyboardMarkup? = null,
    val input_message_content: InputMessageContent? = null
) : InlineQueryResult {
    val type: String = "sticker"
}

public data class InlineQueryResultCachedDocument(
    val id: String,
    val title: String,
    val document_file_id: String,
    val description: String? = null,
    val caption: String? = null,
    val parse_mode: String? = null,
    val reply_markup: InlineKeyboardMarkup? = null,
    val input_message_content: InputMessageContent? = null
) : InlineQueryResult {
    val type: String = "document"
}

public data class InlineQueryResultCachedVideo(
    val id: String,
    val video_file_id: String,
    val title: String,
    val description: String? = null,
    val caption: String? = null,
    val parse_mode: String? = null,
    val reply_markup: InlineKeyboardMarkup? = null,
    val input_message_content: InputMessageContent? = null
) : InlineQueryResult {
    val type: String = "video"
}

public data class InlineQueryResultCachedVoice(
    val id: String,
    val voice_file_id: String,
    val description: String? = null,
    val caption: String? = null,
    val parse_mode: String? = null,
    val reply_markup: InlineKeyboardMarkup? = null,
    val input_message_content: InputMessageContent? = null
) : InlineQueryResult {
    val type: String = "voice"
}

public data class InlineQueryResultCachedAudio(
    val id: String,
    val audio_file_id: String,
    val caption: String? = null,
    val parse_mode: String? = null,
    val reply_markup: InlineKeyboardMarkup? = null,
    val input_message_content: InputMessageContent? = null
) : InlineQueryResult {
    val type: String = "audio"
}

public data class InputTextMessageContent(
    val message_text: String,
    val parse_mode: String? = null,
    val disable_web_page_preview: Boolean? = null
) : InputMessageContent

public data class InputLocationMessageContent(
    val latitude: Float,
    val longitude: Float,
    val live_period: Int? = null
) : InputMessageContent

public data class InputVenueMessageContent(
    val latitude: Float,
    val longitude: Float,
    val title: String,
    val address: String,
    val foursquare_id: String? = null,
    val foursquare_type: String? = null
) : InputMessageContent

public data class InputContactMessageContent(
    val phone_number: String,
    val first_name: String,
    val last_name: String? = null,
    val vcard: String? = null
) : InputMessageContent

public data class ChosenInlineResult(
    val result_id: String,
    val from: User,
    val location: Location?,
    val inline_message_id: String?,
    val query: String
)