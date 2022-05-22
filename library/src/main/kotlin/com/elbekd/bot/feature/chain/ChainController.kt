package com.elbekd.bot.feature.chain

import com.elbekd.bot.types.Message

internal object ChainController {
    private val activeChains = mutableMapOf<Long, Chain>()
    private val registeredChains = mutableSetOf<Chain>()

    internal fun registerChain(chain: Chain) {
        registeredChains.add(chain)
    }

    fun jumpTo(label: String, message: Message) {
        val chatId = message.chat.id
        val activeChain =
            activeChains[chatId] ?: throw IllegalStateException("There is no active chains for chat $chatId")
        activeChain.jumpTo(label)
    }

    suspend fun jumpToAndFire(label: String, message: Message) {
        jumpTo(label, message)
        handle(message)
    }

    suspend fun canHandle(message: Message): Boolean {
        val chatId = message.chat.id
        val activeChain = activeChains[chatId]

        if (activeChain != null) {
            return activeChain.hasNext()
        }

        return registeredChains.any { chain -> chain.canFire(message) }
    }

    suspend fun handle(message: Message) {
        val chatId = message.chat.id
        val activeChain = activeChains[chatId]

        if (activeChain == null) {
            val newChain = registeredChains.first { chain -> chain.canFire(message) }.copy()
            activeChains[chatId] = newChain
            newChain.fire(message)
        } else {
            val isOnTerminalNode = activeChain.isOnTerminalNode()
            activeChain.fire(message)

            if (isOnTerminalNode || !activeChain.hasNext()) {
                terminateChain(chatId)
            }
        }
    }

    fun terminateChain(chatId: Long) {
        activeChains.remove(chatId)
    }
}