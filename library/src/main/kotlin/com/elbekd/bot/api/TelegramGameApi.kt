package com.elbekd.bot.api

import com.elbekd.bot.model.ChatId
import com.elbekd.bot.types.GameHighScore
import com.elbekd.bot.types.InlineKeyboardMarkup
import com.elbekd.bot.types.Message

public interface TelegramGameApi {
    public suspend fun sendGame(
        chatId: Long,
        gameShortName: String,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyToMessageId: Long? = null,
        allowSendingWithoutReply: Boolean? = null,
        replyMarkup: InlineKeyboardMarkup? = null
    ): Message

    public suspend fun setGameScore(
        userId: Long,
        score: Long,
        force: Boolean? = null,
        disableEditMessage: Boolean? = null,
        chatId: ChatId.IntegerId? = null,
        messageId: Long? = null,
        inlineMessageId: String? = null
    ): Message

    public suspend fun getGameHighScores(
        userId: Long,
        chatId: ChatId.IntegerId? = null,
        messageId: Long? = null,
        inlineMessageId: String? = null
    ): List<GameHighScore>
}