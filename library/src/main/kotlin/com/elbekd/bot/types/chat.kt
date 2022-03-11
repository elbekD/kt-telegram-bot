package com.elbekd.bot.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Chat(
    @SerialName("id") val id: Long,
    @SerialName("type") val type: String,
    @SerialName("title") val title: String? = null,
    @SerialName("username") val username: String? = null,
    @SerialName("first_name") val firstName: String? = null,
    @SerialName("last_name") val lastName: String? = null,
    @SerialName("photo") val photo: ChatPhoto? = null,
    @SerialName("bio") val bio: String? = null,
    @SerialName("has_private_forwards") val hasPrivateForwards: Boolean? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("invite_link") val inviteLink: String? = null,
    @SerialName("pinned_message") val pinnedMessage: Message? = null,
    @SerialName("permissions") val permissions: ChatPermissions? = null,
    @SerialName("slow_mode_delay") val slowModeDelay: Boolean? = null,
    @SerialName("message_auto_delete_time") val messageAutoDeleteTime: Int? = null,
    @SerialName("has_protected_content") val hasProtectedContent: Boolean? = null,
    @SerialName("sticker_set_name") val stickerSetName: String? = null,
    @SerialName("can_set_sticker_set") val canSetStickerSet: Boolean? = null,
    @SerialName("linked_chat_id") val linkedChatId: Int? = null,
    @SerialName("location") val location: ChatLocation? = null
)

@Serializable
public data class ChatLocation(
    @SerialName("location") val location: Location,
    @SerialName("address") val address: String
)

@Serializable
public data class ChatPermissions(
    @SerialName("can_send_messages") val canSendMessages: Boolean? = null,
    @SerialName("can_send_media_messages") val canSendMediaMessages: Boolean? = null,
    @SerialName("can_send_polls") val canSendPolls: Boolean? = null,
    @SerialName("can_send_other_messages") val canSendOtherMessages: Boolean? = null,
    @SerialName("can_add_web_page_previews") val canAddWebPagePreviews: Boolean? = null,
    @SerialName("can_change_info") val canChangeInfo: Boolean? = null,
    @SerialName("can_invite_users") val canInviteUsers: Boolean? = null,
    @SerialName("can_pin_messages") val canPinMessages: Boolean? = null
)


@Serializable
public data class ChatPhoto(
    @SerialName("small_file_id") val smallFileId: String,
    @SerialName("small_file_unique_id") val smallFileUniqueId: String,
    @SerialName("big_file_id") val bigFileId: String,
    @SerialName("big_file_unique_id") val bigFileUniqueId: String
)

@Serializable
public sealed class ChatMember {
    public data class Owner(
        @SerialName("status") val status: String,
        @SerialName("user") val user: User,
        @SerialName("is_anonymous") val isAnonymous: Boolean? = null,
        @SerialName("custom_title") val customTitle: String? = null
    ) : ChatMember()

    public data class Administrator(
        @SerialName("status") val status: String,
        @SerialName("user") val user: User,
        @SerialName("can_be_edited") val canBeEdited: Boolean,
        @SerialName("is_anonymous") val isAnonymous: Boolean,
        @SerialName("can_manage_chat") val canManageChat: Boolean,
        @SerialName("can_delete_messages") val canDeleteMessages: Boolean,
        @SerialName("can_manage_video_chats") val canManageVideoChats: Boolean,
        @SerialName("can_restrict_members") val canRestrictMembers: Boolean,
        @SerialName("can_promote_members") val canPromoteMembers: Boolean,
        @SerialName("can_change_info") val canChangeInfo: Boolean,
        @SerialName("can_invite_users") val canInviteUsers: Boolean,
        @SerialName("can_post_messages") val canPostMessages: Boolean? = null,
        @SerialName("can_edit_messages") val canEditMessages: Boolean? = null,
        @SerialName("can_pin_messages") val canPinMessages: Boolean? = null,
        @SerialName("custom_title") val customTitle: String? = null
    ) : ChatMember()

    @Serializable
    public data class Member(
        @SerialName("status") val status: String,
        @SerialName("user") val user: User
    ) : ChatMember()

    @Serializable
    public data class Restricted(
        @SerialName("status") val status: String,
        @SerialName("user") val user: User,
        @SerialName("is_member") val isMember: Boolean,
        @SerialName("can_change_info") val canChangeInfo: Boolean,
        @SerialName("can_invite_users") val canInviteUsers: Boolean,
        @SerialName("can_pin_messages") val canPinMessages: Boolean,
        @SerialName("can_send_messages") val canSendMessages: Boolean,
        @SerialName("can_send_media_messages") val canSendMediaMessages: Boolean,
        @SerialName("can_send_polls") val canSendPolls: Boolean,
        @SerialName("can_send_other_messages") val canSendOtherMessages: Boolean,
        @SerialName("can_add_web_page_previews") val canAddWebPagePreviews: Boolean,
        @SerialName("until_date") val untilDate: Int
    ) : ChatMember()

    @Serializable
    public data class Left(
        @SerialName("status") val status: String,
        @SerialName("user") val user: User
    ) : ChatMember()

    @Serializable
    public data class Banned(
        @SerialName("status") val status: String,
        @SerialName("user") val user: User,
        @SerialName("until_date") val untilDate: Int
    ) : ChatMember()
}

@Serializable
public data class ChatMemberUpdated(
    @SerialName("chat") val chat: Chat,
    @SerialName("from") val from: User,
    @SerialName("date") val date: Int,
    @SerialName("old_chat_member") val oldChatMember: ChatMember,
    @SerialName("new_chat_member") val newChatMember: ChatMember,
    @SerialName("invite_link") val inviteLink: ChatInviteLink? = null
)

@Serializable
public data class ChatInviteLink(
    @SerialName("invite_link") val inviteLink: String,
    @SerialName("creator") val creator: User,
    @SerialName("creates_join_request") val createsJoinRequest: Boolean,
    @SerialName("is_primary") val isPrimary: Boolean,
    @SerialName("is_revoked") val isRevoked: Boolean,
    @SerialName("name") val name: String? = null,
    @SerialName("expire_date") val expireDate: Int? = null,
    @SerialName("member_limit") val memberLimit: Int? = null,
    @SerialName("pending_join_request_count") val pendingJoinRequestCount: Int? = null,
)

@Serializable
public data class ChatJoinRequest(
    @SerialName("chat") val chat: Chat,
    @SerialName("from") val from: Chat,
    @SerialName("date") val date: Long,
    @SerialName("bio") val bio: String? = null,
    @SerialName("invite_link") val inviteLink: ChatInviteLink? = null,
)

@Serializable
public data class ChatAdministratorRights(
    @SerialName("is_anonymous") val isAnonymous: Boolean,
    @SerialName("can_manage_chat") val canManageChat: Boolean,
    @SerialName("can_delete_messages") val canDeleteMessages: Boolean,
    @SerialName("can_manage_video_chats") val canManageVideoChats: Boolean,
    @SerialName("can_restrict_members") val canRestrictMembers: Boolean,
    @SerialName("can_promote_members") val canPromoteMembers: Boolean,
    @SerialName("can_change_info") val canChangeInfo: Boolean,
    @SerialName("can_invite_users") val canInviteUsers: Boolean,
    @SerialName("can_post_messages") val canPostMessages: Boolean? = null,
    @SerialName("can_edit_messages") val canEditMessages: Boolean? = null,
    @SerialName("can_pin_messages") val canPinMessages: Boolean? = null,
)