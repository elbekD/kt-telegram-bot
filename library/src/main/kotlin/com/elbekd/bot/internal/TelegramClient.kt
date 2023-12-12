package com.elbekd.bot.internal

import com.elbekd.bot.api.TelegramApi
import com.elbekd.bot.model.ChatId
import com.elbekd.bot.model.TelegramApiError
import com.elbekd.bot.model.internal.*
import com.elbekd.bot.types.*
import com.elbekd.bot.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.concurrent.TimeUnit

internal class TelegramClient(token: String) : TelegramApi {
    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(60L, TimeUnit.SECONDS)
        .readTimeout(60L, TimeUnit.SECONDS)
        .writeTimeout(60L, TimeUnit.SECONDS)
        .build()

    private val url = ApiConstants.API_URL_FORMAT.format(token)

    private val json: Json = Json { ignoreUnknownKeys = true }

    @Serializable
    private class TelegramObject<out T>(
        @SerialName("ok") val ok: Boolean,
        @SerialName("result") val result: T? = null,
        @SerialName("error_code") val errorCode: Int? = null,
        @SerialName("description") val description: String? = null
    )

    private companion object {
        private val MEDIA_TYPE_JSON = "application/json".toMediaType()
        private val MEDIA_TYPE_OCTET_STREAM = "application/octet-stream".toMediaType()
    }

    private suspend inline fun <reified T> get(method: String): T = withContext(Dispatchers.IO) {
        val request = Request.Builder().url(url(method)).build()
        val response = httpClient.newCall(request).execute()
        val obj = fromJson<T>(response)
        if (!obj.ok) throw TelegramApiError(requireNotNull(obj.errorCode), requireNotNull(obj.description))
        requireNotNull(obj.result)
    }

    private suspend inline fun <reified T> post(method: String, body: RequestBody): T =
        withContext(Dispatchers.IO) {
            val request = Request.Builder().url(url(method)).post(body).build()
            val response = httpClient.newCall(request).execute()
            val obj = fromJson<T>(response)
            if (!obj.ok) throw TelegramApiError(requireNotNull(obj.errorCode), requireNotNull(obj.description))
            requireNotNull(obj.result)
        }

    private inline fun <reified T> fromJson(response: Response): TelegramObject<T> {
        return json.decodeFromString(response.body?.string()!!)
    }

    private inline fun <reified T> toJson(body: T) = json.encodeToString(body)

    private inline fun <reified T> T.body(): RequestBody {
        return toJson(this).toRequestBody(MEDIA_TYPE_JSON)
    }

    private inline fun <reified T> toBody(body: T): RequestBody {
        return toJson(body).toRequestBody(MEDIA_TYPE_JSON)
    }

    private fun url(method: String) = "$url/$method"

    private suspend fun sendFile(
        type: String,
        chatId: ChatId,
        file: SendingDocument,
        optionals: Map<String, String>,
        thumb: File? = null,
        method: String = type
    ): Message {
        val form = MultipartBody.Builder().also { it.setType(MultipartBody.FORM) }
        form.addFormDataPart(ApiConstants.CHAT_ID, chatId.toString())
        optionals.forEach { (k, v) -> form.addFormDataPart(k, v) }

        val content = when (file) {
            is SendingByteArray -> file.content.toRequestBody(null)
            is SendingString -> file.content.toRequestBody(null)
            is SendingFile -> file.file.asRequestBody(null)
        }
        form.addFormDataPart(type, file.fileName, content)

        thumb?.let {
            form.addFormDataPart("attach://${it.name}", it.name, it.asRequestBody(null))
        }

        return post("send${method.replaceFirstChar { it.uppercase() }}", form.build())
    }

    fun onStop() {
        httpClient.dispatcher.cancelAll()
    }

    override suspend fun getUpdates(
        offset: Int?,
        limit: Int?,
        timeout: Int?,
        allowedUpdates: List<AllowedUpdate>?
    ): List<Update> {
        val body = UpdateRequest(
            offset = offset,
            limit = limit,
            timeout = timeout,
            allowedUpdates = allowedUpdates
        ).body()

        return UpdateResponseMapper.map(post<List<UpdateResponse>>(ApiConstants.METHOD_GET_UPDATES, body))
    }

    override suspend fun setMyCommands(
        commands: List<BotCommand>,
        scope: BotCommandScope?,
        languageCode: String?
    ): Boolean {
        val body = SetMyCommands(
            commands = commands,
            scope = scope,
            languageCode = languageCode
        ).body()
        return post(ApiConstants.METHOD_SET_MY_COMMANDS, body)
    }

    override suspend fun deleteMyCommands(
        scope: BotCommandScope?,
        languageCode: String?
    ): Boolean {
        val body = DeleteMyCommands(
            scope = scope,
            languageCode = languageCode
        ).body()
        return post(ApiConstants.METHOD_DELETE_MY_COMMANDS, body)
    }

    override suspend fun getMyCommands(
        scope: BotCommandScope?,
        languageCode: String?
    ): List<BotCommand> {
        val body = GetMyCommands(
            scope = scope,
            languageCode = languageCode
        ).body()
        return post(ApiConstants.METHOD_GET_MY_COMMANDS, body)
    }

    override suspend fun setWebhook(
        url: String,
        certificate: File?,
        ipAddress: String?,
        maxConnections: Int?,
        allowedUpdates: List<AllowedUpdate>?,
        dropPendingUpdates: Boolean?,
        secretToken: String?,
    ): Boolean {
        val form = MultipartBody.Builder().also { it.setType(MultipartBody.FORM) }
        form.addFormDataPart(ApiConstants.URL, url)
        certificate?.let { form.addFormDataPart(ApiConstants.CERTIFICATE, it.name, it.asRequestBody(null)) }
        ipAddress?.let { form.addFormDataPart(ApiConstants.IP_ADDRESS, it) }
        maxConnections?.let { form.addFormDataPart(ApiConstants.MAX_CONNECTIONS, it.toString()) }
        allowedUpdates?.let { form.addFormDataPart(ApiConstants.ALLOWED_UPDATES, toJson(it)) }
        dropPendingUpdates?.let { form.addFormDataPart(ApiConstants.DROP_PENDING_UPDATES, it.toString()) }
        secretToken?.let { form.addFormDataPart(ApiConstants.SECRET_TOKEN, it) }
        return post(ApiConstants.METHOD_SET_WEBHOOK, form.build())
    }

    override suspend fun deleteWebhook(dropPendingUpdates: Boolean?): Boolean {
        val body = toBody(mapOf(ApiConstants.DROP_PENDING_UPDATES to dropPendingUpdates))
        return post(ApiConstants.METHOD_DELETE_WEBHOOK, body)
    }

    override suspend fun getWebhookInfo(): WebhookInfo = get(ApiConstants.METHOD_GET_WEBHOOK_INFO)

    override suspend fun getMe(): User = get(ApiConstants.METHOD_GET_ME)

    override suspend fun logOut(): Boolean = get(ApiConstants.METHOD_LOGOUT)

    override suspend fun close(): Boolean = get(ApiConstants.METHOD_CLOSE)

