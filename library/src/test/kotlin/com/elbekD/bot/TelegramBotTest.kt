package com.elbekD.bot

import com.elbekD.bot.types.ChatPermissions
import com.elbekD.bot.types.InlineKeyboardButton
import com.elbekD.bot.types.InlineKeyboardMarkup
import com.elbekD.bot.util.Action
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.BeforeClass
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Date
import java.util.concurrent.TimeUnit

internal class TelegramBotTest {
    companion object {
        private lateinit var config: TestConfig
        private lateinit var bot: Bot

        @JvmStatic
        @BeforeClass
        fun initConfig() {
            val gson = Gson()
            val reader = Files.newBufferedReader(Paths.get("<>"))
            config = gson.fromJson(reader, TestConfig::class.java)
            bot = Bot.createPolling("test-bot", config.token)
        }
    }

    private fun file(name: String) = Paths.get(config.resourcePath, name).toFile()

    @Test
    fun getMe() {
        val msg = bot.getMe().get()
        assertTrue(msg.is_bot)
    }

    @Test
    fun sendMessage() {
        val testMsg = "This is test message"
        val msg = bot.sendMessage(config.userId, testMsg).get()
        assertNotNull(msg.text)
        assertEquals(msg.text, testMsg)
    }

    @Test
    fun forwardMessage() {
        val msg = bot.forwardMessage(config.userId, config.userId, config.msgId).get()
        assertNotNull(msg.forward_from)
    }

    @Test
    fun sendPhoto() {
        val file = file(config.photos[0])
        val msg1 = bot.sendPhoto(config.userId, file).get()
        assertNotNull(msg1.photo)

        val fileId = msg1.photo?.get(0)?.file_id!!
        val msg2 = bot.sendPhoto(msg1.chat.id, fileId).get()
        assertNotNull(msg2.photo)
    }

    @Test
    fun sendAudio() {
        val file = file(config.audio)
        val msg = bot.sendAudio(
            config.userId, file, "*this is audio file*", "markdown", 4, "Test performer", "Test title"
        ).get()
        assertNotNull(msg.audio)
        assertNotNull(msg.audio?.file_id)
    }

    @Test
    fun sendDocument() {
        val file = config.document
        bot.sendChatAction(config.userId, Action.UploadDocument)
        val msg =
            bot.sendDocument(config.userId, file, caption = "`this is document file`", parseMode = "markdown").get()
        assertNotNull(msg.document)
    }

    @Test
    fun sendVideo() {
        val file = file(config.video)
        val msg = bot.sendVideo(config.userId, file, 5, 560, 320, caption = "this is video file").get()
        assertNotNull(msg.video)
        assertNotNull(msg.video?.file_id)
    }

    @Test
    fun sendAnimation() {
        val thumb = file(config.thumb)
        val msg = bot.sendAnimation(config.userId, config.animation, thumb = thumb).get()
        assertNotNull(msg.animation)
    }

    @Test
    fun sendVoice() {
        val file = file(config.voice)
        val msg = bot.sendVoice(config.userId, file, "this is voice file", duration = 10).get()
        assertNotNull(msg.voice)
        assertNotNull(msg.voice?.file_id)
    }

    @Test
    fun sendVideoNote() {
        val msg = bot.sendVideoNote(config.userId, config.videoNote).get()
        assertNotNull(msg.video_note)
        assertNotNull(msg.video_note?.file_id)
    }

    @Test
    fun sendMediaGroup() {
        val msg = bot.sendMediaGroup(
            config.userId, listOf(
                bot.mediaPhoto("attach://photo1", attachment = file(config.photos[0])),
                bot.mediaPhoto("attach://photo2", attachment = file(config.photos[1])),
                bot.mediaVideo(
                    "attach://video1", attachment = file(config.video),
                    width = 560, height = 320, duration = 5
                )
            )
        ).get()
        assertTrue(msg.size == 3)
    }

    @Test
    fun sendLocation() {
        val lat = config.location.latitude
        val lng = config.location.longitude
        val msg = bot.sendLocation(config.userId, lat, lng, 60).get()
        assertNotNull(msg.location)
    }

    @Test
    fun editMessageLiveLocation() {
        val lat = config.location.latitude
        val lng = config.location.longitude
        val msg1 = bot.sendLocation(config.userId, lat, lng, 60).get()

        assertNotNull(msg1.location)

        val id = msg1.chat.id
        val msgId = msg1.message_id
        val newLat = config.location.latitude - 0.00004
        val newLng = config.location.longitude + 0.00005

        TimeUnit.SECONDS.sleep(3)

        val msg2 = bot.editMessageLiveLocation(newLat, newLng, id, msgId).get()
        assertNotNull(msg2.location)
    }

