package com.elbekd.bot.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Sticker(
    @SerialName("file_id") val fileId: String,
    @SerialName("file_unique_id") val fileUniqueId: String,
    @SerialName("width") val width: Int,
    @SerialName("height") val height: Int,
    @SerialName("is_animated") val isAnimated: Boolean,
    @SerialName("is_video") val isVideo: Boolean,
    @SerialName("thumb") val thumb: PhotoSize? = null,
    @SerialName("emoji") val emoji: String? = null,
    @SerialName("set_name") val setName: String? = null,
    @SerialName("mask_position") val maskPosition: MaskPosition? = null,
    @SerialName("file_size") val fileSize: Int
)

@Serializable
public data class StickerSet(
    @SerialName("name") val name: String,
    @SerialName("title") val title: String,
    @SerialName("is_animated") val isAnimated: Boolean,
    @SerialName("is_video") val isVideo: Boolean,
    @SerialName("contains_mask") val containsMask: Boolean,
    @SerialName("stickers") val stickers: List<Sticker>,
    @SerialName("thumb") val thumb: PhotoSize? = null
)

@Serializable
public data class MaskPosition(
    @SerialName("point") val point: String,
    @SerialName("x_shift") val xShift: Float,
    @SerialName("y_shift") val yShift: Float,
    @SerialName("scale") val scale: Float
)