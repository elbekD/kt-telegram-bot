package com.elbekd.bot.api

import com.elbekd.bot.types.InlineQueryResult
import com.elbekd.bot.types.SentWebAppMessage

public interface TelegramInlineModeApi {
    public suspend fun answerInlineQuery(
        inlineQueryId: String,
        results: List<InlineQueryResult>,
        cacheTime: Int? = null,
        isPersonal: Boolean? = null,
        nextOffset: String? = null,
        switchPmText: String? = null,
        switchPmParameter: String? = null
    ): Boolean

    public suspend fun answerWebAppQuery(
        webAppQueryId: String,
        result: InlineQueryResult
    ): SentWebAppMessage
}