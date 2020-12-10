package com.elbekD.bot.http

import com.elbekD.bot.types.BotCommand
import com.elbekD.bot.types.Chat
import com.elbekD.bot.types.ChatMember
import com.elbekD.bot.types.ChatPermissions
import com.elbekD.bot.types.GameHighScore
import com.elbekD.bot.types.InlineKeyboardMarkup
import com.elbekD.bot.types.InlineQueryResult
import com.elbekD.bot.types.InputMedia
import com.elbekD.bot.types.LabeledPrice
import com.elbekD.bot.types.MaskPosition
import com.elbekD.bot.types.Message
import com.elbekD.bot.types.PassportElementError
import com.elbekD.bot.types.Poll
import com.elbekD.bot.types.ReplyKeyboard
import com.elbekD.bot.types.ShippingOption
import com.elbekD.bot.types.StickerSet
import com.elbekD.bot.types.TelegramObject
import com.elbekD.bot.types.Update
import com.elbekD.bot.types.User
import com.elbekD.bot.types.UserProfilePhotos
import com.elbekD.bot.types.WebhookInfo
import com.elbekD.bot.util.Action
import com.elbekD.bot.util.AllowedUpdate
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.File
import java.lang.reflect.Type
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

// Todo: create general multipart function
internal class TelegramClient(token: String) : TelegramApi {
    private val gson = Gson()
    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(60L, TimeUnit.SECONDS)
        .readTimeout(60L, TimeUnit.SECONDS)
        .writeTimeout(60L, TimeUnit.SECONDS)
        .build()
    private val url = ApiConstants.API_URL_FORMAT.format(token)

    private companion object {
        private val MEDIA_TYPE_JSON = MediaType.parse("application/json")
        private val MEDIA_TYPE_OCTET_STREAM = MediaType.parse("application/octet-stream")

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
        ApiConstants.CAPTION to anyToString,
        ApiConstants.PARSE_MODE to anyToString,
        ApiConstants.DISABLE_NOTIFICATION to anyToString,
        ApiConstants.REPLY_TO_MESSAGE_ID to anyToString,
        ApiConstants.REPLY_MARKUP to markupToString,
        ApiConstants.DURATION to anyToString,
        ApiConstants.PERFORMER to anyToString,
        ApiConstants.TITLE to anyToString,
        ApiConstants.WIDTH to anyToString,
        ApiConstants.HEIGHT to anyToString,
        ApiConstants.SUPPORTS_STREAMING to anyToString
    )

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

    private fun sendFile(
        type: String,
        id: String,
        file: Any,
        opts: Map<String, Any?>,
        thumb: File? = null,
        method: String = type
    ): CompletableFuture<out Message> {
        val form = MultipartBody.Builder().also { it.setType(MultipartBody.FORM) }
        form.addFormDataPart(ApiConstants.CHAT_ID, id)
        addOptsToForm(form, opts)

        when (file) {
            is ByteArray -> form.addFormDataPart(type, "content", RequestBody.create(null, file))
            is File -> form.addFormDataPart(type, file.name, RequestBody.create(null, file))
            is String -> form.addFormDataPart(type, file)
            else -> throw IllegalArgumentException("Unsupported file object. Supported types: ByteArray, File, String.")
        }

        thumb?.let {
            form.addFormDataPart("attach://${it.name}", it.name, RequestBody.create(null, it))
        }

        return post("send${method.capitalize()}", form.build())
    }

    private fun addOptsToForm(form: MultipartBody.Builder, opts: Map<String, Any?>) =
        sendFileOpts
            .filterKeys { opts[it] != null }
            .forEach { form.addFormDataPart(it.key, it.value(opts[it.key] ?: return@forEach)) }

    internal fun onStop() {
        httpClient.dispatcher().cancelAll()
    }

    override fun getUpdates(options: Map<String, Any?>): CompletableFuture<out List<Update>> =
        post(ApiConstants.METHOD_GET_UPDATES, toBody(options))

    override fun getMyCommands(): CompletableFuture<out List<BotCommand>> = get(ApiConstants.METHOD_GET_MY_COMMANDS)

    override fun setMyCommands(commands: List<BotCommand>): CompletableFuture<out Boolean> {
        val body = toBody(mapOf(ApiConstants.COMMANDS to commands))
        return post(ApiConstants.METHOD_SET_MY_COMMANDS, body)
    }

    override fun setWebhook(
        url: String,
        certificate: File?,
        ipAddress: String?,
        maxConnections: Int?,
        allowedUpdates: List<AllowedUpdate>?,
        dropPendingUpdates: Boolean?
    ): CompletableFuture<out Boolean> {
        val form = MultipartBody.Builder().also { it.setType(MultipartBody.FORM) }
        form.addFormDataPart(ApiConstants.URL, url)
        certificate?.let { form.addFormDataPart(ApiConstants.CERTIFICATE, it.name, RequestBody.create(null, it)) }
        ipAddress?.let { form.addFormDataPart(ApiConstants.IP_ADDRESS, it) }
        maxConnections?.let { form.addFormDataPart(ApiConstants.MAX_CONNECTIONS, it.toString()) }
        allowedUpdates?.let { form.addFormDataPart(ApiConstants.ALLOWED_UPDATES, toJson(it)) }
        dropPendingUpdates?.let { form.addFormDataPart(ApiConstants.DROP_PENDING_UPDATES, it.toString()) }
        return post(ApiConstants.METHOD_SET_WEBHOOK, form.build())
    }

    override fun deleteWebhook(dropPendingUpdates: Boolean?): CompletableFuture<out Boolean> {
        val body = toBody(mapOf(ApiConstants.DROP_PENDING_UPDATES to dropPendingUpdates))
        return post(ApiConstants.METHOD_DELETE_WEBHOOK, body)
    }

    override fun getWebhookInfo(): CompletableFuture<out WebhookInfo> = get(ApiConstants.METHOD_GET_WEBHOOK_INFO)

    override fun getMe(): CompletableFuture<out User> = get(ApiConstants.METHOD_GET_ME)

    override fun logOut(): CompletableFuture<out Boolean> = get(ApiConstants.METHOD_LOGOUT)

