package com.elbekd.bot.model.internal

import com.elbekd.bot.model.ChatId
import com.elbekd.bot.types.ChatAdministratorRights
import com.elbekd.bot.types.MenuButton
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SetChatMenuButton(
    @SerialName("chat_id") val chatId: Long?,
    @SerialName("menu_button") val menuButton: MenuButton?,
)

@Serializable
internal data class GetChatMenuButton(
    @SerialName("chat_id") val chatId: Long?,
)

@Serializable
internal data class SetMyDefaultAdministratorRights(
    @SerialName("rights") val rights: ChatAdministratorRights?,
    @SerialName("for_channels") val forChannels: Boolean?,
)

@Serializable
internal data class GetMyDefaultAdministratorRights(
    @SerialName("for_channels") val forChannels: Boolean?,
)

@Serializable
internal data class CreateForumTopic(
    @SerialName("chat_id") val chatId: ChatId,
    @SerialName("name") val name: String,
    @SerialName("icon_color") val iconColor: Int?,
    @SerialName("icon_custom_emoji_id") val iconCustomEmojiId: String?,
)

@Serializable
internal data class EditForumTopic(
    @SerialName("chat_id") val chatId: ChatId,
    @SerialName("message_thread_id") val messageThreadId: Long,
    @SerialName("name") val name: String?,
    @SerialName("icon_custom_emoji_id") val iconCustomEmojiId: String?,
)

@Serializable
internal data class CloseForumTopic(
    @SerialName("chat_id") val chatId: ChatId,
    @SerialName("message_thread_id") val messageThreadId: Long,
)

@Serializable
internal data class ReopenForumTopic(
    @SerialName("chat_id") val chatId: ChatId,
    @SerialName("message_thread_id") val messageThreadId: Long,
)

@Serializable
internal data class DeleteForumTopic(
    @SerialName("chat_id") val chatId: ChatId,
    @SerialName("message_thread_id") val messageThreadId: Long,
)

@Serializable
internal data class UnpinAllForumTopicMessages(
    @SerialName("chat_id") val chatId: ChatId,
    @SerialName("message_thread_id") val messageThreadId: Long,
)

@Serializable
internal data class EditGeneralForumTopic(
    @SerialName("chat_id") val chatId: ChatId,
    @SerialName("name") val name: String,
)

@Serializable
internal data class CloseGeneralForumTopic(
    @SerialName("chat_id") val chatId: ChatId,
)

@Serializable
internal data class ReopenGeneralForumTopic(
    @SerialName("chat_id") val chatId: ChatId,
)

@Serializable
internal data class HideGeneralForumTopic(
    @SerialName("chat_id") val chatId: ChatId,
)

@Serializable
internal data class UnhideGeneralForumTopic(
    @SerialName("chat_id") val chatId: ChatId,
)