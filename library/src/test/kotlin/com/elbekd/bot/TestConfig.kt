package com.elbekd.bot

import com.elbekd.bot.types.LabeledPrice

data class Location(val latitude: Double, val longitude: Double)

data class Contact(val phone: String, val firstName: String, val lastName: String)

data class CreateStickerSet(
    val userId: Long,
    val name: String,
    val title: String,
    val createStickerFilename: String,
    val addStickerFilename: String,
    val emojisCreate: String,
    val emojisAdd: String
)

data class Game(val name: String)

data class Invoice(
    val title: String,
    val desc: String,
    val payload: String,
    val token: String,
    val param: String,
    val currency: String,
    val prices: List<LabeledPrice>,
    val email: Boolean,
    val phone: Boolean,
    val address: Boolean
)

data class TestConfig(
    val token: String,
    val userId: Long,
    val resourcePath: String,
    val photos: List<String>,
    val video: String,
    val animation: String,
    val thumbnail: String,
    val voice: String,
    val msgId: Int,
    val audio: String,
    val document: String,
    val videoNote: String,
    val location: Location,
    val contact: Contact,
    val fileId: String,
    val groupChatId: Long,
    val kikMemberId: Long,
    val stickerSetName: String,
    val sendStickerUrl: String,
    val createStickerSet: CreateStickerSet,
    val game: Game,
    val invoice: Invoice
)