    override fun close(): CompletableFuture<out Boolean> = get(ApiConstants.METHOD_CLOSE)

    override fun sendMessage(
        chatId: Any,
        text: String,
        parseMode: String?,
        disableWebPagePreview: Boolean?,
        disableNotification: Boolean?,
        replyTo: Int?,
        markup: ReplyKeyboard?
    ): CompletableFuture<out Message> {
        val body = toBody(
            mapOf(
                ApiConstants.CHAT_ID to id(chatId),
                ApiConstants.TEXT to text,
                ApiConstants.PARSE_MODE to parseMode,
                ApiConstants.DISABLE_WEB_PAGE_PREVIEW to disableWebPagePreview,
                ApiConstants.DISABLE_NOTIFICATION to disableNotification,
                ApiConstants.REPLY_TO_MESSAGE_ID to replyTo,
                ApiConstants.REPLY_MARKUP to markup
            )
        )
        return post(ApiConstants.METHOD_SEND_MESSAGE, body)
    }

    override fun forwardMessage(
        chatId: Any,
        fromId: Any,
        msgId: Int,
        disableNotification: Boolean?
    ): CompletableFuture<out Message> {
        val body = toBody(
            mapOf(
                ApiConstants.CHAT_ID to id(chatId),
                ApiConstants.FROM_CHAT_ID to id(fromId),
                ApiConstants.MESSAGE_ID to msgId,
                ApiConstants.DISABLE_NOTIFICATION to disableNotification
            )
        )
        return post(ApiConstants.METHOD_FORWARD_MESSAGE, body)
    }

    override fun sendPhoto(
        chatId: Any,
        photo: Any,
        caption: String?,
        parseMode: String?,
        disableNotification: Boolean?,
        replyTo: Int?,
        markup: ReplyKeyboard?
    ): CompletableFuture<out Message> {
        return sendFile(
            ApiConstants.PHOTO,
            id(chatId),
            photo,
            mapOf(
                ApiConstants.CAPTION to caption,
                ApiConstants.PARSE_MODE to parseMode,
                ApiConstants.DISABLE_NOTIFICATION to disableNotification,
                ApiConstants.REPLY_TO_MESSAGE_ID to replyTo,
                ApiConstants.REPLY_MARKUP to markup
            )
        )
    }

    override fun sendAudio(
        chatId: Any,
        audio: Any,
        caption: String?,
        parseMode: String?,
        duration: Int?,
        performer: String?,
        title: String?,
        thumb: File?,
        disableNotification: Boolean?,
        replyTo: Int?,
        markup: ReplyKeyboard?
    ): CompletableFuture<out Message> {
        return sendFile(
            ApiConstants.AUDIO,
            id(chatId),
            audio,
            mapOf(
                ApiConstants.CAPTION to caption,
                ApiConstants.PARSE_MODE to parseMode,
                ApiConstants.DURATION to duration,
                ApiConstants.PERFORMER to performer,
                ApiConstants.TITLE to title,
                ApiConstants.DISABLE_NOTIFICATION to disableNotification,
                ApiConstants.REPLY_TO_MESSAGE_ID to replyTo,
                ApiConstants.REPLY_MARKUP to markup
            ),
            thumb
        )
    }

    override fun sendDocument(
        chatId: Any,
        document: Any,
        thumb: File?,
        caption: String?,
        parseMode: String?,
        disableNotification: Boolean?,
        replyTo: Int?,
        markup: ReplyKeyboard?
    ): CompletableFuture<out Message> {
        return sendFile(
            ApiConstants.DOCUMENT,
            id(chatId),
            document,
            mapOf(
                ApiConstants.CAPTION to caption,
                ApiConstants.PARSE_MODE to parseMode,
                ApiConstants.DISABLE_NOTIFICATION to disableNotification,
                ApiConstants.REPLY_TO_MESSAGE_ID to replyTo,
                ApiConstants.REPLY_MARKUP to markup
            ),
            thumb
        )
    }

    override fun sendVideo(
        chatId: Any,
        video: Any,
        duration: Int?,
        width: Int?,
        height: Int?,
        thumb: File?,
        caption: String?,
        parseMode: String?,
        streaming: Boolean?,
        disableNotification: Boolean?,
        replyTo: Int?,
        markup: ReplyKeyboard?
    ): CompletableFuture<out Message> {
        return sendFile(
            ApiConstants.VIDEO,
            id(chatId),
            video,
            mapOf(
                ApiConstants.DURATION to duration,
                ApiConstants.WIDTH to width,
                ApiConstants.HEIGHT to height,
                ApiConstants.CAPTION to caption,
                ApiConstants.PARSE_MODE to parseMode,
                ApiConstants.SUPPORTS_STREAMING to streaming,
                ApiConstants.DISABLE_NOTIFICATION to disableNotification,
                ApiConstants.REPLY_TO_MESSAGE_ID to replyTo,
                ApiConstants.REPLY_MARKUP to markup
            ),
            thumb
        )
    }

    override fun sendAnimation(
        chatId: Any,
        animation: Any,
        duration: Int?,
        width: Int?,
        height: Int?,
        thumb: File?,
        caption: String?,
        parseMode: String?,
        disableNotification: Boolean?,
        replyTo: Int?,
        markup: ReplyKeyboard?
    ): CompletableFuture<out Message> {
        return sendFile(
            ApiConstants.ANIMATION,
            id(chatId),
            animation,
            mapOf(
                ApiConstants.DURATION to duration,
                ApiConstants.WIDTH to width,
                ApiConstants.HEIGHT to height,
                ApiConstants.CAPTION to caption,
                ApiConstants.PARSE_MODE to parseMode,
                ApiConstants.DISABLE_NOTIFICATION to disableNotification,
                ApiConstants.REPLY_TO_MESSAGE_ID to replyTo,
                ApiConstants.REPLY_MARKUP to markup
            ),
            thumb
        )
    }

