package com.elbekD.bot.feature.chain

import com.elbekD.bot.types.Message
import java.util.LinkedList

class ChainBuilder private constructor(
    private val label: String,
    private val firePredicate: (Message) -> Boolean,
    private val action: (Message) -> Unit
) {

    private constructor(trigger: String, triggerAction: (Message) -> Unit) : this(
        trigger,
        firePredicate = { msg: Message -> msg.text == trigger },
        action = triggerAction
    )

    private val chainList = LinkedList<Chain.Node>().apply {
        add(Chain.Node(label, false, action))
    }

    fun then(
        label: String? = null,
        isTerminal: Boolean = false,
        action: (Message) -> Unit
    ) = apply {
        val nodeLabel = label ?: createNextLabel()
        val node = Chain.Node(nodeLabel, isTerminal, action)
        chainList.last().next = node
        chainList.add(node)
    }

    fun build(): Chain {
        val chain = Chain(label, firePredicate, chainList)
        ChainController.registerChain(chain)
        return chain
    }

    private fun createNextLabel() = "$label-${chainList.size}"

    companion object {
        fun with(trigger: String, action: (Message) -> Unit) = ChainBuilder(trigger, action)

        fun with(label: String, on: (Message) -> Boolean, action: (Message) -> Unit) = ChainBuilder(label, on, action)
    }
}