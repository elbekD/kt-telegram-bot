package com.elbekD.bot

import com.elbekD.bot.types.Update
import com.google.gson.Gson
import org.eclipse.jetty.server.HttpConfiguration
import org.eclipse.jetty.server.HttpConnectionFactory
import org.eclipse.jetty.server.SecureRequestCustomizer
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import org.eclipse.jetty.server.SslConnectionFactory
import org.eclipse.jetty.server.handler.DefaultHandler
import org.eclipse.jetty.server.handler.HandlerCollection
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.eclipse.jetty.util.ssl.SslContextFactory
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

internal class WebhookBot(
        username: String,
        token: String,
        private val webhookOptions: WebhookOptions
) : TelegramBot(username, token) {

    private val server: Server = Server()
    private val gson = Gson()

    init {
        webhookOptions.serverOptions.tlsOptions?.let { tls ->
            val https = HttpConfiguration().apply { addCustomizer(SecureRequestCustomizer()) }

            val sslContextFactory = SslContextFactory.Client().apply {
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
            addServlet(createServletHolder(), "/$token")
        }

        val handlers = HandlerCollection().apply {
            handlers = arrayOf(contextHandler, DefaultHandler())
        }

        server.handler = handlers
    }

    override fun start() {
        try {
            server.start()
            if (webhookOptions.setWebhookAutomatically) {
                deleteWebhook()
                        .thenAccept {
                            setWebhook(webhookOptions.url,
                                    webhookOptions.certificate,
                                    webhookOptions.maxConnections,
                                    webhookOptions.allowedUpdates)
                        }.join()
            }
            server.join()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun stop() {
        try {
            server.stop()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createServletHolder() = ServletHolder("Webhook bot servlet", createServlet())

    private fun createServlet() = object : HttpServlet() {
        override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
            val content = req.inputStream?.bufferedReader()?.readText()
            val updates = gson.fromJson<Update>(content, Update::class.java)
            onUpdate(updates)
        }
    }
}