    override fun sendVoice(
        chatId: Any,
        voice: Any,
        caption: String?,
        parseMode: String?,
        duration: Int?,
        disableNotification: Boolean?,
        replyTo: Int?,
        markup: ReplyKeyboard?
    ): CompletableFuture<out Message> {
        return sendFile(
            ApiConstants.VOICE,
            id(chatId),
            voice,
            mapOf(
                ApiConstants.CAPTION to caption,
                ApiConstants.PARSE_MODE to parseMode,
                ApiConstants.DURATION to duration,
                ApiConstants.DISABLE_NOTIFICATION to disableNotification,
                ApiConstants.REPLY_TO_MESSAGE_ID to replyTo,
                ApiConstants.REPLY_MARKUP to markup
            )
        )
    }

    override fun sendVideoNote(
        chatId: Any,
        note: Any,
        duration: Int?,
        length: Int?,
        thumb: File?,
        disableNotification: Boolean?,
        replyTo: Int?,
        markup: ReplyKeyboard?
    ): CompletableFuture<out Message> {
        return sendFile(
            ApiConstants.VIDEO_NOTE,
            id(chatId),
            note,
            mapOf(
                ApiConstants.DURATION to duration,
                ApiConstants.DISABLE_NOTIFICATION to disableNotification,
                ApiConstants.REPLY_TO_MESSAGE_ID to replyTo,
                ApiConstants.REPLY_MARKUP to markup
            ),
            thumb,
            ApiConstants.METHOD_VIDEO_NOTE
        )
    }

    override fun sendMediaGroup(
        chatId: Any,
        media: List<InputMedia>,
        disableNotification: Boolean?,
        replyTo: Int?
    ): CompletableFuture<out ArrayList<Message>> {
        val form = MultipartBody.Builder().also { it.setType(MultipartBody.FORM) }
        form.addFormDataPart(ApiConstants.CHAT_ID, id(chatId))

        media.forEach { inputMedia ->
            inputMedia.file()?.let {
                form.addFormDataPart(
                    inputMedia.media().split("//")[1],
                    inputMedia.media(),
                    RequestBody.create(MEDIA_TYPE_OCTET_STREAM, it)
                )
            }

            inputMedia.thumb()?.let {
                when (it) {
                    is File -> form.addFormDataPart(
                        "attach://${it.name}",
                        it.name,
                        RequestBody.create(MEDIA_TYPE_OCTET_STREAM, it)
                    )
                    is String -> form.addFormDataPart(ApiConstants.THUMB, it)
                    else -> throw IllegalArgumentException("Neither file nor string")
                }
            }
        }

        form.addFormDataPart(ApiConstants.MEDIA, toJson(media))
        disableNotification?.let { form.addFormDataPart(ApiConstants.DISABLE_NOTIFICATION, it.toString()) }
        replyTo?.let { form.addFormDataPart(ApiConstants.REPLY_TO_MESSAGE_ID, it.toString()) }

        return post(ApiConstants.METHOD_SEND_MEDIA_GROUP, form.build())
    }

    override fun sendLocation(
        chatId: Any,
        latitude: Double,
        longitude: Double,
        period: Int?,
        disableNotification: Boolean?,
        replyTo: Int?,
        markup: ReplyKeyboard?
    ): CompletableFuture<out Message> {
        val body = toBody(
            mapOf(
                ApiConstants.CHAT_ID to chatId,
                ApiConstants.LATITUDE to latitude,
                ApiConstants.LONGITUDE to longitude,
                ApiConstants.LIVE_PERIOD to period,
                ApiConstants.DISABLE_NOTIFICATION to disableNotification,
                ApiConstants.REPLY_TO_MESSAGE_ID to replyTo,
                ApiConstants.REPLY_MARKUP to markup
            )
        )
        return post(ApiConstants.METHOD_SEND_LOCATION, body)
    }

    override fun editMessageLiveLocation(
        latitude: Double,
        longitude: Double,
        chatId: Any?,
        messageId: Int?,
        inlineMessageId: String?,
        markup: InlineKeyboardMarkup?
    ): CompletableFuture<out Message> {
        val body = toBody(
            mapOf(
                ApiConstants.CHAT_ID to if (chatId != null) id(chatId) else null,
                ApiConstants.MESSAGE_ID to messageId,
                ApiConstants.INLINE_MESSAGE_ID to inlineMessageId,
                ApiConstants.LATITUDE to latitude,
                ApiConstants.LONGITUDE to longitude,
                ApiConstants.REPLY_MARKUP to markup
            )
        )
        return post(ApiConstants.METHOD_EDIT_MESSAGE_LIVE_LOCATION, body)
    }

    override fun stopMessageLiveLocation(
        chatId: Any?,
        messageId: Int?,
        inlineMessageId: String?,
        markup: InlineKeyboardMarkup?
    ): CompletableFuture<out Message> {
        val body = toBody(
            mapOf(
                ApiConstants.CHAT_ID to if (chatId != null) id(chatId) else null,
                ApiConstants.MESSAGE_ID to messageId,
                ApiConstants.INLINE_MESSAGE_ID to inlineMessageId,
                ApiConstants.REPLY_MARKUP to markup
            )
        )
        return post(ApiConstants.METHOD_STOP_MESSAGE_LIVE_LOCATION, body)
    }

    override fun sendVenue(
        chatId: Any,
        latitude: Double,
        longitude: Double,
        title: String,
        address: String,
        foursquareId: String?,
        foursquareType: String?,
        disableNotification: Boolean?,
        replyTo: Int?,
        markup: ReplyKeyboard?
    ): CompletableFuture<out Message> {
        val body = toBody(
            mapOf(
                ApiConstants.CHAT_ID to chatId,
                ApiConstants.LATITUDE to latitude,
                ApiConstants.LONGITUDE to longitude,
                ApiConstants.TITLE to title,
                ApiConstants.ADDRESS to address,
                ApiConstants.FOURSQUARE_ID to foursquareId,
                ApiConstants.FOURSQUARE_TYPE to foursquareType,
                ApiConstants.DISABLE_NOTIFICATION to disableNotification,
                ApiConstants.REPLY_TO_MESSAGE_ID to replyTo,
                ApiConstants.REPLY_MARKUP to markup
            )
        )
        return post(ApiConstants.METHOD_SEND_VENUE, body)
    }

