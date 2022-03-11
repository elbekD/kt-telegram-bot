package com.elbekd.bot.model

public data class TelegramApiError(val code: Int, val description: String) :
    Exception("Error code: $code. Description: $description")