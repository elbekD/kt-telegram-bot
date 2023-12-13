package com.elbekd.bot.util

import com.elbekd.bot.types.Message
import com.elbekd.bot.types.Update
import com.elbekd.bot.types.UpdateMessage

private const val COMMAND_FORMAT = "^/([\\w]{1,32}(@%s)?)$"

public fun String.isCommand(username: String?): Boolean = matches(COMMAND_FORMAT.format(username).toRegex())

public fun Update.isCommand(username: String?): Boolean {
    return this is UpdateMessage && message.isCommand(username)
}

public fun Message.isCommand(username: String?): Boolean {
    return text != null && text!!.split(' ')[0].isCommand(username)
}