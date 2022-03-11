package com.elbekd.bot.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public sealed class MenuButton {
    public abstract val type: String

    @Serializable
    @SerialName("commands")
    public data class Commands(@SerialName("type") override val type: String) : MenuButton()

    @Serializable
    @SerialName("web_app")
    public data class WebApp(
        @SerialName("type") override val type: String,
        @SerialName("text") val text: String,
        @SerialName("web_app") val webApp: WebAppInfo,
    ) : MenuButton()

    @Serializable
    @SerialName("default")
    public data class Default(@SerialName("type") override val type: String) : MenuButton()
}