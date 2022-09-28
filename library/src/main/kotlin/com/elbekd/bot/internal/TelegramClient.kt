package com.elbekd.bot.internal

import com.elbekd.bot.api.TelegramApi
import com.elbekd.bot.model.ChatId
import com.elbekd.bot.model.TelegramApiError
import com.elbekd.bot.model.internal.AnswerCallbackQuery
import com.elbekd.bot.model.internal.AnswerInlineQuery
import com.elbekd.bot.model.internal.AnswerPreCheckoutQuery
import com.elbekd.bot.model.internal.AnswerShippingQuery
import com.elbekd.bot.model.internal.AnswerWebAppQuery
import com.elbekd.bot.model.internal.ApproveChatJoinRequest
import com.elbekd.bot.model.internal.BanChatMember
import com.elbekd.bot.model.internal.BanChatSenderChat
import com.elbekd.bot.model.internal.CopyMessage
import com.elbekd.bot.model.internal.CreateChatInviteLink
import com.elbekd.bot.model.internal.CreateInvoiceLink
import com.elbekd.bot.model.internal.DeclineChatJoinRequest
import com.elbekd.bot.model.internal.DeleteChatPhoto
import com.elbekd.bot.model.internal.DeleteChatStickerSet
import com.elbekd.bot.model.internal.DeleteMessage
import com.elbekd.bot.model.internal.DeleteMyCommands
import com.elbekd.bot.model.internal.DeleteStickerFromSet
import com.elbekd.bot.model.internal.EditChatInviteLink
import com.elbekd.bot.model.internal.EditMessageCaption
import com.elbekd.bot.model.internal.EditMessageLiveLocation
import com.elbekd.bot.model.internal.EditMessageReplyMarkup
import com.elbekd.bot.model.internal.EditMessageText
import com.elbekd.bot.model.internal.ExportChatInviteLink
import com.elbekd.bot.model.internal.ForwardMessage
import com.elbekd.bot.model.internal.GetChat
import com.elbekd.bot.model.internal.GetChatAdministrators
import com.elbekd.bot.model.internal.GetChatMember
import com.elbekd.bot.model.internal.GetChatMembersCount
import com.elbekd.bot.model.internal.GetChatMenuButton
import com.elbekd.bot.model.internal.GetCustomEmojiStickers
import com.elbekd.bot.model.internal.GetFile
import com.elbekd.bot.model.internal.GetGameHighScores
import com.elbekd.bot.model.internal.GetMyCommands
import com.elbekd.bot.model.internal.GetMyDefaultAdministratorRights
import com.elbekd.bot.model.internal.GetStickerSet
import com.elbekd.bot.model.internal.GetUserProfilePhotos
import com.elbekd.bot.model.internal.LeaveChat
import com.elbekd.bot.model.internal.PinChatMessage
import com.elbekd.bot.model.internal.PromoteChatMember
import com.elbekd.bot.model.internal.RestrictChatMember
import com.elbekd.bot.model.internal.RevokeChatInviteLink
import com.elbekd.bot.model.internal.SendChatAction
import com.elbekd.bot.model.internal.SendContact
import com.elbekd.bot.model.internal.SendDice
import com.elbekd.bot.model.internal.SendGame
import com.elbekd.bot.model.internal.SendInvoice
import com.elbekd.bot.model.internal.SendLocation
import com.elbekd.bot.model.internal.SendMessage
import com.elbekd.bot.model.internal.SendPoll
import com.elbekd.bot.model.internal.SendVenue
import com.elbekd.bot.model.internal.SetChatAdministratorCustomTitle
import com.elbekd.bot.model.internal.SetChatDescription
import com.elbekd.bot.model.internal.SetChatMenuButton
import com.elbekd.bot.model.internal.SetChatPermissions
import com.elbekd.bot.model.internal.SetChatStickerSet
import com.elbekd.bot.model.internal.SetChatTitle
import com.elbekd.bot.model.internal.SetGameScore
import com.elbekd.bot.model.internal.SetMyCommands
import com.elbekd.bot.model.internal.SetMyDefaultAdministratorRights
import com.elbekd.bot.model.internal.SetPassportDataErrors
import com.elbekd.bot.model.internal.SetStickerPositionInSet
import com.elbekd.bot.model.internal.StopMessageLiveLocation
import com.elbekd.bot.model.internal.StopPoll
import com.elbekd.bot.model.internal.UnbanChatMember
import com.elbekd.bot.model.internal.UnbanChatSenderChat
import com.elbekd.bot.model.internal.UnpinAllChatMessages
import com.elbekd.bot.model.internal.UnpinChatMessage
import com.elbekd.bot.model.internal.UpdateRequest
import com.elbekd.bot.types.BotCommand
import com.elbekd.bot.types.BotCommandScope
import com.elbekd.bot.types.Chat
import com.elbekd.bot.types.ChatAdministratorRights
import com.elbekd.bot.types.ChatInviteLink
import com.elbekd.bot.types.ChatMember
import com.elbekd.bot.types.ChatPermissions
import com.elbekd.bot.types.GameHighScore
import com.elbekd.bot.types.InlineKeyboardMarkup
import com.elbekd.bot.types.InlineQueryResult
import com.elbekd.bot.types.InputMedia
import com.elbekd.bot.types.LabeledPrice
import com.elbekd.bot.types.MaskPosition
import com.elbekd.bot.types.MenuButton
import com.elbekd.bot.types.Message
import com.elbekd.bot.types.MessageEntity
import com.elbekd.bot.types.MessageId
import com.elbekd.bot.types.ParseMode
import com.elbekd.bot.types.PassportElementError
import com.elbekd.bot.types.Poll
import com.elbekd.bot.types.ReplyKeyboard
import com.elbekd.bot.types.SentWebAppMessage
import com.elbekd.bot.types.ShippingOption
import com.elbekd.bot.types.Sticker
import com.elbekd.bot.types.StickerSet
import com.elbekd.bot.types.Update
import com.elbekd.bot.types.UpdateResponse
import com.elbekd.bot.types.User
import com.elbekd.bot.types.UserProfilePhotos
import com.elbekd.bot.types.WebhookInfo
import com.elbekd.bot.util.Action
import com.elbekd.bot.util.AllowedUpdate
import com.elbekd.bot.util.SendingByteArray
import com.elbekd.bot.util.SendingDocument
import com.elbekd.bot.util.SendingFile
import com.elbekd.bot.util.SendingString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
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
        opts: Map<String, Any?>,
        thumb: File? = null,
        method: String = type
    ): Message {
        val form = MultipartBody.Builder().also { it.setType(MultipartBody.FORM) }
        form.addFormDataPart(ApiConstants.CHAT_ID, chatId.toString())
        addOptsToForm(form, opts)

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

    private fun addOptsToForm(form: MultipartBody.Builder, opts: Map<String, Any?>) =
        sendFileOpts
            .filterKeys { opts[it] != null }
            .forEach { form.addFormDataPart(it.key, it.value(opts[it.key] ?: return@forEach)) }

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

    override suspend fun forwardMessage(
        chatId: ChatId,
        fromChatId: ChatId,
        msgId: Long,
        disableNotification: Boolean?,
        protectContent: Boolean?,
    ): Message {
        val body = ForwardMessage(
            chatId = chatId,
            fromChatId = fromChatId,
            messageId = msgId,
            disableNotification = disableNotification,
            protectContent = protectContent,
        ).body()
        return post(ApiConstants.METHOD_FORWARD_MESSAGE, body)
    }

    override suspend fun copyMessage(
        chatId: ChatId,
        fromChatId: ChatId,
        messageId: Long,
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
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ): Message {
        return sendFile(
            ApiConstants.PHOTO,
            chatId,
            photo,
            mapOf(
                ApiConstants.CAPTION to caption,
                ApiConstants.PARSE_MODE to parseMode,
                ApiConstants.CAPTION_ENTITIES to captionEntities,
                ApiConstants.DISABLE_NOTIFICATION to disableNotification,
                ApiConstants.PROTECT_CONTENT to protectContent,
                ApiConstants.REPLY_TO_MESSAGE_ID to replyToMessageId,
                ApiConstants.ALLOW_SENDING_WITHOUT_REPLY to allowSendingWithoutReply,
                ApiConstants.REPLY_MARKUP to replyMarkup
            )
        )
    }

    override suspend fun sendAudio(
        chatId: ChatId,
        audio: SendingDocument,
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
        return sendFile(
            ApiConstants.AUDIO,
            chatId,
            audio,
            mapOf(
                ApiConstants.CAPTION to caption,
                ApiConstants.PARSE_MODE to parseMode,
                ApiConstants.CAPTION_ENTITIES to captionEntities,
                ApiConstants.DURATION to duration,
                ApiConstants.PERFORMER to performer,
                ApiConstants.TITLE to title,
                ApiConstants.DISABLE_NOTIFICATION to disableNotification,
                ApiConstants.PROTECT_CONTENT to protectContent,
                ApiConstants.REPLY_TO_MESSAGE_ID to replyToMessageId,
                ApiConstants.ALLOW_SENDING_WITHOUT_REPLY to allowSendingWithoutReply,
                ApiConstants.REPLY_MARKUP to replyMarkup
            ),
            thumb
        )
    }

    override suspend fun sendDocument(
        chatId: ChatId,
        document: SendingDocument,
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
        return sendFile(
            ApiConstants.DOCUMENT,
            chatId,
            document,
            mapOf(
                ApiConstants.CAPTION to caption,
                ApiConstants.PARSE_MODE to parseMode,
                ApiConstants.CAPTION_ENTITIES to captionEntities,
                ApiConstants.DISABLE_CONTENT_TYPE_DETECTION to disableContentTypeDetection,
                ApiConstants.DISABLE_NOTIFICATION to disableNotification,
                ApiConstants.PROTECT_CONTENT to protectContent,
                ApiConstants.REPLY_TO_MESSAGE_ID to replyToMessageId,
                ApiConstants.ALLOW_SENDING_WITHOUT_REPLY to allowSendingWithoutReply,
                ApiConstants.REPLY_MARKUP to replyMarkup
            ),
            thumb
        )
    }

    override suspend fun sendVideo(
        chatId: ChatId,
        video: SendingDocument,
        duration: Long?,
        width: Long?,
        height: Long?,
        thumb: File?,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        streaming: Boolean?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ): Message {
        return sendFile(
            ApiConstants.VIDEO,
            chatId,
            video,
            mapOf(
                ApiConstants.DURATION to duration,
                ApiConstants.WIDTH to width,
                ApiConstants.HEIGHT to height,
                ApiConstants.CAPTION to caption,
                ApiConstants.PARSE_MODE to parseMode,
                ApiConstants.CAPTION_ENTITIES to captionEntities,
                ApiConstants.SUPPORTS_STREAMING to streaming,
                ApiConstants.DISABLE_NOTIFICATION to disableNotification,
                ApiConstants.PROTECT_CONTENT to protectContent,
                ApiConstants.REPLY_TO_MESSAGE_ID to replyToMessageId,
                ApiConstants.ALLOW_SENDING_WITHOUT_REPLY to allowSendingWithoutReply,
                ApiConstants.REPLY_MARKUP to replyMarkup
            ),
            thumb
        )
    }

    override suspend fun sendAnimation(
        chatId: ChatId,
        animation: SendingDocument,
        duration: Long?,
        width: Long?,
        height: Long?,
        thumb: File?,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ): Message {
        return sendFile(
            ApiConstants.ANIMATION,
            chatId,
            animation,
            mapOf(
                ApiConstants.DURATION to duration,
                ApiConstants.WIDTH to width,
                ApiConstants.HEIGHT to height,
                ApiConstants.CAPTION to caption,
                ApiConstants.PARSE_MODE to parseMode,
                ApiConstants.CAPTION_ENTITIES to captionEntities,
                ApiConstants.DISABLE_NOTIFICATION to disableNotification,
                ApiConstants.PROTECT_CONTENT to protectContent,
                ApiConstants.REPLY_TO_MESSAGE_ID to replyToMessageId,
                ApiConstants.ALLOW_SENDING_WITHOUT_REPLY to allowSendingWithoutReply,
                ApiConstants.REPLY_MARKUP to replyMarkup
            ),
            thumb
        )
    }

    override suspend fun sendVoice(
        chatId: ChatId,
        voice: SendingDocument,
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
        return sendFile(
            ApiConstants.VOICE,
            chatId,
            voice,
            mapOf(
                ApiConstants.CAPTION to caption,
                ApiConstants.PARSE_MODE to parseMode,
                ApiConstants.CAPTION_ENTITIES to captionEntities,
                ApiConstants.DURATION to duration,
                ApiConstants.DISABLE_NOTIFICATION to disableNotification,
                ApiConstants.PROTECT_CONTENT to protectContent,
                ApiConstants.REPLY_TO_MESSAGE_ID to replyToMessageId,
                ApiConstants.ALLOW_SENDING_WITHOUT_REPLY to allowSendingWithoutReply,
                ApiConstants.REPLY_MARKUP to replyMarkup
            )
        )
    }

    override suspend fun sendVideoNote(
        chatId: ChatId,
        note: SendingDocument,
        duration: Long?,
        length: Long?,
        thumb: File?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ): Message {
        return sendFile(
            ApiConstants.VIDEO_NOTE,
            chatId,
            note,
            mapOf(
                ApiConstants.DURATION to duration,
                ApiConstants.DISABLE_NOTIFICATION to disableNotification,
                ApiConstants.PROTECT_CONTENT to protectContent,
                ApiConstants.REPLY_TO_MESSAGE_ID to replyToMessageId,
                ApiConstants.ALLOW_SENDING_WITHOUT_REPLY to allowSendingWithoutReply,
                ApiConstants.REPLY_MARKUP to replyMarkup
            ),
            thumb,
            ApiConstants.METHOD_VIDEO_NOTE
        )
    }

    override suspend fun sendMediaGroup(
        chatId: ChatId,
        media: List<InputMedia>,
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
        emoji: String?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: ReplyKeyboard?
    ): Message {
        val body = SendDice(
            chatId = chatId,
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
        action: Action
    ): Boolean {
        val body = SendChatAction(
            chatId = chatId,
            action = action
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
        untilDate: Long?
    ): Boolean {
        val body = RestrictChatMember(
            chatId = chatId,
            userId = userId,
            permissions = permissions,
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
        canPinMessages: Boolean?
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

    override suspend fun setChatPermissions(chatId: ChatId, permissions: ChatPermissions): Boolean {
        val body = SetChatPermissions(
            chatId = chatId,
            permissions = permissions
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
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyToMessageId: Long?,
        allowSendingWithoutReply: Boolean?,
        replyMarkup: InlineKeyboardMarkup?
    ): Message {
        val body = SendGame(
            chatId = chatId,
            gameShortName = gameShortName,
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
