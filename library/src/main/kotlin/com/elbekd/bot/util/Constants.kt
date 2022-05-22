package com.elbekd.bot.util

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class AllowedUpdate {
    @SerialName("message")
    Message,

    @SerialName("edited_message")
    EditedMessage,

    @SerialName("channel_post")
    ChannelPost,

    @SerialName("edited_channel_post")
    EditedChannelPost,

    @SerialName("inline_query")
    InlineQuery,

    @SerialName("chosen_inline_result")
    ChosenInlineQuery,

    @SerialName("callback_query")
    CallbackQuery,

    @SerialName("shipping_query")
    ShippingQuery,

    @SerialName("pre_checkout_query")
    PreCheckoutQuery,

    @SerialName("poll")
    Poll,

    @SerialName("poll_answer")
    PollAnswer,

    @SerialName("my_chat_member")
    MyChatMember,

    @SerialName("chat_member")
    ChatMember,

    @SerialName("chat_join_request")
    ChatJoinRequest,
}

@Serializable
public enum class Action {
    @SerialName("typing")
    Typing,

    @SerialName("upload_photo")
    UploadPhoto,

    @SerialName("record_video")
    RecordVideo,

    @SerialName("upload_video")
    UploadVideo,

    @SerialName("record_audio")
    RecordAudio,

    @SerialName("upload_audio")
    UploadAudio,

    @SerialName("upload_document")
    UploadDocument,

    @SerialName("find_location")
    FindLocation,

    @SerialName("choose_sticker")
    ChooseSticker,

    @SerialName("record_video_note")
    RecordVideoNote,

    @SerialName("upload_video_note")
    UploadVideoNote,
}