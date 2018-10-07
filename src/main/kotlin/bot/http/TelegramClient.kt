package bot.http

import bot.TelegramBot
import bot.types.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import java.io.File
import java.lang.reflect.Type
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

// Todo: create general multipart function
internal class TelegramClient(token: String) : TelegramApi {
    private val gson = Gson()
    private val httpClient = OkHttpClient.Builder()
            .connectTimeout(60L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .writeTimeout(60L, TimeUnit.SECONDS)
            .build()
    private val url = "https://api.telegram.org/bot$token"

    private companion object {
        @JvmStatic
        private val MEDIA_TYPE_JSON = MediaType.parse("application/json")
        @JvmStatic
        private val MEDIA_TYPE_OCTET_STREAM = MediaType.parse("application/octet-stream")

        @JvmStatic
        private fun id(id: Any) = when (id) {
            is Int -> id.toString()
            is Long -> id.toString()
            is String -> id
            else -> throw IllegalArgumentException("$id neither string nor integer")
        }
    }

    private val anyToString = { a: Any -> a.toString(); }
    private val markupToString = { a: Any -> toJson(a) }

    private val sendFileOpts = mapOf(
            "caption" to anyToString,
            "parse_mode" to anyToString,
            "disable_notification" to anyToString,
            "reply_to_message_id" to anyToString,
            "reply_markup" to markupToString,
            "duration" to anyToString,
            "performer" to anyToString,
            "title" to anyToString,
            "width" to anyToString,
            "height" to anyToString,
            "supports_streaming" to anyToString)

    private inline fun <reified T> get(method: String) = future {
        // FixMe: without this hack fails with ClassCastException in Gson
        T::class.java.simpleName
        val request = Request.Builder().url(url(method)).build()
        val response = httpClient.newCall(request).execute()
        val obj = fromJson<T>(response)
        if (!obj.ok) throw TelegramApiError(obj.error_code!!, obj.description!!)
        obj.result!!
    }

    private inline fun <reified T> post(method: String, body: RequestBody) = future {
        // FixMe: without this hack fails with ClassCastException in Gson
        T::class.java.simpleName
        val request = Request.Builder().url(url(method)).post(body).build()
        val response = httpClient.newCall(request).execute()
        val obj = fromJson<T>(response)
        if (!obj.ok) throw TelegramApiError(obj.error_code!!, obj.description!!)
        obj.result!!
    }

    private inline fun <reified T> fromJson(response: Response): TelegramObject<T> {
        return gson.fromJson(response.body()?.string(), getType<TelegramObject<T>>())
    }

    private inline fun <reified T> getType(): Type {
        return object : TypeToken<T>() {}.type
    }

    private fun toJson(body: Any) = gson.toJson(body)

    private fun toBody(body: Any): RequestBody {
        return RequestBody.create(MEDIA_TYPE_JSON, toJson(body))
    }

    private fun url(method: String) = "$url/$method"

    private fun sendFile(type: String, id: String, file: Any, opts: Map<String, Any?>, thumb: File? = null, method: String = type): CompletableFuture<Message> {
        val form = MultipartBody.Builder().also { it.setType(MultipartBody.FORM) }
        form.addFormDataPart("chat_id", id)
        addOptsToForm(form, opts)

        when (file) {
            is File -> form.addFormDataPart(type, file.name, RequestBody.create(null, file))
            is String -> form.addFormDataPart(type, file)
            else -> throw IllegalArgumentException("Neither file nor string")
        }

        thumb?.let {
            form.addFormDataPart("attach://${it.name}", it.name, RequestBody.create(null, it))
        }

        return post("send${method.capitalize()}", form.build())
    }

    private fun addOptsToForm(form: MultipartBody.Builder, opts: Map<String, Any?>) =
            sendFileOpts.filterKeys { opts[it] != null }.forEach { form.addFormDataPart(it.key, it.value(opts[it.key]!!)) }

    internal fun getUpdates(options: Map<String, Any?>) = post<ArrayList<Update>>("getUpdates", toBody(options))

    internal fun onStop() {
        httpClient.dispatcher().cancelAll()
    }

    override fun getMe() = get<User>("getMe")

    override fun sendMessage(chatId: Any, text: String, parseMode: String?, preview: Boolean?, notification: Boolean?,
                             replyTo: Int?, markup: ReplyKeyboard?): CompletableFuture<Message> {
        val body = toBody(mapOf(
                "chat_id" to id(chatId),
                "text" to text,
                "parse_mode" to parseMode,
                "disable_web_page_preview" to preview,
                "disable_notification" to notification,
                "reply_to_message_id" to replyTo,
                "reply_markup" to markup
        ))
        return post("sendMessage", body)
    }

    override fun forwardMessage(chatId: Any, fromId: Any, msgId: Int, notification: Boolean?): CompletableFuture<Message> {
        val body = toBody(mapOf(
                "chat_id" to id(chatId),
                "from_chat_id" to id(fromId),
                "message_id" to msgId,
                "disable_notification" to notification
        ))
        return post("forwardMessage", body)
    }

    override fun sendPhoto(chatId: Any, photo: Any, caption: String?, parseMode: String?, notification: Boolean?, replyTo: Int?, markup: ReplyKeyboard?): CompletableFuture<Message> {
        return sendFile("photo", id(chatId), photo, mapOf(
                "caption" to caption,
                "parse_mode" to parseMode,
                "disable_notification" to notification,
                "reply_to_message_id" to replyTo,
                "reply_markup" to markup))
    }

    override fun sendAudio(chatId: Any, audio: Any, caption: String?, parseMode: String?, duration: Int?, performer: String?, title: String?, thumb: File?, notification: Boolean?, replyTo: Int?, markup: ReplyKeyboard?): CompletableFuture<Message> {
        return sendFile("audio", id(chatId), audio, mapOf(
                "caption" to caption,
                "parse_mode" to parseMode,
                "duration" to duration,
                "performer" to performer,
                "title" to title,
                "disable_notification" to notification,
                "reply_to_message_id" to replyTo,
                "reply_markup" to markup), thumb)
    }

    override fun sendDocument(chatId: Any, document: Any, thumb: File?, caption: String?, parseMode: String?, notification: Boolean?, replyTo: Int?, markup: ReplyKeyboard?): CompletableFuture<Message> {
        return sendFile("document", id(chatId), document, mapOf(
                "caption" to caption,
                "parse_mode" to parseMode,
                "disable_notification" to notification,
                "reply_to_message_id" to replyTo,
                "reply_markup" to markup), thumb)
    }

    override fun sendVideo(chatId: Any, video: Any, duration: Int?, width: Int?, height: Int?, thumb: File?, caption: String?, parseMode: String?, streaming: Boolean?, notification: Boolean?, replyTo: Int?, markup: ReplyKeyboard?): CompletableFuture<Message> {
        return sendFile("video", id(chatId), video, mapOf(
                "duration" to duration,
                "width" to width,
                "height" to height,
                "caption" to caption,
                "parse_mode" to parseMode,
                "supports_streaming" to streaming,
                "disable_notification" to notification,
                "reply_to_message_id" to replyTo,
                "reply_markup" to markup
        ), thumb)
    }

    override fun sendAnimation(chatId: Any, animation: Any, duration: Int?, width: Int?, height: Int?, thumb: File?, caption: String?, parseMode: String?, notification: Boolean?, replyTo: Int?, markup: ReplyKeyboard?): CompletableFuture<Message> {
        return sendFile("animation", id(chatId), animation, mapOf(
                "duration" to duration,
                "width" to width,
                "height" to height,
                "caption" to caption,
                "parse_mode" to parseMode,
                "disable_notification" to notification,
                "reply_to_message_id" to replyTo,
                "reply_markup" to markup
        ), thumb)
    }

    override fun sendVoice(chatId: Any, voice: Any, caption: String?, parseMode: String?, duration: Int?, notification: Boolean?, replyTo: Int?, markup: ReplyKeyboard?): CompletableFuture<Message> {
        return sendFile("voice", id(chatId), voice, mapOf(
                "caption" to caption,
                "parse_mode" to parseMode,
                "duration" to duration,
                "disable_notification" to notification,
                "reply_to_message_id" to replyTo,
                "reply_markup" to markup))
    }

    override fun sendVideoNote(chatId: Any, note: Any, duration: Int?, length: Int?, thumb: File?, notification: Boolean?, replyTo: Int?, markup: ReplyKeyboard?): CompletableFuture<Message> {
        return sendFile("video_note", id(chatId), note, mapOf(
                "duration" to duration,
                "disable_notification" to notification,
                "reply_to_message_id" to replyTo,
                "reply_markup" to markup
        ), thumb, "videoNote")
    }

    override fun sendMediaGroup(chatId: Any, media: List<InputMedia>, notification: Boolean?, replyTo: Int?): CompletableFuture<ArrayList<Message>> {
        val form = MultipartBody.Builder().also { it.setType(MultipartBody.FORM) }
        form.addFormDataPart("chat_id", id(chatId))

        media.forEach { inputMedia ->
            inputMedia.file()?.let {
                form.addFormDataPart(inputMedia.media().split("//")[1], inputMedia.media(), RequestBody.create(MEDIA_TYPE_OCTET_STREAM, it))
            }

            inputMedia.thumb()?.let {
                when (it) {
                    is File -> form.addFormDataPart("attach://${it.name}", it.name, RequestBody.create(MEDIA_TYPE_OCTET_STREAM, it))
                    is String -> form.addFormDataPart("thumb", it)
                    else -> throw IllegalArgumentException("Neither file nor string")
                }
            }
        }

        form.addFormDataPart("media", toJson(media))
        notification?.let { form.addFormDataPart("disable_notification", it.toString()) }
        replyTo?.let { form.addFormDataPart("reply_to_message_id", it.toString()) }

        return post("sendMediaGroup", form.build())
    }

    override fun sendLocation(chatId: Any, latitude: Double, longitude: Double, period: Int?, notification: Boolean?, replyTo: Int?, markup: ReplyKeyboard?): CompletableFuture<Message> {
        val body = toBody(mapOf(
                "chat_id" to chatId,
                "latitude" to latitude,
                "longitude" to longitude,
                "live_period" to period,
                "disable_notification" to notification,
                "reply_to_message_id" to replyTo,
                "reply_markup" to markup
        ))
        return post("sendLocation", body)
    }

    override fun editMessageLiveLocation(latitude: Double, longitude: Double, chatId: Any?, messageId: Int?, inlineMessageId: String?, markup: InlineKeyboardMarkup?): CompletableFuture<Message> {
        val body = toBody(mapOf(
                "chat_id" to if (chatId != null) id(chatId) else null,
                "message_id" to messageId,
                "inline_message_id" to inlineMessageId,
                "latitude" to latitude,
                "longitude" to longitude,
                "reply_markup" to markup
        ))
        return post("editMessageLiveLocation", body)
    }

    override fun stopMessageLiveLocation(chatId: Any?, messageId: Int?, inlineMessageId: String?, markup: InlineKeyboardMarkup?): CompletableFuture<Message> {
        val body = toBody(mapOf(
                "chat_id" to if (chatId != null) id(chatId) else null,
                "message_id" to messageId,
                "inline_message_id" to inlineMessageId,
                "reply_markup" to markup
        ))
        return post("stopMessageLiveLocation", body)
    }

    override fun sendVenue(chatId: Any, latitude: Double, longitude: Double, title: String, address: String, foursquareId: String?, foursquareType: String?, notification: Boolean?, replyTo: Int?, markup: ReplyKeyboard?): CompletableFuture<Message> {
        val body = toBody(mapOf(
                "chat_id" to chatId,
                "latitude" to latitude,
                "longitude" to longitude,
                "title" to title,
                "address" to address,
                "foursquare_id" to foursquareId,
                "foursquare_type" to foursquareType,
                "disable_notification" to notification,
                "reply_to_message_id" to replyTo,
                "reply_markup" to markup
        ))
        return post("sendVenue", body)
    }

    override fun sendContact(chatId: Any, phone: String, firstName: String, lastName: String?, vcard: String?, notification: Boolean?, replyTo: Int?, markup: ReplyKeyboard?): CompletableFuture<Message> {
        val body = toBody(mapOf(
                "chat_id" to chatId,
                "phone_number" to phone,
                "first_name" to firstName,
                "last_name" to lastName,
                "vcard" to vcard,
                "disable_notification" to notification,
                "reply_to_message_id" to replyTo,
                "reply_markup" to markup
        ))
        return post("sendContact", body)
    }

    override fun sendChatAction(chatId: Any, action: TelegramBot.Actions): CompletableFuture<Boolean> {
        val body = mapOf(
                "chat_id" to id(chatId),
                "action" to action.value)
        return post("sendChatAction", RequestBody.create(MEDIA_TYPE_JSON, toJson(body)))
    }

    override fun getUserProfilePhotos(userId: Long, offset: Int?, limit: Int?): CompletableFuture<UserProfilePhotos> {
        val body = mapOf(
                "user_id" to userId,
                "offset" to offset,
                "limit" to limit)
        return post("getUserProfilePhotos", RequestBody.create(MEDIA_TYPE_JSON, toJson(body)))
    }

    override fun getFile(fileId: String): CompletableFuture<bot.types.File> {
        return post("getFile", RequestBody.create(MEDIA_TYPE_JSON,
                toJson(mapOf("file_id" to fileId))))
    }

    override fun kickChatMember(chatId: Any, userId: Long, untilDate: Int?): CompletableFuture<Boolean> {
        val body = mapOf(
                "chat_id" to id(chatId),
                "user_id" to userId,
                "until_date" to untilDate)
        return post("kickChatMember", RequestBody.create(MEDIA_TYPE_JSON, toJson(body)))
    }

    override fun unbanChatMember(chatId: Any, userId: Long): CompletableFuture<Boolean> {
        val body = mapOf(
                "chat_id" to id(chatId),
                "user_id" to userId)
        return post("unbanChatMember", RequestBody.create(MEDIA_TYPE_JSON, toJson(body)))
    }

    override fun restrictChatMember(chatId: Any, userId: Long, untilDate: Int?, canSendMessage: Boolean?,
                                    canSendMediaMessages: Boolean?, canSendOtherMessages: Boolean?, canAddWebPagePreview: Boolean?): CompletableFuture<Boolean> {
        val body = mapOf(
                "chat_id" to id(chatId),
                "user_id" to userId,
                "until_date" to untilDate,
                "can_send_messages" to canSendMessage,
                "can_send_media_messages" to canSendMediaMessages,
                "can_send_other_messages" to canSendOtherMessages,
                "can_add_web_page_previews" to canAddWebPagePreview)
        return post("restrictChatMember", RequestBody.create(MEDIA_TYPE_JSON, toJson(body)))
    }

    override fun promoteChatMember(chatId: Any, userId: Long, canChangeInfo: Boolean?, canPostMessages: Boolean?, canEditMessages: Boolean?, canDeleteMessages: Boolean?, canInviteUsers: Boolean?, canRestrictMembers: Boolean?, canPinMessages: Boolean?, canPromoteMembers: Boolean?): CompletableFuture<Boolean> {
        val body = mapOf(
                "chat_id" to id(chatId),
                "user_id" to userId,
                "can_change_info" to canChangeInfo,
                "can_post_messages" to canPostMessages,
                "can_edit_messages" to canEditMessages,
                "can_delete_messages" to canDeleteMessages,
                "can_invite_users" to canInviteUsers,
                "can_restrict_members" to canRestrictMembers,
                "can_pin_messages" to canPinMessages,
                "can_promote_members" to canPromoteMembers)
        return post("promoteChatMember", RequestBody.create(MEDIA_TYPE_JSON, toJson(body)))
    }

    override fun exportChatInviteLink(chatId: Any): CompletableFuture<String> = post("exportChatInviteLink",
            RequestBody.create(MEDIA_TYPE_JSON, toJson(mapOf("chat_id" to chatId))))

    override fun setChatPhoto(chatId: Any, photo: Any): CompletableFuture<Boolean> {
        val form = MultipartBody.Builder().also { it.setType(MultipartBody.FORM) }
        form.addFormDataPart("chat_id", id(chatId))
        when (photo) {
            is File -> form.addFormDataPart("photo", photo.name, RequestBody.create(null, photo))
            is String -> form.addFormDataPart("photo", photo)
            else -> throw IllegalArgumentException("<photo> neither java.io.File nor string")
        }
        return post("setChatPhoto", form.build())
    }

    override fun deleteChatPhoto(chatId: Any): CompletableFuture<Boolean> {
        val body = toBody(mapOf("chat_id" to id(chatId)))
        return post("deleteChatPhoto", body)
    }

    override fun setChatTitle(chatId: Any, title: String): CompletableFuture<Boolean> {
        if (title.isEmpty() || title.length > 255)
            throw IllegalArgumentException("title length must be greater then 1 and less then 255")

        val body = toBody(mapOf(
                "chat_id" to id(chatId),
                "title" to title))
        return post("setChatTitle", body)
    }

    override fun setChatDescription(chatId: Any, description: String): CompletableFuture<Boolean> {
        if (description.length > 255)
            throw IllegalArgumentException("title length must be 0 or less then 255")

        val body = toBody(mapOf(
                "chat_id" to id(chatId),
                "description" to description))
        return post("setChatDescription", body)
    }

    override fun pinChatMessage(chatId: Any, messageId: Int, notification: Boolean?): CompletableFuture<Boolean> {
        val body = toBody(mapOf(
                "chat_id" to id(chatId),
                "message_id" to messageId,
                "disable_notification" to notification))
        return post("pinChatMessage", body)
    }

    override fun unpinChatMessage(chatId: Any): CompletableFuture<Boolean> {
        val body = toBody(mapOf("chat_id" to id(chatId)))
        return post("unpinChatMessage", body)
    }

    override fun leaveChat(chatId: Any): CompletableFuture<Boolean> {
        val body = toBody(mapOf("chat_id" to id(chatId)))
        return post("leaveChat", body)
    }

    override fun getChat(chatId: Any): CompletableFuture<Chat> {
        val body = toBody(mapOf("chat_id" to id(chatId)))
        return post("getChat", body)
    }

    override fun getChatAdministrators(chatId: Any): CompletableFuture<ArrayList<ChatMember>> {
        val body = toBody(mapOf("chat_id" to id(chatId)))
        return post("getChatAdministrators", body)
    }

    override fun getChatMembersCount(chatId: Any): CompletableFuture<Int> {
        val body = toBody(mapOf("chat_id" to id(chatId)))
        return post("getChatMembersCount", body)
    }

    override fun getChatMember(chatId: Any, userId: Long): CompletableFuture<ChatMember> {
        val body = toBody(mapOf(
                "chat_id" to id(chatId),
                "user_id" to userId))
        return post("getChatMember", body)
    }

    override fun setChatStickerSet(chatId: Any, stickerSetName: String): CompletableFuture<Boolean> {
        val body = toBody(mapOf(
                "chat_id" to id(chatId),
                "sticker_set_name" to stickerSetName))
        return post("setChatStickerSet", body)
    }

    override fun deleteChatStickerSet(chatId: Any): CompletableFuture<Boolean> {
        val body = toBody(mapOf("chat_id" to id(chatId)))
        return post("deleteChatStickerSet", body)
    }

    override fun answerCallbackQuery(id: String, text: String?, alert: Boolean?, url: String?, cacheTime: Int?): CompletableFuture<Boolean> {
        val body = toBody(mapOf(
                "callback_query_id" to id,
                "text" to text,
                "show_alert" to alert,
                "url" to url,
                "cache_time" to cacheTime))
        return post("answerCallbackQuery", body)
    }

    override fun answerInlineQuery(queryId: String, results: List<InlineQueryResult>, cacheTime: Int?, personal: Boolean?, offset: String?, pmText: String?, pmParameter: String?): CompletableFuture<Boolean> {
        val body = toBody(mapOf(
                "inline_query_id" to queryId,
                "results" to results,
                "cache_time" to cacheTime,
                "is_personal" to personal,
                "next_offset" to offset,
                "switch_pm_text" to pmText,
                "switch_pm_parameter" to pmParameter))
        return post("answerInlineQuery", body)
    }

    override fun editMessageText(chatId: Any?, messageId: Int?, inlineMessageId: String?, text: String, parseMode: String?, preview: Boolean?, markup: InlineKeyboardMarkup?): CompletableFuture<Message> {
        val body = toBody(mapOf(
                "chat_id" to if (chatId != null) id(chatId) else null,
                "message_id" to messageId,
                "inline_message_id" to inlineMessageId,
                "text" to text,
                "parse_mode" to parseMode,
                "disable_web_page_preview" to preview,
                "reply_markup" to markup
        ))
        return post("editMessageText", body)
    }

    override fun editMessageCaption(chatId: Any?, messageId: Int?, inlineMessageId: String?, caption: String?, parseMode: String?, markup: InlineKeyboardMarkup?): CompletableFuture<Message> {
        val body = toBody(mapOf(
                "chat_id" to if (chatId != null) id(chatId) else null,
                "message_id" to messageId,
                "inline_message_id" to inlineMessageId,
                "caption" to caption,
                "parse_mode" to parseMode,
                "reply_markup" to markup
        ))
        return post("editMessageCaption", body)
    }

    override fun editMessageMedia(chatId: Any?, messageId: Int?, inlineMessageId: String?, media: InputMedia, markup: InlineKeyboardMarkup?): CompletableFuture<Message> {
        val form = MultipartBody.Builder().also { it.setType(MultipartBody.FORM) }

        if (inlineMessageId != null) {
            form.addFormDataPart("inline_message_id", inlineMessageId)
        } else {
            form.addFormDataPart("chat_id", id(chatId!!))
            form.addFormDataPart("message_id", messageId!!.toString())
        }

        media.thumb()?.let {
            when (it) {
                is File -> form.addFormDataPart("attach://${it.name}", it.name, RequestBody.create(null, it))
                is String -> form.addFormDataPart("thumb", it)
                else -> throw IllegalArgumentException("Neither file nor string")
            }
        }

        form.addFormDataPart(media.media().split("//")[1], media.media(), RequestBody.create(MEDIA_TYPE_OCTET_STREAM, media.file()!!))
        form.addFormDataPart("media", toJson(media))
        markup?.let { form.addFormDataPart("reply_markup", toJson(it)) }

        return post("editMessageMedia", form.build())
    }

    override fun editMessageReplyMarkup(chatId: Any?, messageId: Int?, inlineMessageId: String?, markup: InlineKeyboardMarkup?): CompletableFuture<Message> {
        val body = toBody(mapOf(
                "chat_id" to if (chatId != null) id(chatId) else null,
                "message_id" to messageId,
                "inline_message_id" to inlineMessageId,
                "reply_markup" to markup
        ))
        return post("editMessageReplyMarkup", body)
    }

    override fun sendSticker(chatId: Any, sticker: Any, notification: Boolean?, replyTo: Int?, markup: ReplyKeyboard?): CompletableFuture<Message> {
        val form = MultipartBody.Builder().also { it.setType(MultipartBody.FORM) }

        form.addFormDataPart("chat_id", id(chatId))

        when (sticker) {
            is File -> form.addFormDataPart("sticker", sticker.name, RequestBody.create(null, sticker))
            is String -> form.addFormDataPart("sticker", sticker)
        }

        notification?.let { form.addFormDataPart("disable_notification", it.toString()) }
        replyTo?.let { form.addFormDataPart("reply_to_message_id", it.toString()) }
        markup?.let { form.addFormDataPart("reply_markup", toJson(it)) }

        return post("sendSticker", form.build())
    }

    override fun getStickerSet(name: String): CompletableFuture<StickerSet> {
        val body = toBody(mapOf("name" to name))
        return post("getStickerSet", body)
    }

    override fun uploadStickerFile(userId: Long, pngSticker: File): CompletableFuture<bot.types.File> {
        val form = MultipartBody.Builder().also { it.setType(MultipartBody.FORM) }
        form.addFormDataPart("user_id", userId.toString())
        form.addFormDataPart("png_sticker", pngSticker.name, RequestBody.create(null, pngSticker))
        return post("uploadStickerFile", form.build())
    }

    override fun createNewStickerSet(userId: Long, name: String, title: String, pngSticker: Any, emojis: String, containsMask: Boolean?, maskPosition: MaskPosition?): CompletableFuture<Boolean> {
        val form = MultipartBody.Builder().also { it.setType(MultipartBody.FORM) }
        with(form) {
            addFormDataPart("user_id", userId.toString())
            addFormDataPart("name", name)
            addFormDataPart("title", title)
            addFormDataPart("emojis", emojis)
            containsMask?.let { addFormDataPart("contains_masks", it.toString()) }
            maskPosition?.let { addFormDataPart("mask_position", toJson(it)) }
            when (pngSticker) {
                is File -> form.addFormDataPart("png_sticker", pngSticker.name, RequestBody.create(null, pngSticker))
                is String -> form.addFormDataPart("png_sticker", pngSticker)
                else -> throw IllegalArgumentException()
            }
        }
        return post("createNewStickerSet", form.build())
    }

    override fun addStickerToSet(userId: Long, name: String, pngSticker: Any, emojis: String, maskPosition: MaskPosition?): CompletableFuture<Boolean> {
        val form = MultipartBody.Builder().also { it.setType(MultipartBody.FORM) }
        with(form) {
            addFormDataPart("user_id", userId.toString())
            addFormDataPart("name", name)
            addFormDataPart("emojis", emojis)
            maskPosition?.let { addFormDataPart("mask_position", toJson(it)) }
            when (pngSticker) {
                is File -> form.addFormDataPart("png_sticker", pngSticker.name, RequestBody.create(null, pngSticker))
                is String -> form.addFormDataPart("png_sticker", pngSticker)
                else -> throw IllegalArgumentException()
            }
        }
        return post("addStickerToSet", form.build())
    }

    override fun setStickerPositionInSet(sticker: String, position: Int): CompletableFuture<Boolean> {
        val body = toBody(mapOf(
                "sticker" to sticker,
                "position" to position))
        return post("setStickerPositionInSet", body)
    }

    override fun deleteStickerFromSet(sticker: String): CompletableFuture<Boolean> {
        val body = toBody(mapOf("sticker" to sticker))
        return post("deleteStickerFromSet", body)
    }

    override fun sendGame(chatId: Long, gameShortName: String, notification: Boolean?, replyTo: Int?, markup: InlineKeyboardMarkup?): CompletableFuture<Message> {
        val body = toBody(mapOf(
                "chat_id" to chatId,
                "game_short_name" to gameShortName,
                "disable_notification" to notification,
                "reply_to_message_id" to replyTo,
                "reply_markup" to markup
        ))
        return post("sendGame", body)
    }

    override fun setGameScore(userId: Long, score: Int, force: Boolean?, disableEditMessage: Boolean?, chatId: Long?, messageId: Int?, inlineMessageId: String?): CompletableFuture<Message> {
        val body = toBody(mapOf(
                "user_id" to userId,
                "score" to score,
                "force" to force,
                "disable_edit_message" to disableEditMessage,
                "chat_id" to chatId,
                "message_id" to messageId,
                "inline_message_id" to inlineMessageId
        ))
        return post("setGameScore", body)
    }

    override fun getGameHighScores(userId: Long, chatId: Long?, messageId: Int?, inlineMessageId: String?): CompletableFuture<List<GameHighScore>> {
        val body = toBody(mapOf(
                "user_id" to userId,
                "chat_id" to chatId,
                "message_id" to messageId,
                "inline_message_id" to inlineMessageId
        ))
        return post("getGameHighScores", body)
    }

    override fun sendInvoice(chatId: Long, title: String, description: String, payload: String, providerToken: String, startParam: String, currency: String, prices: List<LabeledPrice>, providerData: String?, photoUrl: String?, photoSize: Int?, photoWidth: Int?, photoHeight: Int?, needName: Boolean?, needPhoneNumber: Boolean?, needEmail: Boolean?, needShippingAddress: Boolean?, sendPhoneNumberToProvider: Boolean?, sendEmailToProvider: Boolean?, isFlexible: Boolean?, notification: Boolean?, replyTo: Int?, markup: InlineKeyboardMarkup?): CompletableFuture<Message> {
        val body = toBody(mapOf(
                "chat_id" to chatId,
                "title" to title,
                "description" to description,
                "payload" to payload,
                "provider_token" to providerToken,
                "start_parameter" to startParam,
                "currency" to currency,
                "prices" to prices,
                "provider_data" to providerData,
                "photo_url" to photoUrl,
                "photo_size" to photoSize,
                "photo_width" to photoWidth,
                "photo_height" to photoHeight,
                "need_name" to needName,
                "need_phone_number" to needPhoneNumber,
                "need_email" to needEmail,
                "need_shipping_address" to needShippingAddress,
                "send_phone_number_to_provider" to sendPhoneNumberToProvider,
                "send_email_to_provider" to sendEmailToProvider,
                "is_flexible" to isFlexible,
                "disable_notification" to notification,
                "reply_to_message_id" to replyTo,
                "reply_markup" to markup
        ))
        return post("sendInvoice", body)
    }

    override fun answerShippingQuery(shippingQueryId: String, ok: Boolean, shippingOptions: List<ShippingOption>?, errorMessage: String?): CompletableFuture<Boolean> {
        val body = toBody(mapOf(
                "shipping_query_id" to shippingQueryId,
                "ok" to ok,
                "shipping_options" to shippingOptions,
                "error_message" to errorMessage
        ))
        return post("answerShippingQuery", body)
    }

    override fun answerPreCheckoutQuery(preCheckoutQueryId: String, ok: Boolean, errorMessage: String?): CompletableFuture<Boolean> {
        val body = toBody(mapOf(
                "pre_checkout_query_id" to preCheckoutQueryId,
                "ok" to ok,
                "error_message" to errorMessage))
        return post("answerPreCheckoutQuery", body)
    }

    override fun setPassportDataErrors(userId: Long, errors: List<PassportElementError>): CompletableFuture<Boolean> {
        val body = toBody(mapOf(
                "user_id" to userId,
                "errors" to errors))
        return post("setPassportDataErrors", body)
    }
}
