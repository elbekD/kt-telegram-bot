package bot

data class Location(val latitude: Double, val longitude: Double)
data class Contact(val phone: String, val firstName: String, val lastName: String)

data class TestConfig(val token: String,
                      val userId: Int,
                      val resourcePath: String,
                      val photos: Array<String>,
                      val video: String,
                      val voice: String,
                      val msgId: Int,
                      val audio: String,
                      val document: String,
                      val videoNote: String,
                      val location: Location,
                      val contact: Contact,
                      val fileId: String,
                      val groupChatId: Long,
                      val kikMemberId: Int,
                      val stickerSet: String)