    override fun sendContact(
        chatId: Any,
        phone: String,
        firstName: String,
        lastName: String?,
        vcard: String?,
        disableNotification: Boolean?,
        replyTo: Int?,
        markup: ReplyKeyboard?
    ): CompletableFuture<out Message> {
        val body = toBody(
            mapOf(
                ApiConstants.CHAT_ID to chatId,
                ApiConstants.PHONE_NUMBER to phone,
                ApiConstants.FIRST_NAME to firstName,
                ApiConstants.LAST_NAME to lastName,
                ApiConstants.VCARD to vcard,
                ApiConstants.DISABLE_NOTIFICATION to disableNotification,
                ApiConstants.REPLY_TO_MESSAGE_ID to replyTo,
                ApiConstants.REPLY_MARKUP to markup
            )
        )
        return post(ApiConstants.METHOD_SEND_CONTACT, body)
    }

    override fun sendChatAction(chatId: Any, action: Action): CompletableFuture<out Boolean> {
        val body = toBody(
            mapOf(
                ApiConstants.CHAT_ID to id(chatId),
                ApiConstants.ACTION to action.value
            )
        )
        return post(ApiConstants.METHOD_SEND_CHAT_ACTION, body)
    }

    override fun getUserProfilePhotos(
        userId: Long,
        offset: Int?,
        limit: Int?
    ): CompletableFuture<out UserProfilePhotos> {
        val body = toBody(
            mapOf(
                ApiConstants.USER_ID to userId,
                ApiConstants.OFFSET to offset,
                ApiConstants.LIMIT to limit
            )
        )
        return post(ApiConstants.METHOD_GET_USER_PROFILE_PHOTOS, body)
    }

    override fun getFile(fileId: String): CompletableFuture<out com.elbekD.bot.types.File> {
        val body = toBody(mapOf(ApiConstants.FILE_ID to fileId))
        return post(ApiConstants.METHOD_GET_FILE, body)
    }

    override fun kickChatMember(chatId: Any, userId: Long, untilDate: Int?): CompletableFuture<out Boolean> {
        val body = toBody(
            mapOf(
                ApiConstants.CHAT_ID to id(chatId),
                ApiConstants.USER_ID to userId,
                ApiConstants.UNTIL_DATE to untilDate
            )
        )
        return post(ApiConstants.METHOD_KICK_CHAT_MEMBER, body)
    }

    override fun unbanChatMember(
        chatId: Any,
        userId: Long,
        onlyIfBanned: Boolean?
    ): CompletableFuture<out Boolean> {
        val body = toBody(
            mapOf(
                ApiConstants.CHAT_ID to id(chatId),
                ApiConstants.USER_ID to userId,
                ApiConstants.ONLY_IF_BANNED to onlyIfBanned
            )
        )
        return post(ApiConstants.METHOD_UNBAN_CHAT_MEMBER, body)
    }

    override fun restrictChatMember(
        chatId: Any,
        userId: Long,
        permissions: ChatPermissions,
        untilDate: Int?
    ): CompletableFuture<out Boolean> {
        val body = toBody(
            mapOf(
                ApiConstants.CHAT_ID to id(chatId),
                ApiConstants.USER_ID to userId,
                ApiConstants.PERMISSIONS to permissions,
                ApiConstants.UNTIL_DATE to untilDate
            )
        )
        return post(ApiConstants.METHOD_RESTRICT_CHAT_MEMBER, body)
    }

    override fun promoteChatMember(
        chatId: Any,
        userId: Long,
        canChangeInfo: Boolean?,
        canPostMessages: Boolean?,
        canEditMessages: Boolean?,
        canDeleteMessages: Boolean?,
        canInviteUsers: Boolean?,
        canRestrictMembers: Boolean?,
        canPinMessages: Boolean?,
        canPromoteMembers: Boolean?
    ): CompletableFuture<out Boolean> {
        val body = toBody(
            mapOf(
                ApiConstants.CHAT_ID to id(chatId),
                ApiConstants.USER_ID to userId,
                ApiConstants.CAN_CHANGE_INFO to canChangeInfo,
                ApiConstants.CAN_POST_MESSAGES to canPostMessages,
                ApiConstants.CAN_EDIT_MESSAGES to canEditMessages,
                ApiConstants.CAN_DELETE_MESSAGES to canDeleteMessages,
                ApiConstants.CAN_INVITE_USERS to canInviteUsers,
                ApiConstants.CAN_RESTRICT_MEMBERS to canRestrictMembers,
                ApiConstants.CAN_PIN_MESSAGES to canPinMessages,
                ApiConstants.CAN_PROMOTE_MEMBERS to canPromoteMembers
            )
        )
        return post(ApiConstants.METHOD_PROMOTE_CHAT_MEMBER, body)
    }

    override fun exportChatInviteLink(chatId: Any): CompletableFuture<out String> = post(
        ApiConstants.METHOD_EXPORT_CHAT_INVITE_LINK,
        toBody(mapOf(ApiConstants.CHAT_ID to chatId))
    )

    override fun setChatPhoto(chatId: Any, photo: Any): CompletableFuture<out Boolean> {
        val form = MultipartBody.Builder().also { it.setType(MultipartBody.FORM) }
        form.addFormDataPart(ApiConstants.CHAT_ID, id(chatId))
        when (photo) {
            is File -> form.addFormDataPart(ApiConstants.PHOTO, photo.name, RequestBody.create(null, photo))
            is String -> form.addFormDataPart(ApiConstants.PHOTO, photo)
            else -> throw IllegalArgumentException("<photo> neither java.io.File nor string")
        }
        return post(ApiConstants.METHOD_SET_CHAT_PHOTO, form.build())
    }

    override fun deleteChatPhoto(chatId: Any): CompletableFuture<out Boolean> {
        val body = toBody(mapOf(ApiConstants.CHAT_ID to id(chatId)))
        return post(ApiConstants.METHOD_DELETE_CHAT_PHOTO, body)
    }

    override fun setChatTitle(chatId: Any, title: String): CompletableFuture<out Boolean> {
        if (title.isEmpty() || title.length > 255)
            throw IllegalArgumentException("title length must be greater then 1 and less then 255")

        val body = toBody(
            mapOf(
                ApiConstants.CHAT_ID to id(chatId),
                ApiConstants.TITLE to title
            )
        )
        return post(ApiConstants.METHOD_SET_CHAT_TITLE, body)
    }

