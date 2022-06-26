package com.elbekd.bot.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class UpdateResponse(
    @SerialName("update_id") val updateId: Int,
    @SerialName("message") val message: Message? = null,
    @SerialName("edited_message") val editedMessage: Message? = null,
    @SerialName("channel_post") val channelPost: Message? = null,
    @SerialName("edited_channel_post") val editedChannelPost: Message? = null,
    @SerialName("inline_query") val inlineQuery: InlineQuery? = null,
    @SerialName("chosen_inline_result") val chosenInlineResult: ChosenInlineResult? = null,
    @SerialName("callback_query") val callbackQuery: CallbackQuery? = null,
    @SerialName("shipping_query") val shippingQuery: ShippingQuery? = null,
    @SerialName("pre_checkout_query") val preCheckoutQuery: PreCheckoutQuery? = null,
    @SerialName("poll") val poll: Poll? = null,
    @SerialName("poll_answer") val pollAnswer: PollAnswer? = null,
    @SerialName("my_chat_member") val myChatMember: ChatMemberUpdated? = null,
    @SerialName("chat_member") val chatMember: ChatMemberUpdated? = null,
    @SerialName("chat_join_request") val chatJoinRequest: ChatJoinRequest? = null,
)

public sealed class Update {
    public abstract val updateId: Int
}

public data class UpdateMessage(
    override val updateId: Int,
    val message: Message
) : Update()

public data class UpdateEditedMessage(
    override val updateId: Int,
    val editedMessage: Message
) : Update()

public data class UpdateChannelPost(
    override val updateId: Int,
    val channelPost: Message
) : Update()

public data class UpdateEditedChannelPost(
    override val updateId: Int,
    val editedChannelPost: Message
) : Update()

public data class UpdateInlineQuery(
    override val updateId: Int,
    val inlineQuery: InlineQuery
) : Update()

public data class UpdateChosenInlineResult(
    override val updateId: Int,
    val chosenInlineResult: ChosenInlineResult
) : Update()

public data class UpdateCallbackQuery(
    override val updateId: Int,
    val callbackQuery: CallbackQuery
) : Update()

public data class UpdateShippingQuery(
    override val updateId: Int,
    val shippingQuery: ShippingQuery
) : Update()

public data class UpdatePreCheckoutQuery(
    override val updateId: Int,
    val preCheckoutQuery: PreCheckoutQuery
) : Update()

public data class UpdatePoll(
    override val updateId: Int,
    val poll: Poll
) : Update()

public data class UpdatePollAnswer(
    override val updateId: Int,
    val pollAnswer: PollAnswer
) : Update()

public data class UpdateMyChatMember(
    override val updateId: Int,
    val myChatMember: ChatMemberUpdated
) : Update()

public data class UpdateChatMember(
    override val updateId: Int,
    val chatMember: ChatMemberUpdated
) : Update()

public data class UpdateChatJoinRequest(
    override val updateId: Int,
    val chatJoinRequest: ChatJoinRequest
) : Update()