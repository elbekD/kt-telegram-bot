package com.elbekD.bot.http

data class TelegramApiError(private val code: Int, private val msg: String) :
    Throwable("Error code: $code\nDescription: $msg")