    override fun setChatDescription(chatId: Any, description: String): CompletableFuture<out Boolean> {
        if (description.length > 255)
            throw IllegalArgumentException("title length must be 0 or less then 255")

        val body = toBody(
            mapOf(
                ApiConstants.CHAT_ID to id(chatId),
                ApiConstants.DESCRIPTION to description
            )
        )
        return post(ApiConstants.METHOD_SET_CHAT_DESCRIPTION, body)
    }

    override fun pinChatMessage(
        chatId: Any,
        messageId: Int,
        disableNotification: Boolean?
    ): CompletableFuture<out Boolean> {
        val body = toBody(
            mapOf(
                ApiConstants.CHAT_ID to id(chatId),
                ApiConstants.MESSAGE_ID to messageId,
                ApiConstants.DISABLE_NOTIFICATION to disableNotification
            )
        )
        return post(ApiConstants.METHOD_PIN_CHAT_MESSAGE, body)
    }

    override fun unpinChatMessage(chatId: Any): CompletableFuture<out Boolean> {
        val body = toBody(mapOf(ApiConstants.CHAT_ID to id(chatId)))
        return post(ApiConstants.METHOD_UNPIN_CHAT_MESSAGE, body)
    }

    override fun leaveChat(chatId: Any): CompletableFuture<out Boolean> {
        val body = toBody(mapOf(ApiConstants.CHAT_ID to id(chatId)))
        return post(ApiConstants.METHOD_LEAVE_CHAT, body)
    }

    override fun getChat(chatId: Any): CompletableFuture<out Chat> {
        val body = toBody(mapOf(ApiConstants.CHAT_ID to id(chatId)))
        return post(ApiConstants.METHOD_GET_CHAT, body)
    }

    override fun getChatAdministrators(chatId: Any): CompletableFuture<out ArrayList<ChatMember>> {
        val body = toBody(mapOf(ApiConstants.CHAT_ID to id(chatId)))
        return post(ApiConstants.METHOD_GET_CHAT_ADMINISTRATORS, body)
    }

    override fun getChatMembersCount(chatId: Any): CompletableFuture<out Int> {
        val body = toBody(mapOf(ApiConstants.CHAT_ID to id(chatId)))
        return post(ApiConstants.METHOD_GET_CHAT_MEMBERS_COUNT, body)
    }

    override fun getChatMember(chatId: Any, userId: Long): CompletableFuture<out ChatMember> {
        val body = toBody(
            mapOf(
                ApiConstants.CHAT_ID to id(chatId),
                ApiConstants.USER_ID to userId
            )
        )
        return post(ApiConstants.METHOD_GET_CHAT_MEMBER, body)
    }

    override fun setChatStickerSet(chatId: Any, stickerSetName: String): CompletableFuture<out Boolean> {
        val body = toBody(
            mapOf(
                ApiConstants.CHAT_ID to id(chatId),
                ApiConstants.STICKER_SET_NAME to stickerSetName
            )
        )
        return post(ApiConstants.METHOD_SET_CHAT_STICKER_SET, body)
    }

    override fun deleteChatStickerSet(chatId: Any): CompletableFuture<out Boolean> {
        val body = toBody(mapOf(ApiConstants.CHAT_ID to id(chatId)))
        return post(ApiConstants.METHOD_DELETE_CHAT_STICKER_SET, body)
    }

    override fun answerCallbackQuery(
        id: String,
        text: String?,
        alert: Boolean?,
        url: String?,
        cacheTime: Int?
    ): CompletableFuture<out Boolean> {
        val body = toBody(
            mapOf(
                ApiConstants.CALLBACK_QUERY_ID to id,
                ApiConstants.TEXT to text,
                ApiConstants.SHOW_ALERT to alert,
                ApiConstants.URL to url,
                ApiConstants.CACHE_TIME to cacheTime
            )
        )
        return post(ApiConstants.METHOD_ANSWER_CALLBACK_QUERY, body)
    }

    override fun answerInlineQuery(
        queryId: String,
        results: List<InlineQueryResult>,
        cacheTime: Int?,
        personal: Boolean?,
        offset: String?,
        pmText: String?,
        pmParameter: String?
    ): CompletableFuture<out Boolean> {
        val body = toBody(
            mapOf(
                ApiConstants.INLINE_QUERY_ID to queryId,
                ApiConstants.RESULTS to results,
                ApiConstants.CACHE_TIME to cacheTime,
                ApiConstants.IS_PERSONAL to personal,
                ApiConstants.NEXT_OFFSET to offset,
                ApiConstants.SWITCH_PM_TEXT to pmText,
                ApiConstants.SWITCH_PM_PARAMETER to pmParameter
            )
        )
        return post(ApiConstants.METHOD_ANSWER_INLINE_QUERY, body)
    }

    override fun editMessageText(
        chatId: Any?,
        messageId: Int?,
        inlineMessageId: String?,
        text: String,
        parseMode: String?,
        disableWebPagePreview: Boolean?,
        markup: InlineKeyboardMarkup?
    ): CompletableFuture<out Message> {
        val body = toBody(
            mapOf(
                ApiConstants.CHAT_ID to if (chatId != null) id(chatId) else null,
                ApiConstants.MESSAGE_ID to messageId,
                ApiConstants.INLINE_MESSAGE_ID to inlineMessageId,
                ApiConstants.TEXT to text,
                ApiConstants.PARSE_MODE to parseMode,
                ApiConstants.DISABLE_WEB_PAGE_PREVIEW to disableWebPagePreview,
                ApiConstants.REPLY_MARKUP to markup
            )
        )
        return post(ApiConstants.METHOD_EDIT_MESSAGE_TEXT, body)
    }

