package com.elbekd.bot.model

import com.elbekd.bot.model.internal.ChatIdSerializer
import kotlinx.serialization.Serializable

@Serializable(with = ChatIdSerializer::class)
public sealed class ChatId {
    abstract override fun toString(): String

    public class IntegerId(public val id: Long) : ChatId() {
        override fun toString(): String = id.toString()
    }

    public class StringId(public val id: String) : ChatId() {
        override fun toString(): String = id
    }
}

public fun String.toChatId(): ChatId {
    return ChatId.StringId(this)
}

public fun Long.toChatId(): ChatId {
    return ChatId.IntegerId(this)
}