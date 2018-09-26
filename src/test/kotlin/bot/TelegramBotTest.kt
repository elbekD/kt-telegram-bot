package bot

import bot.types.InlineKeyboardButton
import bot.types.InlineKeyboardMarkup
import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.BeforeClass
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

internal class TelegramBotTest {
    companion object {
        private lateinit var config: TestConfig
        private lateinit var bot: Bot

        @JvmStatic
        @BeforeClass
        fun initConfig() {
            val gson = Gson()
            val reader = Files.newBufferedReader(Paths.get("D:\\Dev\\kt-telegram-bot\\src\\test\\resources\\test-config.json"))
            config = gson.fromJson(reader, TestConfig::class.java)
            bot = TelegramBot.createPolling(config.token)
        }
    }

    private fun file(name: String) = Paths.get(config.resourcePath, name).toFile()

    @Test
    fun getMe() {
        bot.getMe().handle { msg, err ->
            println(err)
            assertNotNull(msg)
        }.join()
    }

    @Test
    fun sendMessage() {
        val testMsg = "This is test message"
        bot.sendMessage(config.userId, testMsg).handle { msg, _ ->
            assertNotNull(msg)
            assertNotNull(msg.text)
            assertTrue(msg.text!! == testMsg)
        }.join()
    }

    @Test
    fun forwardMessage() {
        bot.forwardMessage(config.userId, config.userId, config.msgId).handle { msg, _ ->
            assertNotNull(msg)
        }.join()
    }

    @Test
    fun sendPhoto() {
        val file = file(config.photos[0])
        bot.sendPhoto(config.userId, file, "<i>this is photo file</i>", "html", true)
                .handle { msg, _ ->
                    assertNotNull(msg)
                    assertNotNull(msg.photo)
                    val fileId = msg.photo?.get(0)?.file_id!!
                    bot.sendPhoto(config.userId, fileId, "<b>this is file_id</b>", "html")
                            .handle { msg1, _ ->
                                assertNotNull(msg1)
                                assertNotNull(msg1.photo)
                            }.join()
                }.join()
    }

    @Test
    fun sendAudio() {
        val file = file(config.audio)
        bot.sendAudio(config.userId, file, "*this is audio file*", "markdown",
                4, "Test performer", "Test title")
                .handle { msg, _ ->
                    assertNotNull(msg)
                    assertNotNull(msg.audio)
                    assertNotNull(msg.audio?.file_id)
                }.join()
    }

    @Test
    fun sendDocument() {
        val file = file(config.document)
        bot.sendDocument(config.userId, file, "`this is document file`", "markdown")
                .handle { msg, _ ->
                    assertNotNull(msg)
                    assertNotNull(msg.document)
                    assertNotNull(msg.document?.file_id)
                }.join()
    }

    @Test
    fun sendVideo() {
        val file = file(config.video)
        bot.sendVideo(config.userId, file, 5, 560, 320, "this is video file")
                .handle { msg, _ ->
                    assertNotNull(msg)
                    assertNotNull(msg.video)
                    assertNotNull(msg.video?.file_id)
                }.join()
    }

    @Test
    fun sendVoice() {
        val file = file(config.voice)
        bot.sendVoice(config.userId, file, "this is voice file", duration = 10)
                .handle { msg, _ ->
                    assertNotNull(msg)
                    assertNotNull(msg.voice)
                    assertNotNull(msg.voice?.file_id)
                }.join()
    }

    @Test
    fun sendVideoNote() {
        bot.sendVideoNote(config.userId, config.videoNote)
                .handle { msg, _ ->
                    assertNotNull(msg)
                    assertNotNull(msg.video_note)
                    assertNotNull(msg.video_note?.file_id)
                }.join()
    }

    @Test
    fun sendMediaGroup() {
        bot.sendMediaGroup(config.userId, arrayOf(
                bot.mediaPhoto("attach://photo1", attachment = file(config.photos[0])),
                bot.mediaPhoto("attach://photo2", attachment = file(config.photos[1])),
                bot.mediaVideo("attach://video1", attachment = file(config.video),
                        width = 560, height = 320, duration = 5)
        )).handle { msg, _ ->
            assertNotNull(msg)
            assertTrue(msg.size == 3)
        }.join()
    }