    override suspend fun sendMessage(
        chatId: ChatId,
        text: String,
        messageThreadId: Long?,
        parseMode: ParseMode?,
        entities: List<MessageEntity>?,
        disableWebPagePreview: Boolean?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ): Message {
        val body = SendMessage(
            chatId = chatId,
            text = text,
            messageThreadId = messageThreadId,
            parseMode = parseMode,
            entities = entities,
            disableWebPagePreview = disableWebPagePreview,
            disableNotification = disableNotification,
            protectContent = protectContent,
            replyToMessageId = replyToMessageId,
            allowSendingWithoutReply = allowSendingWithoutReply,
            replyMarkup = replyMarkup
        ).body()
        return post(ApiConstants.METHOD_SEND_MESSAGE, body)
    }

    override fun sendAsyncMessage(
        chatId: ChatId,
        text: String,
        messageThreadId: Long?,
        parseMode: ParseMode?,
        entities: List<MessageEntity>?,
        disableWebPagePreview: Boolean?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ) {
        CoroutineScope(Dispatchers.Default).launch {
            val body = SendMessage(
                chatId = chatId,
                text = text,
                messageThreadId = messageThreadId,
                parseMode = parseMode,
                entities = entities,
                disableWebPagePreview = disableWebPagePreview,
                disableNotification = disableNotification,
                protectContent = protectContent,
                replyToMessageId = replyToMessageId,
                allowSendingWithoutReply = allowSendingWithoutReply,
                replyMarkup = replyMarkup
            ).body()
            post(ApiConstants.METHOD_SEND_MESSAGE, body)
        }
    }

    override suspend fun forwardMessage(
        chatId: ChatId,
        fromChatId: ChatId,
        msgId: Long,
        messageThreadId: Long?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
    ): Message {
        val body = ForwardMessage(
            chatId = chatId,
            fromChatId = fromChatId,
            messageId = msgId,
            messageThreadId = messageThreadId,
            disableNotification = disableNotification,
            protectContent = protectContent,
        ).body()
        return post(ApiConstants.METHOD_FORWARD_MESSAGE, body)
    }

    override suspend fun copyMessage(
        chatId: ChatId,
        fromChatId: ChatId,
        messageId: Long,
        messageThreadId: Long?,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ): MessageId {
        val body = CopyMessage(
            chatId = chatId,
            fromChatId = fromChatId,
            messageId = messageId,
            messageThreadId = messageThreadId,
            caption = caption,
            parseMode = parseMode,
            captionEntities = captionEntities,
            disableNotification = disableNotification,
            protectContent = protectContent,
            replyToMessageId = replyToMessageId,
            allowSendingWithoutReply = allowSendingWithoutReply,
            replyMarkup = replyMarkup
        ).body()
        return post(ApiConstants.METHOD_COPY_MESSAGE, body)
    }

    override suspend fun sendPhoto(
        chatId: ChatId,
        photo: SendingDocument,
        messageThreadId: Long?,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        hasSpoiler: Boolean?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ): Message {
        val optionals = mutableMapOf<String, String>()
        caption?.let { optionals += ApiConstants.CAPTION to it }
        messageThreadId?.let { optionals += ApiConstants.MESSAGE_THREAD_ID to toJson(it) }
        parseMode?.let { optionals += ApiConstants.PARSE_MODE to toJson(it) }
        captionEntities?.let { optionals += ApiConstants.CAPTION_ENTITIES to toJson(it) }
        hasSpoiler?.let { ApiConstants.HAS_SPOILER to toJson(it) }
        disableNotification?.let { optionals += ApiConstants.DISABLE_NOTIFICATION to toJson(it) }
        protectContent?.let { optionals += ApiConstants.PROTECT_CONTENT to toJson(it) }
        replyToMessageId?.let { optionals += ApiConstants.REPLY_TO_MESSAGE_ID to toJson(it) }
        allowSendingWithoutReply?.let { optionals += ApiConstants.ALLOW_SENDING_WITHOUT_REPLY to toJson(it) }
        replyMarkup?.let { optionals += ApiConstants.REPLY_MARKUP to toJson(it) }

        return sendFile(
            ApiConstants.PHOTO,
            chatId,
            photo,
            optionals
        )
    }

    override suspend fun sendAudio(
        chatId: ChatId,
        audio: SendingDocument,
        messageThreadId: Long?,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        duration: Long?,
        performer: String?,
        title: String?,
        thumb: File?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ): Message {
        val optionals = mutableMapOf<String, String>()
        messageThreadId?.let { optionals += ApiConstants.MESSAGE_THREAD_ID to toJson(it) }
        caption?.let { optionals += ApiConstants.CAPTION to toJson(caption) }
        parseMode?.let { optionals += ApiConstants.PARSE_MODE to toJson(parseMode) }
        captionEntities?.let { optionals += ApiConstants.CAPTION_ENTITIES to toJson(captionEntities) }
        duration?.let { optionals += ApiConstants.DURATION to toJson(duration) }
        performer?.let { optionals += ApiConstants.PERFORMER to toJson(performer) }
        title?.let { optionals += ApiConstants.TITLE to toJson(title) }
        disableNotification?.let { optionals += ApiConstants.DISABLE_NOTIFICATION to toJson(thumb) }
        protectContent?.let { optionals += ApiConstants.PROTECT_CONTENT to toJson(disableNotification) }
        replyToMessageId?.let { optionals += ApiConstants.REPLY_TO_MESSAGE_ID to toJson(protectContent) }
        allowSendingWithoutReply?.let { optionals += ApiConstants.ALLOW_SENDING_WITHOUT_REPLY to toJson(replyToMessageId) }
        replyMarkup?.let { optionals += ApiConstants.REPLY_MARKUP to toJson(allowSendingWithoutReply) }

        return sendFile(
            ApiConstants.AUDIO,
            chatId,
            audio,
            optionals,
            thumb
        )
    }

    override suspend fun sendDocument(
        chatId: ChatId,
        document: SendingDocument,
        messageThreadId: Long?,
        thumb: File?,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        disableContentTypeDetection: Boolean?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ): Message {
        val optionals = mutableMapOf<String, String>()
        messageThreadId?.let { optionals += ApiConstants.MESSAGE_THREAD_ID to toJson(it) }
        caption?.let { optionals += ApiConstants.CAPTION to toJson(it) }
        parseMode?.let { optionals += ApiConstants.PARSE_MODE to toJson(it) }
        captionEntities?.let { optionals += ApiConstants.CAPTION_ENTITIES to toJson(it) }
        disableContentTypeDetection?.let { optionals += ApiConstants.DISABLE_CONTENT_TYPE_DETECTION to toJson(it) }
        disableNotification?.let { optionals += ApiConstants.DISABLE_NOTIFICATION to toJson(it) }
        protectContent?.let { optionals += ApiConstants.PROTECT_CONTENT to toJson(it) }
        replyToMessageId?.let { optionals += ApiConstants.REPLY_TO_MESSAGE_ID to toJson(it) }
        allowSendingWithoutReply?.let { optionals += ApiConstants.ALLOW_SENDING_WITHOUT_REPLY to toJson(it) }
        replyMarkup?.let { optionals += ApiConstants.REPLY_MARKUP to toJson(it) }

        return sendFile(
            ApiConstants.DOCUMENT,
            chatId,
            document,
            optionals,
            thumb
        )
    }

