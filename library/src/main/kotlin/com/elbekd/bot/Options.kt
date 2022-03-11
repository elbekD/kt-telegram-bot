package com.elbekd.bot

import com.elbekd.bot.util.AllowedUpdate
import java.io.File

public class PollingOptions {
    public var limit: Int? = null
        set(value) {
            if (value != null && (value < 1 || value > 100))
                throw IllegalArgumentException("Limit <$value> not allowed")
            field = value
        }

    public var timeout: Int? = null
        set(value) {
            if (value != null && value < 0)
                throw IllegalArgumentException("Timeout <$value> not allowed")
            field = value
        }

    public var allowedUpdates: List<AllowedUpdate>? = null

    override fun toString(): String {
        return """
            PollingOptions:
              limit: $limit
              timeout: $timeout
              allowedUpdated: $allowedUpdates
        """.trimIndent()
    }
}

@DslMarker
public annotation class OptionsMarker

@OptionsMarker
public class WebhookOptions(
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

    public var ipAddress: String? = null

    public var maxConnections: Int? = null
        set(value) {
            if (value != null && (value < 1 || value > 100))
                throw IllegalArgumentException("Incorrect max connections value: $value not in [1, 100]")
            field = value
        }

    public var allowedUpdates: List<AllowedUpdate>? = null

    public var dropPendingUpdates: Boolean? = null

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
              ipAddress: $ipAddress
              maxConnections: $maxConnections
              allowedUpdates: $allowedUpdates
              dropPendingUpdates: $dropPendingUpdates
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
        val supportedPorts = setOf(80, 88, 443, 8443)
    }

    public var port: Int = 8443
        set(value) {
            if (value !in supportedPorts)
                throw IllegalArgumentException("<$value> not in $supportedPorts. Change and try again")
            field = value
        }

    public var keyStorePath: String? = null

    public var keyStorePassword: String? = null

    public var keyManagerPassword: String? = null

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