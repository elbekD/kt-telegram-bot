package bot

import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

enum class AllowedUpdates(private val value: String) {
    Message("message"),
    EditedMessage("edited_message"),
    ChannelPost("channel_post"),
    EditedChannelPost("edited_channel_post"),
    InlineQuery("inline_query"),
    ChosenInlineQuery("chosen_inline_result"),
    CallbackQuery("callback_query"),
    ShippingQuery("shipping_query"),
    PreCheckoutQuery("pre_checkout_query");

    override fun toString() = value
}

class PollingOptions {
    var limit: Int? = null
        set(value) {
            if (value != null) {
                if (value < 1 || value > 100) throw IllegalArgumentException("Limit <$value> not allowed")
                field = value
            }
        }

    var timeout: Int? = null
        set(value) {
            if (value != null) {
                if (value < 0)
                    throw IllegalArgumentException("Timeout <$value> not allowed")
                field = value
            }
        }

    var allowedUpdates: Array<AllowedUpdates>? = null
        set(value) {
            if (value != null) field = value
        }

    override fun toString() = "PollingOptions(limit=$limit, timeout=$timeout" +
            ", allowedUpdates=${Arrays.toString(allowedUpdates)})"
}

class WebhookOptions(val url: String) {
    var crtPath: String? = null
        set(value) {
            if (value != null) {
                if (Files.notExists(Paths.get(value)))
                    throw IllegalArgumentException("Cannot find certificate file! Incorrect path: $value")
                field = value
            }
        }

    var maxCons: Int? = null
        set(value) {
            if (value != null) {
                if (value < 1 || value > 100)
                    throw IllegalArgumentException("Incorrect max connections value: $value not in [1, 100]")
                field = value
            }
        }

    var allowedUpdates: Array<AllowedUpdates>? = null
        set(value) {
            if (value != null) field = value
        }

    override fun toString() = "WebhookOptions(url=$url, crtPath=$crtPath" +
            ", maxCons=$maxCons, allowedUpdates=${Arrays.toString(allowedUpdates)})"
}
