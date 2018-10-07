package bot.types

data class Game(val title: String,
                val description: String,
                val photo: List<PhotoSize>,
                val text: String,
                val text_entities: List<MessageEntity>,
                val animation: Animation)

data class GameHighScore(val position: Int, val user: User, val score: Int)

open class CallbackGame