package com.elbekd.bot.api

import com.elbekd.bot.types.Update
import com.elbekd.bot.types.WebhookInfo
import com.elbekd.bot.util.AllowedUpdate

public interface TelegramUpdatesApi {
    public suspend fun getUpdates(
        offset: Int? = null,
        limit: Int? = null,
        timeout: Int? = null,
        allowedUpdates: List<AllowedUpdate>? = null
    ): List<Update>

    public suspend fun setWebhook(
        url: String,
        certificate: java.io.File? = null,
        ipAddress: String? = null,
        maxConnections: Int? = null,
        allowedUpdates: List<AllowedUpdate>? = null,
        dropPendingUpdates: Boolean? = null
    ): Boolean

    public suspend fun deleteWebhook(dropPendingUpdates: Boolean? = null): Boolean

    public suspend fun getWebhookInfo(): WebhookInfo
}