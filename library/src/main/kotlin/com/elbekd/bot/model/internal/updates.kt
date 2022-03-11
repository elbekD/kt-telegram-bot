package com.elbekd.bot.model.internal

import com.elbekd.bot.internal.ApiConstants
import com.elbekd.bot.model.ChatId
import com.elbekd.bot.types.InlineKeyboardMarkup
import com.elbekd.bot.types.InputMedia
import com.elbekd.bot.types.MessageEntity
import com.elbekd.bot.types.ParseMode
import com.elbekd.bot.util.AllowedUpdate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class UpdateRequest(
    @SerialName("offset") val offset: Int? = null,
    @SerialName("limit") val limit: Int? = null,
    @SerialName("timeout") val timeout: Int? = null,
    @SerialName("allowed_updates") val allowedUpdates: List<AllowedUpdate>? = null
)

@Serializable
internal class EditMessageText(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId? = null,
    @SerialName(ApiConstants.MESSAGE_ID) val messageId: Long? = null,
    @SerialName(ApiConstants.INLINE_MESSAGE_ID) val inlineMessageId: String? = null,
    @SerialName(ApiConstants.TEXT) val text: String,
    @SerialName(ApiConstants.PARSE_MODE) val parseMode: ParseMode? = null,
    @SerialName(ApiConstants.ENTITIES) val entities: List<MessageEntity>? = null,
    @SerialName(ApiConstants.DISABLE_WEB_PAGE_PREVIEW) val disableWebPagePreview: Boolean? = null,
    @SerialName(ApiConstants.REPLY_MARKUP) val replyMarkup: InlineKeyboardMarkup? = null
) {
    init {
        if (chatId == null && messageId == null) {
            requireNotNull(
                value = inlineMessageId,
                lazyMessage = { "inlineMessageId is required if chatId and messageId are not provided" }
            )
        }

        if (inlineMessageId == null &&
            (chatId == null || messageId == null)
        ) {
            throw IllegalArgumentException("chatId and messageId are required if inlineMessageId not provided")
        }
    }
}

@Serializable
internal class EditMessageCaption(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId? = null,
    @SerialName(ApiConstants.MESSAGE_ID) val messageId: Long? = null,
    @SerialName(ApiConstants.INLINE_MESSAGE_ID) val inlineMessageId: String? = null,
    @SerialName(ApiConstants.CAPTION) val caption: String? = null,
    @SerialName(ApiConstants.PARSE_MODE) val parseMode: ParseMode? = null,
    @SerialName(ApiConstants.CAPTION_ENTITIES) val captionEntities: List<MessageEntity>? = null,
    @SerialName(ApiConstants.REPLY_MARKUP) val replyMarkup: InlineKeyboardMarkup? = null
) {
    init {
        if (chatId == null && messageId == null) {
            requireNotNull(
                value = inlineMessageId,
                lazyMessage = { "inlineMessageId is required if chatId and messageId are not provided" }
            )
        }

        if (inlineMessageId == null &&
            (chatId == null || messageId == null)
        ) {
            throw IllegalArgumentException("chatId and messageId are required if inlineMessageId not provided")
        }
    }
}

@Serializable
internal class EditMessageMedia(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId? = null,
    @SerialName(ApiConstants.MESSAGE_ID) val messageId: Long? = null,
    @SerialName(ApiConstants.INLINE_MESSAGE_ID) val inlineMessageId: String? = null,
    @SerialName(ApiConstants.MEDIA) val inputMedia: InputMedia,
    @SerialName(ApiConstants.REPLY_MARKUP) val replyMarkup: InlineKeyboardMarkup? = null
) {
    init {
        if (chatId == null && messageId == null) {
            requireNotNull(
                value = inlineMessageId,
                lazyMessage = { "inlineMessageId is required if chatId and messageId are not provided" }
            )
        }

        if (inlineMessageId == null &&
            (chatId == null || messageId == null)
        ) {
            throw IllegalArgumentException("chatId and messageId are required if inlineMessageId not provided")
        }
    }
}

@Serializable
internal class EditMessageReplyMarkup(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId? = null,
    @SerialName(ApiConstants.MESSAGE_ID) val messageId: Long? = null,
    @SerialName(ApiConstants.INLINE_MESSAGE_ID) val inlineMessageId: String? = null,
    @SerialName(ApiConstants.REPLY_MARKUP) val replyMarkup: InlineKeyboardMarkup? = null
) {
    init {
        if (chatId == null && messageId == null) {
            requireNotNull(
                value = inlineMessageId,
                lazyMessage = { "inlineMessageId is required if chatId and messageId are not provided" }
            )
        }

        if (inlineMessageId == null &&
            (chatId == null || messageId == null)
        ) {
            throw IllegalArgumentException("chatId and messageId are required if inlineMessageId not provided")
        }
    }
}

@Serializable
internal class StopPoll(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId,
    @SerialName(ApiConstants.MESSAGE_ID) val messageId: Long,
    @SerialName(ApiConstants.REPLY_MARKUP) val replyMarkup: InlineKeyboardMarkup? = null
)

@Serializable
internal class DeleteMessage(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId,
    @SerialName(ApiConstants.MESSAGE_ID) val messageId: Long
)