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

internal class TelegramClient(token: String) : TelegramApi {
    private val gson = Gson()
    private val client = OkHttpClient.Builder().connectTimeout(60L, TimeUnit.SECONDS).build()
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

    private val anyToString = { a: Any -> a.toString() }
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

    private inline fun <reified R> get(method: String) = future {
        // FixMe: without this hack fails with ClassCastException in Gson
        R::class.java.simpleName
        val request = Request.Builder().url(url(method)).build()
        val response = client.newCall(request).execute()
        val obj = fromJson<R>(response)
        if (!obj.ok) throw TelegramApiError(obj.error_code!!, obj.description!!)
        obj.result!!
    }

    private inline fun <reified R> post(method: String, body: RequestBody) = future {
        // FixMe: without this hack fails with ClassCastException in Gson
        R::class.java.simpleName
        val request = Request.Builder().url(url(method)).post(body).build()
        val response = client.newCall(request).execute()
        val obj = fromJson<R>(response)
        if (!obj.ok) throw TelegramApiError(obj.error_code!!, obj.description!!)
        obj.result!!
    }

    private inline fun <reified R> fromJson(response: Response): TelegramObject<R> {
        return gson.fromJson(response.body()?.string(), getType<TelegramObject<R>>())
    }

    private fun toJson(body: Any) = gson.toJson(body)

    private fun toBody(body: Any): RequestBody {
        val json = toJson(body)
        return RequestBody.create(MEDIA_TYPE_JSON, json)
    }

    private inline fun <reified T> getType(): Type {
        return object : TypeToken<T>() {}.type
    }

    private fun url(method: String): String = "$url/$method"

    private fun sendFile(type: String, id: String, file: Any, opts: Map<String, Any?>, method: String = type):
            CompletableFuture<Message> {
        val form = MultipartBody.Builder().also { it.setType(MultipartBody.FORM) }
        form.addFormDataPart("chat_id", id)
        addOptsToForm(form, opts)

        when (file) {
            is File -> form.addFormDataPart(type, file.name, RequestBody.create(null, file))
            is String -> form.addFormDataPart(type, file)   // file_id
            else -> throw IllegalArgumentException("Neither file nor string")
        }

        return post("send${method.capitalize()}", form.build())
    }

    private fun addOptsToForm(form: MultipartBody.Builder, opts: Map<String, Any?>) =
            sendFileOpts.filterKeys { opts[it] != null }.forEach { form.addFormDataPart(it.key, it.value(opts[it.key]!!)) }

    internal fun getUpdates(options: Map<String, Any?>) = future {
        val request = Request.Builder()
                .url(url("getUpdates"))
                .post(RequestBody.create(MEDIA_TYPE_JSON, toJson(options)))
                .build()

        val response = client.newCall(request).execute()

        val obj = fromJson<List<Update>>(response)

        if (!obj.ok)
            throw TelegramApiError(obj.error_code!!, obj.description!!)
        obj.result!!
    }

    override fun getMe() = get<User>("getMe")

    override fun sendMessage(chatId: Any, text: String, parseMode: String?, preview: Boolean?, notification: Boolean?,
                             replyTo: Int?, markup: ReplyKeyboard?): CompletableFuture<Message> {
        val body = toBody(SendMessage(id(chatId), text, parseMode, preview, notification, replyTo, markup))
        return post("sendMessage", body)
    }

    override fun forwardMessage(chatId: Any, fromId: Any, msgId: Int, notification: Boolean?): CompletableFuture<Message> {
        val body = toBody(ForwardMessage(id(chatId), id(fromId), msgId, notification))
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

    override fun sendAudio(chatId: Any, audio: Any, caption: String?, parseMode: String?, duration: Int?, performer: String?, title: String?, notification: Boolean?, replyTo: Int?, markup: ReplyKeyboard?): CompletableFuture<Message> {
        return sendFile("audio", id(chatId), audio, mapOf(
                "caption" to caption,
                "parse_mode" to parseMode,
                "duration" to duration,
                "performer" to performer,
                "title" to title,
                "disable_notification" to notification,
                "reply_to_message_id" to replyTo,
                "reply_markup" to markup))
    }

    override fun sendDocument(chatId: Any, document: Any, caption: String?, parseMode: String?, notification: Boolean?, replyTo: Int?, markup: ReplyKeyboard?): CompletableFuture<Message> {
        return sendFile("document", id(chatId), document, mapOf(
                "caption" to caption,
                "parse_mode" to parseMode,
                "disable_notification" to notification,
                "reply_to_message_id" to replyTo,
                "reply_markup" to markup))
    }

    override fun sendVideo(chatId: Any, video: Any, duration: Int?, width: Int?, height: Int?, caption: String?, parseMode: String?, streaming: Boolean?, notification: Boolean?, replyTo: Int?, markup: ReplyKeyboard?): CompletableFuture<Message> {
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
        ))
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

    override fun sendVideoNote(chatId: Any, note: Any, duration: Int?, length: Int?, notification: Boolean?, replyTo: Int?, markup: ReplyKeyboard?): CompletableFuture<Message> {
        return sendFile("video_note", id(chatId), note, mapOf(
                "duration" to duration,
                "disable_notification" to notification,
                "reply_to_message_id" to replyTo,
                "reply_markup" to markup
        ), "videoNote")
    }

