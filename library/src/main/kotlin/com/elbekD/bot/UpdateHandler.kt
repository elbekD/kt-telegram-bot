package com.elbekD.bot

import com.elbekD.bot.feature.chain.ChainController
import com.elbekD.bot.types.CallbackQuery
import com.elbekD.bot.types.ChosenInlineResult
import com.elbekD.bot.types.InlineQuery
import com.elbekD.bot.types.Message
import com.elbekD.bot.types.PreCheckoutQuery
import com.elbekD.bot.types.ShippingQuery
import com.elbekD.bot.types.Update
import com.elbekD.bot.util.AllowedUpdate
import com.elbekD.bot.util.isCallbackQuery
import com.elbekD.bot.util.isChannelPost
import com.elbekD.bot.util.isChosenInlineQuery
import com.elbekD.bot.util.isCommand
import com.elbekD.bot.util.isEditedChannelPost
import com.elbekD.bot.util.isEditedMessage
import com.elbekD.bot.util.isInlineQuery
import com.elbekD.bot.util.isMessage
import com.elbekD.bot.util.isPreCheckoutQuery
import com.elbekD.bot.util.isShippingQuery
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

internal class UpdateHandler(private val username: String) {
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

    private val onCommand = mutableMapOf<String, suspend (Message, String?) -> Unit>()
    private val onInlineQuery = mutableMapOf<String, suspend (InlineQuery) -> Unit>()
    private val onCallbackQueryData = mutableMapOf<String, suspend (CallbackQuery) -> Unit>()

    private companion object {
        private fun extractCommandAndArgument(text: String): Pair<String, String?> {
            val cmd = text.substringBefore(' ').substringBefore('@')
            val arg = text.substringAfter(' ', "")
            return Pair(cmd, if (arg.isEmpty()) null else arg)
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

    internal fun onCommand(command: String, action: suspend (Message, String?) -> Unit) {
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
        when {
            update.isMessage() -> {
                when {
                    ChainController.canHandle(update.message!!) -> {
                        ChainController.handle(update.message)
                    }

                    update.isCommand(username) -> {
                        val (cmd, args) = extractCommandAndArgument(update.message.text!!)
                        val trigger = if (onCommand.containsKey(cmd)) {
                            cmd
                        } else {
                            onCommand.keys.firstOrNull { cmd.matches(it.toRegex()) }
                        }
                        trigger?.let { GlobalScope.launch { onCommand[it]?.invoke(update.message, args) } }
                    }

                    else -> {
                        onAnyMessage?.let { GlobalScope.launch { it.invoke(update.message) } }
                    }
                }
            }

            update.isEditedMessage() -> {
                GlobalScope.launch { onAnyEditedMessage?.invoke(update.edited_message!!) }
            }

            update.isChannelPost() -> {
                GlobalScope.launch { onAnyChannelPost?.invoke(update.channel_post!!) }
            }

            update.isEditedChannelPost() -> {
                GlobalScope.launch { onAnyEditedChannelPost?.invoke(update.edited_channel_post!!) }
            }

            update.isInlineQuery() -> {
                val query = update.inline_query!!.query
                val trigger = if (onInlineQuery.containsKey(query)) {
                    query
                } else {
                    onInlineQuery.keys.firstOrNull { query.matches(it.toRegex()) }
                }
                if (trigger != null) {
                    GlobalScope.launch { onInlineQuery[trigger]?.invoke(update.inline_query) }
                } else {
                    onAnyInlineQuery?.let { GlobalScope.launch { it.invoke(update.inline_query) } }
                }
            }

            update.isChosenInlineQuery() -> {
                GlobalScope.launch { onAnyChosenInlineQuery?.invoke(update.chosen_inline_result!!) }
            }

            update.isCallbackQuery() -> {
                val data = update.callback_query!!.data
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
                    GlobalScope.launch { onCallbackQueryData[trigger]?.invoke(update.callback_query) }
                } else {
                    onAnyCallbackQuery?.let { GlobalScope.launch { it.invoke(update.callback_query) } }
                }
            }

            update.isShippingQuery() -> {
                GlobalScope.launch { onAnyShippingQuery?.invoke(update.shipping_query!!) }
            }

            update.isPreCheckoutQuery() -> {
                GlobalScope.launch { onAnyPreCheckoutQuery?.invoke(update.pre_checkout_query!!) }
            }
        }
    }
}