    override suspend fun sendVideo(
        chatId: ChatId,
        video: SendingDocument,
        messageThreadId: Long?,
        duration: Long?,
        width: Long?,
        height: Long?,
        thumb: File?,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        hasSpoiler: Boolean?,
        streaming: Boolean?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ): Message {
        val optionals = mutableMapOf<String, String>()
        messageThreadId?.let { optionals += ApiConstants.MESSAGE_THREAD_ID to toJson(it) }
        duration?.let { ApiConstants.DURATION to toJson(it) }
        width?.let { ApiConstants.WIDTH to toJson(it) }
        height?.let { ApiConstants.HEIGHT to toJson(it) }
        caption?.let { ApiConstants.CAPTION to toJson(it) }
        parseMode?.let { ApiConstants.PARSE_MODE to toJson(it) }
        captionEntities?.let { ApiConstants.CAPTION_ENTITIES to toJson(it) }
        hasSpoiler?.let { ApiConstants.HAS_SPOILER to toJson(it) }
        streaming?.let { ApiConstants.SUPPORTS_STREAMING to toJson(it) }
        disableNotification?.let { ApiConstants.DISABLE_NOTIFICATION to toJson(it) }
        protectContent?.let { ApiConstants.PROTECT_CONTENT to toJson(it) }
        replyToMessageId?.let { ApiConstants.REPLY_TO_MESSAGE_ID to toJson(it) }
        allowSendingWithoutReply?.let { ApiConstants.ALLOW_SENDING_WITHOUT_REPLY to toJson(it) }
        replyMarkup?.let { ApiConstants.REPLY_MARKUP to toJson(it) }

        return sendFile(
            ApiConstants.VIDEO,
            chatId,
            video,
            optionals,
            thumb
        )
    }

    override suspend fun sendAnimation(
        chatId: ChatId,
        animation: SendingDocument,
        messageThreadId: Long?,
        duration: Long?,
        width: Long?,
        height: Long?,
        thumb: File?,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        hasSpoiler: Boolean?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ): Message {
        val optionals = mutableMapOf<String, String>()
        messageThreadId?.let { optionals += ApiConstants.MESSAGE_THREAD_ID to toJson(it) }
        duration?.let { ApiConstants.DURATION to toJson(it) }
        width?.let { ApiConstants.WIDTH to toJson(it) }
        height?.let { ApiConstants.HEIGHT to toJson(it) }
        caption?.let { ApiConstants.CAPTION to toJson(it) }
        parseMode?.let { ApiConstants.PARSE_MODE to toJson(it) }
        captionEntities?.let { ApiConstants.CAPTION_ENTITIES to toJson(it) }
        hasSpoiler?.let { ApiConstants.HAS_SPOILER to toJson(it) }
        disableNotification?.let { ApiConstants.DISABLE_NOTIFICATION to toJson(it) }
        protectContent?.let { ApiConstants.PROTECT_CONTENT to toJson(it) }
        replyToMessageId?.let { ApiConstants.REPLY_TO_MESSAGE_ID to toJson(it) }
        allowSendingWithoutReply?.let { ApiConstants.ALLOW_SENDING_WITHOUT_REPLY to toJson(it) }
        replyMarkup?.let { ApiConstants.REPLY_MARKUP to toJson(it) }

        return sendFile(
            ApiConstants.ANIMATION,
            chatId,
            animation,
            optionals,
            thumb
        )
    }

    override suspend fun sendVoice(
        chatId: ChatId,
        voice: SendingDocument,
        messageThreadId: Long?,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        duration: Long?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ): Message {
        val optionals = mutableMapOf<String, String>()
        messageThreadId?.let { optionals += ApiConstants.MESSAGE_THREAD_ID to toJson(it) }
        caption?.let { ApiConstants.CAPTION to toJson(it) }
        parseMode?.let { ApiConstants.PARSE_MODE to toJson(it) }
        captionEntities?.let { ApiConstants.CAPTION_ENTITIES to toJson(it) }
        duration?.let { ApiConstants.DURATION to toJson(it) }
        disableNotification?.let { ApiConstants.DISABLE_NOTIFICATION to toJson(it) }
        protectContent?.let { ApiConstants.PROTECT_CONTENT to toJson(it) }
        replyToMessageId?.let { ApiConstants.REPLY_TO_MESSAGE_ID to toJson(it) }
        allowSendingWithoutReply?.let { ApiConstants.ALLOW_SENDING_WITHOUT_REPLY to toJson(it) }
        replyMarkup?.let { ApiConstants.REPLY_MARKUP to toJson(it) }

        return sendFile(
            ApiConstants.VOICE,
            chatId,
            voice,
            optionals
        )
    }

    override suspend fun sendVideoNote(
        chatId: ChatId,
        note: SendingDocument,
        messageThreadId: Long?,
        duration: Long?,
        length: Long?,
        thumb: File?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ): Message {
        val optionals = mutableMapOf<String, String>()
        messageThreadId?.let { optionals += ApiConstants.MESSAGE_THREAD_ID to toJson(it) }
        duration?.let { ApiConstants.DURATION to toJson(it) }
        disableNotification?.let { ApiConstants.DISABLE_NOTIFICATION to toJson(it) }
        protectContent?.let { ApiConstants.PROTECT_CONTENT to toJson(it) }
        replyToMessageId?.let { ApiConstants.REPLY_TO_MESSAGE_ID to toJson(it) }
        allowSendingWithoutReply?.let { ApiConstants.ALLOW_SENDING_WITHOUT_REPLY to toJson(it) }
        replyMarkup?.let { ApiConstants.REPLY_MARKUP to toJson(it) }

        return sendFile(
            ApiConstants.VIDEO_NOTE,
            chatId,
            note,
            optionals,
            thumb,
            ApiConstants.METHOD_VIDEO_NOTE
        )
    }

    override suspend fun sendMediaGroup(
        chatId: ChatId,
        media: List<InputMedia>,
        messageThreadId: Long?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?
    ): ArrayList<Message> {
        val form = MultipartBody.Builder().also { it.setType(MultipartBody.FORM) }
        form.addFormDataPart(ApiConstants.CHAT_ID, chatId.toString())

        media.forEach { inputMedia ->
            inputMedia.attachment?.let {
                form.addFormDataPart(
                    inputMedia.media.split("//")[1],
                    inputMedia.media,
                    it.asRequestBody(MEDIA_TYPE_OCTET_STREAM)
                )
            }

            inputMedia.thumb?.let {
                when (it) {
                    is File -> form.addFormDataPart(
                        "attach://${it.name}",
                        it.name,
                        it.asRequestBody(MEDIA_TYPE_OCTET_STREAM)
                    )

                    is String -> form.addFormDataPart(ApiConstants.THUMB, it)
                    else -> throw IllegalArgumentException("Neither file nor string")
                }
            }
        }

        form.addFormDataPart(ApiConstants.MEDIA, toJson(media))
        messageThreadId?.let { form.addFormDataPart(ApiConstants.MESSAGE_THREAD_ID, it.toString()) }
        disableNotification?.let { form.addFormDataPart(ApiConstants.DISABLE_NOTIFICATION, it.toString()) }
        protectContent?.let { form.addFormDataPart(ApiConstants.PROTECT_CONTENT, it.toString()) }
        replyToMessageId?.let { form.addFormDataPart(ApiConstants.REPLY_TO_MESSAGE_ID, it.toString()) }
        allowSendingWithoutReply?.let { form.addFormDataPart(ApiConstants.ALLOW_SENDING_WITHOUT_REPLY, it.toString()) }

        return post(ApiConstants.METHOD_SEND_MEDIA_GROUP, form.build())
    }

