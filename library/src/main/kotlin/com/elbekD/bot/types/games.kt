package com.elbekD.bot.types

public data class Game(
    val title: String,
    val description: String,
    val photo: List<PhotoSize>,
    val text: String,
    val text_entities: List<MessageEntity>,
    val animation: Animation
)

public data class GameHighScore(val position: Int, val user: User, val score: Int)

public open class CallbackGame