    override fun editMessageCaption(
        chatId: Any?,
        messageId: Int?,
        inlineMessageId: String?,
        caption: String?,
        parseMode: String?,
        markup: InlineKeyboardMarkup?
    ): CompletableFuture<out Message> {
        val body = toBody(
            mapOf(
                ApiConstants.CHAT_ID to if (chatId != null) id(chatId) else null,
                ApiConstants.MESSAGE_ID to messageId,
                ApiConstants.INLINE_MESSAGE_ID to inlineMessageId,
                ApiConstants.CAPTION to caption,
                ApiConstants.PARSE_MODE to parseMode,
                ApiConstants.REPLY_MARKUP to markup
            )
        )
        return post(ApiConstants.METHOD_EDIT_MESSAGE_CAPTION, body)
    }

    override fun editMessageMedia(
        chatId: Any?,
        messageId: Int?,
        inlineMessageId: String?,
        media: InputMedia,
        markup: InlineKeyboardMarkup?
    ): CompletableFuture<out Message> {
        val form = MultipartBody.Builder().also { it.setType(MultipartBody.FORM) }

        if (inlineMessageId != null) {
            form.addFormDataPart(ApiConstants.INLINE_MESSAGE_ID, inlineMessageId)
        } else {
            form.addFormDataPart(ApiConstants.CHAT_ID, id(chatId!!))
            form.addFormDataPart(ApiConstants.MESSAGE_ID, messageId!!.toString())
        }

        media.thumb()?.let {
            when (it) {
                is File -> form.addFormDataPart("attach://${it.name}", it.name, RequestBody.create(null, it))
                is String -> form.addFormDataPart(ApiConstants.THUMB, it)
                else -> throw IllegalArgumentException("Neither file nor string")
            }
        }

        form.addFormDataPart(
            media.media().split("//")[1],
            media.media(),
            RequestBody.create(MEDIA_TYPE_OCTET_STREAM, media.file()!!)
        )
        form.addFormDataPart(ApiConstants.MEDIA, toJson(media))
        markup?.let { form.addFormDataPart(ApiConstants.REPLY_MARKUP, toJson(it)) }

        return post(ApiConstants.METHOD_EDIT_MESSAGE_MEDIA, form.build())
    }

    override fun editMessageReplyMarkup(
        chatId: Any?,
        messageId: Int?,
        inlineMessageId: String?,
        markup: InlineKeyboardMarkup?
    ): CompletableFuture<out Message> {
        val body = toBody(
            mapOf(
                ApiConstants.CHAT_ID to if (chatId != null) id(chatId) else null,
                ApiConstants.MESSAGE_ID to messageId,
                ApiConstants.INLINE_MESSAGE_ID to inlineMessageId,
                ApiConstants.REPLY_MARKUP to markup
            )
        )
        return post(ApiConstants.METHOD_EDIT_MESSAGE_REPLY_MARKUP, body)
    }

    override fun sendSticker(
        chatId: Any,
        sticker: Any,
        disableNotification: Boolean?,
        replyTo: Int?,
        markup: ReplyKeyboard?
    ): CompletableFuture<out Message> {
        val form = MultipartBody.Builder().also { it.setType(MultipartBody.FORM) }

        form.addFormDataPart(ApiConstants.CHAT_ID, id(chatId))

        when (sticker) {
            is File -> form.addFormDataPart(ApiConstants.STICKER, sticker.name, RequestBody.create(null, sticker))
            is String -> form.addFormDataPart(ApiConstants.STICKER, sticker)
        }

        disableNotification?.let { form.addFormDataPart(ApiConstants.DISABLE_NOTIFICATION, it.toString()) }
        replyTo?.let { form.addFormDataPart(ApiConstants.REPLY_TO_MESSAGE_ID, it.toString()) }
        markup?.let { form.addFormDataPart(ApiConstants.REPLY_MARKUP, toJson(it)) }

        return post(ApiConstants.METHOD_SEND_STICKER, form.build())
    }

    override fun getStickerSet(name: String): CompletableFuture<out StickerSet> {
        val body = toBody(mapOf(ApiConstants.NAME to name))
        return post(ApiConstants.METHOD_GET_STICKER_SET, body)
    }

    override fun uploadStickerFile(userId: Long, pngSticker: File): CompletableFuture<out com.elbekD.bot.types.File> {
        val form = MultipartBody.Builder().also { it.setType(MultipartBody.FORM) }
        form.addFormDataPart(ApiConstants.USER_ID, userId.toString())
        form.addFormDataPart(ApiConstants.PNG_STICKER, pngSticker.name, RequestBody.create(null, pngSticker))
        return post(ApiConstants.METHOD_UPLOAD_STICKER_FILE, form.build())
    }

    override fun createNewStickerSet(
        userId: Long,
        name: String,
        title: String,
        emojis: String,
        pngSticker: Any?,
        tgsSticker: File?,
        containsMask: Boolean?,
        maskPosition: MaskPosition?
    ): CompletableFuture<out Boolean> {
        val form = MultipartBody.Builder().also { it.setType(MultipartBody.FORM) }
        with(form) {
            addFormDataPart(ApiConstants.USER_ID, userId.toString())
            addFormDataPart(ApiConstants.NAME, name)
            addFormDataPart(ApiConstants.TITLE, title)
            addFormDataPart(ApiConstants.EMOJIS, emojis)
            containsMask?.let { addFormDataPart(ApiConstants.CONTAINS_MASKS, it.toString()) }
            maskPosition?.let { addFormDataPart(ApiConstants.MASK_POSITION, toJson(it)) }
            when (pngSticker) {
                is File -> addFormDataPart(
                    ApiConstants.PNG_STICKER,
                    pngSticker.name,
                    RequestBody.create(null, pngSticker)
                )
                is String -> addFormDataPart(ApiConstants.PNG_STICKER, pngSticker)
                else -> throw IllegalArgumentException()
            }
            tgsSticker?.let {
                addFormDataPart(
                    ApiConstants.TGS_STICKER,
                    tgsSticker.name,
                    RequestBody.create(null, tgsSticker)
                )
            }
        }
        return post(ApiConstants.METHOD_CREATE_NEW_STICKER_SET, form.build())
    }

