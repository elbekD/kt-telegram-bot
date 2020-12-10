package com.elbekD.bot

import com.elbekD.bot.util.AllowedUpdate
import java.io.File
import java.util.Timer

/**
 * @param removeWebhookAutomatically if `true` calls [TelegramBot.deleteWebhook] before switching to long polling.
 * Default is `true`.
 */
public class PollingOptions(public var removeWebhookAutomatically: Boolean = true) {
    public var limit: Int? = null
        set(value) {
            if (value == null)
                throw NullPointerException()
            if (value < 1 || value > 100)
                throw IllegalArgumentException("Limit <$value> not allowed")
            field = value
        }

    public var timeout: Int? = null
        set(value) {
            if (value == null)
                throw NullPointerException()
            if (value < 0)
                throw IllegalArgumentException("Timeout <$value> not allowed")
            field = value
        }

    public var allowedUpdates: List<AllowedUpdate>? = null
        set(value) {
            if (value == null)
                throw NullPointerException()
            field = value
        }

    /**
     * period for [Timer]
     */
    public var period: Long = 1000L
        set(value) {
            if (value > 1000L) field = value
        }

    override fun toString(): String {
        return """
            PollingOptions:
              limit: $limit
              timeout: $timeout
              allowedUpdated: $allowedUpdates
              period: $period
        """.trimIndent()
    }
}

@DslMarker
public annotation class OptionsMarker

/**
 * @param setWebhookAutomatically if true then calls `setWebhook()` method when server is started.
 * Else call `setWebhook()` method manually. Default is `true`
 */
@OptionsMarker
public class WebhookOptions(
    public var setWebhookAutomatically: Boolean = true,
    internal var serverOptions: ServerOptions = ServerOptions()
) {
    public var url: String = ""
        set(value) {
            if (!value.matches("^https://.+$".toRegex()))
                throw IllegalArgumentException("$value is invalid. Check and try again")
            field = value
        }
        get() {
            if (!field.matches("^https://.+$".toRegex()))
                throw IllegalArgumentException("$field is invalid. Check and try again")
            return field
        }

    public var certificate: File? = null
        set(value) {
            if (value == null)
                throw NullPointerException()
            field = value
        }

    public var ipAddress: String? = null
        set(value) {
            if (value == null)
                throw NullPointerException()
            field = value
        }

    public var maxConnections: Int? = null
        set(value) {
            if (value == null)
                throw NullPointerException()
            if (value < 1 || value > 100)
                throw IllegalArgumentException("Incorrect max connections value: $value not in [1, 100]")
            field = value
        }

    public var allowedUpdates: List<AllowedUpdate>? = null
        set(value) {
            if (value == null)
                throw NullPointerException()
            field = value
        }

    public var dropPendingUpdates: Boolean? = null
        set(value) {
            if (value == null)
                throw NullPointerException()
            field = value
        }

    /**
     * Set to false if you use proxy engines like nginx.
     * If set to true than request path must contain /token path spec.
     */
    public var useTokenBasedPathSpec: Boolean = false

    override fun toString(): String {
        return """
            WebhookOptions:
              url: $url
              certificate: ${certificate?.absolutePath}
              maxConnections: $maxConnections
              allowedUpdates: $allowedUpdates
              useTokenBasedPathSpec: $useTokenBasedPathSpec
        """.trimIndent()
    }
}

public fun WebhookOptions.server(block: ServerOptions.() -> Unit) {
    serverOptions = ServerOptions().apply(block)
}

@OptionsMarker
public class TLSOptions {
    private companion object {
        @JvmField
        val supportedPorts = listOf(80, 88, 443, 8443)
    }

    public var port: Int = 8443
        set(value) {
            if (value !in supportedPorts)
                throw IllegalArgumentException("<$value> not in $supportedPorts. Change and try again")
            field = value
        }

    public var keyStorePath: String? = null
        set(value) {
            if (value == null)
                throw NullPointerException()
            field = value
        }

    public var keyStorePassword: String? = null
        set(value) {
            if (value == null)
                throw NullPointerException()
            field = value
        }

    public var keyManagerPassword: String? = null
        set(value) {
            if (value == null)
                throw NullPointerException()
            field = value
        }

    override fun toString(): String {
        return """
            TLSOptions:
              port: $port
              keyStorePath: $keyStorePath
              keyStorePassword: $keyStorePassword
              keyManagerPassword: $keyManagerPassword
        """.trimIndent()
    }
}

@OptionsMarker
public class ServerOptions(
    public var host: String = "127.0.0.1",
    public var port: Int = 8080,
    internal var tlsOptions: TLSOptions? = null
) {
    override fun toString(): String {
        return """
            ServerOptions:
              host: $host
              port: $port
              tlsOptions: $tlsOptions
        """.trimIndent()
    }
}

public fun ServerOptions.tls(block: TLSOptions.() -> Unit) {
    tlsOptions = TLSOptions().apply(block)
}