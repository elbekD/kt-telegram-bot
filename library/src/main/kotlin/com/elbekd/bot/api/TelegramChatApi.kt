package com.elbekd.bot.api

import com.elbekd.bot.model.ChatId
import com.elbekd.bot.types.ChatAdministratorRights
import com.elbekd.bot.types.ForumTopic
import com.elbekd.bot.types.MenuButton
import com.elbekd.bot.types.Sticker

public interface TelegramChatApi {
    public suspend fun setChatMenuButton(
        chatId: Long? = null,
        menuButton: MenuButton? = null
    ): Boolean

    public suspend fun getChatMenuButton(chatId: Long? = null): MenuButton

    public suspend fun setMyDefaultAdministratorRights(
        rights: ChatAdministratorRights? = null,
        forChannels: Boolean? = null,
    ): Boolean

    public suspend fun getMyDefaultAdministratorRights(
        forChannels: Boolean? = null,
    ): ChatAdministratorRights

    public suspend fun getForumTopicIconStickers(): List<Sticker>

    public suspend fun createForumTopic(
        chatId: ChatId,
        name: String,
        iconColor: Int? = null,
        iconCustomEmojiId: String? = null,
    ): ForumTopic

    public suspend fun editForumTopic(
        chatId: ChatId,
        messageThreadId: Long,
        name: String? = null,
        iconCustomEmojiId: String? = null,
    ): Boolean

    public suspend fun closeForumTopic(
        chatId: ChatId,
        messageThreadId: Long,
    ): Boolean

    public suspend fun reopenForumTopic(
        chatId: ChatId,
        messageThreadId: Long,
    ): Boolean

    public suspend fun deleteForumTopic(
        chatId: ChatId,
        messageThreadId: Long,
    ): Boolean

    public suspend fun unpinAllForumTopicMessages(
        chatId: ChatId,
        messageThreadId: Long,
    ): Boolean

    public suspend fun editGeneralForumTopic(
        chatId: ChatId,
        name: String,
    ): Boolean

    public suspend fun closeGeneralForumTopic(
        chatId: ChatId,
    ): Boolean

    public suspend fun reopenGeneralForumTopic(
        chatId: ChatId,
    ): Boolean

    public suspend fun hideGeneralForumTopic(
        chatId: ChatId,
    ): Boolean

    public suspend fun unhideGeneralForumTopic(
        chatId: ChatId,
    ): Boolean
}