    override suspend fun sendLocation(
        chatId: ChatId,
        latitude: Float,
        longitude: Float,
        messageThreadId: Long?,
        horizontalAccuracy: Float?,
        livePeriod: Long?,
        heading: Long?,
        proximityAlertRadius: Long?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ): Message {
        val body = SendLocation(
            chatId = chatId,
            latitude = latitude,
            longitude = longitude,
            messageThreadId = messageThreadId,
            horizontalAccuracy = horizontalAccuracy,
            livePeriod = livePeriod,
            heading = heading,
            proximityAlertRadius = proximityAlertRadius,
            disableNotification = disableNotification,
            protectContent = protectContent,
            replyToMessageId = replyToMessageId,
            allowSendingWithoutReply = allowSendingWithoutReply,
            replyMarkup = replyMarkup
        ).body()
        return post(ApiConstants.METHOD_SEND_LOCATION, body)
    }

    override suspend fun editMessageLiveLocation(
        latitude: Float,
        longitude: Float,
        horizontalAccuracy: Float?,
        heading: Long?,
        proximityAlertRadius: Long?,
        chatId: ChatId?,
        messageId: Long?,
        inlineMessageId: String?,
        replyMarkup: InlineKeyboardMarkup?
    ): Message {
        val body = EditMessageLiveLocation(
            chatId = chatId,
            messageId = messageId,
            inlineMessageId = inlineMessageId,
            latitude = latitude,
            longitude = longitude,
            horizontalAccuracy = horizontalAccuracy,
            heading = heading,
            proximityAlertRadius = proximityAlertRadius,
            replyMarkup = replyMarkup
        ).body()
        return post(ApiConstants.METHOD_EDIT_MESSAGE_LIVE_LOCATION, body)
    }

    override suspend fun stopMessageLiveLocation(
        chatId: ChatId?,
        messageId: Long?,
        inlineMessageId: String?,
        replyMarkup: InlineKeyboardMarkup?
    ): Message {
        val body = StopMessageLiveLocation(
            chatId = chatId,
            messageId = messageId,
            inlineMessageId = inlineMessageId,
            replyMarkup = replyMarkup
        ).body()
        return post(ApiConstants.METHOD_STOP_MESSAGE_LIVE_LOCATION, body)
    }

    override suspend fun sendVenue(
        chatId: ChatId,
        latitude: Float,
        longitude: Float,
        title: String,
        address: String,
        messageThreadId: Long?,
        foursquareId: String?,
        foursquareType: String?,
        googlePlaceId: String?,
        googlePlaceType: String?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ): Message {
        val body = SendVenue(
            chatId = chatId,
            latitude = latitude,
            longitude = longitude,
            title = title,
            address = address,
            messageThreadId = messageThreadId,
            foursquareId = foursquareId,
            foursquareType = foursquareType,
            googlePlaceId = googlePlaceId,
            googlePlaceType = googlePlaceType,
            disableNotification = disableNotification,
            protectContent = protectContent,
            replyToMessageId = replyToMessageId,
            allowSendingWithoutReply = allowSendingWithoutReply,
            replyMarkup = replyMarkup
        ).body()
        return post(ApiConstants.METHOD_SEND_VENUE, body)
    }

    override suspend fun sendContact(
        chatId: ChatId,
        phoneNumber: String,
        firstName: String,
        messageThreadId: Long?,
        lastName: String?,
        vcard: String?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ): Message {
        val body = SendContact(
            chatId = chatId,
            phone = phoneNumber,
            firstName = firstName,
            messageThreadId = messageThreadId,
            lastName = lastName,
            vcard = vcard,
            disableNotification = disableNotification,
            protectContent = protectContent,
            replyToMessageId = replyToMessageId,
            allowSendingWithoutReply = allowSendingWithoutReply,
            replyMarkup = replyMarkup
        ).body()
        return post(ApiConstants.METHOD_SEND_CONTACT, body)
    }

    override suspend fun sendPoll(
        chatId: ChatId,
        question: String,
        options: List<String>,
        messageThreadId: Long?,
        isAnonymous: Boolean?,
        type: String?,
        allowsMultipleAnswers: Boolean?,
        correctOptionId: Long?,
        explanation: String?,
        explanationParseMode: String?,
        explanationEntities: List<MessageEntity>?,
        openPeriod: Long?,
        closeDate: Long?,
        isClosed: Boolean?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ): Message {
        val body = SendPoll(
            chatId = chatId,
            question = question,
            messageThreadId = messageThreadId,
            options = options,
            isAnonymous = isAnonymous,
            type = type,
            allowsMultipleAnswers = allowsMultipleAnswers,
            correctOptionId = correctOptionId,
            explanation = explanation,
            explanationParseMode = explanationParseMode,
            explanationEntities = explanationEntities,
            isClosed = isClosed,
            disableNotification = disableNotification,
            protectContent = protectContent,
            replyToMessageId = replyToMessageId,
            allowSendingWithoutReply = allowSendingWithoutReply,
            replyMarkup = replyMarkup
        ).body()
        return post(ApiConstants.METHOD_SEND_POLL, body)
    }

    override suspend fun sendDice(
        chatId: ChatId,
        messageThreadId: Long?,
        emoji: String?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ): Message {
        val body = SendDice(
            chatId = chatId,
            messageThreadId = messageThreadId,
            emoji = emoji,
            disableNotification = disableNotification,
            protectContent = protectContent,
            replyToMessageId = replyToMessageId,
            allowSendingWithoutReply = allowSendingWithoutReply,
            replyMarkup = replyMarkup
        ).body()
        return post(ApiConstants.METHOD_SEND_DICE, body)
    }

    override suspend fun sendChatAction(
        chatId: ChatId,
        action: Action,
        messageThreadId: Long?,
    ): Boolean {
        val body = SendChatAction(
            chatId = chatId,
            action = action,
            messageThreadId = messageThreadId,
        ).body()
        return post(ApiConstants.METHOD_SEND_CHAT_ACTION, body)
    }

    override suspend fun setChatMenuButton(
        chatId: Long?,
        menuButton: MenuButton?
    ): Boolean {
        val body = SetChatMenuButton(
            chatId = chatId,
            menuButton = menuButton,
        ).body()
        return post("setChatMenuButton", body)
    }

    override suspend fun getChatMenuButton(chatId: Long?): MenuButton {
        val body = GetChatMenuButton(
            chatId = chatId,
        ).body()
        return post("getChatMenuButton", body)
    }

    override suspend fun setMyDefaultAdministratorRights(
        rights: ChatAdministratorRights?,
        forChannels: Boolean?
    ): Boolean {
        val body = SetMyDefaultAdministratorRights(
            rights = rights,
            forChannels = forChannels,
        ).body()
        return post("setMyDefaultAdministratorRights", body)
    }

    override suspend fun getMyDefaultAdministratorRights(forChannels: Boolean?): ChatAdministratorRights {
        val body = GetMyDefaultAdministratorRights(
            forChannels = forChannels,
        ).body()
        return post("getMyDefaultAdministratorRights", body)
    }

    override suspend fun getForumTopicIconStickers(): List<Sticker> {
        return get("getForumTopicIconStickers")
    }

    override suspend fun createForumTopic(
        chatId: ChatId,
        name: String,
        iconColor: Int?,
        iconCustomEmojiId: String?,
    ): ForumTopic {
        val body = CreateForumTopic(
            chatId = chatId,
            name = name,
            iconColor = iconColor,
            iconCustomEmojiId = iconCustomEmojiId,
        ).body()
        return post("createForumTopic", body)
    }

