package com.elbekd.bot.internal

import com.elbekd.bot.feature.chain.ChainController
import com.elbekd.bot.types.CallbackQuery
import com.elbekd.bot.types.ChosenInlineResult
import com.elbekd.bot.types.InlineQuery
import com.elbekd.bot.types.Message
import com.elbekd.bot.types.PreCheckoutQuery
import com.elbekd.bot.types.ShippingQuery
import com.elbekd.bot.types.Update
import com.elbekd.bot.types.UpdateCallbackQuery
import com.elbekd.bot.types.UpdateChannelPost
import com.elbekd.bot.types.UpdateChatMember
import com.elbekd.bot.types.UpdateChosenInlineResult
import com.elbekd.bot.types.UpdateEditedChannelPost
import com.elbekd.bot.types.UpdateEditedMessage
import com.elbekd.bot.types.UpdateInlineQuery
import com.elbekd.bot.types.UpdateMessage
import com.elbekd.bot.types.UpdateMyChatMember
import com.elbekd.bot.types.UpdatePoll
import com.elbekd.bot.types.UpdatePollAnswer
import com.elbekd.bot.types.UpdatePreCheckoutQuery
import com.elbekd.bot.types.UpdateShippingQuery
import com.elbekd.bot.util.AllowedUpdate
import com.elbekd.bot.util.isCommand
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

internal class UpdateHandler(private val username: String?) {
    private var onAnyMessage: (suspend (Message) -> Unit)? = null
    private var onAnyEditedMessage: (suspend (Message) -> Unit)? = null
    private var onAnyChannelPost: (suspend (Message) -> Unit)? = null
    private var onAnyEditedChannelPost: (suspend (Message) -> Unit)? = null
    private var onAnyInlineQuery: (suspend (InlineQuery) -> Unit)? = null
    private var onAnyChosenInlineQuery: (suspend (ChosenInlineResult) -> Unit)? = null
    private var onAnyCallbackQuery: (suspend (CallbackQuery) -> Unit)? = null
    private var onAnyShippingQuery: (suspend (ShippingQuery) -> Unit)? = null
    private var onAnyPreCheckoutQuery: (suspend (PreCheckoutQuery) -> Unit)? = null
    private var onAnyUpdate: (suspend (Update) -> Unit)? = null

    private val onCommand = mutableMapOf<String, suspend (Pair<Message, String?>) -> Unit>()
    private val onInlineQuery = mutableMapOf<String, suspend (InlineQuery) -> Unit>()
    private val onCallbackQueryData = mutableMapOf<String, suspend (CallbackQuery) -> Unit>()

    private companion object {
        private fun extractCommandAndArgument(text: String): Pair<String, String?> {
            val cmd = text.substringBefore(' ').substringBefore('@')
            val arg = text.substringAfter(' ', "")
            return Pair(cmd, arg.ifEmpty { null })
        }
    }

    @Suppress("UNCHECKED_CAST")
    internal fun <T> on(type: AllowedUpdate, action: (suspend (T) -> Unit)?) {
        when (type) {
            AllowedUpdate.Message -> onAnyMessage = action as? (suspend (Message) -> Unit)
            AllowedUpdate.EditedMessage -> onAnyEditedMessage = action as? (suspend (Message) -> Unit)
            AllowedUpdate.ChannelPost -> onAnyChannelPost = action as? (suspend (Message) -> Unit)
            AllowedUpdate.EditedChannelPost -> onAnyEditedChannelPost = action as? (suspend (Message) -> Unit)
            AllowedUpdate.InlineQuery -> onAnyInlineQuery = action as? (suspend (InlineQuery) -> Unit)
            AllowedUpdate.ChosenInlineQuery -> onAnyChosenInlineQuery =
                action as? (suspend (ChosenInlineResult) -> Unit)
            AllowedUpdate.CallbackQuery -> onAnyCallbackQuery = action as? (suspend (CallbackQuery) -> Unit)
            AllowedUpdate.ShippingQuery -> onAnyShippingQuery = action as? (suspend (ShippingQuery) -> Unit)
            AllowedUpdate.PreCheckoutQuery -> onAnyPreCheckoutQuery = action as? (suspend (PreCheckoutQuery) -> Unit)
        }
    }

    internal fun onCommand(command: String, action: suspend (Pair<Message, String?>) -> Unit) {
        onCommand[command] = action
    }

    internal fun onCallbackQuery(data: String, action: suspend (CallbackQuery) -> Unit) {
        onCallbackQueryData[data] = action
    }

    internal fun onInlineQuery(query: String, action: suspend (InlineQuery) -> Unit) {
        onInlineQuery[query] = action
    }

    internal fun onAnyUpdate(action: suspend (Update) -> Unit) {
        onAnyUpdate = action
    }

    internal fun handle(update: Update) {
        GlobalScope.launch { onAnyUpdate?.invoke(update) }

        when (update) {
            is UpdateCallbackQuery -> {
                val data = update.callbackQuery.data
                val trigger = if (data != null) {
                    if (onCallbackQueryData.containsKey(data)) {
                        data
                    } else {
                        onCallbackQueryData.keys.firstOrNull { data.matches(it.toRegex()) }
                    }
                } else {
                    null
                }
                if (trigger != null) {
                    GlobalScope.launch { onCallbackQueryData[trigger]?.invoke(update.callbackQuery) }
                } else {
                    onAnyCallbackQuery?.let { GlobalScope.launch { it.invoke(update.callbackQuery) } }
                }
            }

            is UpdateChannelPost -> GlobalScope.launch { onAnyChannelPost?.invoke(update.channelPost) }

            is UpdateChatMember -> TODO()

            is UpdateChosenInlineResult -> GlobalScope.launch { onAnyChosenInlineQuery?.invoke(update.chosenInlineResult) }

            is UpdateEditedChannelPost -> GlobalScope.launch { onAnyEditedChannelPost?.invoke(update.editedChannelPost) }

            is UpdateEditedMessage -> GlobalScope.launch { onAnyEditedMessage?.invoke(update.editedMessage) }

            is UpdateInlineQuery -> {
                val query = update.inlineQuery.query
                val trigger = if (onInlineQuery.containsKey(query)) {
                    query
                } else {
                    onInlineQuery.keys.firstOrNull { query.matches(it.toRegex()) }
                }
                if (trigger != null) {
                    GlobalScope.launch { onInlineQuery[trigger]?.invoke(update.inlineQuery) }
                } else {
                    onAnyInlineQuery?.let { GlobalScope.launch { it.invoke(update.inlineQuery) } }
                }
            }

            is UpdateMessage -> {
                when {
                    ChainController.canHandle(update.message) -> {
                        ChainController.handle(update.message)
                    }

                    update.isCommand(username) -> {
                        val (cmd, args) = extractCommandAndArgument(requireNotNull(update.message.text))
                        val trigger = if (onCommand.containsKey(cmd)) cmd
                        else onCommand.keys.firstOrNull { cmd.matches(it.toRegex()) }
                        trigger?.let { GlobalScope.launch { onCommand[it]?.invoke(update.message to args) } }
                    }

                    else -> onAnyMessage?.let { GlobalScope.launch { it.invoke(update.message) } }
                }
            }

            is UpdateMyChatMember -> TODO()

            is UpdatePoll -> TODO()

            is UpdatePollAnswer -> TODO()

            is UpdatePreCheckoutQuery -> GlobalScope.launch { onAnyPreCheckoutQuery?.invoke(update.preCheckoutQuery) }

            is UpdateShippingQuery -> GlobalScope.launch { onAnyShippingQuery?.invoke(update.shippingQuery) }
        }
    }
}
