package com.elbekd.bot.model.internal

import com.elbekd.bot.types.ChatAdministratorRights
import com.elbekd.bot.types.MenuButton
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SetChatMenuButton(
    @SerialName("chat_id") val chatId: Long?,
    @SerialName("menu_button") val menuButton: MenuButton?
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