package com.elbekd.bot.model.internal

import com.elbekd.bot.internal.ApiConstants
import com.elbekd.bot.types.InlineQueryResult
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class AnswerInlineQuery(
    @SerialName(ApiConstants.INLINE_QUERY_ID) val inlineQueryId: String,
    @SerialName(ApiConstants.RESULTS) val results: List<InlineQueryResult>,
    @SerialName(ApiConstants.CACHE_TIME) val cacheTime: Int? = null,
    @SerialName(ApiConstants.IS_PERSONAL) val isPersonal: Boolean? = null,
    @SerialName(ApiConstants.NEXT_OFFSET) val nextOffset: String? = null,
    @SerialName(ApiConstants.SWITCH_PM_TEXT) val switchPmText: String? = null,
    @SerialName(ApiConstants.SWITCH_PM_PARAMETER) val switchPmParameter: String? = null,
)


@Serializable
internal class AnswerWebAppQuery(
    @SerialName("web_app_query_id") val webAppQueryId: String,
    @SerialName("result") val result: InlineQueryResult,
)