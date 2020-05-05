package com.elbekD.bot.util.keyboard

import com.elbekD.bot.types.InlineKeyboardButton
import com.elbekD.bot.types.KeyboardButton

object KeyboardFactory {
    fun replyMarkup(vararg buttons: KeyboardButton, rowWidth: Int = 1): List<List<KeyboardButton>> =
        replyMarkup(buttons.toList(), rowWidth)

    fun replyMarkup(buttons: List<KeyboardButton>, rowWidth: Int = 1): List<List<KeyboardButton>> {
        val keyboard = mutableListOf<List<KeyboardButton>>()
        val buttonsCount = buttons.size
        var index = 0
        while (index < buttonsCount) {
            val row = mutableListOf<KeyboardButton>()
            for (x in 0 until rowWidth) {
                if (index >= buttonsCount) break
                row.add(buttons[index])
                index++
            }
            keyboard.add(row)
        }
        return keyboard
    }

    fun inlineMarkup(vararg buttons: InlineKeyboardButton, rowWidth: Int = 1): List<List<InlineKeyboardButton>> =
        inlineMarkup(buttons.toList(), rowWidth)

    fun inlineMarkup(buttons: List<InlineKeyboardButton>, rowWidth: Int = 1): List<List<InlineKeyboardButton>> {
        val keyboard = mutableListOf<List<InlineKeyboardButton>>()
        val buttonsCount = buttons.size
        var index = 0
        while (index < buttonsCount) {
            val row = mutableListOf<InlineKeyboardButton>()
            for (x in 0 until rowWidth) {
                if (index >= buttonsCount) break
                row.add(buttons[index])
                index++
            }
            keyboard.add(row)
        }
        return keyboard
    }
}