    @Test
    fun stopMessageLiveLocation() {
        val lat = config.location.latitude
        val lng = config.location.longitude
        val msg = bot.sendLocation(config.userId, lat, lng, 60).get()

        assertNotNull(msg.location)

        TimeUnit.SECONDS.sleep(3)

        val id = msg.chat.id
        val msgId = msg.message_id
        val msg1 = bot.stopMessageLiveLocation(id, msgId).get()
        assertNotNull(msg1.location)
    }

    @Test
    fun sendVenue() {
        val lat = config.location.latitude
        val lng = config.location.longitude
        val msg = bot.sendVenue(
            config.userId, lat, lng,
            "This is venue", "This is address", "This is ID"
        ).get()
        assertNotNull(msg.venue)
    }

    @Test
    fun sendContact() {
        val msg =
            bot.sendContact(config.userId, config.contact.phone, config.contact.firstName, config.contact.lastName)
                .get()
        assertNotNull(msg.contact)
    }

    @Test
    fun sendChatAction() {
        val msg = bot.sendChatAction(config.userId, Action.RecordAudio).get()
        assertTrue(msg)
    }

    @Test
    fun getUserProfilePhotos() {
        val msg = bot.getUserProfilePhotos(config.userId).get()
        assertNotNull(msg.photos)
    }

    @Test
    fun getFile() {
        val file = bot.getFile(config.fileId).get()
        assertTrue(file.file_size > 0)
    }

    @Test
    fun kickChatMember() {
        val date = (Date().time + 60 * 1000).toInt()
        val msg = bot.kickChatMember(config.groupChatId, config.kikMemberId, date).get()
        assertTrue(msg)
    }

    @Test
    fun unbanChatMember() {
        val msg = bot.unbanChatMember(config.groupChatId, config.kikMemberId).get()
        assertTrue(msg)
    }

    @Test
    fun restrictChatMember() {
        val msg =
            bot.restrictChatMember(config.groupChatId, config.kikMemberId, ChatPermissions(can_send_messages = false))
                .get()
        assertTrue(msg)
    }

    @Test
    fun promoteChatMember() {
        val msg = bot.promoteChatMember(config.groupChatId, config.kikMemberId, canPinMessages = true).get()
        assertTrue(msg)
    }

    @Test
    fun exportChatInviteLink() {
        bot.exportChatInviteLink(config.groupChatId).get()
    }

    @Test
    fun setChatPhoto() {
        val msg = bot.setChatPhoto(config.groupChatId, file(config.photos[1])).get()
        assertTrue(msg)
    }

    @Test
    fun deleteChatPhoto() {
        val msg = bot.deleteChatPhoto(config.groupChatId).get()
        assertTrue(msg)
    }

    @Test
    fun setChatTitle() {
        val msg = bot.setChatTitle(config.groupChatId, "This is chat title").get()
        assertTrue(msg)
    }

    @Test
    fun setChatDescription() {
        val msg = bot.setChatDescription(config.groupChatId, "This is chat description").get()
        assertTrue(msg)
    }

    @Test
    fun pinChatMessage() {
        val msg = bot.sendMessage(config.groupChatId, "This is universal message").get()
        val isMsgPinned = bot.pinChatMessage(msg.chat.id, msg.message_id).get()
        assertTrue(isMsgPinned)
    }

    @Test
    fun unpinChatMessage() {
        val msg = bot.sendMessage(config.groupChatId, "This is universal message").get()
        bot.pinChatMessage(msg.chat.id, msg.message_id).get()
        val isMsgUnpinned = bot.unpinChatMessage(msg.chat.id).get()
        assertTrue(isMsgUnpinned)
    }

    @Test
    fun leaveChat() {
        val msg = bot.leaveChat(config.groupChatId).get()
        assertTrue(msg)
    }

    @Test
    fun getChat() {
        val chat = bot.getChat(config.groupChatId).get()
        assertTrue(chat.id == config.groupChatId)
    }

    @Test
    fun getChatAdministrators() {
        val members = bot.getChatAdministrators(config.groupChatId).get()
        assertTrue(members.isNotEmpty())
    }

    @Test
    fun getChatMembersCount() {
        val count = bot.getChatMembersCount(config.groupChatId).get()
        assertTrue(count != 0)
    }

    @Test
    fun getChatMember() {
        val member = bot.getChatMember(config.groupChatId, config.userId).get()
        assertEquals(member.user.id, config.userId)
    }

    @Test
    fun setChatStickerSet() {
        val msg = bot.setChatStickerSet(config.groupChatId, config.stickerSetName).get()
        assertTrue(msg)
    }

    @Test
    fun deleteChatStickerSet() {
        val msg = bot.deleteChatStickerSet(config.groupChatId).get()
        assertTrue(msg)
    }

