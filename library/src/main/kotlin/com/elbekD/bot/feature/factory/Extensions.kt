package com.elbekD.bot.feature.factory

import com.elbekD.bot.types.InlineKeyboardButton
import com.elbekD.bot.types.KeyboardButton

/**
 * Useful when you want to add 1 [InlineKeyboardButton] to a new keyboard row quickly
 */
fun InlineKeyboardButton.toListButtons(): List<InlineKeyboardButton> = listOf(this)

/**
 * Create list (keyboard) of [InlineKeyboardButton] with fixed [rowWidth]
 */
fun List<InlineKeyboardButton>.toInlineKeyboard(rowWidth: Int = 1): List<List<InlineKeyboardButton>> =
    KeyboardFactory.inlineMarkup(this, rowWidth)

/**
 * Useful when you want to add 1 [KeyboardButton] to a new keyboard row quickly
 */
fun KeyboardButton.toListButtons(): List<KeyboardButton> = listOf(this)

/**
 * Create [KeyboardButton] from [String] with only [KeyboardButton.text] property
 */
fun String.toKeyboardButton(): KeyboardButton = KeyboardButton(this)

/**
 * Create list of [KeyboardButton] from [String] with only [KeyboardButton.text] property
 * Useful when you want to add 1 simple KeyboardButton to a new keyboard row quickly
 */
fun String.toListKeyboardButtons(): List<KeyboardButton> = listOf(KeyboardButton(this))

/**
 * Create list of [KeyboardButton] from Strings with only [KeyboardButton.text] property
 */
fun List<String>.toListKeyboardButtons(): List<KeyboardButton> = this.map { it.toKeyboardButton() }

/**
 * Create list (keyboard) of [KeyboardButton] from Strings with only [KeyboardButton.text] property
 * and fixed [rowWidth]
 */
fun List<String>.toReplyKeyboard(rowWidth: Int = 1): List<List<KeyboardButton>> =
    KeyboardFactory.replyMarkup(this.toListKeyboardButtons(), rowWidth)

