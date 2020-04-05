package com.elbekD.bot.feature.chain

import com.elbekD.bot.types.Message

data class Chain internal constructor(
    private val label: String,
    private val firePredicate: (Message) -> Boolean,
    private val chainList: List<Node>
) {

    private val nodeTable = mutableMapOf<String, Node>()
    private var currentNode: Node? = chainList.first()

    init {
        chainList.forEach { node -> nodeTable[node.label] = node }
    }

    internal fun canFire(message: Message): Boolean {
        return firePredicate(message)
    }

    internal fun fire(message: Message) {
        val node = currentNode ?: throw IllegalStateException("Trying to fire terminated chain $label")
        currentNode = node.next
        node.action(message)
    }

    internal fun hasNext(): Boolean {
        return currentNode != null
    }

    internal fun isOnTerminalNode(): Boolean {
        return currentNode?.isTerminal == true
    }

    internal fun jumpTo(label: String) {
        val node = nodeTable[label] ?: throw IllegalArgumentException("There is no node with label $label")
        currentNode = node
    }

    internal class Node(
        val label: String,
        val isTerminal: Boolean,
        val action: (Message) -> Unit,
        var next: Node? = null
    )
}