    override suspend fun editForumTopic(
        chatId: ChatId,
        messageThreadId: Long,
        name: String?,
        iconCustomEmojiId: String?,
    ): Boolean {
        val body = EditForumTopic(
            chatId = chatId,
            messageThreadId = messageThreadId,
            name = name,
            iconCustomEmojiId = iconCustomEmojiId,
        ).body()
        return post("editForumTopic", body)
    }

    override suspend fun closeForumTopic(chatId: ChatId, messageThreadId: Long): Boolean {
        val body = CloseForumTopic(
            chatId = chatId,
            messageThreadId = messageThreadId,
        ).body()
        return post("closeForumTopic", body)
    }

    override suspend fun reopenForumTopic(chatId: ChatId, messageThreadId: Long): Boolean {
        val body = ReopenForumTopic(
            chatId = chatId,
            messageThreadId = messageThreadId,
        ).body()
        return post("reopenForumTopic", body)
    }

    override suspend fun deleteForumTopic(chatId: ChatId, messageThreadId: Long): Boolean {
        val body = DeleteForumTopic(
            chatId = chatId,
            messageThreadId = messageThreadId,
        ).body()
        return post("deleteForumTopic", body)
    }

    override suspend fun unpinAllForumTopicMessages(chatId: ChatId, messageThreadId: Long): Boolean {
        val body = UnpinAllForumTopicMessages(
            chatId = chatId,
            messageThreadId = messageThreadId,
        ).body()
        return post("unpinAllForumTopicMessages", body)
    }

    override suspend fun editGeneralForumTopic(chatId: ChatId, name: String): Boolean {
        val body = EditGeneralForumTopic(
            chatId = chatId,
            name = name,
        ).body()
        return post("editGeneralForumTopic", body)
    }

    override suspend fun closeGeneralForumTopic(chatId: ChatId): Boolean {
        val body = CloseGeneralForumTopic(
            chatId = chatId,
        ).body()
        return post("closeGeneralForumTopic", body)
    }

    override suspend fun reopenGeneralForumTopic(chatId: ChatId): Boolean {
        val body = ReopenGeneralForumTopic(
            chatId = chatId,
        ).body()
        return post("reopenGeneralForumTopic", body)
    }

    override suspend fun hideGeneralForumTopic(chatId: ChatId): Boolean {
        val body = HideGeneralForumTopic(
            chatId = chatId,
        ).body()
        return post("hideGeneralForumTopic", body)
    }

    override suspend fun unhideGeneralForumTopic(chatId: ChatId): Boolean {
        val body = UnhideGeneralForumTopic(
            chatId = chatId,
        ).body()
        return post("unhideGeneralForumTopic", body)
    }

    override suspend fun banChatSenderChat(
        chatId: ChatId,
        senderChatId: Long
    ): Boolean {
        val body = BanChatSenderChat(
            chatId = chatId,
            senderChatId = senderChatId
        ).body()
        return post(ApiConstants.METHOD_BAN_CHAT_SENDER_CHAT, body)
    }

    override suspend fun unbanChatSenderChat(
        chatId: ChatId,
        senderChatId: Long
    ): Boolean {
        val body = UnbanChatSenderChat(
            chatId = chatId,
            senderChatId = senderChatId
        ).body()
        return post(ApiConstants.METHOD_UNBAN_CHAT_SENDER_CHAT, body)
    }

    override suspend fun getUserProfilePhotos(
        userId: Long,
        offset: Long?,
        limit: Long?
    ): UserProfilePhotos {
        val body = GetUserProfilePhotos(
            userId = userId,
            offset = offset,
            limit = limit
        ).body()
        return post(ApiConstants.METHOD_GET_USER_PROFILE_PHOTOS, body)
    }

    override suspend fun getFile(fileId: String): com.elbekd.bot.types.File {
        val body = GetFile(fileId = fileId).body()
        return post(ApiConstants.METHOD_GET_FILE, body)
    }

    override suspend fun banChatMember(
        chatId: ChatId,
        userId: Long,
        untilDate: Long?,
        revokeMessages: Boolean?
    ): Boolean {
        val body = BanChatMember(
            chatId = chatId,
            userId = userId,
            untilDate = untilDate,
            revokeMessages = revokeMessages
        ).body()
        return post(ApiConstants.METHOD_BAN_CHAT_MEMBER, body)
    }

    override suspend fun unbanChatMember(
        chatId: ChatId,
        userId: Long,
        onlyIfBanned: Boolean?
    ): Boolean {
        val body = UnbanChatMember(
            chatId = chatId,
            userId = userId,
            onlyIfBanned = onlyIfBanned
        ).body()
        return post(ApiConstants.METHOD_UNBAN_CHAT_MEMBER, body)
    }

    override suspend fun restrictChatMember(
        chatId: ChatId,
        userId: Long,
        permissions: ChatPermissions,
        useIndependentChatPermissions: Boolean?,
        untilDate: Long?,
    ): Boolean {
        val body = RestrictChatMember(
            chatId = chatId,
            userId = userId,
            permissions = permissions,
            useIndependentChatPermissions = useIndependentChatPermissions,
            untilDate = untilDate
        ).body()
        return post(ApiConstants.METHOD_RESTRICT_CHAT_MEMBER, body)
    }

    override suspend fun promoteChatMember(
        chatId: ChatId,
        userId: Long,
        isAnonymous: Boolean?,
        canManageChat: Boolean?,
        canPostMessages: Boolean?,
        canEditMessages: Boolean?,
        canDeleteMessages: Boolean?,
        canManageVideoChats: Boolean?,
        canRestrictMembers: Boolean?,
        canPromoteMembers: Boolean?,
        canChangeInfo: Boolean?,
        canInviteUsers: Boolean?,
        canPinMessages: Boolean?,
        canManageTopics: Boolean?,
    ): Boolean {
        val body = PromoteChatMember(
            chatId = chatId,
            userId = userId,
            isAnonymous = isAnonymous,
            canManageChat = canManageChat,
            canPostMessages = canPostMessages,
            canEditMessages = canEditMessages,
            canDeleteMessages = canDeleteMessages,
            canManageVideoChats = canManageVideoChats,
            canRestrictMembers = canRestrictMembers,
            canPromoteMembers = canPromoteMembers,
            canChangeInfo = canChangeInfo,
            canInviteUsers = canInviteUsers,
            canPinMessages = canPinMessages,
            canManageTopics = canManageTopics,
        ).body()
        return post(ApiConstants.METHOD_PROMOTE_CHAT_MEMBER, body)
    }

    override suspend fun setChatAdministratorCustomTitle(
        chatId: ChatId,
        userId: Long,
        customTitle: String
    ): Boolean {
        val body = SetChatAdministratorCustomTitle(
            chatId = chatId,
            userId = userId,
            customTitle = customTitle
        ).body()
        return post(ApiConstants.METHOD_SET_CHAT_ADMINISTRATOR_CUSTOM_TITLE, body)
    }

    override suspend fun setChatPermissions(
        chatId: ChatId,
        permissions: ChatPermissions,
        useIndependentChatPermissions: Boolean?,
    ): Boolean {
        val body = SetChatPermissions(
            chatId = chatId,
            permissions = permissions,
            useIndependentChatPermissions = useIndependentChatPermissions,
        ).body()
        return post(ApiConstants.METHOD_SET_CHAT_PERMISSIONS, body)
    }

    override suspend fun exportChatInviteLink(chatId: ChatId): String {
        val body = ExportChatInviteLink(chatId = chatId).body()
        return post(ApiConstants.METHOD_EXPORT_CHAT_INVITE_LINK, body)
    }

