package com.elbekd.bot.internal

import com.elbekd.bot.types.*

internal object UpdateResponseMapper {
    fun map(response: UpdateResponse): Update? {
        val id = response.updateId
        return when {
            response.message != null -> UpdateMessage(
                updateId = id,
                message = response.message
            )

            response.editedMessage != null -> UpdateEditedMessage(
                updateId = id,
                editedMessage = response.editedMessage
            )

            response.channelPost != null -> UpdateChannelPost(
                updateId = id,
                channelPost = response.channelPost
            )

            response.editedChannelPost != null -> UpdateEditedChannelPost(
                updateId = id,
                editedChannelPost = response.editedChannelPost
            )

            response.inlineQuery != null -> UpdateInlineQuery(
                updateId = id,
                inlineQuery = response.inlineQuery
            )

            response.chosenInlineResult != null -> UpdateChosenInlineResult(
                updateId = id,
                chosenInlineResult = response.chosenInlineResult
            )

            response.callbackQuery != null -> UpdateCallbackQuery(
                updateId = id,
                callbackQuery = response.callbackQuery
            )

            response.shippingQuery != null -> UpdateShippingQuery(
                updateId = id,
                shippingQuery = response.shippingQuery
            )

            response.preCheckoutQuery != null -> UpdatePreCheckoutQuery(
                updateId = id,
                preCheckoutQuery = response.preCheckoutQuery
            )

            response.poll != null -> UpdatePoll(
                updateId = id,
                poll = response.poll
            )

            response.pollAnswer != null -> UpdatePollAnswer(
                updateId = id,
                pollAnswer = response.pollAnswer
            )

            response.myChatMember != null -> UpdateMyChatMember(
                updateId = id,
                myChatMember = response.myChatMember
            )

            response.chatMember != null -> UpdateChatMember(
                updateId = id,
                chatMember = response.chatMember
            )

            response.chatJoinRequest != null -> UpdateChatJoinRequest(
                updateId = id,
                chatJoinRequest = response.chatJoinRequest
            )

            else -> null
        }
    }

    fun map(updates: List<UpdateResponse>): List<Update> {
        return updates.mapNotNull(::map)
    }
}