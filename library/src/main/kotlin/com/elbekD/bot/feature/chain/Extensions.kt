@file:Suppress("unused")

package com.elbekD.bot.feature.chain

import com.elbekD.bot.Bot
import com.elbekD.bot.types.Message

fun Bot.chain(trigger: String, action: (Message) -> Unit): ChainBuilder {
    return ChainBuilder.with(trigger, action)
}

fun Bot.chain(label: String, on: (Message) -> Boolean, action: (Message) -> Unit): ChainBuilder {
    return ChainBuilder.with(label, on, action)
}

fun Bot.jumpTo(label: String, message: Message) {
    ChainController.jumpTo(label, message)
}

fun Bot.jumpToAndFire(label: String, message: Message) {
    ChainController.jumpToAndFire(label, message)
}

fun Bot.terminateChain(chatId: Long) {
    ChainController.terminateChain(chatId)
}