    override suspend fun createChatInviteLink(
        chatId: ChatId,
        name: String?,
        expireDate: Long?,
        memberLimit: Long?,
        createsJoinRequest: Boolean?,
    ): ChatInviteLink {
        val body = CreateChatInviteLink(
            chatId = chatId,
            name = name,
            expireDate = expireDate,
            memberLimit = memberLimit,
            createsJoinRequest = createsJoinRequest,
        ).body()
        return post(ApiConstants.METHOD_CREATE_CHAT_INVITE_LINK, body)
    }

    override suspend fun editChatInviteLink(
        chatId: ChatId,
        inviteLink: String,
        name: String?,
        expireDate: Long?,
        memberLimit: Long?,
        createsJoinRequest: Boolean?,
    ): ChatInviteLink {
        val body = EditChatInviteLink(
            chatId = chatId,
            inviteLink = inviteLink,
            name = name,
            expireDate = expireDate,
            memberLimit = memberLimit,
            createsJoinRequest = createsJoinRequest,
        ).body()
        return post(ApiConstants.METHOD_EDIT_CHAT_INVITE_LINK, body)
    }

    override suspend fun revokeChatInviteLink(
        chatId: ChatId,
        inviteLink: String
    ): ChatInviteLink {
        val body = RevokeChatInviteLink(
            chatId = chatId,
            inviteLink = inviteLink
        ).body()
        return post(ApiConstants.METHOD_REVOKE_CHAT_INVITE_LINK, body)
    }

    override suspend fun approveChatJoinRequest(
        chatId: ChatId,
        inviteLink: String
    ): Boolean {
        val body = ApproveChatJoinRequest(
            chatId = chatId,
            inviteLink = inviteLink
        ).body()
        return post(ApiConstants.METHOD_APPROVE_CHAT_JOIN_REQUEST, body)
    }

    override suspend fun declineChatJoinRequest(
        chatId: ChatId,
        inviteLink: String
    ): Boolean {
        val body = DeclineChatJoinRequest(
            chatId = chatId,
            inviteLink = inviteLink
        ).body()
        return post(ApiConstants.METHOD_DECLINE_CHAT_JOIN_REQUEST, body)
    }

    // todo InputFile
    override suspend fun setChatPhoto(chatId: ChatId, photo: Any): Boolean {
        val form = MultipartBody.Builder().also { it.setType(MultipartBody.FORM) }
        form.addFormDataPart(ApiConstants.CHAT_ID, chatId.toString())
        when (photo) {
            is File -> form.addFormDataPart(ApiConstants.PHOTO, photo.name, photo.asRequestBody(null))
            is String -> form.addFormDataPart(ApiConstants.PHOTO, photo)
            else -> throw IllegalArgumentException("<photo> neither java.io.File nor string")
        }
        return post(ApiConstants.METHOD_SET_CHAT_PHOTO, form.build())
    }

    override suspend fun deleteChatPhoto(chatId: ChatId): Boolean {
        val body = DeleteChatPhoto(chatId).body()
        return post(ApiConstants.METHOD_DELETE_CHAT_PHOTO, body)
    }

    override suspend fun setChatTitle(chatId: ChatId, title: String): Boolean {
        val body = SetChatTitle(
            chatId = chatId,
            title = title
        ).body()
        return post(ApiConstants.METHOD_SET_CHAT_TITLE, body)
    }

    override suspend fun setChatDescription(chatId: ChatId, description: String): Boolean {
        val body = SetChatDescription(
            chatId = chatId,
            description = description
        ).body()
        return post(ApiConstants.METHOD_SET_CHAT_DESCRIPTION, body)
    }

    override suspend fun pinChatMessage(
        chatId: ChatId,
        messageId: Long,
        disableNotification: Boolean?
    ): Boolean {
        val body = PinChatMessage(
            chatId = chatId,
            messageId = messageId,
            disableNotification = disableNotification
        ).body()
        return post(ApiConstants.METHOD_PIN_CHAT_MESSAGE, body)
    }

    override suspend fun unpinChatMessage(chatId: ChatId, messageId: Long?): Boolean {
        val body = UnpinChatMessage(
            chatId = chatId,
            messageId = messageId
        ).body()
        return post(ApiConstants.METHOD_UNPIN_CHAT_MESSAGE, body)
    }

    override suspend fun unpinAllChatMessages(chatId: ChatId): Boolean {
        val body = UnpinAllChatMessages(chatId = chatId).body()
        return post(ApiConstants.METHOD_UNPIN_ALL_CHAT_MESSAGES, body)
    }

    override suspend fun leaveChat(chatId: ChatId): Boolean {
        val body = LeaveChat(chatId = chatId).body()
        return post(ApiConstants.METHOD_LEAVE_CHAT, body)
    }

    override suspend fun getChat(chatId: ChatId): Chat {
        val body = GetChat(chatId = chatId).body()
        return post(ApiConstants.METHOD_GET_CHAT, body)
    }

    override suspend fun getChatAdministrators(chatId: ChatId): ArrayList<ChatMember> {
        val body = GetChatAdministrators(chatId = chatId).body()
        return post(ApiConstants.METHOD_GET_CHAT_ADMINISTRATORS, body)
    }

    override suspend fun getChatMemberCount(chatId: ChatId): Long {
        val body = GetChatMembersCount(chatId = chatId).body()
        return post(ApiConstants.METHOD_GET_CHAT_MEMBER_COUNT, body)
    }

    override suspend fun getChatMember(chatId: ChatId, userId: Long): ChatMember {
        val body = GetChatMember(
            chatId = chatId,
            userId = userId
        ).body()
        return post(ApiConstants.METHOD_GET_CHAT_MEMBER, body)
    }

    override suspend fun setChatStickerSet(chatId: ChatId, stickerSetName: String): Boolean {
        val body = SetChatStickerSet(
            chatId = chatId,
            stickerSetName = stickerSetName
        ).body()
        return post(ApiConstants.METHOD_SET_CHAT_STICKER_SET, body)
    }

    override suspend fun deleteChatStickerSet(chatId: ChatId): Boolean {
        val body = DeleteChatStickerSet(chatId = chatId).body()
        return post(ApiConstants.METHOD_DELETE_CHAT_STICKER_SET, body)
    }

    override suspend fun answerCallbackQuery(
        callbackQueryId: String,
        text: String?,
        showAlert: Boolean?,
        url: String?,
        cacheTime: Long?
    ): Boolean {
        val body = AnswerCallbackQuery(
            callbackQueryId = callbackQueryId,
            text = text,
            showAlert = showAlert,
            url = url,
            cacheTime = cacheTime
        ).body()
        return post(ApiConstants.METHOD_ANSWER_CALLBACK_QUERY, body)
    }

    override suspend fun answerInlineQuery(
        inlineQueryId: String,
        results: List<InlineQueryResult>,
        cacheTime: Int?,
        isPersonal: Boolean?,
        nextOffset: String?,
        switchPmText: String?,
        switchPmParameter: String?
    ): Boolean {
        val body = AnswerInlineQuery(
            inlineQueryId = inlineQueryId,
            results = results,
            cacheTime = cacheTime,
            isPersonal = isPersonal,
            nextOffset = nextOffset,
            switchPmText = switchPmText,
            switchPmParameter = switchPmParameter
        ).body()
        return post(ApiConstants.METHOD_ANSWER_INLINE_QUERY, body)
    }