    override fun addStickerToSet(
        userId: Long,
        name: String,
        emojis: String,
        pngSticker: Any?,
        tgsSticker: File?,
        maskPosition: MaskPosition?
    ): CompletableFuture<out Boolean> {
        val form = MultipartBody.Builder().also { it.setType(MultipartBody.FORM) }
        with(form) {
            addFormDataPart(ApiConstants.USER_ID, userId.toString())
            addFormDataPart(ApiConstants.NAME, name)
            addFormDataPart(ApiConstants.EMOJIS, emojis)
            maskPosition?.let { addFormDataPart(ApiConstants.MASK_POSITION, toJson(it)) }
            when (pngSticker) {
                is File -> addFormDataPart(
                    ApiConstants.PNG_STICKER,
                    pngSticker.name,
                    RequestBody.create(null, pngSticker)
                )
                is String -> addFormDataPart(ApiConstants.PNG_STICKER, pngSticker)
                else -> throw IllegalArgumentException()
            }
            tgsSticker?.let {
                addFormDataPart(
                    ApiConstants.TGS_STICKER,
                    tgsSticker.name,
                    RequestBody.create(null, tgsSticker)
                )
            }
        }
        return post(ApiConstants.METHOD_ADD_STICKER_TO_SET, form.build())
    }

    override fun setStickerPositionInSet(sticker: String, position: Int): CompletableFuture<out Boolean> {
        val body = toBody(
            mapOf(
                ApiConstants.STICKER to sticker,
                ApiConstants.POSITION to position
            )
        )
        return post(ApiConstants.METHOD_SET_STICKER_POSITION_IN_SET, body)
    }

    override fun deleteStickerFromSet(sticker: String): CompletableFuture<out Boolean> {
        val body = toBody(mapOf(ApiConstants.STICKER to sticker))
        return post(ApiConstants.METHOD_DELETE_STICKER_FROM_SET, body)
    }

    override fun setStickerSetThumb(name: String, userId: Long, thumb: Any?): CompletableFuture<out Boolean> {
        val form = MultipartBody.Builder().also { it.setType(MultipartBody.FORM) }
        form.addFormDataPart(ApiConstants.NAME, name)
        form.addFormDataPart(ApiConstants.USER_ID, userId.toString())

        when (thumb) {
            is File -> form.addFormDataPart(ApiConstants.THUMB, thumb.name, RequestBody.create(null, thumb))
            is String -> form.addFormDataPart(ApiConstants.THUMB, thumb)
            else -> throw IllegalArgumentException()
        }

        return post(ApiConstants.METHOD_SET_STICKER_SET_THUMB, form.build())
    }

    override fun sendGame(
        chatId: Long,
        gameShortName: String,
        disableNotification: Boolean?,
        replyTo: Int?,
        markup: InlineKeyboardMarkup?
    ): CompletableFuture<out Message> {
        val body = toBody(
            mapOf(
                ApiConstants.CHAT_ID to chatId,
                ApiConstants.GAME_SHORT_NAME to gameShortName,
                ApiConstants.DISABLE_NOTIFICATION to disableNotification,
                ApiConstants.REPLY_TO_MESSAGE_ID to replyTo,
                ApiConstants.REPLY_MARKUP to markup
            )
        )
        return post(ApiConstants.METHOD_SEND_GAME, body)
    }

    override fun setGameScore(
        userId: Long,
        score: Int,
        force: Boolean?,
        disableEditMessage: Boolean?,
        chatId: Long?,
        messageId: Int?,
        inlineMessageId: String?
    ): CompletableFuture<out Message> {
        val body = toBody(
            mapOf(
                ApiConstants.USER_ID to userId,
                ApiConstants.SCORE to score,
                ApiConstants.FORCE to force,
                ApiConstants.DISABLE_EDIT_MESSAGE to disableEditMessage,
                ApiConstants.CHAT_ID to chatId,
                ApiConstants.MESSAGE_ID to messageId,
                ApiConstants.INLINE_MESSAGE_ID to inlineMessageId
            )
        )
        return post(ApiConstants.METHOD_SET_GAME_SCORE, body)
    }

    override fun getGameHighScores(
        userId: Long,
        chatId: Long?,
        messageId: Int?,
        inlineMessageId: String?
    ): CompletableFuture<out List<GameHighScore>> {
        val body = toBody(
            mapOf(
                ApiConstants.USER_ID to userId,
                ApiConstants.CHAT_ID to chatId,
                ApiConstants.MESSAGE_ID to messageId,
                ApiConstants.INLINE_MESSAGE_ID to inlineMessageId
            )
        )
        return post(ApiConstants.METHOD_GET_GAME_HIGH_SCORES, body)
    }

    override fun sendInvoice(
        chatId: Long,
        title: String,
        description: String,
        payload: String,
        providerToken: String,
        startParam: String,
        currency: String,
        prices: List<LabeledPrice>,
        providerData: String?,
        photoUrl: String?,
        photoSize: Int?,
        photoWidth: Int?,
        photoHeight: Int?,
        needName: Boolean?,
        needPhoneNumber: Boolean?,
        needEmail: Boolean?,
        needShippingAddress: Boolean?,
        sendPhoneNumberToProvider: Boolean?,
        sendEmailToProvider: Boolean?,
        isFlexible: Boolean?,
        disableNotification: Boolean?,
        replyTo: Int?,
        markup: InlineKeyboardMarkup?
    ): CompletableFuture<out Message> {
        val body = toBody(
            mapOf(
                ApiConstants.CHAT_ID to chatId,
                ApiConstants.TITLE to title,
                ApiConstants.DESCRIPTION to description,
                ApiConstants.PAYLOAD to payload,
                ApiConstants.PROVIDER_TOKEN to providerToken,
                ApiConstants.START_PARAMETER to startParam,
                ApiConstants.CURRENCY to currency,
                ApiConstants.PRICES to prices,
                ApiConstants.PROVIDER_DATA to providerData,
                ApiConstants.PHOTO_URL to photoUrl,
                ApiConstants.PHOTO_SIZE to photoSize,
                ApiConstants.PHOTO_WIDTH to photoWidth,
                ApiConstants.PHOTO_HEIGHT to photoHeight,
                ApiConstants.NEED_NAME to needName,
                ApiConstants.NEED_PHONE_NUMBER to needPhoneNumber,
                ApiConstants.NEED_EMAIL to needEmail,
                ApiConstants.NEED_SHIPPING_ADDRESS to needShippingAddress,
                ApiConstants.SEND_PHONE_NUMBER_TO_PROVIDER to sendPhoneNumberToProvider,
                ApiConstants.SEND_EMAIL_TO_PROVIDER to sendEmailToProvider,
                ApiConstants.IS_FLEXIBLE to isFlexible,
                ApiConstants.DISABLE_NOTIFICATION to disableNotification,
                ApiConstants.REPLY_TO_MESSAGE_ID to replyTo,
                ApiConstants.REPLY_MARKUP to markup
            )
        )
        return post(ApiConstants.METHOD_SEND_INVOICE, body)
    }

