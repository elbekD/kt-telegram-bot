package com.elbekd.bot.model.internal

import com.elbekd.bot.internal.ApiConstants
import com.elbekd.bot.model.ChatId
import com.elbekd.bot.types.InlineKeyboardMarkup
import com.elbekd.bot.types.LabeledPrice
import com.elbekd.bot.types.ShippingOption
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class SendInvoice(
    @SerialName(ApiConstants.CHAT_ID) val chatId: ChatId,
    @SerialName(ApiConstants.TITLE) val title: String,
    @SerialName(ApiConstants.DESCRIPTION) val description: String,
    @SerialName(ApiConstants.PAYLOAD) val payload: String,
    @SerialName(ApiConstants.PROVIDER_TOKEN) val providerToken: String,
    @SerialName(ApiConstants.CURRENCY) val currency: String,
    @SerialName(ApiConstants.PRICES) val prices: List<LabeledPrice>,
    @SerialName(ApiConstants.MAX_TIP_AMOUNT) val maxTipAmount: Int? = null,
    @SerialName(ApiConstants.SUGGESTED_TIP_AMOUNTS) val suggestedTipAmount: List<Int>? = null,
    @SerialName(ApiConstants.START_PARAMETER) val startParameter: String? = null,
    @SerialName(ApiConstants.PROVIDER_DATA) val providerData: String? = null,
    @SerialName(ApiConstants.PHOTO_URL) val photoUrl: String? = null,
    @SerialName(ApiConstants.PHOTO_SIZE) val photoSize: Int? = null,
    @SerialName(ApiConstants.PHOTO_WIDTH) val photoWidth: Int? = null,
    @SerialName(ApiConstants.PHOTO_HEIGHT) val photoHeight: Int? = null,
    @SerialName(ApiConstants.NEED_NAME) val needName: Boolean? = null,
    @SerialName(ApiConstants.NEED_PHONE_NUMBER) val needPhoneNumber: Boolean? = null,
    @SerialName(ApiConstants.NEED_EMAIL) val needEmail: Boolean? = null,
    @SerialName(ApiConstants.NEED_SHIPPING_ADDRESS) val needShippingAddress: Boolean? = null,
    @SerialName(ApiConstants.SEND_PHONE_NUMBER_TO_PROVIDER) val sendPhoneNumberToProvider: Boolean? = null,
    @SerialName(ApiConstants.SEND_EMAIL_TO_PROVIDER) val sendEmailToProvider: Boolean? = null,
    @SerialName(ApiConstants.IS_FLEXIBLE) val isFlexible: Boolean? = null,
    @SerialName(ApiConstants.DISABLE_NOTIFICATION) val disableNotification: Boolean? = null,
    @SerialName(ApiConstants.PROTECT_CONTENT) val protectContent: Boolean? = null,
    @SerialName(ApiConstants.REPLY_TO_MESSAGE_ID) val replyToMessageId: Long? = null,
    @SerialName(ApiConstants.ALLOW_SENDING_WITHOUT_REPLY) val allowSendingWithoutReply: Boolean? = null,
    @SerialName(ApiConstants.REPLY_MARKUP) val replyMarkup: InlineKeyboardMarkup? = null
)

@Serializable
internal class CreateInvoiceLink(
    @SerialName(ApiConstants.TITLE) val title: String,
    @SerialName(ApiConstants.DESCRIPTION) val description: String,
    @SerialName(ApiConstants.PAYLOAD) val payload: String,
    @SerialName(ApiConstants.PROVIDER_TOKEN) val providerToken: String,
    @SerialName(ApiConstants.CURRENCY) val currency: String,
    @SerialName(ApiConstants.PRICES) val prices: List<LabeledPrice>,
    @SerialName(ApiConstants.MAX_TIP_AMOUNT) val maxTipAmount: Int? = null,
    @SerialName(ApiConstants.SUGGESTED_TIP_AMOUNTS) val suggestedTipAmount: List<Int>? = null,
    @SerialName(ApiConstants.PROVIDER_DATA) val providerData: String? = null,
    @SerialName(ApiConstants.PHOTO_URL) val photoUrl: String? = null,
    @SerialName(ApiConstants.PHOTO_SIZE) val photoSize: Int? = null,
    @SerialName(ApiConstants.PHOTO_WIDTH) val photoWidth: Int? = null,
    @SerialName(ApiConstants.PHOTO_HEIGHT) val photoHeight: Int? = null,
    @SerialName(ApiConstants.NEED_NAME) val needName: Boolean? = null,
    @SerialName(ApiConstants.NEED_PHONE_NUMBER) val needPhoneNumber: Boolean? = null,
    @SerialName(ApiConstants.NEED_EMAIL) val needEmail: Boolean? = null,
    @SerialName(ApiConstants.NEED_SHIPPING_ADDRESS) val needShippingAddress: Boolean? = null,
    @SerialName(ApiConstants.SEND_PHONE_NUMBER_TO_PROVIDER) val sendPhoneNumberToProvider: Boolean? = null,
    @SerialName(ApiConstants.SEND_EMAIL_TO_PROVIDER) val sendEmailToProvider: Boolean? = null,
    @SerialName(ApiConstants.IS_FLEXIBLE) val isFlexible: Boolean? = null
)

@Serializable
internal class AnswerShippingQuery(
    @SerialName(ApiConstants.SHIPPING_QUERY_ID) val shippingQueryId: String,
    @SerialName(ApiConstants.OK) val ok: Boolean,
    @SerialName(ApiConstants.SHIPPING_OPTIONS) val shippingOptions: List<ShippingOption>? = null,
    @SerialName(ApiConstants.ERROR_MESSAGE) val errorMessage: String? = null
)

@Serializable
internal class AnswerPreCheckoutQuery(
    @SerialName(ApiConstants.PRE_CHECKOUT_QUERY_ID) val preCheckoutQueryId: String,
    @SerialName(ApiConstants.OK) val ok: Boolean,
    @SerialName(ApiConstants.ERROR_MESSAGE) val errorMessage: String? = null
)