    @Test(expected = RuntimeException::class)
    fun answerCallbackQuery() {
        throw RuntimeException("no unit test provided")
    }

    @Test(expected = RuntimeException::class)
    fun answerInlineQuery() {
        throw RuntimeException("no unit test provided")
    }

    @Test
    fun editMessageText() {
        val prev = bot.sendMessage(config.userId, "helo").get()
        val curr = bot.editMessageText(prev.chat.id, prev.message_id, text = "Hello").get()
        assertEquals("Hello", curr.text)
    }

    @Test
    fun editMessageCaption() {
        val file = file(config.photos[0])
        val prev = bot.sendPhoto(config.userId, file, "cccaption").get()
        val curr = bot.editMessageCaption(prev.chat.id, prev.message_id, caption = "Caption").get()
        assertEquals("Caption", curr.caption)
    }

    @Test
    fun editMessageMedia() {
        val file = file(config.photos[0])

        val prev = bot.sendPhoto(config.userId, file).get()
        val cur = bot.editMessageMedia(
            prev.chat.id, prev.message_id,
            media = bot.mediaPhoto("attach://photo2", attachment = file(config.photos[1]))
        ).get()

        assertTrue(cur.edit_date != null)
    }

    @Test
    fun editMessageReplyMarkup() {
        val keyboard1 = InlineKeyboardMarkup(
            listOf(
                listOf(
                    InlineKeyboardButton("button 1", callback_data = "data1"),
                    InlineKeyboardButton("button 2", callback_data = "data2")
                )
            )
        )

        val keyboard2 = InlineKeyboardMarkup(
            listOf(
                listOf(
                    InlineKeyboardButton("button 2", callback_data = "data2")
                )
            )
        )

        val prev = bot.sendMessage(config.userId, "hello", markup = keyboard1).get()
        val curr = bot.editMessageReplyMarkup(prev.chat.id, prev.message_id, markup = keyboard2).get()
        assertTrue(curr.edit_date != null)
    }

    @Test
    fun sendSticker() {
        val msg = bot.sendSticker(config.userId, config.sendStickerUrl).get()
        assertTrue(msg.sticker != null)
    }

    @Test
    fun getStickerSet() {
        val set = bot.getStickerSet(config.stickerSetName).get()
        assertEquals(set.name, config.stickerSetName)
    }

    @Test
    fun createStickerSet_thenAddSticker_thenDeleteSticker() {
        with(config.createStickerSet) {
            val sticker = bot.uploadStickerFile(config.userId, file(createStickerFilename)).get()

            val stickerSetName = "v${Math.random().toString().substring(2, 10)}$name"
            bot.createNewStickerSet(userId, stickerSetName, title, sticker.file_id, emojisCreate).get()

            val addedStickerFile = bot.uploadStickerFile(config.userId, file(addStickerFilename)).get()
            bot.addStickerToSet(userId, stickerSetName, addedStickerFile.file_id, emojisAdd).get()

            val stickers = bot.getStickerSet(stickerSetName).get().stickers
            bot.setStickerPositionInSet(stickers.last().file_id, 0).get()

            stickers.forEach { bot.deleteStickerFromSet(it.file_id).get() }
        }
    }

    @Test
    fun sendGame_thenGetGame_thenSetNewScore() {
        val game = bot.sendGame(config.userId, config.game.name).get()
        val scores = bot.getGameHighScores(config.userId, chatId = game.chat.id, messageId = game.message_id).get()
        val newScore = if (scores.isNotEmpty()) scores[0].score + 100 else 100
        bot.setGameScore(config.userId, newScore, chatId = game.chat.id, messageId = game.message_id).get()
    }

    @Test
    fun sendInvoice() {
        with(config.invoice) {
            val msg = bot.sendInvoice(
                config.userId,
                title,
                desc,
                payload,
                token,
                param,
                currency,
                prices,
                needEmail = email,
                needPhoneNumber = phone,
                needShippingAddress = address
            ).get()
            assertNotNull(msg.invoice)
        }
    }

    @Test(expected = RuntimeException::class)
    fun answerShippingQuery() {
        throw RuntimeException("no unit test provided")
    }

    @Test(expected = RuntimeException::class)
    fun answerPreCheckoutQuery() {
        throw RuntimeException("no unit test provided")
    }

    @Test
    fun sendPoll() {
        val msg = bot.sendPoll(
            chatId = config.groupChatId,
            question = "Test poll question",
            options = listOf("Option 1", "Option 2")
        ).get()

        assertNotNull(msg.poll)
    }

    @Test
    fun stopPoll() {
        val poll = bot.stopPoll(
            config.groupChatId,
            config.msgId
        ).get()

        assertTrue(poll.is_closed)
    }

}