    override suspend fun answerWebAppQuery(
        webAppQueryId: String,
        result: InlineQueryResult
    ): SentWebAppMessage {
        val body = AnswerWebAppQuery(
            webAppQueryId = webAppQueryId,
            result = result,
        ).body()
        return post(ApiConstants.METHOD_WEB_APP_QUERY, body)
    }

    override suspend fun editMessageText(
        chatId: ChatId?,
        messageId: Long?,
        inlineMessageId: String?,
        text: String,
        parseMode: ParseMode?,
        entities: List<MessageEntity>?,
        disableWebPagePreview: Boolean?,
        replyMarkup: InlineKeyboardMarkup?
    ): Message {
        val body = EditMessageText(
            chatId = chatId,
            messageId = messageId,
            inlineMessageId = inlineMessageId,
            text = text,
            parseMode = parseMode,
            entities = entities,
            disableWebPagePreview = disableWebPagePreview,
            replyMarkup = replyMarkup
        ).body()
        return post(ApiConstants.METHOD_EDIT_MESSAGE_TEXT, body)
    }

    override suspend fun editMessageCaption(
        chatId: ChatId?,
        messageId: Long?,
        inlineMessageId: String?,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        replyMarkup: InlineKeyboardMarkup?
    ): Message {
        val body = EditMessageCaption(
            chatId = chatId,
            messageId = messageId,
            inlineMessageId = inlineMessageId,
            caption = caption,
            parseMode = parseMode,
            captionEntities = captionEntities,
            replyMarkup = replyMarkup
        ).body()
        return post(ApiConstants.METHOD_EDIT_MESSAGE_CAPTION, body)
    }

    override suspend fun editMessageMedia(
        chatId: ChatId?,
        messageId: Long?,
        inlineMessageId: String?,
        media: InputMedia,
        replyMarkup: InlineKeyboardMarkup?
    ): Message {
        val form = MultipartBody.Builder().also { it.setType(MultipartBody.FORM) }

        if (inlineMessageId != null) {
            form.addFormDataPart(ApiConstants.INLINE_MESSAGE_ID, inlineMessageId)
        } else {
            form.addFormDataPart(ApiConstants.CHAT_ID, requireNotNull(chatId).toString())
            form.addFormDataPart(ApiConstants.MESSAGE_ID, requireNotNull(messageId).toString())
        }

        media.thumb?.let {
            when (it) {
                is File -> form.addFormDataPart("attach://${it.name}", it.name, it.asRequestBody(null))
                is String -> form.addFormDataPart(ApiConstants.THUMB, it)
                else -> throw IllegalArgumentException("Neither file nor string")
            }
        }

        media.attachment?.let {
            form.addFormDataPart(
                media.media.split("//")[1],
                media.media,
                it.asRequestBody(MEDIA_TYPE_OCTET_STREAM)
            )
        }
        form.addFormDataPart(ApiConstants.MEDIA, toJson(media))
        replyMarkup?.let { form.addFormDataPart(ApiConstants.REPLY_MARKUP, toJson(it)) }

        return post(ApiConstants.METHOD_EDIT_MESSAGE_MEDIA, form.build())
    }

    override suspend fun editMessageReplyMarkup(
        chatId: ChatId?,
        messageId: Long?,
        inlineMessageId: String?,
        replyMarkup: InlineKeyboardMarkup?
    ): Message {
        val body = EditMessageReplyMarkup(
            chatId = chatId,
            messageId = messageId,
            inlineMessageId = inlineMessageId,
            replyMarkup = replyMarkup
        ).body()
        return post(ApiConstants.METHOD_EDIT_MESSAGE_REPLY_MARKUP, body)
    }

    override suspend fun stopPoll(chatId: ChatId, messageId: Long, replyMarkup: InlineKeyboardMarkup?): Poll {
        val body = StopPoll(
            chatId = chatId,
            messageId = messageId,
            replyMarkup = replyMarkup
        ).body()
        return post(ApiConstants.METHOD_STOP_POLL, body)
    }

    override suspend fun deleteMessage(chatId: ChatId, messageId: Long): Boolean {
        val body = DeleteMessage(
            chatId = chatId,
            messageId = messageId
        ).body()
        return post(ApiConstants.METHOD_DELETE_MESSAGE, body)
    }

    override suspend fun sendSticker(
        chatId: ChatId,
        sticker: Any,
        messageThreadId: Long?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ): Message {
        val form = MultipartBody.Builder().also { it.setType(MultipartBody.FORM) }

        form.addFormDataPart(ApiConstants.CHAT_ID, chatId.toString())

        when (sticker) {
            is File -> form.addFormDataPart(ApiConstants.STICKER, sticker.name, sticker.asRequestBody(null))
            is String -> form.addFormDataPart(ApiConstants.STICKER, sticker)
        }

        messageThreadId?.let { form.addFormDataPart(ApiConstants.MESSAGE_THREAD_ID, it.toString()) }
        disableNotification?.let { form.addFormDataPart(ApiConstants.DISABLE_NOTIFICATION, it.toString()) }
        protectContent?.let { form.addFormDataPart(ApiConstants.PROTECT_CONTENT, it.toString()) }
        replyToMessageId?.let { form.addFormDataPart(ApiConstants.REPLY_TO_MESSAGE_ID, it.toString()) }
        replyMarkup?.let { form.addFormDataPart(ApiConstants.REPLY_MARKUP, toJson(it)) }
        allowSendingWithoutReply?.let { form.addFormDataPart(ApiConstants.ALLOW_SENDING_WITHOUT_REPLY, it.toString()) }
        return post(ApiConstants.METHOD_SEND_STICKER, form.build())
    }

    override suspend fun getStickerSet(name: String): StickerSet {
        val body = GetStickerSet(name).body()
        return post(ApiConstants.METHOD_GET_STICKER_SET, body)
    }

    override suspend fun getCustomEmojiStickers(customEmojiIds: List<String>): List<Sticker> {
        val body = GetCustomEmojiStickers(customEmojiIds).body()
        return post(ApiConstants.METHOD_GET_CUSTOM_EMOJI_STICKERS, body)
    }

    override suspend fun uploadStickerFile(userId: Long, pngSticker: File): com.elbekd.bot.types.File {
        val form = MultipartBody.Builder().also { it.setType(MultipartBody.FORM) }
        form.addFormDataPart(ApiConstants.USER_ID, userId.toString())
        form.addFormDataPart(ApiConstants.PNG_STICKER, pngSticker.name, pngSticker.asRequestBody(null))
        return post(ApiConstants.METHOD_UPLOAD_STICKER_FILE, form.build())
    }

    override suspend fun createNewStickerSet(
        userId: Long,
        name: String,
        title: String,
        emojis: String,
        pngSticker: Any?,
        tgsSticker: File?,
        webmSticker: File?,
        stickerType: String?,
        containsMask: Boolean?,
        maskPosition: MaskPosition?
    ): Boolean {
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
                    pngSticker.asRequestBody(null)
                )

