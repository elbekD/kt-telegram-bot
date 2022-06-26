package com.elbekd.bot.internal

import com.elbekd.bot.WebhookOptions
import com.elbekd.bot.types.UpdateResponse
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.eclipse.jetty.server.*
import org.eclipse.jetty.server.handler.DefaultHandler
import org.eclipse.jetty.server.handler.HandlerCollection
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.eclipse.jetty.util.ssl.SslContextFactory
import kotlin.coroutines.CoroutineContext

internal class WebhookBot(
    username: String?,
    token: String,
    private val webhookOptions: WebhookOptions
) : TelegramBot(username, token) {

    private val json = Json { ignoreUnknownKeys = true }
    private val server: Server = Server()
    private val scope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = Job()
    }

    init {
        webhookOptions.serverOptions.tlsOptions?.let { tls ->
            val https = HttpConfiguration().apply { addCustomizer(SecureRequestCustomizer()) }

            val sslContextFactory = SslContextFactory.Server().apply {
                keyStorePath = WebhookBot::class.java.getResource(tls.keyStorePath).toExternalForm()
                setKeyStorePassword(tls.keyStorePassword)
                setKeyManagerPassword(tls.keyManagerPassword)
            }

            val sslConnector = ServerConnector(
                server,
                SslConnectionFactory(sslContextFactory, "http/1.1"),
                HttpConnectionFactory(https)
            ).apply {
                host = webhookOptions.serverOptions.host
                port = tls.port
            }

            server.addConnector(sslConnector)
        }

        val connector = ServerConnector(server).apply {
            host = webhookOptions.serverOptions.host
            port = webhookOptions.serverOptions.port
        }

        server.addConnector(connector)

        val contextHandler = ServletContextHandler().apply {
            contextPath = "/"
            addServlet(createServletHolder(), if (webhookOptions.useTokenBasedPathSpec) "/$token" else "/")
        }

        val handlers = HandlerCollection().apply {
            handlers = arrayOf(contextHandler, DefaultHandler())
        }

        server.handler = handlers
    }

    override fun start() {
        server.start()
        scope.launch {
            deleteWebhook(webhookOptions.dropPendingUpdates)
            setWebhook(
                webhookOptions.url,
                webhookOptions.certificate,
                webhookOptions.ipAddress,
                webhookOptions.maxConnections,
                webhookOptions.allowedUpdates,
                webhookOptions.dropPendingUpdates
            )
        }
        server.join()
    }

    override fun stop() {
        server.stop()
        scope.cancel()
    }

    private fun createServletHolder() = ServletHolder("Webhook bot servlet", createServlet())

    private fun createServlet() = object : HttpServlet() {
        override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
            val content = req.inputStream?.bufferedReader()?.readText() ?: return
            val updates = json.decodeFromString<UpdateResponse>(content)
            scope.launch { onUpdate(updates) }
        }
    }
}
