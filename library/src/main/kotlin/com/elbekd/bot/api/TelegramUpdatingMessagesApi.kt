package com.elbekd.bot.api

import com.elbekd.bot.model.ChatId
import com.elbekd.bot.types.InlineKeyboardMarkup
import com.elbekd.bot.types.InputMedia
import com.elbekd.bot.types.Message
import com.elbekd.bot.types.MessageEntity
import com.elbekd.bot.types.ParseMode
import com.elbekd.bot.types.Poll

public interface TelegramUpdatingMessagesApi {
    public suspend fun editMessageText(
        chatId: ChatId? = null,
        messageId: Long? = null,
        inlineMessageId: String? = null,
        text: String,
        parseMode: ParseMode? = null,
        entities: List<MessageEntity>? = null,
        disableWebPagePreview: Boolean? = null,
        replyMarkup: InlineKeyboardMarkup? = null
    ): Message

    public suspend fun editMessageCaption(
        chatId: ChatId? = null,
        messageId: Long? = null,
        inlineMessageId: String? = null,
        caption: String? = null,
        parseMode: ParseMode? = null,
        captionEntities: List<MessageEntity>? = null,
        replyMarkup: InlineKeyboardMarkup? = null
    ): Message

    public suspend fun editMessageMedia(
        chatId: ChatId? = null,
        messageId: Long? = null,
        inlineMessageId: String? = null,
        media: InputMedia,
        replyMarkup: InlineKeyboardMarkup? = null
    ): Message

    public suspend fun editMessageReplyMarkup(
        chatId: ChatId? = null,
        messageId: Long? = null,
        inlineMessageId: String? = null,
        replyMarkup: InlineKeyboardMarkup? = null
    ): Message

    public suspend fun stopPoll(
        chatId: ChatId,
        messageId: Long,
        replyMarkup: InlineKeyboardMarkup? = null
    ): Poll

    public suspend fun deleteMessage(
        chatId: ChatId,
        messageId: Long
    ): Boolean
}