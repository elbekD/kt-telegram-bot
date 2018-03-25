package bot.types

data class Game(val title: String,
                val description: String,
                val photo: Array<PhotoSize>,
                val text: String,
                val text_entities: Array<MessageEntity>,
                val animation: Animation
)

data class Animation(val field_id: String,
                     val thumb: PhotoSize,
                     val file_name: String,
                     val mime_type: String,
                     val file_size: Int
)

data class GameHighScore(val position: Int, val user: User, val score: Int)

open class CallbackGame