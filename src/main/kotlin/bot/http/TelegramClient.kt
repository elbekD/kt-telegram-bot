package bot.http

import bot.TelegramBot
import bot.types.*
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.*
import java.io.File
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

internal class TelegramClient(token: String) : TelegramApi {
    private val gson = GsonBuilder().disableHtmlEscaping().create()
    private val mapper = ObjectMapper().registerKotlinModule().also {
        it.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }
    private val client = OkHttpClient.Builder().connectTimeout(60L, TimeUnit.SECONDS).build()
    private val url = "https://api.telegram.org/bot$token"

    private companion object {
        @JvmStatic
        private val MEDIA_TYPE_JSON = MediaType.parse("application/json")

        @JvmStatic
        private fun id(id: Any) = when (id) {
            is Int -> id.toString()
            is String -> id
            else -> throw IllegalArgumentException("$id neither string nor integer")
        }
    }

    private val anyToString = { a: Any -> a.toString() }
    private val markupToString = { a: Any -> toJson(a) }

    private val sendFileOpts = mapOf(
            "caption" to anyToString, "parse_mode" to anyToString, "disable_notification" to anyToString,
            "reply_to_message_id" to anyToString, "reply_markup" to markupToString,
            "duration" to anyToString, "performer" to anyToString, "title" to anyToString,
            "width" to anyToString, "height" to anyToString, "supports_streaming" to anyToString
    )

    private fun <R> get(method: String, result: Class<R>) = future {
        val request = Request.Builder()
                .url(url(method))
                .build()
        val response = client.newCall(request).execute()
        val obj = fromJson(response, result)
        if (!obj.ok)
            throw TelegramApiError(obj.error_code!!, obj.description!!)
        obj.result!!
    }

    private fun <R> post(method: String, body: RequestBody, result: Class<R>,
                         deserialize: (Response, Class<R>) -> TelegramObject<R> = { res, cl -> fromJson(res, cl) }) =
            future {
                val request = Request.Builder()
                        .url(url(method))
                        .post(body)
                        .build()

                val response = client.newCall(request).execute()
                val obj = deserialize(response, result)
                if (!obj.ok)
                    throw TelegramApiError(obj.error_code!!, obj.description!!)
                obj.result!!
            }

    private fun <R> fromJson(response: Response, result: Class<R>): TelegramObject<R> =
            gson.fromJson(response.body()?.string(), getType(TelegramObject::class.java, result))

    @SuppressWarnings("unused")
    private fun <R> fromJsonPrimitive(response: Response, cl: Class<R>): TelegramObject<R> =
            gson.fromJson(response.body()?.string(), object : TypeToken<TelegramObject<R>>() {}.type)

    private fun toJson(body: Any) = gson.toJson(body)

    private fun toBody(body: Any): RequestBody {
        val json = toJson(body)
        return RequestBody.create(MEDIA_TYPE_JSON, json)
    }

    private fun getType(rawClass: Class<*>, parameterClass: Class<*>): Type {
        return object : ParameterizedType {
            override fun getActualTypeArguments() = arrayOf(parameterClass)
            override fun getRawType() = rawClass
            override fun getOwnerType() = null
        }
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

        return post("send${method.capitalize()}", form.build(), Message::class.java)
    }

    private fun addOptsToForm(form: MultipartBody.Builder, opts: Map<String, Any?>) =
            sendFileOpts.filterKeys { opts[it] != null }.forEach { form.addFormDataPart(it.key, it.value(opts[it.key]!!)) }

    internal fun getUpdates(options: Map<String, Any?>) = future {
        val request = Request.Builder()
                .url(url("getUpdates"))
                .post(RequestBody.create(MEDIA_TYPE_JSON, toJson(options)))
                .build()

        val response = client.newCall(request).execute()

        val obj = mapper.readValue<TelegramObject<ArrayList<Update>>>(response.body()?.string(),
                object : TypeReference<TelegramObject<ArrayList<Update>>>() {})
//        val obj = gson.fromJson<TelegramObject<ArrayList<Update>>>(response.body()?.string(),
//                object : TypeToken<TelegramObject<ArrayList<Update>>>() {}.type)

        if (!obj.ok)
            throw TelegramApiError(obj.error_code!!, obj.description!!)
        obj.result!!
    }

