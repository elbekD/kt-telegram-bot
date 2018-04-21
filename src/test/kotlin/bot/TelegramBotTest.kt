package bot

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Paths

internal class TelegramBotTest {
    companion object {
        private lateinit var config: TestConfig
        @JvmStatic
        @BeforeClass
        fun initConfig() {
            val mapper = ObjectMapper().registerKotlinModule()
            val reader = Files.newBufferedReader(Paths.get("D:\\Dev\\kt-telegram-bot\\src\\test\\resources", "test-config.json"))
            config = mapper.readValue(reader, TestConfig::class.java)
        }
    }

    private lateinit var bot: Bot

    private fun file(name: String) = Paths.get(config.resourcePath, name).toFile()

    @Before
    fun init() {
        bot = TelegramBot.createPolling(config.token)
    }

    @Test
    fun getMe() {
        bot.getMe().handle { msg, _ -> assertNotNull(msg) }.join()
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
}