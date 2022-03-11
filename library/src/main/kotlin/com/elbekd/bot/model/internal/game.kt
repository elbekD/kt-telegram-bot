package com.elbekd.bot.model.internal

import com.elbekd.bot.internal.ApiConstants
import com.elbekd.bot.model.ChatId
import com.elbekd.bot.types.InlineKeyboardMarkup
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
internal class SendGame(
    @SerialName(ApiConstants.CHAT_ID) val chatId: Long,
    @SerialName(ApiConstants.GAME_SHORT_NAME) val gameShortName: String,
    @SerialName(ApiConstants.DISABLE_NOTIFICATION) val disableNotification: Boolean? = null,
    @SerialName(ApiConstants.PROTECT_CONTENT) val protectContent: Boolean? = null,
    @SerialName(ApiConstants.REPLY_TO_MESSAGE_ID) val replyToMessageId: Long? = null,
    @SerialName(ApiConstants.ALLOW_SENDING_WITHOUT_REPLY) val allowSendingWithoutReply: Boolean? = null,
    @SerialName(ApiConstants.REPLY_MARKUP) val replyMarkup: InlineKeyboardMarkup? = null
)

@Serializable
internal class SetGameScore(
    @SerialName(ApiConstants.USER_ID) val userId: Long,
    @SerialName(ApiConstants.SCORE) val score: Long,
    @SerialName(ApiConstants.FORCE) val force: Boolean? = null,
    @SerialName(ApiConstants.DISABLE_EDIT_MESSAGE) val disableEditMessage: Boolean? = null,
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId? = null,
    @SerialName(ApiConstants.MESSAGE_ID) val messageId: Long? = null,
    @SerialName(ApiConstants.INLINE_MESSAGE_ID) val inlineMessageId: String? = null
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
internal class GetGameHighScores(
    @SerialName(ApiConstants.USER_ID) val userId: Long,
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId? = null,
    @SerialName(ApiConstants.MESSAGE_ID) val messageId: Long? = null,
    @SerialName(ApiConstants.INLINE_MESSAGE_ID) val inlineMessageId: String? = null
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