                is String -> addFormDataPart(ApiConstants.PNG_STICKER, pngSticker)
                else -> throw IllegalArgumentException()
            }
            tgsSticker?.let {
                addFormDataPart(
                    ApiConstants.TGS_STICKER,
                    tgsSticker.name,
                    tgsSticker.asRequestBody(null)
                )
            }
        }
        return post(ApiConstants.METHOD_CREATE_NEW_STICKER_SET, form.build())
    }

    override suspend fun addStickerToSet(
        userId: Long,
        name: String,
        emojis: String,
        pngSticker: Any?,
        tgsSticker: File?,
        webmSticker: File?,
        maskPosition: MaskPosition?
    ): Boolean {
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
                    pngSticker.asRequestBody(null)
                )

                is String -> addFormDataPart(ApiConstants.PNG_STICKER, pngSticker)
                else -> throw IllegalArgumentException()
            }
            tgsSticker?.let {
                addFormDataPart(
                    ApiConstants.TGS_STICKER,
                    tgsSticker.name,
                    tgsSticker.asRequestBody(null)
                )
            }
        }
        return post(ApiConstants.METHOD_ADD_STICKER_TO_SET, form.build())
    }

    override suspend fun setStickerPositionInSet(sticker: String, position: Int): Boolean {
        val body = SetStickerPositionInSet(
            sticker = sticker,
            position = position
        ).body()
        return post(ApiConstants.METHOD_SET_STICKER_POSITION_IN_SET, body)
    }

    override suspend fun deleteStickerFromSet(sticker: String): Boolean {
        val body = DeleteStickerFromSet(sticker).body()
        return post(ApiConstants.METHOD_DELETE_STICKER_FROM_SET, body)
    }

    override suspend fun setStickerSetThumb(name: String, userId: Long, thumb: Any?): Boolean {
        val form = MultipartBody.Builder().also { it.setType(MultipartBody.FORM) }
        form.addFormDataPart(ApiConstants.NAME, name)
        form.addFormDataPart(ApiConstants.USER_ID, userId.toString())

        when (thumb) {
            is File -> form.addFormDataPart(ApiConstants.THUMB, thumb.name, thumb.asRequestBody(null))
            is String -> form.addFormDataPart(ApiConstants.THUMB, thumb)
            else -> throw IllegalArgumentException()
        }

        return post(ApiConstants.METHOD_SET_STICKER_SET_THUMB, form.build())
    }

    override suspend fun sendInvoice(
        chatId: ChatId,
        title: String,
        description: String,
        payload: String,
        providerToken: String,
        currency: String,
        prices: List<LabeledPrice>,
        messageThreadId: Long?,
        maxTipAmount: Int?,
        suggestedTipAmount: List<Int>?,
        startParameter: String?,
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
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: InlineKeyboardMarkup?
    ): Message {
        val body = SendInvoice(
            chatId = chatId,
            title = title,
            description = description,
            payload = payload,
            providerToken = providerToken,
            currency = currency,
            prices = prices,
            messageThreadId = messageThreadId,
            maxTipAmount = maxTipAmount,
            suggestedTipAmount = suggestedTipAmount,
            startParameter = startParameter,
            providerData = providerData,
            photoUrl = photoUrl,
            photoSize = photoSize,
            photoWidth = photoWidth,
            photoHeight = photoHeight,
            needName = needName,
            needPhoneNumber = needPhoneNumber,
            needEmail = needEmail,
            needShippingAddress = needShippingAddress,
            sendPhoneNumberToProvider = sendPhoneNumberToProvider,
            sendEmailToProvider = sendEmailToProvider,
            isFlexible = isFlexible,
            disableNotification = disableNotification,
            protectContent = protectContent,
            replyToMessageId = replyToMessageId,
            allowSendingWithoutReply = allowSendingWithoutReply,
            replyMarkup = replyMarkup
        ).body()
        return post(ApiConstants.METHOD_SEND_INVOICE, body)
    }

    override suspend fun createInvoiceLink(
        title: String,
        description: String,
        payload: String,
        providerToken: String,
        currency: String,
        prices: List<LabeledPrice>,
        maxTipAmount: Int?,
        suggestedTipAmount: List<Int>?,
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
    ): String {
        val body = CreateInvoiceLink(
            title = title,
            description = description,
            payload = payload,
            providerToken = providerToken,
            currency = currency,
            prices = prices,
            maxTipAmount = maxTipAmount,
            suggestedTipAmount = suggestedTipAmount,
            providerData = providerData,
            photoUrl = photoUrl,
            photoSize = photoSize,
            photoWidth = photoWidth,
            photoHeight = photoHeight,
            needName = needName,
            needPhoneNumber = needPhoneNumber,
            needEmail = needEmail,
            needShippingAddress = needShippingAddress,
            sendPhoneNumberToProvider = sendPhoneNumberToProvider,
            sendEmailToProvider = sendEmailToProvider,
            isFlexible = isFlexible,
        ).body()
        return post(ApiConstants.METHOD_CREATE_INVOICE_LINK, body)
    }

    override suspend fun answerShippingQuery(
        shippingQueryId: String,
        ok: Boolean,
        shippingOptions: List<ShippingOption>?,
        errorMessage: String?
    ): Boolean {
        val body = AnswerShippingQuery(
            shippingQueryId = shippingQueryId,
            ok = ok,
            shippingOptions = shippingOptions,
            errorMessage = errorMessage
        ).body()
        return post(ApiConstants.METHOD_ANSWER_SHIPPING_QUERY, body)
    }

    override suspend fun answerPreCheckoutQuery(
        preCheckoutQueryId: String,
        ok: Boolean,
        errorMessage: String?
    ): Boolean {
        val body = AnswerPreCheckoutQuery(
            preCheckoutQueryId = preCheckoutQueryId,
            ok = ok,
            errorMessage = errorMessage
        ).body()
        return post(ApiConstants.METHOD_ANSWER_PRE_CHECKOUT_QUERY, body)
    }

    override suspend fun setPassportDataErrors(
        userId: Long,
        errors: List<PassportElementError>
    ): Boolean {
        val body = SetPassportDataErrors(
            userId = userId,
            errors = errors
        ).body()
        return post(ApiConstants.METHOD_SET_PASSPORT_DATA_ERRORS, body)
    }

    override suspend fun sendGame(
        chatId: Long,
        gameShortName: String,
        messageThreadId: Long?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: InlineKeyboardMarkup?
    ): Message {
        val body = SendGame(
            chatId = chatId,
            gameShortName = gameShortName,
            messageThreadId = messageThreadId,
            disableNotification = disableNotification,
            protectContent = protectContent,
            replyToMessageId = replyToMessageId,
            allowSendingWithoutReply = allowSendingWithoutReply,
            replyMarkup = replyMarkup
        ).body()
        return post(ApiConstants.METHOD_SEND_GAME, body)
    }

    override suspend fun setGameScore(
        userId: Long,
        score: Long,
        force: Boolean?,
        disableEditMessage: Boolean?,
        chatId: ChatId.IntegerId?,
        messageId: Long?,
        inlineMessageId: String?
    ): Message {
        val body = SetGameScore(
            userId = userId,
            score = score,
            force = force,
            disableEditMessage = disableEditMessage,
            chatId = chatId,
            messageId = messageId,
            inlineMessageId = inlineMessageId
        ).body()
        return post(ApiConstants.METHOD_SET_GAME_SCORE, body)
    }

    override suspend fun getGameHighScores(
        userId: Long,
        chatId: ChatId.IntegerId?,
        messageId: Long?,
        inlineMessageId: String?
    ): List<GameHighScore> {
        val body = GetGameHighScores(
            userId = userId,
            chatId = chatId,
            messageId = messageId,
            inlineMessageId = inlineMessageId
        ).body()
        return post(ApiConstants.METHOD_GET_GAME_HIGH_SCORES, body)
    }
}
