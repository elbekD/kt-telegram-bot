package com.elbekd.bot.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
public data class WebAppInfo(
    @SerialName("url") val url: String
)

@Serializable
public data class SentWebAppMessage(
    @SerialName("inline_message_id") val inlineMessageId: String? = null
)

@Serializable
public data class WebAppData(
    @SerialName("data") val data: String,
    @SerialName("button_text") val buttonText: String
)