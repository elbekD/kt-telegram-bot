package com.elbekd.bot.types

import com.elbekd.bot.model.internal.InlineQueryResultSerializer
import com.elbekd.bot.model.internal.InputMessageContentSerializer
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class InlineQuery(
    @SerialName("id") val id: String,
    @SerialName("from") val from: User,
    @SerialName("query") val query: String,
    @SerialName("offset") val offset: String,
    @SerialName("chat_type") val chatType: String? = null,
    @SerialName("location") val location: Location? = null
)

@Serializable(with = InlineQueryResultSerializer::class)
public sealed class InlineQueryResult {
    @SerialName("type")
    public abstract val type: String
}

@Serializable
public data class InlineQueryResultArticle(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent,
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @SerialName("url") val url: String? = null,
    @SerialName("hide_url") val hideUrl: Boolean? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("thumb_url") val thumbUrl: String? = null,
    @SerialName("thumb_width") val thumbWidth: Int? = null,
    @SerialName("thumb_height") val thumbHeight: Int? = null,
) : InlineQueryResult() {
    @EncodeDefault
    @SerialName("type")
    override val type: String = "article"
}

@Serializable
public data class InlineQueryResultPhoto(
    @SerialName("id") val id: String,
    @SerialName("photo_url") val photoUrl: String,
    @SerialName("thumb_url") val thumbUrl: String,
    @SerialName("photo_width") val photoWidth: Int? = null,
    @SerialName("photo_height") val photoHeight: Int? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("caption") val caption: String? = null,
    @SerialName("parse_mode") val parseMode: String? = null,
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult() {
    @EncodeDefault
    @SerialName("type")
    override val type: String = "photo"
}

@Serializable
public data class InlineQueryResultGif(
    @SerialName("id") val id: String,
    @SerialName("gif_url") val gifUrl: String,
    @SerialName("thumb_url") val thumbUrl: String,
    @SerialName("gif_width") val gifWidth: Int? = null,
    @SerialName("gif_height") val gifHeight: Int? = null,
    @SerialName("gif_duration") val gifDuration: Int? = null,
    @SerialName("thumb_mime_type") val thumbMimeType: String? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("caption") val caption: String? = null,
    @SerialName("parse_mode") val parseMode: String? = null,
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult() {
    @EncodeDefault
    @SerialName("type")
    override val type: String = "gif"
}

@Serializable
public data class InlineQueryResultMpeg4Gif(
    @SerialName("id") val id: String,
    @SerialName("mpeg4_url") val mpeg4Url: String,
    @SerialName("thumb_url") val thumbUrl: String,
    @SerialName("mpeg4_width") val mpeg4Width: Int? = null,
    @SerialName("mpeg4_height") val mpeg4Height: Int? = null,
    @SerialName("mpeg4_duration") val mpeg4Duration: Int? = null,
    @SerialName("thumb_mime_type") val thumbMimeType: String? = null,
    @SerialName("caption") val caption: String? = null,
    @SerialName("parse_mode") val parseMode: String? = null,
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult() {
    @EncodeDefault
    @SerialName("type")
    override val type: String = "mpeg4_gif"
}

@Serializable
public data class InlineQueryResultVideo(
    @SerialName("id") val id: String,
    @SerialName("video_url") val videoUrl: String,
    @SerialName("mime_type") val mimeType: String,
    @SerialName("thumb_url") val thumbUrl: String,
    @SerialName("title") val title: String,
    @SerialName("caption") val caption: String? = null,
    @SerialName("parse_mode") val parseMode: String? = null,
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,
    @SerialName("video_width") val videoWidth: Int? = null,
    @SerialName("video_height") val videoHeight: Int? = null,
    @SerialName("video_duration") val videoDuration: Int? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult() {
    @EncodeDefault
    @SerialName("type")
    override val type: String = "video"
}

@Serializable
public data class InlineQueryResultAudio(
    @SerialName("id") val id: String,
    @SerialName("audio_url") val audioUrl: String,
    @SerialName("title") val title: String,
    @SerialName("caption") val caption: String? = null,
    @SerialName("parse_mode") val parseMode: String? = null,
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,
    @SerialName("performer") val performer: String? = null,
    @SerialName("audio_duration") val audioDuration: Int? = null,
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult() {
    @EncodeDefault
    @SerialName("type")
    override val type: String = "audio"
}

@Serializable
public data class InlineQueryResultVoice(
    @SerialName("id") val id: String,
    @SerialName("voice_url") val voiceUrl: String,
    @SerialName("title") val title: String,
    @SerialName("caption") val caption: String? = null,
    @SerialName("parse_mode") val parseMode: String? = null,
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,
    @SerialName("voice_duration") val voiceDuration: Int? = null,
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult() {
    @EncodeDefault
    @SerialName("type")
    override val type: String = "voice"
}

@Serializable
public data class InlineQueryResultDocument(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("document_url") val documentUrl: String,
    @SerialName("mime_type") val mimeType: String,
    @SerialName("caption") val caption: String? = null,
    @SerialName("parse_mode") val parseMode: String? = null,
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null,
    @SerialName("thumb_url") val thumbUrl: String? = null,
    @SerialName("thumb_width") val thumbWidth: Int? = null,
    @SerialName("thumb_height") val thumbHeight: Int? = null
) : InlineQueryResult() {
    @EncodeDefault
    @SerialName("type")
    override val type: String = "document"
}

@Serializable
public data class InlineQueryResultLocation(
    @SerialName("id") val id: String,
    @SerialName("latitude") val latitude: Float,
    @SerialName("longitude") val longitude: Float,
    @SerialName("title") val title: String,
    @SerialName("horizontal_accuracy") val horizontalAccuracy: Float? = null,
    @SerialName("live_period") val live_period: Int? = null,
    @SerialName("heading") val heading: Int? = null,
    @SerialName("proximity_alert_radius") val proximityAlertRadius: Int? = null,
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null,
    @SerialName("thumb_url") val thumbUrl: String? = null,
    @SerialName("thumb_width") val thumbWidth: Int? = null,
    @SerialName("thumb_height") val thumbHeight: Int? = null
) : InlineQueryResult() {
    @EncodeDefault
    @SerialName("type")
    override val type: String = "location"
}

@Serializable
public data class InlineQueryResultVenue(
    @SerialName("id") val id: String,
    @SerialName("latitude") val latitude: Float,
    @SerialName("longitude") val longitude: Float,
    @SerialName("title") val title: String,
    @SerialName("address") val address: String,
    @SerialName("foursquare_id") val foursquareId: String? = null,
    @SerialName("foursquare_type") val foursquareType: String? = null,
    @SerialName("google_place_id") val googlePlaceId: String? = null,
    @SerialName("google_place_type") val googlePlaceType: String? = null,
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null,
    @SerialName("thumb_url") val thumbUrl: String? = null,
    @SerialName("thumb_width") val thumbWidth: Int? = null,
    @SerialName("thumb_height") val thumbHeight: Int? = null
) : InlineQueryResult() {
    @EncodeDefault
    @SerialName("type")
    override val type: String = "venue"
}

@Serializable
public data class InlineQueryResultContact(
    @SerialName("id") val id: String,
    @SerialName("phone_number") val phoneNumber: String,
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String? = null,
    @SerialName("vcard") val vcard: String? = null,
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null,
    @SerialName("thumb_url") val thumbUrl: String? = null,
    @SerialName("thumb_width") val thumbWidth: Int? = null,
    @SerialName("thumb_height") val thumbHeight: Int? = null
) : InlineQueryResult() {
    @EncodeDefault
    @SerialName("type")
    override val type: String = "contact"
}

@Serializable
public data class InlineQueryResultGame(
    @SerialName("id") val id: String,
    @SerialName("game_short_name") val gameShortName: String,
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null
) : InlineQueryResult() {
    @EncodeDefault
    @SerialName("type")
    override val type: String = "game"
}

@Serializable
public data class InlineQueryResultCachedPhoto(
    @SerialName("id") val id: String,
    @SerialName("photo_file_id") val photoFileId: String,
    @SerialName("title") val title: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("caption") val caption: String? = null,
    @SerialName("parse_mode") val parseMode: String? = null,
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult() {
    @EncodeDefault
    @SerialName("type")
    override val type: String = "photo"
}

@Serializable
public data class InlineQueryResultCachedGif(
    @SerialName("id") val id: String,
    @SerialName("gif_file_id") val gifFileId: String,
    @SerialName("title") val title: String? = null,
    @SerialName("caption") val caption: String? = null,
    @SerialName("parse_mode") val parseMode: String? = null,
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult() {
    @EncodeDefault
    @SerialName("type")
    override val type: String = "gif"
}

@Serializable
public data class InlineQueryResultCachedMpeg4Gif(
    @SerialName("id") val id: String,
    @SerialName("mpeg4_file_id") val mpeg4FileId: String,
    @SerialName("title") val title: String? = null,
    @SerialName("caption") val caption: String? = null,
    @SerialName("parse_mode") val parseMode: String? = null,
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult() {
    @EncodeDefault
    @SerialName("type")
    override val type: String = "mpeg4_gif"
}

@Serializable
public data class InlineQueryResultCachedSticker(
    @SerialName("id") val id: String,
    @SerialName("sticker_file_id") val stickerFileId: String,
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult() {
    @EncodeDefault
    @SerialName("type")
    override val type: String = "sticker"
}

@Serializable
public data class InlineQueryResultCachedDocument(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("document_file_id") val documentFileId: String,
    @SerialName("description") val description: String? = null,
    @SerialName("caption") val caption: String? = null,
    @SerialName("parse_mode") val parseMode: String? = null,
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult() {
    @EncodeDefault
    @SerialName("type")
    override val type: String = "document"
}

@Serializable
public data class InlineQueryResultCachedVideo(
    @SerialName("id") val id: String,
    @SerialName("video_file_id") val videoFileId: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String? = null,
    @SerialName("caption") val caption: String? = null,
    @SerialName("parse_mode") val parseMode: String? = null,
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult() {
    @EncodeDefault
    @SerialName("type")
    override val type: String = "video"
}

@Serializable
public data class InlineQueryResultCachedVoice(
    @SerialName("id") val id: String,
    @SerialName("voice_file_id") val voiceFileId: String,
    @SerialName("description") val description: String? = null,
    @SerialName("caption") val caption: String? = null,
    @SerialName("parse_mode") val parseMode: String? = null,
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult() {
    @EncodeDefault
    @SerialName("type")
    override val type: String = "voice"
}

@Serializable
public data class InlineQueryResultCachedAudio(
    @SerialName("id") val id: String,
    @SerialName("audio_file_id") val audioFileId: String,
    @SerialName("caption") val caption: String? = null,
    @SerialName("parse_mode") val parseMode: String? = null,
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult() {
    @EncodeDefault
    @SerialName("type")
    override val type: String = "audio"
}

@Serializable(with = InputMessageContentSerializer::class)
public sealed class InputMessageContent

@Serializable
public data class InputTextMessageContent(
    @SerialName("message_text") val messageText: String,
    @SerialName("parse_mode") val parseMode: String? = null,
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,
    @SerialName("disable_web_page_preview") val disableWebPagePreview: Boolean? = null
) : InputMessageContent()

@Serializable
public data class InputLocationMessageContent(
    @SerialName("latitude") val latitude: Float,
    @SerialName("longitude") val longitude: Float,
    @SerialName("horizontal_accuracy") val horizontalAccuracy: Float? = null,
    @SerialName("live_period") val livePeriod: Int? = null,
    @SerialName("heading") val heading: Int? = null,
    @SerialName("proximity_alert_radius") val proximityAlertRadius: Int? = null
) : InputMessageContent()

@Serializable
public data class InputVenueMessageContent(
    @SerialName("latitude") val latitude: Float,
    @SerialName("longitude") val longitude: Float,
    @SerialName("title") val title: String,
    @SerialName("address") val address: String,
    @SerialName("foursquare_id") val foursquareId: String? = null,
    @SerialName("foursquare_type") val foursquareType: String? = null,
    @SerialName("google_place_id") val googlePlaceId: String? = null,
    @SerialName("google_place_type") val googlePlaceType: String? = null
) : InputMessageContent()

@Serializable
public data class InputContactMessageContent(
    @SerialName("phone_number") val phoneNumber: String,
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String? = null,
    @SerialName("vcard") val vcard: String? = null
) : InputMessageContent()

@Serializable
public data class InputInvoiceMessageContent(
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("payload") val payload: String,
    @SerialName("provider_token") val providerToken: String,
    @SerialName("currency") val currency: String,
    @SerialName("prices") val prices: List<LabeledPrice>,
    @SerialName("max_tip_amount") val maxTipAmount: Long? = null,
    @SerialName("suggested_tip_amounts") val suggestedTipAmounts: Long? = null,
    @SerialName("provider_data") val providerData: String? = null,
    @SerialName("photo_url") val photoUrl: String? = null,
    @SerialName("photo_size") val photoSize: Int? = null,
    @SerialName("photo_width") val photoWidth: Int? = null,
    @SerialName("photo_height") val photoHeight: Int? = null,
    @SerialName("need_name") val needName: Boolean? = null,
    @SerialName("need_phone_number") val needPhoneNumber: Boolean? = null,
    @SerialName("need_email") val needEmail: Boolean? = null,
    @SerialName("need_shipping_address") val needShippingAddress: Boolean? = null,
    @SerialName("send_phone_number_to_provider") val sendPhoneNumberToProvider: Boolean? = null,
    @SerialName("send_email_to_provider") val sendEmailToProvider: Boolean? = null,
    @SerialName("is_flexible") val isFlexible: Boolean? = null,
) : InputMessageContent()

@Serializable
public data class ChosenInlineResult(
    @SerialName("result_id") val resultId: String,
    @SerialName("from") val from: User,
    @SerialName("location") val location: Location? = null,
    @SerialName("inline_message_id") val inlineMessageId: String? = null,
    @SerialName("query") val query: String
)