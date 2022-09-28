import com.elbekd.bot.Bot
import com.elbekd.bot.model.toChatId
import com.elbekd.bot.types.BotCommand
import com.elbekd.bot.types.BotCommandScope
import com.elbekd.bot.types.KeyboardButton
import com.elbekd.bot.types.KeyboardButtonPollType
import com.elbekd.bot.types.ParseMode
import com.elbekd.bot.types.ReplyKeyboardMarkup

fun main() {
    val token = "<TOKEN>"
    val bot = Bot.createPolling(token)

    bot.onCommand("/start") { (msg, _) ->
        bot.sendMessage(
            chatId = msg.chat.id.toChatId(),
            text = "`Hello`",
            parseMode = ParseMode.MarkdownV2,
            replyMarkup = ReplyKeyboardMarkup(
                keyboard = listOf(
                    listOf(
                        KeyboardButton(
                            text = "Button 1",
                            requestPoll = KeyboardButtonPollType(KeyboardButtonPollType.Type.Any)
                        ),
                        KeyboardButton(
                            text = "Button 2",
                            requestPoll = KeyboardButtonPollType(KeyboardButtonPollType.Type.Regular)
                        )
                    ),
                    listOf(
                        KeyboardButton(
                            text = "Button 3",
                            requestPoll = KeyboardButtonPollType(KeyboardButtonPollType.Type.Quiz)
                        )
                    )
                ),
                inputFieldPlaceholder = "this is place holder"
            )
        )
    }

    bot.onCommand("/stop") { (msg, _) ->
        val button = bot.getChatMenuButton(msg.chat.id)
        bot.sendMessage(msg.chat.id.toChatId(), "$button")
        bot.setMyCommands(
            commands = listOf(
                BotCommand(
                    command = "/start", description = "this is start"
                )
            ),
            scope = BotCommandScope.BotCommandScopeAllGroupChats
        )
    }
    bot.start()
}