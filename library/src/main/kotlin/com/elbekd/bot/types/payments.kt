package com.elbekd.bot.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class LabeledPrice(
    @SerialName("label") val label: String,
    @SerialName("amount") val amount: Int
)

@Serializable
public data class Invoice(
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("start_parameter") val startParameter: String,
    @SerialName("currency") val currency: String,
    @SerialName("total_amount") val totalAmount: Int
)

@Serializable
public data class ShippingAddress(
    @SerialName("country_code") val country_code: String,
    @SerialName("state") val state: String,
    @SerialName("city") val city: String,
    @SerialName("street_line1") val streetLine1: String,
    @SerialName("street_line2") val streetLine2: String,
    @SerialName("post_code") val postCode: String
)

@Serializable
public data class OrderInfo(
    @SerialName("name") val name: String,
    @SerialName("phone_number") val phoneNumber: String,
    @SerialName("email") val email: String,
    @SerialName("shipping_address") val shippingAddress: ShippingAddress
)

@Serializable
public data class ShippingOption(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("prices") val prices: List<LabeledPrice>
)

@Serializable
public data class SuccessfulPayment(
    @SerialName("currency") val currency: String,
    @SerialName("total_amount") val totalAmount: Int,
    @SerialName("invoice_payload") val invoicePayload: String,
    @SerialName("shipping_option_id") val shippingOptionId: String,
    @SerialName("order_info") val orderInfo: OrderInfo,
    @SerialName("telegram_payment_charge_id") val telegramPaymentChargeId: String,
    @SerialName("provider_payment_charge_id") val providerPaymentChargeId: String
)

@Serializable
public data class ShippingQuery(
    @SerialName("id") val id: String,
    @SerialName("from") val from: User,
    @SerialName("invoice_payload") val invoicePayload: String,
    @SerialName("shipping_address") val shippingAddress: ShippingAddress
)

@Serializable
public data class PreCheckoutQuery(
    @SerialName("id") val id: String,
    @SerialName("from") val from: User,
    @SerialName("currency") val currency: String,
    @SerialName("total_amount") val totalAmount: Int,
    @SerialName("invoice_payload") val invoicePayload: String,
    @SerialName("shipping_option_id") val shippingOptionId: String,
    @SerialName("order_info") val orderInfo: OrderInfo
)