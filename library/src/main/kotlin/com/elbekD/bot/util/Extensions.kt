package com.elbekD.bot.util

import com.elbekD.bot.types.Message
import com.elbekD.bot.types.Update

private const val COMMAND_FORMAT = "^/([\\w]{1,32}(@%s)?)$"

public fun String.isCommand(username: String): Boolean = matches(COMMAND_FORMAT.format(username).toRegex())

public fun Update.isCommand(username: String): Boolean {
    return isMessage() && message!!.isCommand(username)
}

public fun Update.isMessage(): Boolean {
    return message != null
}

public fun Update.isEditedMessage(): Boolean {
    return edited_message != null
}

public fun Update.isChannelPost(): Boolean {
    return channel_post != null
}

public fun Update.isEditedChannelPost(): Boolean {
    return edited_channel_post != null
}

public fun Update.isInlineQuery(): Boolean {
    return inline_query != null
}

public fun Update.isChosenInlineQuery(): Boolean {
    return chosen_inline_result != null
}

public fun Update.isCallbackQuery(): Boolean {
    return callback_query != null
}

public fun Update.isShippingQuery(): Boolean {
    return shipping_query != null
}

public fun Update.isPreCheckoutQuery(): Boolean {
    return pre_checkout_query != null
}

public fun Message.isCommand(username: String): Boolean {
    return text != null && text.split(' ')[0].isCommand(username)
}