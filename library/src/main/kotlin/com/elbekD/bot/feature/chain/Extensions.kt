@file:Suppress("unused")

package com.elbekD.bot.feature.chain

import com.elbekD.bot.Bot
import com.elbekD.bot.types.Message

/**
 * Helper method for creating a [Chain] that is triggered by
 * text message.
 * @param trigger a text from [Message] that triggers this [Chain]
 * @param action an action that is called when [Chain] is triggered
 * @return [ChainBuilder]
 */
fun Bot.chain(trigger: String, action: (Message) -> Unit): ChainBuilder {
    return ChainBuilder.with(trigger, action)
}

/**
 * Helper method for creating a [Chain] that triggered by
 * specified predicate.
 * @param label used just as a name of the chain
 * @param predicate a predicate that fires the chain if returns `true`
 *                  for incoming [Message]. If there are several predicates
 *                  with the same condition only the first one is called
 * @param action an action that will be called when the chain is fired
 * @return [ChainBuilder]
 */
fun Bot.chain(label: String, predicate: (Message) -> Boolean, action: (Message) -> Unit): ChainBuilder {
    return ChainBuilder.with(label, predicate, action)
}

/**
 * Jumps to the specified step of the chain that will be fired
 * for the next incoming [message].
 * @param label label of the step to jump to
 * @param message incoming [Message]
 */
fun Bot.jumpTo(label: String, message: Message) {
    ChainController.jumpTo(label, message)
}

/**
 * Jumps to the specified step of the chain will be fired
 * for the current incoming [message]
 * @param label label of the step to jump to
 * @param message incoming [Message]
 */
fun Bot.jumpToAndFire(label: String, message: Message) {
    ChainController.jumpToAndFire(label, message)
}

/**
 * Helper method that terminates chain on the current step.
 * @param chatId id of the chat to stop the chain for
 */
fun Bot.terminateChain(chatId: Long) {
    ChainController.terminateChain(chatId)
}
