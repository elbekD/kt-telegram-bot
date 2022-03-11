package com.elbekd.bot.api

import com.elbekd.bot.types.PassportElementError

public interface TelegramPassportApi {
    public suspend fun setPassportDataErrors(
        userId: Long,
        errors: List<PassportElementError>
    ): Boolean
}