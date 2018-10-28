package com.github.elbekD.bot

import java.io.File
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

class PollingOptions(val removeWebhookAutomatically: Boolean = true) {
    var limit: Int? = null
        set(value) {
            if (value == null)
                throw NullPointerException()
            if (value < 1 || value > 100)
                throw IllegalArgumentException("Limit <$value> not allowed")
            field = value
        }

    var timeout: Int? = null
        set(value) {
            if (value == null)
                throw NullPointerException()
            if (value < 0)
                throw IllegalArgumentException("Timeout <$value> not allowed")
            field = value
        }

    var allowedUpdates: Array<AllowedUpdates>? = null
        set(value) {
            if (value == null)
                throw NullPointerException()
            field = value
        }

    /**
     * period for [Timer]
     */
    var period: Long = 1000L
        set(value) {
            if (value > 1000L) field = value
        }
}

@DslMarker
annotation class OptionsMarker

/**
 * @param setWebhookAutomatically if true then calls `setWebhook()` method when server is started.
 * Else call `setWebhook()` method manually. By default is `true`
 */
@OptionsMarker
class WebhookOptions(var setWebhookAutomatically: Boolean = true,
                     internal var serverOptions: ServerOptions = ServerOptions()) {
    var url: String = ""
        set(value) {
            if (!value.matches("^https://.+$".toRegex()))
                throw IllegalArgumentException("<$url> is invalid. Check and try again")
            field = value
        }
        get() {
            if (!field.matches("^https://.+$".toRegex()))
                throw IllegalArgumentException("<$field> is invalid. Check and try again")
            return field
        }

    var certificate: File? = null
        set(value) {
            if (value == null)
                throw NullPointerException()
            field = value
        }

    var maxConnections: Int? = null
        set(value) {
            if (value == null)
                throw NullPointerException()
            if (value < 1 || value > 100)
                throw IllegalArgumentException("Incorrect max connections value: $value not in [1, 100]")
            field = value
        }

    var allowedUpdates: List<AllowedUpdates>? = null
        set(value) {
            if (value == null) throw NullPointerException()
            field = value
        }
}

fun WebhookOptions.server(block: ServerOptions.() -> Unit) {
    serverOptions = ServerOptions().apply(block)
}

@OptionsMarker
class TLSOptions {
    private companion object {
        @JvmStatic
        val supportedPorts = listOf(80, 88, 443, 8443)
    }

    var port = 8443
        set(value) {
            if (value !in supportedPorts)
                throw IllegalArgumentException("<$value> not in $supportedPorts. Change and try again")
            field = value
        }

    var keyStorePath: String? = null
        set(value) {
            if (value == null)
                throw NullPointerException()
            field = value
        }

    var keyStorePassword: String? = null
        set(value) {
            if (value == null)
                throw NullPointerException()
            field = value
        }

    var keyManagerPassword: String? = null
        set(value) {
            if (value == null)
                throw NullPointerException()
            field = value
        }
}

@OptionsMarker
class ServerOptions(var host: String = "127.0.0.1",
                    var port: Int = 8080,
                    internal var tlsOptions: TLSOptions? = null)

fun ServerOptions.tls(block: TLSOptions.() -> Unit) {
    tlsOptions = TLSOptions().apply(block)
}