package bot.http

import bot.TelegramBot
import bot.types.*
import java.util.concurrent.CompletableFuture

interface TelegramApi {
    fun getMe(): CompletableFuture<User>

    /**
     * @param chatId is [Int] or [String]
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun sendMessage(chatId: Any, text: String, parseMode: String? = null, preview: Boolean? = null,
                    notification: Boolean? = null, replyTo: Int? = null, markup: ReplyKeyboard? = null):
            CompletableFuture<Message>

    /**
     * @param chatId is [Int] or [String]
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun forwardMessage(chatId: Any, fromId: Any, msgId: Int, notification: Boolean? = null):
            CompletableFuture<Message>

    /**
     * @param chatId is [Int] or [String]
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun sendPhoto(chatId: Any, photo: Any, caption: String? = null, parseMode: String? = null,
                  notification: Boolean? = null, replyTo: Int? = null,
                  markup: ReplyKeyboard? = null):
            CompletableFuture<Message>

    /**
     * @param chatId is [Int] or [String]
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     */
    fun sendAudio(chatId: Any, audio: Any, caption: String? = null, parseMode: String? = null,
                  duration: Int? = null, performer: String? = null, title: String? = null,
                  notification: Boolean? = null, replyTo: Int? = null, markup: ReplyKeyboard? = null):
            CompletableFuture<Message>

    /**
     * @param chatId is [Int] or [String]
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun sendDocument(chatId: Any, document: Any, caption: String? = null, parseMode: String? = null,
                     notification: Boolean? = null, replyTo: Int? = null, markup: ReplyKeyboard? = null):
            CompletableFuture<Message>

    /**
     * @param chatId is [Int] or [String]
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun sendVideo(chatId: Any, video: Any, duration: Int? = null, width: Int? = null, height: Int? = null,
                  caption: String? = null, parseMode: String? = null, streaming: Boolean? = null,
                  notification: Boolean? = null, replyTo: Int? = null, markup: ReplyKeyboard? = null):
            CompletableFuture<Message>

    /**
     * @param chatId is [Int] or [String]
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun sendVoice(chatId: Any, voice: Any, caption: String? = null, parseMode: String? = null,
                  duration: Int? = null, notification: Boolean? = null,
                  replyTo: Int? = null, markup: ReplyKeyboard? = null):
            CompletableFuture<Message>

    /**
     * @param chatId is [Int] or [String]
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun sendVideoNote(chatId: Any, note: Any, duration: Int? = null, length: Int? = null,
                      notification: Boolean? = null, replyTo: Int? = null, markup: ReplyKeyboard? = null):
            CompletableFuture<Message>

    /**
     * @param chatId is [Int] or [String]
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun sendMediaGroup(chatId: Any, media: Array<InputMedia>, notification: Boolean? = null, replyTo: Int? = null):
            CompletableFuture<ArrayList<Message>>

    /**
     * @param chatId is [Int] or [String]
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun sendLocation(chatId: Any, latitude: Double, longitude: Double, period: Int? = null,
                     notification: Boolean? = null, replyTo: Int? = null, markup: ReplyKeyboard? = null):
            CompletableFuture<Message>

    /**
     * @throws IllegalArgumentException if not provided [inlineMessageId] or [chatId] and [messageId]
     * @throws TelegramApiError if error returned in response
     */
    fun editMessageLiveLocation(latitude: Double, longitude: Double, chatId: Any? = null,
                                messageId: Int? = null, inlineMessageId: String? = null, markup: InlineKeyboardMarkup? = null):
            CompletableFuture<Message>

    /**
     * @param chatId is [Int] or [String]
     * @throws IllegalArgumentException if not provided [inlineMessageId] or [chatId] and [messageId]
     * @throws TelegramApiError if error returned in response
     */
    fun stopMessageLiveLocation(chatId: Any? = null, messageId: Int? = null, inlineMessageId: String? = null,
                                markup: InlineKeyboardMarkup? = null):
            CompletableFuture<Message>

    /**
     * @param chatId is [Int] or [String]
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun sendVenue(chatId: Any, latitude: Double, longitude: Double, title: String, address: String,
                  foursquareId: String? = null, notification: Boolean? = null,
                  replyTo: Int? = null, markup: ReplyKeyboard? = null):
            CompletableFuture<Message>

    /**
     * @param chatId is [Int] or [String]
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun sendContact(chatId: Any, phone: String, firstName: String, lastName: String? = null,
                    notification: Boolean? = null, replyTo: Int? = null, markup: ReplyKeyboard? = null):
            CompletableFuture<Message>

    /**
     * @param chatId is [Int] or [String]
     * @throws IllegalArgumentException if `chatId` neither integer nor string
     * @throws TelegramApiError if error returned in response
     */
    fun sendChatAction(chatId: Any, action: TelegramBot.Actions): CompletableFuture<Boolean>
}