package http

import bot.types.*
import com.google.gson.Gson
import okhttp3.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.concurrent.CompletableFuture

internal class TelegramClient(token: String) : TelegramApi {
    private val url = "https://api.telegram.org/bot$token"
    companion object {
        @JvmStatic private val MEDIA_TYPE_JSON = MediaType.parse("application/json")
    }

    private val client = OkHttpClient()
    private val gson = Gson()

    private fun <R> get(method: String, clazz: Class<R>) = future {
        val request = Request.Builder()
                .url(url(method))
                .build()
        val response = client.newCall(request).execute()
        val obj = fromJson(response, clazz)
        if (!obj.ok)
            throw TelegramApiError(obj.error_code!!, obj.description!!)
        obj.result!!
    }

    private fun <R> post(method: String, body: RequestBody, clazz: Class<R>) = future {
        val request = Request.Builder()
                .url(url(method))
                .post(body)
                .build()
        val response = client.newCall(request).execute()
        val obj = fromJson(response, clazz)
        if (!obj.ok)
            throw TelegramApiError(obj.error_code!!, obj.description!!)
        obj.result!!

    }

    private fun <R> fromJson(response: Response, clazz: Class<R>): TelegramObject<R> {
        return gson.fromJson(response.body()?.string(), getType(TelegramObject::class.java, clazz))
    }

    private fun toJson(body: Any) = gson.toJson(body)

    private fun getType(rawClass: Class<*>, parameterClass: Class<*>): Type {
        return object : ParameterizedType {
            override fun getActualTypeArguments() = arrayOf(parameterClass)
            override fun getRawType() = rawClass
            override fun getOwnerType() = null
        }
    }

    private fun url(method: String): String = "$url/$method"

    private fun id(id: Any) = when (id) {
        is Int -> id.toString()
        is String -> id
        else -> throw IllegalArgumentException("$id neither string nor integer")
    }

    override fun getMe() = get("getMe", User::class.java)

    override fun sendMessage(id: Any, text: String, parseMode: String?, preview: Boolean?, notification: Boolean?,
                             replyTo: Int?, markup: ReplyKeyboard?): CompletableFuture<Message> {
        val json = toJson(Send(id(id), text, parseMode, preview, notification, replyTo, markup))
        val body = RequestBody.create(MEDIA_TYPE_JSON, json)
        return post("sendMessage", body, Message::class.java)
    }
}