    @Test
    fun sendLocation() {
        val lat = config.location.latitude
        val lng = config.location.longitude
        bot.sendLocation(config.userId, lat, lng, 60)
                .handle { msg, _ ->
                    assertNotNull(msg)
                    assertNotNull(msg.location)
                }.join()
    }

    @Test
    fun editMessageLiveLocation() {
        val lat = config.location.latitude
        val lng = config.location.longitude
        bot.sendLocation(config.userId, lat, lng, 60)
                .handle { msg, _ ->
                    assertNotNull(msg)
                    assertNotNull(msg.location)

                    Thread.sleep(5000L)

                    val id = msg.chat.id
                    val msgId = msg.message_id
                    val newLat = config.location.latitude - 0.00004
                    val newLng = config.location.longitude + 0.00005

                    bot.editMessageLiveLocation(newLat, newLng, id, msgId)
                            .handle { msg1, _ ->
                                assertNotNull(msg1)
                                assertNotNull(msg1.location)
                            }.join()

                }.join()
    }

    @Test
    fun stopMessageLiveLocation() {
        val lat = config.location.latitude
        val lng = config.location.longitude
        bot.sendLocation(config.userId, lat, lng, 60)
                .handle { msg, _ ->
                    assertNotNull(msg)
                    assertNotNull(msg.location)

                    Thread.sleep(5000L)
                    val id = msg.chat.id
                    val msgId = msg.message_id
                    bot.stopMessageLiveLocation(id, msgId).handle { msg1, _ ->
                        assertNotNull(msg1)
                        assertNotNull(msg1.location)
                    }.join()
                }.join()
    }

    @Test
    fun sendVenue() {
        val lat = config.location.latitude
        val lng = config.location.longitude
        bot.sendVenue(config.userId, lat, lng, "This is venue", "This is address", "This is ID")
                .handle { msg, _ ->
                    assertNotNull(msg)
                    assertNotNull(msg.venue)
                }.join()
    }

    @Test
    fun sendContact() {
        bot.sendContact(config.userId, config.contact.phone, config.contact.firstName, config.contact.lastName)
                .handle { msg, _ ->
                    assertNotNull(msg)
                    assertNotNull(msg.contact)
                }.join()
    }

    @Test
    fun sendChatAction() {
        bot.sendChatAction(config.userId, TelegramBot.Actions.RecordAudio)
                .handle { msg, _ ->
                    assertNotNull(msg)
                    assertTrue(msg)
                }.join()
    }

    @Test
    fun getUserProfilePhotos() {
        bot.getUserProfilePhotos(config.userId).handle { msg, _ ->
            assertNotNull(msg)
            assertNotNull(msg.photos)
        }.join()
    }

    @Test
    fun getFile() {
        bot.getFile(config.fileId).handle { file, _ ->
            assertNotNull(file)
        }.join()
    }

    @Test
    fun kickChatMember() {
        val date = (Date().time + 60 * 1000).toInt()
        bot.kickChatMember(config.groupChatId, config.kikMemberId, date)
                .handle { msg, err ->
                    println(err)
                    assertTrue(msg)
                }.join()
    }

    @Test
    fun unbanChatMember() {
        bot.unbanChatMember(config.groupChatId, config.kikMemberId)
                .handle { msg, err ->
                    println(err)
                    assertTrue(msg)
                }.join()
    }

    @Test
    fun restrictChatMember() {
        bot.restrictChatMember(config.groupChatId, config.kikMemberId, canSendMessage = false)
                .handle { msg, _ ->
                    assertTrue(msg)
                }.join()
    }

    @Test
    fun promoteChatMember() {
        bot.promoteChatMember(config.groupChatId, config.kikMemberId, canPinMessages = true)
                .handle { msg, _ ->
                    assertTrue(msg)
                }.join()
    }

    @Test
    fun exportChatInviteLink() {
        bot.exportChatInviteLink(config.groupChatId).handle { msg, _ ->
            assertNotNull(msg)
        }.join()
    }

