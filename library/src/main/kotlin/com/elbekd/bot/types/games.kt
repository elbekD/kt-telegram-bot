package com.elbekd.bot.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Game(
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("photo") val photo: List<PhotoSize>,
    @SerialName("text") val text: String? = null,
    @SerialName("text_entities") val textEntities: List<MessageEntity>? = null,
    @SerialName("animation") val animation: Animation? = null
)

@Serializable
public data class GameHighScore(
    @SerialName("position") val position: Int,
    @SerialName("user") val user: User,
    @SerialName("score") val score: Int
)

@Serializable
public class CallbackGame