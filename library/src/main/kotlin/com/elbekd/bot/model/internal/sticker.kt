package com.elbekd.bot.model.internal

import com.elbekd.bot.internal.ApiConstants
import com.elbekd.bot.types.MaskPosition
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class GetStickerSet(
    @SerialName(ApiConstants.NAME) val name: String
)

@Serializable
internal class GetCustomEmojiStickers(
    @SerialName(ApiConstants.CUSTOM_EMOJI_IDS) val customEmojiIds: List<String>
)

@Serializable
internal class SetStickerPositionInSet(
    @SerialName(ApiConstants.STICKER) val sticker: String,
    @SerialName(ApiConstants.POSITION) val position: Int
)

@Serializable
internal class SetStickerSetTitle(
    @SerialName(ApiConstants.NAME) val name: String,
    @SerialName(ApiConstants.TITLE) val title: String
)
@Serializable
internal class SetStickerMaskPosition(
    @SerialName(ApiConstants.STICKER) val sticker: String,
    @SerialName(ApiConstants.MASK_POSITION) val maskPosition: MaskPosition
)

@Serializable
internal class SetCustomEmojiStickerSetThumbnail(
    @SerialName(ApiConstants.NAME) val name: String,
    @SerialName(ApiConstants.CUSTOM_EMOJI_ID) val customEmojiId: String?
)

@Serializable
internal class DeleteStickerSet(
    @SerialName(ApiConstants.NAME) val name: String
)

@Serializable
internal class SetStickerEmojiList(
    @SerialName(ApiConstants.STICKER) val sticker: String,
    @SerialName(ApiConstants.EMOJI_LIST) val keywords: Collection<String>
)

@Serializable
internal class SetStickerKeywords(
    @SerialName(ApiConstants.STICKER) val sticker: String,
    @SerialName(ApiConstants.KEYWORDS) val keywords: Collection<String>
)

@Serializable
internal class DeleteStickerFromSet(
    @SerialName(ApiConstants.STICKER) val sticker: String
)