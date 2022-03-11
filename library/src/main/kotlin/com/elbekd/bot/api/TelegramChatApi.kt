package com.elbekd.bot.api

import com.elbekd.bot.types.ChatAdministratorRights
import com.elbekd.bot.types.MenuButton

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
}