    override fun getMe() = get("getMe", User::class.java)

    override fun sendMessage(chatId: Any, text: String, parseMode: String?, preview: Boolean?, notification: Boolean?,
                             replyTo: Int?, markup: ReplyKeyboard?): CompletableFuture<Message> {
        val body = toBody(SendMessage(id(chatId), text, parseMode, preview, notification, replyTo, markup))
        return post("sendMessage", body, Message::class.java)
    }

    override fun forwardMessage(chatId: Any, fromId: Any, msgId: Int, notification: Boolean?): CompletableFuture<Message> {
        val body = toBody(ForwardMessage(id(chatId), id(fromId), msgId, notification))
        return post("forwardMessage", body, Message::class.java)
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
                        RequestBody.create(MediaType.parse("application/octet-stream"), it.file()!!))
        }

        form.addFormDataPart("media", toJson(media))
        if (notification != null)
            form.addFormDataPart("disable_notification", notification.toString())
        if (replyTo != null)
            form.addFormDataPart("reply_to_message_id", replyTo.toString())

        // TODO somehow avoid creating new instance of ArrayList
        return post("sendMediaGroup", form.build(), arrayListOf<Message>().javaClass)
    }

    override fun sendLocation(chatId: Any, latitude: Double, longitude: Double, period: Int?, notification: Boolean?, replyTo: Int?, markup: ReplyKeyboard?): CompletableFuture<Message> {
        val body = toBody(SendLocation(id(chatId), latitude, longitude, period, notification, replyTo, markup))
        return post("sendLocation", body, Message::class.java)
    }

    override fun editMessageLiveLocation(latitude: Double, longitude: Double, chatId: Any?, messageId: Int?, inlineMessageId: String?, markup: InlineKeyboardMarkup?): CompletableFuture<Message> {
        val body = toBody(EditLocation(if (chatId != null) id(chatId) else null, messageId, inlineMessageId,
                latitude, longitude, markup))
        return post("editMessageLiveLocation", body, Message::class.java)
    }

    override fun stopMessageLiveLocation(chatId: Any?, messageId: Int?, inlineMessageId: String?, markup: InlineKeyboardMarkup?): CompletableFuture<Message> {
        val body = toBody(StopLocation(if (chatId != null) id(chatId) else null, messageId, inlineMessageId, markup))
        return post("stopMessageLiveLocation", body, Message::class.java)
    }

    override fun sendVenue(chatId: Any, latitude: Double, longitude: Double, title: String, address: String, foursquareId: String?, notification: Boolean?, replyTo: Int?, markup: ReplyKeyboard?): CompletableFuture<Message> {
        val body = toBody(SendVenue(id(chatId), latitude, longitude, title, address, foursquareId, notification,
                replyTo, markup))
        return post("sendVenue", body, Message::class.java)
    }

    override fun sendContact(chatId: Any, phone: String, firstName: String, lastName: String?, notification: Boolean?, replyTo: Int?, markup: ReplyKeyboard?): CompletableFuture<Message> {
        val body = toBody(SendContact(id(chatId), phone, firstName, lastName, notification, replyTo, markup))
        return post("sendContact", body, Message::class.java)
    }

    override fun sendChatAction(chatId: Any, action: TelegramBot.Actions): CompletableFuture<Boolean> {
        val body = mapOf("chat_id" to id(chatId), "action" to action.value)
        return post("sendChatAction", RequestBody.create(MEDIA_TYPE_JSON, toJson(body)), Boolean::class.java,
                { response, clazz -> fromJsonPrimitive(response, clazz) })
    }
}
