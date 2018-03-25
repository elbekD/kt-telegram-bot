package bot

import bot.types.*
import http.TelegramClient

abstract class TelegramBot protected constructor(tk: String) : Bot {
    private val commands = mutableMapOf<String, (Message) -> Unit>()
    private val client = TelegramClient(tk)

    companion object {
        fun createPolling(token: String, options: PollingOptions.() -> Unit = { PollingOptions() }): Bot {
            validateToken(token)
            return LongPollingBot(token, PollingOptions().apply(options))
        }

        fun createWebhook(token: String, url: String, options: WebhookOptions.() -> Unit = {}): Bot {
            validateToken(token)
            return WebhookBot(token, WebhookOptions(url).apply(options))
        }

        private fun validateToken(token: String) {
            if (token.isBlank())
                throw IllegalArgumentException("Token cannot be empty")
        }
    }

    private fun Message.isCommand() = text != null && text.isCommand()
    private fun String.isCommand() = isNotBlank() && split(' ').size == 1 && startsWith("/")

    protected fun onUpdate(msg: Message) {
        if (msg.isCommand()) {
            commands[msg.text]?.invoke(msg)
        }
    }

    override fun on(trigger: String, action: (Message) -> Unit) {
        when {
            trigger.isCommand() -> commands[trigger] = action
            else -> throw IllegalArgumentException("$trigger is not a command")
        }
    }

    override fun keyboard(buttons: Array<Array<KeyboardButton>>, resize: Boolean?, once: Boolean?, selective: Boolean?): ReplyKeyboardMarkup {
        return ReplyKeyboardMarkup(buttons, resize, once, selective)
    }

    override fun button(text: String, contact: Boolean?, location: Boolean?): KeyboardButton {
        if (contact != null && contact && location != null && location)
            throw IllegalArgumentException("both contact and location cannot be set")

        return KeyboardButton(text, contact, location)
    }

    override fun removeKeyboard(remove: Boolean, selective: Boolean?): ReplyKeyboard {
        return ReplyKeyboardRemove(remove, selective)
    }

    override fun getMe() = client.getMe()
    override fun sendMessage(id: Any, text: String, parseMode: String?,
                             preview: Boolean?, notification: Boolean?,
                             replyTo: Int?, markup: ReplyKeyboard?) = client.sendMessage(id, text, parseMode, preview,
            notification, replyTo, markup)
}