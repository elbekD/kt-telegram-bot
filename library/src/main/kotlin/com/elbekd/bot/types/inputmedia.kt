package com.elbekd.bot.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonClassDiscriminator
import java.io.File

@Serializable
@JsonClassDiscriminator("message_type")
public sealed class InputMedia(
    @SerialName("type")
    public val type: String
) {
    public abstract val media: String
    public abstract val attachment: File?
    public abstract val thumb: Any?
}

@Serializable
@SerialName("photo")
public data class InputMediaPhoto(
    @SerialName("media") override val media: String,
    @Transient override val attachment: File? = null,
    @SerialName("caption") val caption: String? = null,
    @SerialName("parse_mode") val parseMode: String? = null,
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null
) : InputMedia(type = "photo") {
    @Transient
    override val thumb: Any? = null
}

@Serializable
@SerialName("video")
public data class InputMediaVideo(
    @SerialName("media") override val media: String,
    @Transient override val attachment: File? = null,
    @Transient override val thumb: Any? = null,
    @SerialName("caption") val caption: String? = null,
    @SerialName("parse_mode") val parseMode: String? = null,
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,
    @SerialName("width") val width: Int? = null,
    @SerialName("height") val height: Int? = null,
    @SerialName("duration") val duration: Int? = null,
    @SerialName("supports_streaming") val supportsStreaming: Boolean? = null
) : InputMedia(type = "video")

@Serializable
@SerialName("animation")
public data class InputMediaAnimation(
    @SerialName("media") override val media: String,
    @Transient override val attachment: File? = null,
    @Transient override val thumb: Any? = null,
    @SerialName("caption") val caption: String? = null,
    @SerialName("parse_mode") val parseMode: String? = null,
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,
    @SerialName("width") val width: Int? = null,
    @SerialName("height") val height: Int? = null,
    @SerialName("duration") val duration: Int? = null
) : InputMedia(type = "animation")

@Serializable
@SerialName("audio")
public data class InputMediaAudio(
    @SerialName("media") override val media: String,
    @Transient override val attachment: File? = null,
    @Transient override val thumb: Any? = null,
    @SerialName("caption") val caption: String? = null,
    @SerialName("parse_mode") val parseMode: String? = null,
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,
    @SerialName("duration") val duration: Int? = null,
    @SerialName("performer") val performer: String? = null,
    @SerialName("title") val title: String? = null
) : InputMedia(type = "audio")

@Serializable
@SerialName("document")
public data class InputMediaDocument(
    @SerialName("media") override val media: String,
    @Transient override val attachment: File? = null,
    @Transient override val thumb: Any? = null,
    @SerialName("caption") val caption: String? = null,
    @SerialName("parse_mode") val parseMode: String? = null,
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,
    @SerialName("disable_content_type_detection") val disableContentTypeDetection: Boolean? = null
) : InputMedia(type = "document")