    override fun sendMediaGroup(chatId: Any, media: Array<InputMedia>, notification: Boolean?, replyTo: Int?):
            CompletableFuture<ArrayList<Message>> {
        val form = MultipartBody.Builder().also { it.setType(MultipartBody.FORM) }
        form.addFormDataPart("chat_id", id(chatId))

        media.forEach {
            if (it.file() != null)
                form.addFormDataPart(it.media().split("//")[1], it.media(),
                        RequestBody.create(MEDIA_TYPE_OCTET_STREAM, it.file()!!))
        }

        form.addFormDataPart("media", toJson(media))
        if (notification != null)
            form.addFormDataPart("disable_notification", notification.toString())
        if (replyTo != null)
            form.addFormDataPart("reply_to_message_id", replyTo.toString())

        return post("sendMediaGroup", form.build())
    }

    override fun sendLocation(chatId: Any, latitude: Double, longitude: Double, period: Int?, notification: Boolean?, replyTo: Int?, markup: ReplyKeyboard?): CompletableFuture<Message> {
        val body = toBody(SendLocation(id(chatId), latitude, longitude, period, notification, replyTo, markup))
        return post("sendLocation", body)
    }

    override fun editMessageLiveLocation(latitude: Double, longitude: Double, chatId: Any?, messageId: Int?, inlineMessageId: String?, markup: ReplyKeyboard?): CompletableFuture<Message> {
        val body = toBody(EditLocation(if (chatId != null) id(chatId) else null, messageId, inlineMessageId,
                latitude, longitude, markup))
        return post("editMessageLiveLocation", body)
    }

    override fun stopMessageLiveLocation(chatId: Any?, messageId: Int?, inlineMessageId: String?, markup: ReplyKeyboard?): CompletableFuture<Message> {
        val body = toBody(StopLocation(if (chatId != null) id(chatId) else null, messageId, inlineMessageId, markup))
        return post("stopMessageLiveLocation", body)
    }

    override fun sendVenue(chatId: Any, latitude: Double, longitude: Double, title: String, address: String, foursquareId: String?, notification: Boolean?, replyTo: Int?, markup: ReplyKeyboard?): CompletableFuture<Message> {
        val body = toBody(SendVenue(id(chatId), latitude, longitude, title, address, foursquareId, notification,
                replyTo, markup))
        return post("sendVenue", body)
    }

    override fun sendContact(chatId: Any, phone: String, firstName: String, lastName: String?, notification: Boolean?, replyTo: Int?, markup: ReplyKeyboard?): CompletableFuture<Message> {
        val body = toBody(SendContact(id(chatId), phone, firstName, lastName, notification, replyTo, markup))
        return post("sendContact", body)
    }

    override fun sendChatAction(chatId: Any, action: TelegramBot.Actions): CompletableFuture<Boolean> {
        val body = mapOf(
                "chat_id" to id(chatId),
                "action" to action.value)
        return post("sendChatAction", RequestBody.create(MEDIA_TYPE_JSON, toJson(body)))
    }

    override fun getUserProfilePhotos(userId: Int, offset: Int?, limit: Int?): CompletableFuture<UserProfilePhotos> {
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

    override fun kickChatMember(chatId: Any, userId: Int, untilDate: Int?): CompletableFuture<Boolean> {
        val body = mapOf(
                "chat_id" to id(chatId),
                "user_id" to userId,
                "until_date" to untilDate)
        return post("kickChatMember", RequestBody.create(MEDIA_TYPE_JSON, toJson(body)))
    }

    override fun unbanChatMember(chatId: Any, userId: Int): CompletableFuture<Boolean> {
        val body = mapOf(
                "chat_id" to id(chatId),
                "user_id" to userId)
        return post("unbanChatMember", RequestBody.create(MEDIA_TYPE_JSON, toJson(body)))
    }

    override fun restrictChatMember(chatId: Any, userId: Int, untilDate: Int?, canSendMessage: Boolean?,
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

    override fun promoteChatMember(chatId: Any, userId: Int, canChangeInfo: Boolean?, canPostMessages: Boolean?, canEditMessages: Boolean?, canDeleteMessages: Boolean?, canInviteUsers: Boolean?, canRestrictMembers: Boolean?, canPinMessages: Boolean?, canPromoteMembers: Boolean?): CompletableFuture<Boolean> {
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
            is File -> {
                form.addFormDataPart("photo", photo.name, RequestBody.create(null, photo))
                println("${photo.absolutePath}, ${photo.name}, ${id(chatId)}")
            }
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

    override fun getChatMember(chatId: Any, userId: Int): CompletableFuture<ChatMember> {
        val body = toBody(mapOf(
                "chat_id" to id(chatId),
                "user_id" to userId))
        return post("getChatMember", body)
    }

    override fun setChatStickerSet(chatId: Any, stickerSet: String): CompletableFuture<Boolean> {
        val body = toBody(mapOf(
                "chat_id" to id(chatId),
                "sticker_set_name" to stickerSet))
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

    override fun answerInlineQuery(queryId: String, results: Array<InlineQueryResult>, cacheTime: Int?, personal: Boolean?, offset: String?, pmText: String?, pmParameter: String?): CompletableFuture<Boolean> {
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
}
