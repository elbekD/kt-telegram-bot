package com.github.elbekD.bot.util

import com.github.elbekD.bot.types.Message
import com.github.elbekD.bot.types.Update
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

private val COMMAND_REGEX = "^/([\\w]{1,32}|$ANY_CALLBACK_TRIGGER)$".toRegex()

fun String.isCommand() = matches(COMMAND_REGEX)

fun Update.isCommand(): Boolean {
    return isMessage() && message!!.isCommand()
}

fun Update.isMessage(): Boolean {
    return message != null
}

fun Update.isEditedMessage(): Boolean {
    return message != null && message.isEditedMessage()
}

fun Update.isChannelPost(): Boolean {
    return channel_post != null
}

fun Update.isEditedChannelPost(): Boolean {
    return isChannelPost() && channel_post!!.edit_date != null
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

fun Message.isCommand(): Boolean {
    return text != null && text.split(' ')[0].isCommand()
}

fun Message.isEditedMessage(): Boolean {
    return text != null && edit_date != null
}
