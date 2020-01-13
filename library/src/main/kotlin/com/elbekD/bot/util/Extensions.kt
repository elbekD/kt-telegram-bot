package com.elbekD.bot.util

import com.elbekD.bot.types.Message
import com.elbekD.bot.types.Update

private const val COMMAND_FORMAT = "^/([\\w]{1,32}(@%s)?)$"

fun String.isCommand(username: String) = matches(COMMAND_FORMAT.format(username).toRegex())

fun Update.isCommand(username: String): Boolean {
    return isMessage() && message!!.isCommand(username)
}

fun Update.isMessage(): Boolean {
    return message != null
}

fun Update.isEditedMessage(): Boolean {
    return edited_message != null
}

fun Update.isChannelPost(): Boolean {
    return channel_post != null
}

fun Update.isEditedChannelPost(): Boolean {
    return edited_channel_post != null
}

fun Update.isInlineQuery(): Boolean {
    return inline_query != null
}

fun Update.isChosenInlineQuery(): Boolean {
    return chosen_inline_result != null
}

fun Update.isCallbackQuery(): Boolean {
    return callback_query != null
}

fun Update.isShippingQuery(): Boolean {
    return shipping_query != null
}

fun Update.isPreCheckoutQuery(): Boolean {
    return pre_checkout_query != null
}

fun Message.isCommand(username: String): Boolean {
    return text != null && text.split(' ')[0].isCommand(username)
}
