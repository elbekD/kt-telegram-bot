package bot

import bot.types.KeyboardButton
import bot.types.Message
import bot.types.ReplyKeyboard
import http.TelegramApi

interface Bot : TelegramApi {
    fun start()
    fun stop()
    fun on(trigger: String, action: (Message) -> Unit)
    fun keyboard(buttons: Array<Array<KeyboardButton>>, resize: Boolean? = null, once: Boolean? = null, selective:
    Boolean? = null): ReplyKeyboard

    fun button(text: String, contact: Boolean? = null, location: Boolean? = null): KeyboardButton
    fun removeKeyboard(remove: Boolean, selective: Boolean? = null): ReplyKeyboard
}