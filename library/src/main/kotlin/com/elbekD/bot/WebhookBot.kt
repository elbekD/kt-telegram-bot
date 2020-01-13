package com.elbekD.bot

import com.elbekD.bot.types.Update
import com.google.gson.Gson
import org.eclipse.jetty.server.HttpConfiguration
import org.eclipse.jetty.server.HttpConnectionFactory
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.SecureRequestCustomizer
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import org.eclipse.jetty.server.SslConnectionFactory
import org.eclipse.jetty.server.handler.AbstractHandler
import org.eclipse.jetty.util.ssl.SslContextFactory
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

internal class WebhookBot(username: String,
                          token: String,
                          private val webhookOptions: WebhookOptions) : TelegramBot(username, token) {
    private val server: Server = Server()
    private val gson = Gson()

    init {
        webhookOptions.serverOptions.tlsOptions?.let { tls ->
            val https = HttpConfiguration()
            https.addCustomizer(SecureRequestCustomizer())

            val sslContextFactory = SslContextFactory.Client()

            sslContextFactory.keyStorePath = WebhookBot::class.java.getResource(tls.keyStorePath).toExternalForm()
            sslContextFactory.setKeyStorePassword(tls.keyStorePassword)
            sslContextFactory.setKeyManagerPassword(tls.keyManagerPassword)

            val sslConnector = ServerConnector(server,
                    SslConnectionFactory(sslContextFactory, "http/1.1"),
                    HttpConnectionFactory(https))
            sslConnector.host = webhookOptions.serverOptions.host
            sslConnector.port = tls.port
            server.addConnector(sslConnector)
        }

        val connector = ServerConnector(server)
        connector.host = webhookOptions.serverOptions.host
        connector.port = webhookOptions.serverOptions.port
        server.addConnector(connector)

        server.handler = object : AbstractHandler() {
            override fun handle(target: String, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse) {
                val content = request.inputStream?.bufferedReader()?.readText()
                val updates = gson.fromJson<Update>(content, Update::class.java)
                onUpdate(updates)
                baseRequest.isHandled = true
            }
        }
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
}
