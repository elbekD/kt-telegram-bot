package com.elbekd.bot.model.internal

import com.elbekd.bot.internal.ApiConstants
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class GetStickerSet(
    @SerialName(ApiConstants.NAME) val name: String
)

@Serializable
internal class SetStickerPositionInSet(
    @SerialName(ApiConstants.STICKER) val sticker: String,
    @SerialName(ApiConstants.POSITION) val position: Int
)

@Serializable
internal class DeleteStickerFromSet(
    @SerialName(ApiConstants.STICKER) val sticker: String
)