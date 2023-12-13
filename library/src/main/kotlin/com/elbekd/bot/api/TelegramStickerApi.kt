package com.elbekd.bot.api

import com.elbekd.bot.model.ChatId
import com.elbekd.bot.types.*

public interface TelegramStickerApi {
    public suspend fun sendSticker(
        chatId: ChatId,
        sticker: Any,
        messageThreadId: Long? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyToMessageId: Long? = null,
        allowSendingWithoutReply: Boolean? = null,
        replyMarkup: ReplyKeyboard? = null
    ): Message

    public suspend fun getStickerSet(name: String): StickerSet

    public suspend fun getCustomEmojiStickers(customEmojiIds: List<String>): List<Sticker>

    public suspend fun uploadStickerFile(
        userId: Long, sticker: java.io.File, stickerFormat: String
    ): File

    /**
     * @param containsMask is deprecated. Use [stickerType] instead
     */
    public suspend fun createNewStickerSet(
        userId: Long,
        name: String,
        title: String,
        emojis: String,
        pngSticker: Any? = null,
        tgsSticker: java.io.File? = null,
        webmSticker: java.io.File? = null,
        stickerType: String? = null,
        containsMask: Boolean? = null,
        maskPosition: MaskPosition? = null
    ): Boolean

    public suspend fun addStickerToSet(
        userId: Long,
        name: String,
        emojis: String,
        pngSticker: Any? = null,
        tgsSticker: java.io.File? = null,
        webmSticker: java.io.File? = null,
        maskPosition: MaskPosition? = null
    ): Boolean

    public suspend fun setStickerPositionInSet(
        sticker: String, position: Int
    ): Boolean

    public suspend fun setStickerMaskPosition(sticker: String, maskPosition: MaskPosition): Boolean

    public suspend fun setCustomEmojiStickerSetThumbnail(name: String, customEmojiId: String? = null): Boolean

    public suspend fun setStickerSetTitle(name: String, title: String): Boolean

    public suspend fun deleteStickerSet(name: String): Boolean

    public suspend fun setStickerEmojiList(sticker: String, emojiList: Collection<String>): Boolean

    public suspend fun setStickerKeywords(sticker: String, keywords: Collection<String>): Boolean

    public suspend fun deleteStickerFromSet(sticker: String): Boolean

    public suspend fun setStickerSetThumbnail(
        name: String, userId: Long, thumbnail: Any? = null
    ): Boolean
}