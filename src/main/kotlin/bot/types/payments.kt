package bot.types

data class LabeledPrice(val label: String, val amount: Int)

data class Invoice(val title: String,
                   val description: String,
                   val start_parameter: String,
                   val currency: String,
                   val total_amount: Int
)

data class ShippingAddress(val country_code: String,
                           val state: String,
                           val city: String,
                           val street_line1: String,
                           val street_line2: String,
                           val post_code: String
)

data class OrderInfo(val name: String,
                     val phone_number: String,
                     val email: String,
                     val shipping_address: ShippingAddress
)

data class ShippingOption(val id: String, val title: String, val prices: List<LabeledPrice>)

data class SuccessfulPayment(val currency: String,
                             val total_amount: Int,
                             val invoice_payload: String,
                             val shipping_option_id: String,
                             val order_info: OrderInfo,
                             val telegram_payment_charge_id: String,
                             val provider_payment_charge_id: String
)

data class ShippingQuery(val id: String,
                         val from: User,
                         val invoice_payload: String,
                         val shipping_address: ShippingAddress
)

data class PreCheckoutQuery(val id: String,
                            val from: User,
                            val currency: String,
                            val total_amount: Int,
                            val invoice_payload: String,
                            val shipping_option_id: String,
                            val order_info: OrderInfo
)
