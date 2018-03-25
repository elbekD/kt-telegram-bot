package http

import bot.types.Message
import bot.types.ReplyKeyboard
import bot.types.User
import java.util.concurrent.CompletableFuture

interface TelegramApi {
    fun getMe(): CompletableFuture<User>
    fun sendMessage(id: Any, text: String, parseMode: String? = null, preview: Boolean? = null, notification: Boolean? = null,
                    replyTo: Int? = null, markup: ReplyKeyboard? = null): CompletableFuture<Message>
}