package com.elbekd.bot.internal

import com.elbekd.bot.Bot
import com.elbekd.bot.feature.chain.ChainController
import com.elbekd.bot.types.CallbackQuery
import com.elbekd.bot.types.ChatJoinRequest
import com.elbekd.bot.types.ChatMemberUpdated
import com.elbekd.bot.types.ChosenInlineResult
import com.elbekd.bot.types.InlineQuery
import com.elbekd.bot.types.Message
import com.elbekd.bot.types.Poll
import com.elbekd.bot.types.PollAnswer
import com.elbekd.bot.types.PreCheckoutQuery
import com.elbekd.bot.types.ShippingQuery
import com.elbekd.bot.types.Update
import com.elbekd.bot.types.UpdateCallbackQuery
import com.elbekd.bot.types.UpdateChannelPost
import com.elbekd.bot.types.UpdateChatJoinRequest
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
import com.elbekd.bot.types.UpdateResponse
import com.elbekd.bot.types.UpdateShippingQuery
import com.elbekd.bot.util.AllowedUpdate
import com.elbekd.bot.util.isCommand
import okhttp3.internal.notifyAll

internal class UpdateHandler(private val username: String?) {
    private var onMessageUpdate: (suspend (Message) -> Unit)? = null
    private var onEditedMessageUpdate: (suspend (Message) -> Unit)? = null
    private var onChannelPostUpdate: (suspend (Message) -> Unit)? = null
    private var onEditedChannelPostUpdate: (suspend (Message) -> Unit)? = null
    private var onInlineQueryUpdate: (suspend (InlineQuery) -> Unit)? = null
    private var onChosenInlineQueryUpdate: (suspend (ChosenInlineResult) -> Unit)? = null
    private var onCallbackQueryUpdate: (suspend (CallbackQuery) -> Unit)? = null
    private var onShippingQueryUpdate: (suspend (ShippingQuery) -> Unit)? = null
    private var onPreCheckoutQueryUpdate: (suspend (PreCheckoutQuery) -> Unit)? = null
    private var onPollUpdate: (suspend (Poll) -> Unit)? = null
    private var onPollAnswerUpdate: (suspend (PollAnswer) -> Unit)? = null
    private var onMyChatMemberUpdate: (suspend (ChatMemberUpdated) -> Unit)? = null
    private var onChatMemberUpdate: (suspend (ChatMemberUpdated) -> Unit)? = null
    private var onChatJoinRequestUpdate: (suspend (ChatJoinRequest) -> Unit)? = null
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
            AllowedUpdate.Message -> onMessageUpdate = action as? (suspend (Message) -> Unit)
            AllowedUpdate.EditedMessage -> onEditedMessageUpdate = action as? (suspend (Message) -> Unit)
            AllowedUpdate.ChannelPost -> onChannelPostUpdate = action as? (suspend (Message) -> Unit)
            AllowedUpdate.EditedChannelPost -> onEditedChannelPostUpdate = action as? (suspend (Message) -> Unit)
            AllowedUpdate.InlineQuery -> onInlineQueryUpdate = action as? (suspend (InlineQuery) -> Unit)
            AllowedUpdate.ChosenInlineQuery -> onChosenInlineQueryUpdate =
                action as? (suspend (ChosenInlineResult) -> Unit)

            AllowedUpdate.CallbackQuery -> onCallbackQueryUpdate = action as? (suspend (CallbackQuery) -> Unit)
            AllowedUpdate.ShippingQuery -> onShippingQueryUpdate = action as? (suspend (ShippingQuery) -> Unit)
            AllowedUpdate.PreCheckoutQuery -> onPreCheckoutQueryUpdate = action as? (suspend (PreCheckoutQuery) -> Unit)
            AllowedUpdate.Poll -> onPollUpdate = action as? (suspend (Poll) -> Unit)
            AllowedUpdate.PollAnswer -> onPollAnswerUpdate = action as? (suspend (PollAnswer) -> Unit)
            AllowedUpdate.MyChatMember -> onMyChatMemberUpdate = action as? (suspend (ChatMemberUpdated) -> Unit)
            AllowedUpdate.ChatMember -> onChatMemberUpdate = action as? (suspend (ChatMemberUpdated) -> Unit)
            AllowedUpdate.ChatJoinRequest -> onChatJoinRequestUpdate = action as? (suspend (ChatJoinRequest) -> Unit)
        }
    }

    internal fun onCommand(command: String, action: (suspend (Pair<Message, String?>) -> Unit)?) {
        if (action == null) {
            onCommand.remove(command)
        } else {
            onCommand[command] = action
        }
    }

    internal fun onCallbackQuery(data: String, action: (suspend (CallbackQuery) -> Unit)?) {
        if (action == null) {
            onCallbackQueryData.remove(data)
        } else {
            onCallbackQueryData[data] = action
        }
    }

    internal fun onInlineQuery(query: String, action: (suspend (InlineQuery) -> Unit)?) {
        if (action == null) {
            onInlineQuery.remove(query)
        } else {
            onInlineQuery[query] = action
        }
    }

    internal fun onAnyUpdate(action: (suspend (Update) -> Unit)?) {
        onAnyUpdate = action
    }

    internal suspend fun handle(update: Update) {
        onAnyUpdate?.invoke(update)

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
                    onCallbackQueryData[trigger]?.invoke(update.callbackQuery)
                } else {
                    onCallbackQueryUpdate?.invoke(update.callbackQuery)
                }
            }

            is UpdateChannelPost -> onChannelPostUpdate?.invoke(update.channelPost)

            is UpdateChatMember -> onChatMemberUpdate?.invoke(update.chatMember)

            is UpdateChosenInlineResult -> onChosenInlineQueryUpdate?.invoke(update.chosenInlineResult)

            is UpdateEditedChannelPost -> onEditedChannelPostUpdate?.invoke(update.editedChannelPost)

            is UpdateEditedMessage -> onEditedMessageUpdate?.invoke(update.editedMessage)

            is UpdateInlineQuery -> {
                val query = update.inlineQuery.query
                val trigger = if (onInlineQuery.containsKey(query)) {
                    query
                } else {
                    onInlineQuery.keys.firstOrNull { query.matches(it.toRegex()) }
                }
                if (trigger != null) {
                    onInlineQuery[trigger]?.invoke(update.inlineQuery)
                } else {
                    onInlineQueryUpdate?.invoke(update.inlineQuery)
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
                        val changedMessage = update.message
                        changedMessage.text = cmd
                        trigger?.let { onCommand[it]?.invoke(changedMessage to args) }
                    }

                    else -> onMessageUpdate?.invoke(update.message)
                }
            }

            is UpdateMyChatMember -> onMyChatMemberUpdate?.invoke(update.myChatMember)

            is UpdatePoll -> onPollUpdate?.invoke(update.poll)

            is UpdatePollAnswer -> onPollAnswerUpdate?.invoke(update.pollAnswer)

            is UpdatePreCheckoutQuery -> onPreCheckoutQueryUpdate?.invoke(update.preCheckoutQuery)

            is UpdateShippingQuery -> onShippingQueryUpdate?.invoke(update.shippingQuery)

            is UpdateChatJoinRequest -> onChatJoinRequestUpdate?.invoke(update.chatJoinRequest)
        }
    }

    internal suspend fun handle(updateResponse: UpdateResponse) {
        val update = UpdateResponseMapper.map(updateResponse)

        @Suppress("FoldInitializerAndIfToElvis")
        if (update == null) {
            // TODO: log
            System.err.println("Could not deserialize update response $updateResponse")
            return
        }

        handle(update)
    }
}
