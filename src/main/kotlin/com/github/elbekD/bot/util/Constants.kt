package com.github.elbekD.bot.util

internal const val ANY_CALLBACK_TRIGGER = "="

enum class AllowedUpdate(val value: String) {
    Message("message"),
    EditedMessage("edited_message"),
    ChannelPost("channel_post"),
    EditedChannelPost("edited_channel_post"),
    InlineQuery("inline_query"),
    ChosenInlineQuery("chosen_inline_result"),
    CallbackQuery("callback_query"),
    ShippingQuery("shipping_query"),
    PreCheckoutQuery("pre_checkout_query");

    override fun toString() = value
}

enum class Action(val value: String) {
    Typing("typing"),
    UploadPhoto("upload_photo"),
    RecordVideo("record_video"),
    UploadVideo("upload_video"),
    RecordAudio("record_audio"),
    UploadAudio("upload_audio"),
    UploadDocument("upload_document"),
    FindLocation("find_location"),
    RecordVideoNote("record_video_note"),
    UploadVideoNote("upload_video_note ");

    override fun toString() = value
}