    override fun answerShippingQuery(
        shippingQueryId: String,
        ok: Boolean,
        shippingOptions: List<ShippingOption>?,
        errorMessage: String?
    ): CompletableFuture<out Boolean> {
        val body = toBody(
            mapOf(
                ApiConstants.SHIPPING_QUERY_ID to shippingQueryId,
                ApiConstants.OK to ok,
                ApiConstants.SHIPPING_OPTIONS to shippingOptions,
                ApiConstants.ERROR_MESSAGE to errorMessage
            )
        )
        return post(ApiConstants.METHOD_ANSWER_SHIPPING_QUERY, body)
    }

    override fun answerPreCheckoutQuery(
        preCheckoutQueryId: String,
        ok: Boolean,
        errorMessage: String?
    ): CompletableFuture<out Boolean> {
        val body = toBody(
            mapOf(
                ApiConstants.PRE_CHECKOUT_QUERY_ID to preCheckoutQueryId,
                ApiConstants.OK to ok,
                ApiConstants.ERROR_MESSAGE to errorMessage
            )
        )
        return post(ApiConstants.METHOD_ANSWER_PRE_CHECKOUT_QUERY, body)
    }

    override fun setPassportDataErrors(
        userId: Long,
        errors: List<PassportElementError>
    ): CompletableFuture<out Boolean> {
        val body = toBody(
            mapOf(
                ApiConstants.USER_ID to userId,
                ApiConstants.ERRORS to errors
            )
        )
        return post(ApiConstants.METHOD_SET_PASSPORT_DATA_ERRORS, body)
    }

    override fun sendPoll(
        chatId: Any,
        question: String,
        options: List<String>,
        anonymous: Boolean?,
        type: String?,
        allowsMultipleAnswers: Boolean?,
        correctOptionId: Int?,
        explanation: String?,
        explanationParseMode: String?,
        openPeriod: Int?,
        closeDate: Long?,
        closed: Boolean?,
        disableNotification: Boolean?,
        replyTo: Int?,
        markup: ReplyKeyboard?
    ): CompletableFuture<out Message> {
        val body = toBody(
            mapOf(
                ApiConstants.CHAT_ID to id(chatId),
                ApiConstants.QUESTION to question,
                ApiConstants.OPTIONS to options,
                ApiConstants.IS_ANONYMOUS to anonymous,
                ApiConstants.TYPE to type,
                ApiConstants.ALLOWS_MULTIPLE_ANSWERS to allowsMultipleAnswers,
                ApiConstants.CORRECT_OPTION_ID to correctOptionId,
                ApiConstants.EXPLANATION to explanation,
                ApiConstants.EXPLANATION_PARSE_MODE to explanationParseMode,
                ApiConstants.OPEN_PERIOD to explanationParseMode,
                ApiConstants.CLOSE_DATE to explanationParseMode,
                ApiConstants.IS_CLOSED to closed,
                ApiConstants.DISABLE_NOTIFICATION to disableNotification,
                ApiConstants.REPLY_TO_MESSAGE_ID to replyTo,
                ApiConstants.REPLY_MARKUP to markup
            )
        )
        return post(ApiConstants.METHOD_SEND_POLL, body)
    }

    override fun stopPoll(chatId: Any, messageId: Int, markup: InlineKeyboardMarkup?): CompletableFuture<out Poll> {
        val body = toBody(
            mapOf(
                ApiConstants.CHAT_ID to id(chatId),
                ApiConstants.MESSAGE_ID to messageId,
                ApiConstants.REPLY_MARKUP to markup
            )
        )
        return post(ApiConstants.METHOD_STOP_POLL, body)
    }

    override fun setChatPermissions(chatId: Any, permissions: ChatPermissions): CompletableFuture<out Boolean> {
        val body = toBody(
            mapOf(
                ApiConstants.CHAT_ID to id(chatId),
                ApiConstants.PERMISSIONS to permissions
            )
        )
        return post(ApiConstants.METHOD_SET_CHAT_PERMISSIONS, body)
    }

    override fun setChatAdministratorCustomTitle(
        chatId: Any,
        userId: Long,
        customTitle: String
    ): CompletableFuture<out Boolean> {
        val body = toBody(
            mapOf(
                ApiConstants.CHAT_ID to id(chatId),
                ApiConstants.USER_ID to userId,
                ApiConstants.CUSTOM_TITLE to customTitle
            )
        )
        return post(ApiConstants.METHOD_SET_CHAT_ADMINISTRATOR_CUSTOM_TITLE, body)
    }

    override fun deleteMessage(chatId: Any, messageId: Int): CompletableFuture<out Boolean> {
        val body = toBody(
            mapOf(
                ApiConstants.CHAT_ID to id(chatId),
                ApiConstants.MESSAGE_ID to messageId
            )
        )
        return post(ApiConstants.METHOD_DELETE_MESSAGE, body)
    }

    override fun sendDice(
        chatId: Any,
        emoji: String?,
        disableNotification: Boolean?,
        replyTo: Int?,
        markup: ReplyKeyboard?
    ): CompletableFuture<out Message> {
        val body = toBody(
            mapOf(
                ApiConstants.CHAT_ID to id(chatId),
                ApiConstants.EMOJI to emoji,
                ApiConstants.DISABLE_NOTIFICATION to disableNotification,
                ApiConstants.REPLY_TO_MESSAGE_ID to replyTo,
                ApiConstants.REPLY_MARKUP to markup
            )
        )
        return post(ApiConstants.METHOD_SEND_DICE, body)
    }
}