    @Test
    fun setChatPhoto() {
        bot.setChatPhoto(config.groupChatId, file(config.photos[1])).handle { msg, _ ->
            {
                assertTrue(msg)
            }
        }.join()
    }

    @Test
    fun deleteChatPhoto() {
        bot.deleteChatPhoto(config.groupChatId).handle { msg, _ ->
            {
                assertTrue(msg)
            }
        }.join()
    }

    @Test
    fun setChatTitle() {
        bot.setChatTitle(config.groupChatId, "This is chat title").handle { msg, _ ->
            assertTrue(msg)
        }.join()
    }

    @Test
    fun setChatDescription() {
        bot.setChatDescription(config.groupChatId, "This is chat description").handle { msg, _ ->
            assertTrue(msg)
        }.join()
    }

    @Test
    fun pinChatMessage() {
        bot.sendMessage(config.groupChatId, "This is universal message")
                .thenAccept {
                    bot.pinChatMessage(it.chat.id, it.message_id).handle { msg, _ -> assertTrue(msg) }.join()
                }.join()
    }

    @Test
    fun unpinChatMessage() {
        bot.sendMessage(config.groupChatId, "This is universal message")
                .thenAccept { msg ->
                    bot.pinChatMessage(msg.chat.id, msg.message_id)
                            .thenAccept { bot.unpinChatMessage(msg.chat.id) }
                            .join()
                }.join()
    }

    @Test
    fun leaveChat() {
        bot.leaveChat(config.groupChatId).handle { msg, _ -> assertTrue(msg) }.join()
    }

    @Test
    fun getChat() {
        bot.getChat(config.groupChatId).handle { chat, _ -> assertTrue(chat.id == config.groupChatId) }.join()
    }

    @Test
    fun getChatAdministrators() {
        bot.getChatAdministrators(config.groupChatId).handle { members, _ -> assertNotNull(members) }.join()
    }

    @Test
    fun getChatMembersCount() {
        bot.getChatMembersCount(config.groupChatId).handle { count, _ -> assertTrue(count != 0) }.join()
    }

    @Test
    fun getChatMember() {
        bot.getChatMember(config.groupChatId, config.userId).handle { member, _ -> assertNotNull(member) }.join()
    }

    @Test
    fun setChatStickerSet() {
        bot.setChatStickerSet(config.groupChatId, config.stickerSet).handle { msg, _ -> assertTrue(msg) }.join()
    }

    @Test
    fun deleteChatStickerSet() {
        bot.deleteChatStickerSet(config.groupChatId).handle { msg, _ -> assertTrue(msg) }.join()
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
        assertNotNull(curr)
        assertEquals("Hello", curr.text)
    }

    @Test
    fun editMessageCaption() {
        val file = file(config.photos[0])
        val prev = bot.sendPhoto(config.userId, file, "cccaption").get()
        val curr = bot.editMessageCaption(prev.chat.id, prev.message_id, caption = "Caption").get()
        assertNotNull(curr)
        assertEquals("Caption", curr.caption)
    }

    @Test
    fun editMessageMedia() {
        val file = file(config.photos[0])

        val prev = bot.sendPhoto(config.userId, file).get()
        val cur = bot.editMessageMedia(prev.chat.id, prev.message_id,
                media = bot.mediaPhoto("attach://photo2", attachment = file(config.photos[1]))).get()

        assertTrue(cur.edit_date != null)
    }

    @Test
    fun editMessageReplyMarkup() {
        val keyboard1 = InlineKeyboardMarkup(arrayOf(
                arrayOf(
                        InlineKeyboardButton("button 1", callback_data = "data1"),
                        InlineKeyboardButton("button 2", callback_data = "data2")
                )
        ))

        val keyboard2 = InlineKeyboardMarkup(arrayOf(
                arrayOf(
                        InlineKeyboardButton("button 2", callback_data = "data2")
                )
        ))

        val prev = bot.sendMessage(config.userId, "hello", markup = keyboard1).get()
        val curr = bot.editMessageReplyMarkup(prev.chat.id, prev.message_id, markup = keyboard2).get()
        assertNotNull(curr)
        assertTrue(curr.edit_date != null)
    }
}
